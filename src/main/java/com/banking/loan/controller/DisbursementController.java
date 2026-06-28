package com.banking.loan.controller;

import com.banking.loan.dto.ApiResponse;
import com.banking.loan.dto.DisbursementRequest;
import com.banking.loan.model.EMISchedule;
import com.banking.loan.model.LoanApplication;
import com.banking.loan.model.LoanDisbursement;
import com.banking.loan.model.User;
import com.banking.loan.service.AuthService;
import com.banking.loan.service.DisbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/disbursements")
public class DisbursementController {

    @Autowired private DisbursementService disbursementService;
    @Autowired private AuthService authService;

    @GetMapping("/approved")
    public ResponseEntity<ApiResponse<List<LoanApplication>>> getApproved() {
        return ResponseEntity.ok(ApiResponse.ok(disbursementService.getApprovedApplications()));
    }

    @PostMapping("/{applicationId}")
    public ResponseEntity<ApiResponse<LoanDisbursement>> disburse(
            @PathVariable Long applicationId,
            @RequestBody DisbursementRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = authService.getCurrentUser(userDetails.getUsername());
            LoanDisbursement disbursement = disbursementService.disburse(applicationId, req, user);
            return ResponseEntity.ok(ApiResponse.ok("Loan disbursed successfully", disbursement));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApiResponse<LoanDisbursement>> getDisbursement(@PathVariable Long applicationId) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(disbursementService.getDisbursementForApplication(applicationId)));
        } catch (Exception e) {
            return ResponseEntity.notFound().<ApiResponse<LoanDisbursement>>build();
        }
    }

    @GetMapping("/{applicationId}/emi-schedule")
    public ResponseEntity<ApiResponse<List<EMISchedule>>> getEmiSchedule(@PathVariable Long applicationId) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(disbursementService.getEmiSchedule(applicationId)));
        } catch (Exception e) {
            return ResponseEntity.notFound().<ApiResponse<List<EMISchedule>>>build();
        }
    }
}
