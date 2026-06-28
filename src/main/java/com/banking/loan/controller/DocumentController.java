package com.banking.loan.controller;

import com.banking.loan.dto.ApiResponse;
import com.banking.loan.model.Document;
import com.banking.loan.model.User;
import com.banking.loan.service.AuthService;
import com.banking.loan.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired private DocumentService documentService;
    @Autowired private AuthService authService;

    @PostMapping("/upload/{applicationId}")
    public ResponseEntity<ApiResponse<Document>> upload(
            @PathVariable Long applicationId,
            @RequestParam String documentType,
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = authService.getCurrentUser(userDetails.getUsername());
            Document doc = documentService.uploadDocument(applicationId, documentType, file, user);
            return ResponseEntity.ok(ApiResponse.ok("Document uploaded", doc));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<ApiResponse<List<Document>>> getDocuments(@PathVariable Long applicationId) {
        return ResponseEntity.ok(ApiResponse.ok(documentService.getDocumentsForApplication(applicationId)));
    }

    @GetMapping("/application/{applicationId}/missing")
    public ResponseEntity<ApiResponse<List<String>>> getMissing(@PathVariable Long applicationId) {
        return ResponseEntity.ok(ApiResponse.ok(documentService.getMissingDocuments(applicationId)));
    }

    @PutMapping("/{documentId}/verify")
    public ResponseEntity<ApiResponse<Document>> verify(
            @PathVariable Long documentId,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = authService.getCurrentUser(userDetails.getUsername());
            Document doc = documentService.verifyDocument(
                documentId, body.get("status"), body.get("remarks"), user);
            return ResponseEntity.ok(ApiResponse.ok("Document verification updated", doc));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
