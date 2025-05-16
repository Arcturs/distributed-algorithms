package ru.spb.itmo.asashina.lab4.environment;

import ru.spb.itmo.asashina.lab4.Document;
import ru.spb.itmo.asashina.lab4.Message;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Replica {

    private final int id;
    private final Document document;
    private final TestEnvironment env;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public Replica(int id, TestEnvironment env) {
        this.id = id;
        this.env = env;
        this.document = new Document(id);

        scheduler.scheduleAtFixedRate(this::syncWithOthers, 10, 100, TimeUnit.MILLISECONDS);
    }

    public int getId() {
        return id;
    }

    public void insert(char letter) {
        document.insert(letter);
        broadcastUpdate();
    }

    public void insert(char letter, int position) {
        document.insert(letter, position);
        broadcastUpdate();
    }

    public void delete(int position) {
        document.delete(position);
        broadcastUpdate();
    }

    public String getText() {
        return document.toText();
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    private void broadcastUpdate() {
        var message = document.prepareSyncForBroadcast();
        for (int targetReplicaId : env.getMessageQueues().keySet()) {
            if (targetReplicaId != id) {
                env.sendMessage(targetReplicaId, message);
            }
        }
    }

    private void syncWithOthers() {
        List<Message> messages = env.receiveMessages(id);
        messages.forEach(message -> document.applyRemoteOperations(message.getOperations()));
    }

}
