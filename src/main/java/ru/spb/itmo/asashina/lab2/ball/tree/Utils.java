package ru.spb.itmo.asashina.lab2.ball.tree;

import java.util.PriorityQueue;

public class Utils {

    public static double euclideanDistance(int[] a, int[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Dimensions are not the same");
        }
        var sum = 0.0;
        for (var i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(sum);
    }

    public static void updateKNeighbors(
            PriorityQueue<DistanceIndex> kNeighbors,
            int neighborsAmount,
            double distance,
            int index) {

        if (kNeighbors.size() < neighborsAmount) {
            kNeighbors.add(new DistanceIndex(distance, index));
            return;
        }

        if (distance < kNeighbors.peek().distance()) {
            kNeighbors.poll();
            kNeighbors.add(new DistanceIndex(distance, index));
        }
    }

    public record DistanceIndex(double distance, int index) implements Comparable<DistanceIndex> {

        @Override
        public int compareTo(DistanceIndex other) {
            return Double.compare(other.distance, this.distance);
        }

    }

}
