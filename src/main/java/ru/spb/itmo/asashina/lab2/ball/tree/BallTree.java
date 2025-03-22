package ru.spb.itmo.asashina.lab2.ball.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class BallTree {

    private List<BallTreePoint> points;

    private final int leafSize;
    private final List<int[]> x;
    private final int neighborsAmount;
    private final Map<Integer, BallTreeNode> nodes = new HashMap<>();
    private final PriorityQueue<DistanceIndex> kNeighbors = new PriorityQueue<>();

    public BallTree(int neighborsAmount, int leafSize, List<int[]> x) {
        this.neighborsAmount = neighborsAmount;
        this.points = new ArrayList<>();
        this.leafSize = leafSize;
        this.x = x;

        for (var i = 0; i < x.size(); i++) {
            points.add(new BallTreePoint(x.get(i), i));
        }

        createTree(1, 0, points.size());
    }

    public void clearKNeighbors() {
        kNeighbors.clear();
    }

    public PriorityQueue<DistanceIndex> getKNeighbors() {
        return kNeighbors;
    }

    public List<int[]> getX() {
        return x;
    }

    public void search(int vertexIndex, int[] newPoint) {
        var currentNode = nodes.get(vertexIndex);
        if (currentNode == null) {
            return;
        }

        var currentNodePoints = currentNode.getPoints();
        if (currentNodePoints.size() > 1) {
            for (var point : currentNodePoints) {
                if (point.coordinates() == newPoint) {
                    continue;
                }
                double distance = euclideanDistance(newPoint, point.coordinates());
                updateKNeighbors(distance, point.index(), neighborsAmount);
            }
            return;
        }

        var currentNodePivot = currentNode.getPivot();
        var leftChild = vertexIndex * 2;
        var rightChild = vertexIndex * 2 + 1;
        double pivotDistance = euclideanDistance(newPoint, currentNodePivot.coordinates());
        if (newPoint != currentNodePivot.coordinates()) {
            updateKNeighbors(pivotDistance, currentNodePivot.index(), neighborsAmount);
        }

        searchInSubTree(leftChild, newPoint);
        searchInSubTree(rightChild, newPoint);
    }

    private void createTree(int vertexIndex, int startPoint, int endPoint) {
        if (endPoint - startPoint <= 0) {
            return;
        }

        List<BallTreePoint> subPoints = points.subList(startPoint, endPoint);
        var dimension = getMaxSpreadDimension(subPoints);
        this.points = getSortedPointsByDimension(startPoint, endPoint, dimension);
        var centroidIndex = startPoint + getCentroidIndex(subPoints, dimension);
        var radius = getMaxDistance(points.get(centroidIndex), subPoints);
        var currentNode = new BallTreeNode(points.get(centroidIndex), new ArrayList<>(), (int) radius);
        nodes.put(vertexIndex, currentNode);

        if (endPoint - startPoint <= leafSize) {
            currentNode.addAllPoints(subPoints);
        } else {
            currentNode.addPoint(points.get(centroidIndex));
            createTree(vertexIndex * 2, startPoint, centroidIndex);
            createTree(vertexIndex * 2 + 1, centroidIndex + 1, endPoint);
        }
    }

    private int getMaxSpreadDimension(List<BallTreePoint> points) {
        if (points.isEmpty()) {
            throw new IllegalArgumentException("Points are empty");
        }

        var dimensions = points.get(0)
                .coordinates()
                .length;
        List<Integer> differences = getDimensionValuesDifferences(dimensions);
        var maxDimension = 0;
        for (var i = 1; i < differences.size(); i++) {
            if (differences.get(i) > differences.get(maxDimension)) {
                maxDimension = i;
            }
        }
        return maxDimension;
    }

    private List<Integer> getDimensionValuesDifferences(int dimensions) {
        List<Integer> differences = new ArrayList<>(dimensions);
        for (var i = 0; i < dimensions; i++) {
            int maxVal = Integer.MIN_VALUE;
            int minVal = Integer.MAX_VALUE;
            for (var p : points) {
                var val = p.coordinates()[i];
                if (val > maxVal) {
                    maxVal = val;
                }
                if (val < minVal) {
                    minVal = val;
                }
            }
            differences.add(maxVal - minVal);
        }
        return differences;
    }

    private List<BallTreePoint> getSortedPointsByDimension(int startPoint, int endPoint, int dimension) {
        List<BallTreePoint> result = new ArrayList<>(points);
        result.subList(startPoint, endPoint)
                .sort(Comparator.comparingInt(p -> p.coordinates()[dimension]));
        return result;
    }

    private int getCentroidIndex(List<BallTreePoint> points, int dimension) {
        if (points.isEmpty()) {
            throw new IllegalArgumentException("Points are empty");
        }

        var sum = points.stream()
                .mapToDouble(p -> p.coordinates()[dimension])
                .sum();
        double average = sum / points.size();
        var minDiff = Double.MAX_VALUE;
        var closestIndex = 0;
        for (var i = 0; i < points.size(); i++) {
            double diff = Math.abs(points.get(i).coordinates()[dimension] - average);
            if (diff < minDiff) {
                minDiff = diff;
                closestIndex = i;
            }
        }
        return closestIndex;
    }

    private double getMaxDistance(BallTreePoint point, List<BallTreePoint> points) {
        var max = 0.0;
        var coordinates = point.coordinates();

        for (var p : points) {
            var currentDist = euclideanDistance(coordinates, p.coordinates());
            if (currentDist > max) {
                max = currentDist;
            }
        }
        return max;
    }

    private double euclideanDistance(int[] a, int[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Dimensions are not the same");
        }
        var sum = 0.0;
        for (var i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(sum);
    }

    private void updateKNeighbors(double distance, int index, int neighborsAmount) {
        if (kNeighbors.size() < neighborsAmount) {
            kNeighbors.add(new DistanceIndex(distance, index));
            return;
        }

        if (distance < kNeighbors.peek().distance()) {
            kNeighbors.poll();
            kNeighbors.add(new DistanceIndex(distance, index));
        }
    }

    private void searchInSubTree(int child, int[] newPoint) {
        var node = nodes.get(child);
        if (node != null && !node.getPoints().isEmpty() && node.getPivot().coordinates() != newPoint) {
            var childDistance = euclideanDistance(newPoint, node.getPivot().coordinates());
            if (kNeighbors.size() < neighborsAmount
                    || childDistance - node.getRadius() < kNeighbors.peek().distance()) {
                search(child, newPoint);
            }
        }
    }

    public record DistanceIndex(double distance, int index) implements Comparable<DistanceIndex> {

        @Override
        public int compareTo(DistanceIndex other) {
            return Double.compare(other.distance, this.distance);
        }

    }

}
