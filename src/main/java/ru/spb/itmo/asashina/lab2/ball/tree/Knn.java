package ru.spb.itmo.asashina.lab2.ball.tree;

import ru.spb.itmo.asashina.lab2.ball.tree.BallTree.DistanceIndex;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Knn {

    private final BallTree tree;

    public Knn(List<int[]> x, int leafSize, int neighboursAmount) {
        this.tree = new BallTree(neighboursAmount, leafSize, x);
    }

    public Map<int[], List<int[]>> getKNeighbours(List<int[]> x) {
        Map<int[], List<int[]>> results = new HashMap<>();
        for (var point : x) {
            tree.clearKNeighbors();
            tree.search(1, point);

            List<int[]> neighbors = tree.getKNeighbors()
                    .stream()
                    .sorted(Comparator.comparingDouble(DistanceIndex::distance))
                    .map(di -> tree.getX().get(di.index()))
                    .toList();
            results.put(point, neighbors);
        }
        return results;
    }

}
