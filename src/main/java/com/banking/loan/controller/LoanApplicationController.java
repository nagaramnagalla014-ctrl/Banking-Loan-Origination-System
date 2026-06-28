package com.banking.loan.controller;

import com.banking.loan.dto.ApiResponse;
import com.banking.loan.dto.LoanApplicationRequest;
import com.banking.loan.model.AuditLog;
import com.banking.loan.model.LoanApplication;
import com.banking.loan.model.User;
import com.banking.loan.service.AuditService;
import com.banking.loan.service.AuthService;
import com.banking.loan.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanApplicationController {

    @Autowired private LoanApplicationService loanApplicationService;
    @Autowired private AuthService authService;
    @Autowired private AuditService auditService;

    @PostMapping
    public ResponseEntity<ApiResponse<LoanApplication>> submit(
            @RequestBody LoanApplicationRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = authService.getCurrentUser(userDetails.getUsername());
            LoanApplication app = loanApplicationService.submitApplication(req, user);
            return ResponseEntity.ok(ApiResponse.ok("Application submitted", app));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<LoanApplication>>> myApplications(
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = authService.getCurrentUser(userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.ok(loanApplicationService.getMyApplications(user)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LoanApplication>> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(loanApplicationService.getById(id)));
        } catch (Exception e) {
            return ResponseEntity.notFound().<ApiResponse<LoanApplication>>build();
        }
    }

    @GetMapping("/track/{applicationNumber}")
    public ResponseEntity<ApiResponse<LoanApplication>> track(@PathVariable String applicationNumber) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(loanApplicationService.getByApplicationNumber(applicationNumber)));
        } catch (Exception e) {
            return ResponseEntity.notFound().<ApiResponse<LoanApplication>>build();
        }
    }

    @GetMapping("/{id}/audit")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditTrail(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(auditService.getLogsForApplication(id)));
    }
}
