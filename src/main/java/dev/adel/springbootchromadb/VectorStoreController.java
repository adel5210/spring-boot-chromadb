package dev.adel.springbootchromadb;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vector")
public class VectorStoreController {

    private final VectorStore vectorStore;

    public VectorStoreController(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostMapping("/add")
    public String addDocs(@RequestBody List<String> texts) {
        final List<Document> documents = texts.stream()
                .map(text -> new Document(text, Map.of("source", "user-input")))
                .toList();
        vectorStore.add(documents);
        return "Added " + documents.size() + " documents";
    }

    @GetMapping("/search")
    public List<Document> getDocuments(@RequestParam String query,
                                       @RequestParam(defaultValue = "3") int topK) {
        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(topK)
                        .similarityThreshold(0.7)
                        .build()
        );
    }

}
