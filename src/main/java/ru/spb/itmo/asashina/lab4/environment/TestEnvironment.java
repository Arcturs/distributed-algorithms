package ru.spb.itmo.asashina.lab4.environment;

import ru.spb.itmo.asashina.lab4.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

public class TestEnvironment {

    private static final int NETWORK_DEFAULT_VALUE_DELAY = 50;
    private static final Random RANDOM = ThreadLocalRandom.current();

    private final Map<Integer, BlockingQueue<Message>> messageQueues = new ConcurrentHashMap<>();

    public Map<Integer, BlockingQueue<Message>> getMessageQueues() {
        return messageQueues;
    }

    public void sendMessage(int targetReplicaId, Message message) {
        CompletableFuture.runAsync(() -> {
            messageQueues.get(targetReplicaId).add(message);
            try {
                Thread.sleep(RANDOM.nextInt(NETWORK_DEFAULT_VALUE_DELAY));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).join();
    }

    public void registerReplica(int replicaId) {
        messageQueues.put(replicaId, new LinkedBlockingQueue<>());
    }

    public List<Message> receiveMessages(int replicaId) {
        List<Message> messages = new ArrayList<>();
        messageQueues.get(replicaId).drainTo(messages);
        return messages;
    }

}
