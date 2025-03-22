package ru.spb.itmo.asashina.lab2.ball.tree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KnnProfileRunner {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        var data = readCsvFile();
        var knn = new Knn(data, 25, 15);
        knn.getKNeighbours(data.get(RANDOM.nextInt(999)));
    }

    private static List<int[]> readCsvFile() {
        boolean isFirstLine = true;
        var size = 0;
        var data = new ArrayList<int[]>();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/fashion-mnist_test.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] columns = line.split(",");
                if (columns.length <= 1) continue;

                int[] rowData = new int[columns.length - 1];
                for (int i = 1; i < columns.length; i++) {
                    rowData[i - 1] = Integer.parseInt(columns[i].trim());
                }
                data.add(rowData);
                size++;
                if (size == 1_000) {
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

}
