package tz.projects.service;


import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tz.projects.entities.DocumentRecord;
import tz.projects.repositories.DocumentRecordRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class DocumentService {

    private final DocumentRecordRepository repository;
    private final GridFsTemplate gridFsTemplate;

    public DocumentService(DocumentRecordRepository repository, GridFsTemplate gridFsTemplate) {
        this.repository = repository;
        this.gridFsTemplate = gridFsTemplate;
    }

    public DocumentRecord saveDocument(String title, String author, MultipartFile file) throws IOException {
        String fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType()).toString();

        DocumentRecord record = new DocumentRecord();
        record.setTitle(title);
        record.setAuthor(author);
        record.setCreatedAt(LocalDateTime.now());
        record.setFileId(fileId);

        return repository.save(record);
    }

    public Optional<GridFsResource> getFile(String fileId) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(query(where("_id").is(fileId)));
        return Optional.of(gridFsTemplate.getResource(gridFSFile));
    }

    public Optional<DocumentRecord> getDocument(String id) {
        return repository.findById(id);
    }
}