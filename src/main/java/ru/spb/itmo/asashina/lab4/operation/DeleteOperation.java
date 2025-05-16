package ru.spb.itmo.asashina.lab4.operation;

import ru.spb.itmo.asashina.lab4.NodeId;

public class DeleteOperation extends Operation {

    private final int deletedBy;

    public DeleteOperation(NodeId nodeId, int deletedBy) {
        super(nodeId);
        this.deletedBy = deletedBy;
    }

    public int getDeletedBy() {
        return deletedBy;
    }

}
