package tz.projects.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tz.projects.entities.DocumentRecord;
import tz.projects.service.DocumentService;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentControllerTest {

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController documentController;

    @Test
    void uploadDocumentReturnsDocumentRecordOnValidInput() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        DocumentRecord expectedRecord = new DocumentRecord("1", "Title", "Author");
        when(documentService.saveDocument("Title", "Author", file)).thenReturn(expectedRecord);

        DocumentRecord result = documentController.uploadDocument("Title", "Author", file);

        assertEquals(expectedRecord, result);
    }

    @Test
    void getDocumentReturnsDocumentRecordWhenFound() {
        DocumentRecord expectedRecord = new DocumentRecord("1", "Title", "Author");
        when(documentService.getDocument("1")).thenReturn(Optional.of(expectedRecord));

        ResponseEntity<DocumentRecord> response = documentController.getDocument("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRecord, response.getBody());
    }

    @Test
    void getDocumentReturnsNotFoundWhenDocumentDoesNotExist() {
        when(documentService.getDocument("1")).thenReturn(Optional.empty());

        ResponseEntity<DocumentRecord> response = documentController.getDocument("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getFileReturnsNotFoundWhenFileDoesNotExist() throws IOException {
        when(documentService.getFile("1")).thenReturn(Optional.empty());

        ResponseEntity<InputStreamResource> response = documentController.getFile("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
