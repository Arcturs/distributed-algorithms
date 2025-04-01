package ru.spb.itmo.asashina.lab2.ball.tree;

import ru.spb.itmo.asashina.lab2.ball.tree.Utils.DistanceIndex;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static ru.spb.itmo.asashina.lab2.ball.tree.Utils.euclideanDistance;
import static ru.spb.itmo.asashina.lab2.ball.tree.Utils.updateKNeighbors;

public class LinearKnn {

    private final List<int[]> x;
    private final int neighboursAmount;

    public LinearKnn(List<int[]> x, int neighboursAmount) {
        this.x = x;
        this.neighboursAmount = neighboursAmount;
    }

    public List<int[]> getKNeighbours(int[] x) {
        var queue = new PriorityQueue<DistanceIndex>();
        for (var i = 0; i < this.x.size(); i++) {
            var current = this.x.get(i);
            if (current == x) {
                continue;
            }
            var distance = euclideanDistance(current, x);
            updateKNeighbors(queue, neighboursAmount, distance, i);
        }
        return queue.stream()
                .sorted(Comparator.comparingDouble(DistanceIndex::distance))
                .map(di -> this.x.get(di.index()))
                .toList();
    }

}
