package ru.spb.itmo.asashina.lab4;

import ru.spb.itmo.asashina.lab4.operation.InsertOperation;
import ru.spb.itmo.asashina.lab4.operation.Operation;

import java.util.List;
import java.util.Map;

public class Message {

    private final int sourceId;
    private final Map<Integer, Long> documentClocks;
    private final List<Operation> operations;

    public Message(int sourceId, Map<Integer, Long> documentClocks, List<Operation> operations) {
        this.sourceId = sourceId;
        this.documentClocks = documentClocks;
        this.operations = operations;
    }

    public int getSourceId() {
        return sourceId;
    }

    public Map<Integer, Long> getDocumentClocks() {
        return documentClocks;
    }

    public List<Operation> getOperations() {
        return operations;
    }



}
