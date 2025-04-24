package ru.spb.itmo.asashina.lab3;

public class LuceneIndexProfileRunner {

    public static void main(String[] args) {
        var searcher = new LuceneSearcher("./indexes/benchmark", 16_000);
    }

}
