package ru.spb.itmo.asashina.lab2.ball.tree;

import java.util.ArrayList;
import java.util.List;

public class BallTreeNode {

    private final BallTreePoint pivot;
    private final Integer radius;
    private final List<BallTreePoint> points;

    public BallTreeNode(BallTreePoint pivot, List<BallTreePoint> points, Integer radius) {
        this.pivot = pivot;
        this.radius = radius;
        this.points = new ArrayList<>(points);
    }

    public List<BallTreePoint> getPoints() {
        return new ArrayList<>(points);
    }

    public BallTreePoint getPivot() {
        return pivot;
    }

    public int getRadius() {
        return radius;
    }

    public void addAllPoints(List<BallTreePoint> points) {
        this.points.addAll(points);
    }

    public void addPoint(BallTreePoint point) {
        this.points.add(point);
    }

}
