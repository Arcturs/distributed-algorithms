package ru.spb.itmo.asashina.lab4.operation;

import ru.spb.itmo.asashina.lab4.NodeId;

public class InsertOperation extends Operation {

    private final char letter;
    private final NodeId prevNodeId;

    public InsertOperation(NodeId nodeId, char letter, NodeId prevNodeId) {
        super(nodeId);
        this.letter = letter;
        this.prevNodeId = prevNodeId;
    }

    public char getLetter() {
        return letter;
    }

    public NodeId getPrevNodeId() {
        return prevNodeId;
    }

}
