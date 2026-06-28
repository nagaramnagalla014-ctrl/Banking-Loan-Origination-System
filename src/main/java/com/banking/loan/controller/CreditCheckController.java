package com.banking.loan.controller;

import com.banking.loan.dto.ApiResponse;
import com.banking.loan.model.CreditReport;
import com.banking.loan.model.LoanApplication;
import com.banking.loan.model.User;
import com.banking.loan.service.AuthService;
import com.banking.loan.service.CreditCheckService;
import com.banking.loan.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/credit-check")
public class CreditCheckController {

    @Autowired private CreditCheckService creditCheckService;
    @Autowired private LoanApplicationService loanApplicationService;
    @Autowired private AuthService authService;

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<LoanApplication>>> pendingApplications() {
        return ResponseEntity.ok(ApiResponse.ok(loanApplicationService.getByStatus("SUBMITTED")));
    }

    @PostMapping("/initiate/{applicationId}")
    public ResponseEntity<ApiResponse<CreditReport>> initiate(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = authService.getCurrentUser(userDetails.getUsername());
            CreditReport report = creditCheckService.initiateCreditCheck(applicationId, user);
            return ResponseEntity.ok(ApiResponse.ok("Credit check completed", report));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApiResponse<CreditReport>> getReport(@PathVariable Long applicationId) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(creditCheckService.getReportForApplication(applicationId)));
        } catch (Exception e) {
            return ResponseEntity.notFound().<ApiResponse<CreditReport>>build();
        }
    }
}
