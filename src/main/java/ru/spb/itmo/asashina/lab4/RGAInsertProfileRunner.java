package ru.spb.itmo.asashina.lab4;

public class RGAInsertProfileRunner {

    public static void main(String[] args) {
        var document = new Document(1);
        for (var i = 0; i < 5_000; i++) {
            document.insert('a');
        }
    }

}
