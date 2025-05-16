package ru.spb.itmo.asashina.lab4;

public record NodeId(long timestamp, int id) implements Comparable<NodeId> {

    @Override
    public int compareTo(NodeId o) {
        if (timestamp != o.timestamp) {
            return Long.compare(timestamp, o.timestamp);
        }
        return Integer.compare(id, o.id);
    }

}
