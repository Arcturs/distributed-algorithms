package ru.spb.itmo.asashina.lab4;

public class Node {

    private final NodeId id;
    private final char letter;

    private Node next;
    private boolean isVisible;
    private int deletedBy = -1;

    public Node(NodeId id, char letter) {
        this.id = id;
        this.letter = letter;
        this.next = null;
        this.isVisible = true;
    }

    public NodeId getId() {
        return id;
    }

    public char getLetter() {
        return letter;
    }

    public Node getNext() {
        return next;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public int getDeletedBy() {
        return deletedBy;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void markDeleted(int replicaId) {
        this.isVisible = false;
        this.deletedBy = replicaId;
    }

}
