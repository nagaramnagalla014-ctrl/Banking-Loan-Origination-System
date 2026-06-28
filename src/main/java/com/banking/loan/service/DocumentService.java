package com.banking.loan.service;

import com.banking.loan.exception.LoanException;
import com.banking.loan.model.Document;
import com.banking.loan.model.LoanApplication;
import com.banking.loan.model.User;
import com.banking.loan.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DocumentService {

    private static final List<String> REQUIRED_DOCS =
        Arrays.asList("PAN_CARD", "AADHAR_CARD", "INCOME_PROOF", "BANK_STATEMENT", "PHOTO");

    @Autowired private DocumentRepository documentRepository;
    @Autowired private LoanApplicationService loanApplicationService;
    @Autowired private AuditService auditService;

    public Document uploadDocument(Long applicationId, String documentType,
                                   MultipartFile file, User uploader) {
        LoanApplication app = loanApplicationService.getById(applicationId);
        if (!app.getApplicant().getUserId().equals(uploader.getUserId())) {
            throw new LoanException("You can only upload documents for your own application");
        }

        Document doc = new Document();
        doc.setLoanApplication(app);
        doc.setDocumentType(documentType.toUpperCase());
        doc.setFileName(file.getOriginalFilename());
        doc.setMimeType(file.getContentType());
        doc.setFileSize(file.getSize());
        doc.setFilePath("/uploads/" + applicationId + "/" + documentType + "_" + System.currentTimeMillis());
        doc.setVerificationStatus("PENDING");

        Document saved = documentRepository.save(doc);
        auditService.log("LOAN_APPLICATION", applicationId, "DOCUMENT_UPLOADED",
            null, null, uploader, "Document uploaded: " + documentType);

        // Auto-move to DOCUMENT_PENDING status
        if (app.getStatus() == LoanApplication.ApplicationStatus.CREDIT_CHECKED) {
            loanApplicationService.updateStatus(applicationId,
                LoanApplication.ApplicationStatus.DOCUMENT_PENDING, uploader);
        }
        return saved;
    }

    public Document verifyDocument(Long documentId, String status, String remarks, User verifier) {
        Document doc = documentRepository.findById(documentId)
            .orElseThrow(() -> new LoanException("Document not found: " + documentId));
        doc.setVerificationStatus(status);
        doc.setVerificationRemarks(remarks);
        doc.setVerifiedBy(verifier);
        doc.setVerifiedAt(new Date());
        Document saved = documentRepository.save(doc);

        // Check if all docs verified
        LoanApplication app = doc.getLoanApplication();
        List<Document> docs = documentRepository.findByLoanApplication(app);
        boolean allVerified = docs.stream().allMatch(d -> "VERIFIED".equals(d.getVerificationStatus()));
        if (allVerified && docs.size() >= 3) {
            loanApplicationService.updateStatus(app.getApplicationId(),
                LoanApplication.ApplicationStatus.DOCUMENTS_VERIFIED, verifier);
        }
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Document> getDocumentsForApplication(Long applicationId) {
        LoanApplication app = loanApplicationService.getById(applicationId);
        return documentRepository.findByLoanApplication(app);
    }

    @Transactional(readOnly = true)
    public List<String> getMissingDocuments(Long applicationId) {
        LoanApplication app = loanApplicationService.getById(applicationId);
        List<Document> uploaded = documentRepository.findByLoanApplication(app);
        List<String> uploaded_types = new java.util.ArrayList<>();
        for (Document d : uploaded) uploaded_types.add(d.getDocumentType());

        List<String> missing = new java.util.ArrayList<>();
        for (String req : REQUIRED_DOCS) {
            if (!uploaded_types.contains(req)) missing.add(req);
        }
        return missing;
    }
}
