package ru.spb.itmo.asashina.lab4;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spb.itmo.asashina.lab4.environment.Replica;
import ru.spb.itmo.asashina.lab4.environment.TestEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RGATest {

    private static final Random RANDOM = ThreadLocalRandom.current();

    @BeforeAll
    static void beforeAll() {
        LoggerContext ctx = (LoggerContext) LoggerFactory.getILoggerFactory();
        ctx.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.DEBUG);
    }

    @AfterAll
    static void afterAll() {
        LoggerContext ctx = (LoggerContext) LoggerFactory.getILoggerFactory();
        ctx.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.INFO);
    }

    @Test
    void sequentialAndRandomEditTest() throws InterruptedException {
        var env = new TestEnvironment();
        var writer = new Replica(1, env);
        var editor = new Replica(2, env);
        env.registerReplica(1);
        env.registerReplica(2);

        ScheduledExecutorService editorExecutor = Executors.newScheduledThreadPool(1);
        editorExecutor.scheduleAtFixedRate(() -> {
            String currentText = editor.getText();
            if (!StringUtils.isBlank(currentText)) {
                int pos = RANDOM.nextInt(currentText.length() - 1);
                if (RANDOM.nextBoolean()) {
                    editor.delete(pos);
                } else {
                    var newChar = (char) ('a' + RANDOM.nextInt(26));
                    editor.insert(newChar, pos);
                }
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

        String text = "Cat";
        for (char c : text.toCharArray()) {
            writer.insert(c);
            try {
                Thread.sleep(RANDOM.nextInt(10));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Thread.sleep(2_000);
        editorExecutor.shutdownNow();
        editorExecutor.awaitTermination(600, TimeUnit.MILLISECONDS);

        Thread.sleep(1_000);

        assertEquals(writer.getText(), editor.getText());
        System.out.println(writer.getText());
        System.out.println(editor.getText());
        writer.shutdown();
        editor.shutdown();
    }

    @Test
    void randomReplicaEditsTest() throws InterruptedException {
        var env = new TestEnvironment();
        var replicas = IntStream.range(0, 5)
                .mapToObj(id -> new Replica(id, env))
                .toList();
        replicas.forEach(r -> env.registerReplica(r.getId()));

        List<ScheduledExecutorService> executors = new ArrayList<>();
        for (Replica replica : replicas) {
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(() -> {
                String text = replica.getText();
                if (text.isEmpty() || RANDOM.nextBoolean()) {
                    char c = (char) ('a' + RANDOM.nextInt(26));
                    replica.insert(c);
                } else {
                    int pos = RANDOM.nextInt(text.length());
                    replica.delete(pos);
                }
            }, 0, 500, TimeUnit.MILLISECONDS);
            executors.add(exec);
        }

        Thread.sleep(2_000);
        executors.forEach(it -> {
            it.shutdownNow();
            try {
                it.awaitTermination(600, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread.sleep(600);

        String firstText = replicas.get(0).getText();
        System.out.println(firstText);
        for (Replica r : replicas) {
            assertEquals(firstText, r.getText());
            r.shutdown();
        }
    }

    @Test
    void conflictTest() throws InterruptedException {
        var env = new TestEnvironment();
        Replica replicaA = new Replica(1, env);
        Replica replicaB = new Replica(2, env);
        env.registerReplica(1);
        env.registerReplica(2);

        String initialText = "I love potatoes";
        for (char c : initialText.toCharArray()) {
            replicaA.insert(c);
        }
        Thread.sleep(1_000);

        Runnable replaceTaskA = () -> replaceTask(replicaA, "bananas");
        Runnable replaceTaskB = () -> replaceTask(replicaB, "cucumbers");

        new Thread(replaceTaskA).start();
        new Thread(replaceTaskB).start();
        Thread.sleep(1_000);

        String resultA = replicaA.getText();
        String resultB = replicaB.getText();
        System.out.println(resultA);
        System.out.println(resultB);
        assertEquals(resultA, resultB);
        replicaA.shutdown();
        replicaB.shutdown();
    }

    private static void replaceTask(Replica replica, String replacement) {
        String text = replica.getText();
        int start = text.indexOf("potatoes") + 1;
        int end = start + "potatoes".length();

        for (int i = end - 1; i >= start; i--) {
            replica.delete(i);
        }
        for (char c : replacement.toCharArray()) {
            replica.insert(c, text.indexOf("love") + 5);
        }
    }

}
