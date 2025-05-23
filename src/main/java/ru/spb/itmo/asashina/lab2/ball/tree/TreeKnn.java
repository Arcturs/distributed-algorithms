package ru.spb.itmo.asashina.lab2.ball.tree;

import ru.spb.itmo.asashina.lab2.ball.tree.Utils.DistanceIndex;

import java.util.Comparator;
import java.util.List;

public class TreeKnn {

    private final BallTree tree;

    public TreeKnn(List<int[]> x, int leafSize, int neighboursAmount) {
        this.tree = new BallTree(neighboursAmount, leafSize, x);
    }

    public List<int[]> getKNeighbours(int[] x) {
        tree.clearKNeighbors();
        tree.search(1, x);

        return tree.getKNeighbors()
                .stream()
                .sorted(Comparator.comparingDouble(DistanceIndex::distance))
                .map(di -> tree.getX().get(di.index()))
                .toList();
    }

}
