package tz.projects.controllers;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tz.projects.entities.DocumentRecord;
import tz.projects.service.DocumentService;

import java.io.IOException;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DocumentRecord uploadDocument(
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam MultipartFile file) throws IOException {
        return service.saveDocument(title, author, file);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentRecord> getDocument(@PathVariable String id) {
        return service.getDocument(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<InputStreamResource> getFile(@PathVariable String fileId) throws IOException {
        return service.getFile(fileId)
                .map(resource -> {
                    try {
                        return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                                .contentType(MediaType.parseMediaType(resource.getContentType()))
                                .body(new InputStreamResource(resource.getInputStream()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
}