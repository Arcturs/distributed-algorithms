package ru.spb.itmo.asashina.lab2.trie;

public class Trie {

    private final TrieNode root = new TrieNode();

    public void insert(String word) {
        var current = root;
        for (char letter : word.toCharArray()) {
            current = current.getChildren().computeIfAbsent(letter, character -> new TrieNode());
        }
        current.setEndOfWord(true);
    }

    public boolean delete(String word) {
        return delete(root, word, 0);
    }

    private boolean delete(TrieNode current, String word, int index) {
        if (index == word.length()) {
            if (!current.isEndOfWord()) {
                return false;
            }
            current.setEndOfWord(false);
            return current.getChildren().isEmpty();
        }

        var letter = word.charAt(index);
        var node = current.getChildren().get(letter);
        if (node == null) {
            return false;
        }

        var shouldDeleteCurrentNode = delete(node, word, index + 1) && !node.isEndOfWord();
        if (shouldDeleteCurrentNode) {
            current.getChildren().remove(letter);
            return true;
        }
        return false;
    }

    public boolean startsWith(String prefix) {
        var current = findNode(prefix);
        return current != null;
    }

    private TrieNode findNode(String prefix) {
        var current = root;
        for (int i = 0; i < prefix.length(); i++) {
            var letter = prefix.charAt(i);
            var node = current.getChildren().get(letter);
            if (node == null) {
                return null;
            }
            current = node;
        }
        return current;
    }

    public boolean containsWord(String word) {
        var current = findNode(word);
        return current != null && current.isEndOfWord();
    }

    public boolean isEmpty() {
        return root.getChildren().isEmpty();
    }

}
