package ru.spb.itmo.asashina.lab1.ext.hash;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import static java.nio.file.StandardOpenOption.APPEND;
import static ru.spb.itmo.asashina.lab1.ext.hash.Directory.PARENT_DIRECTORY;

public class Bucket<T> {

    private static final Logger log = Logger.getLogger(Bucket.class.getName());

    private final long capacity;

    private int id;
    private String fileName;
    private long localDepth = 0;
    private long remainingSize;

    public Bucket(long capacity, int lastNBits) {
        this.capacity = capacity;
        this.remainingSize = capacity;
        this.id = lastNBits;
        localDepth++;
        try {
            var parentPath = PARENT_DIRECTORY + "/" + lastNBits;
            Files.createDirectories(Path.of(parentPath));
            fileName = parentPath + "/" + "bucket.dat";
            Files.createFile(Path.of(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Bucket(long capacity) {
        this.capacity = capacity;
        this.remainingSize = capacity;
        localDepth++;
    }

    public int getId() {
        return id;
    }

    public long getLocalDepth() {
        return localDepth;
    }

    public void setLocalDepth(long localDepth) {
        this.localDepth = localDepth;
        remainingSize = capacity;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean insert(T element, int lastNBits) {
        checkIfFileExists(lastNBits);
        if (remainingSize - 1 >= 0) {
            try (DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(
                            Files.newOutputStream(Path.of(fileName), APPEND)))) {
                dos.writeLong(remainingSize);
                dos.writeUTF(element.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            remainingSize--;
            return true;
        }

        return false;
    }

    public boolean insert(String element, int lastNBits) {
        checkIfFileExists(lastNBits);
        if (remainingSize - 1 >= 0) {
            try (DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(
                            Files.newOutputStream(Path.of(fileName), APPEND)))) {
                dos.writeLong(remainingSize);
                dos.writeUTF(element);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            remainingSize--;
            return true;
        }

        return false;
    }

    public boolean insert(DataOutputStream dos, String element, int lastNBits) {
        checkIfFileExists(lastNBits);
        if (remainingSize - 1 >= 0) {
            try {
                dos.writeLong(remainingSize);
                dos.writeUTF(element);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            remainingSize--;
            return true;
        }

        return false;
    }

    public boolean exists(T element) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileName))) {
            while (true) {
                var remainingSize = dis.readLong();
                var value = dis.readUTF();
                if (value.equals(element.toString())) {
                    return true;
                }
                if (remainingSize == 1) {
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private void checkIfFileExists(int lastNBits) {
        if (fileName == null) {
            try {
                this.id = lastNBits;
                var parentPath = PARENT_DIRECTORY + "/" + lastNBits;
                Files.createDirectories(Path.of(parentPath));
                fileName = parentPath + "/" + "bucket.dat";
                var path = Path.of(fileName);
                if (Files.exists(path)) {
                    Files.delete(path);
                }
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
