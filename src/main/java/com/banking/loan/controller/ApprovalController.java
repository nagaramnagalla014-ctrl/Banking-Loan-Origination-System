package com.banking.loan.controller;

import com.banking.loan.dto.ApiResponse;
import com.banking.loan.dto.ApprovalRequest;
import com.banking.loan.model.LoanApproval;
import com.banking.loan.model.LoanApplication;
import com.banking.loan.model.User;
import com.banking.loan.service.ApprovalService;
import com.banking.loan.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    @Autowired private ApprovalService approvalService;
    @Autowired private AuthService authService;

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<LoanApplication>>> getPending() {
        return ResponseEntity.ok(ApiResponse.ok(approvalService.getPendingApprovals()));
    }

    @PostMapping("/{applicationId}")
    public ResponseEntity<ApiResponse<LoanApproval>> processApproval(
            @PathVariable Long applicationId,
            @RequestBody ApprovalRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = authService.getCurrentUser(userDetails.getUsername());
            LoanApproval approval = approvalService.processApproval(applicationId, req, user);
            return ResponseEntity.ok(ApiResponse.ok("Decision recorded: " + req.getDecision(), approval));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApiResponse<LoanApproval>> getApproval(@PathVariable Long applicationId) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(approvalService.getApprovalForApplication(applicationId)));
        } catch (Exception e) {
            return ResponseEntity.notFound().<ApiResponse<LoanApproval>>build();
        }
    }
}
