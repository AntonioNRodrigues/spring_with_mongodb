package tz.projects.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import tz.projects.entities.DocumentRecord;

public interface DocumentRecordRepository extends MongoRepository<DocumentRecord, String> {}