package ru.spb.itmo.asashina.lab4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spb.itmo.asashina.lab4.operation.DeleteOperation;
import ru.spb.itmo.asashina.lab4.operation.InsertOperation;
import ru.spb.itmo.asashina.lab4.operation.Operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Document {

    private static final Logger log = LoggerFactory.getLogger(Document.class);

    private final Node root;
    private final int id;
    private final AtomicLong logicTime = new AtomicLong(0);
    private final Map<NodeId, Node> nodeTree = new HashMap<>();
    private final Map<Integer, Long> clocks = new HashMap<>();

    public Document(int id) {
        this.root = new Node(new NodeId(Long.MIN_VALUE, id), '\0');
        this.id = id;
        clocks.put(id, logicTime.get());
    }

    public String toText() {
        List<Character> text = new ArrayList<>();
        var current = root.getNext();
        while (current != null) {
            if (current.isVisible()) {
                text.add(current.getLetter());
            }
            current = current.getNext();
        }
        var sb = new StringBuilder();
        text.forEach(sb::append);
        return sb.toString();
    }

    public void applyRemoteOperations(List<Operation> operations) {
        operations.forEach(op -> {
            if (op instanceof InsertOperation) {
                handleInsert((InsertOperation) op);
            } else if (op instanceof DeleteOperation) {
                handleDelete((DeleteOperation) op);
            }
        });
    }

    public Message prepareSyncForBroadcast() {
        return new Message(id, new HashMap<>(clocks), getPendingOperations());
    }

    public void insert(char letter) {
        var prevNodeId = findLastVisibleNode().getId();
        insert(letter, prevNodeId);
    }

    public void insert(char letter, int position) {
        var prevNode = findNodeAtPosition(position);
        insert(letter, prevNode.getId());
    }

    public void delete(int position) {
        var node = findNodeAtPosition(position);
        if (node != null) {
            delete(node.getId());
        }
    }

    private Node findLastVisibleNode() {
        var current = root;
        var lastVisible = root;
        while (current != null) {
            if (current.isVisible()) {
                lastVisible = current;
            }
            current = current.getNext();
        }

        return lastVisible;
    }

    private void insert(char letter, NodeId prevNodeId) {
        var prevNode = nodeTree.getOrDefault(prevNodeId, root);
        var newNode = new Node(new NodeId(logicTime.incrementAndGet(), id), letter);
        while (prevNode.getNext() != null && prevNode.getNext().getId().compareTo(newNode.getId()) < 0) {
            prevNode = prevNode.getNext();
        }

        synchronized (this) {
            newNode.setNext(prevNode.getNext());
            prevNode.setNext(newNode);
            nodeTree.put(newNode.getId(), newNode);
        }
        clocks.merge(id, logicTime.get(), Math::max);
        log.debug("Добавлена новая буква {} в документ {} в момент времени {}, текущее состояние {}",
                letter, id, clocks.get(id), toText());
    }

    private Node findNodeAtPosition(int targetPos) {
        var current = root;
        int pos = 0;
        while (current != null) {
            if (current.isVisible()) {
                if (pos == targetPos) {
                    return current;
                }
                pos++;
            }
            current = current.getNext();
        }
        return root;
    }

    private void delete(NodeId nodeId) {
        var node = nodeTree.get(nodeId);
        if (node != null) {
            node.markDeleted(id);
        }
        clocks.merge(id, nodeId.timestamp(), Math::max);
        log.debug("Удален символ в документе {} в момент времени {}, текущее состояние {}",
                id, clocks.get(id), toText());
    }

    private NodeId findPreviousNodeId(Node node) {
        Node current = root;
        while (current.getNext() != null && current.getNext() != node) {
            current = current.getNext();
        }
        return current.getId();
    }

    private void handleInsert(InsertOperation op) {
        if (nodeTree.containsKey(op.getNodeId())) {
            return;
        }

        var newNode = new Node(op.getNodeId(), op.getLetter());
        nodeTree.put(op.getNodeId(), newNode);

        Node current = nodeTree.get(op.getPrevNodeId());
        if (current == null) {
            current = root;
        }

        while (current.getNext() != null && current.getNext().getId().compareTo(op.getNodeId()) < 0) {
            current = current.getNext();
        }

        synchronized (this) {
            newNode.setNext(current.getNext());
            current.setNext(newNode);
        }

        clocks.merge(op.getNodeId().id(), op.getNodeId().timestamp(), Math::max);
        log.debug("Добавлена новая буква {} в документ {} в момент времени {}, текущее состояние {}",
                op.getLetter(), id, clocks.get(id), toText());
    }

    private void handleDelete(DeleteOperation op) {
        Node node = nodeTree.get(op.getNodeId());
        if (node != null) {
            node.markDeleted(op.getDeletedBy());
            node.setVisible(false);
        }
        clocks.merge(op.getNodeId().id(), op.getNodeId().timestamp(), Math::max);
        log.debug("Удален символ в документе {} в момент времени {}, текущее состояние {}",
                id, clocks.get(id), toText());
    }

    private List<Operation> getPendingOperations() {
        return nodeTree.values()
                .stream()
                .filter(node -> {
                    if (node.getId().id() == id && node.isVisible()) {
                        return true;
                    }
                    return !node.isVisible() && node.getDeletedBy() == id;
                })
                .map(node -> {
                    if (node.isVisible()) {
                        return new InsertOperation(node.getId(), node.getLetter(), findPreviousNodeId(node));
                    }
                    return new DeleteOperation(node.getId(), node.getDeletedBy());
                })
                .toList();
    }

}
