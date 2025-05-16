package ru.spb.itmo.asashina.lab4.operation;

import ru.spb.itmo.asashina.lab4.NodeId;

public abstract class Operation {

    protected final NodeId nodeId;

    public Operation(NodeId nodeId) {
        this.nodeId = nodeId;
    }

    public NodeId getNodeId() {
        return nodeId;
    }

}
