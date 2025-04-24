package ru.spb.itmo.asashina.lab3;

import jakarta.annotation.PreDestroy;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.lucene.document.Field.Store.YES;

@Component
public class LuceneSearcher {

    private static final String DEFAULT_INDEX_PATH = "./indexes";
    private static final int BUFFER_SIZE = 1024;
    private static final QueryParser QUERY_PARSER = new QueryParser("description", new StandardAnalyzer());

    private String indexPath = DEFAULT_INDEX_PATH;

    private Directory indexDirectory;

    public LuceneSearcher(String indexPath, int rowNumber) {
        this.indexPath = indexPath;
        try {
            index(rowNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public LuceneSearcher(int rowNumber) {
        try {
            index(rowNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public LuceneSearcher() {
        try {
            index();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getIndexPath() {
        return indexPath;
    }

    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }

    public List<Document> search(RequestQuery inQuery) {
        try (var indexReader = DirectoryReader.open(indexDirectory)) {
            var indexSearcher = new IndexSearcher(indexReader);
            var query = QUERY_PARSER.parse(inQuery.getQuery());
            var result = indexSearcher.search(query, inQuery.getResultAmount());
            var storedFields = indexSearcher.storedFields();
            List<Document> documents = new ArrayList<>();
            for (var score : result.scoreDocs) {
                var document = storedFields.document(score.doc);
                documents.add(document);
            }
            return documents;
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void index() throws IOException {
        index(16_000);
    }

    private void index(int rowNumber) throws IOException {
        index("./resources/netflix_movies_detailed_up_to_2025.csv", rowNumber);
    }

    private void index(String dataFileName, int rowNumber) throws IOException {
        indexDirectory = FSDirectory.open(new File(indexPath).toPath());
        if (indexDirectory.listAll().length > 0) {
            return;
        }

        try (var indexWriter = new IndexWriter(indexDirectory, new IndexWriterConfig())) {
            boolean isFirstLine = true;
            var size = 0;
            var buffer = new ArrayList<Document>();
            try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }
                    var document = mapToDocument(line);
                    if (document == null) {
                        continue;
                    }
                    if (buffer.size() < BUFFER_SIZE) {
                        buffer.add(document);
                    } else {
                        indexWriter.addDocuments(buffer);
                        buffer.clear();
                    }
                    size++;
                    if (size == rowNumber) {
                        break;
                    }
                }
            }
            if (!buffer.isEmpty()) {
                indexWriter.addDocuments(buffer);
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private Document mapToDocument(String row) {
        String[] columns = row.split(";");
        if (columns.length <= 1) {
            return null;
        }
        var document = new Document();
        document.add(new TextField("show_id", columns[0], YES));
        document.add(new TextField("title", columns[1], YES));
        document.add(new TextField("director", columns[2], YES));
        document.add(new TextField("cast", columns[3], YES));
        if (columns.length == 4) {
            return document;
        }
        document.add(new TextField("country", columns[4], YES));
        document.add(new TextField("date_added", columns[5], YES));
        document.add(new IntField("release_year", Integer.parseInt(columns[6]), YES));
        document.add(new FloatField("rating", Float.parseFloat(columns[7]), YES));
        document.add(new TextField("genres", columns[8], YES));
        document.add(new TextField("language", columns[9], YES));
        document.add(new TextField("description", columns[10], YES));
        if (columns.length == 11) {
            return document;
        }
        document.add(new FloatField("popularity", Float.parseFloat(columns[11]), YES));
        document.add(new LongField("budget", Long.parseLong(columns[12]), YES));
        document.add(new LongField("revenue", Long.parseLong(columns[13]), YES));
        return document;
    }

    @PreDestroy
    private void preDestroy() throws IOException {
        indexDirectory.close();
    }

}
