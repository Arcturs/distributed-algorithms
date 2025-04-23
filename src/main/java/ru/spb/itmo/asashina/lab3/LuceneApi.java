package ru.spb.itmo.asashina.lab3;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LuceneApi {

    private final LuceneSearcher searcher = new LuceneSearcher();

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok("pong");
    }

    @PostMapping("/query")
    public ResponseEntity<?> findByQuery(@RequestBody RequestQuery query) {
        return ResponseEntity.ok(searcher.search(query));
    }

}
