package com.banking.loan.controller;

import com.banking.loan.dto.ApiResponse;
import com.banking.loan.dto.UnderwritingRequest;
import com.banking.loan.model.LoanApplication;
import com.banking.loan.model.UnderwritingReport;
import com.banking.loan.model.User;
import com.banking.loan.service.AuthService;
import com.banking.loan.service.UnderwritingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/underwriting")
public class UnderwritingController {

    @Autowired private UnderwritingService underwritingService;
    @Autowired private AuthService authService;

    @GetMapping("/queue")
    public ResponseEntity<ApiResponse<List<LoanApplication>>> getQueue() {
        return ResponseEntity.ok(ApiResponse.ok(underwritingService.getApplicationsForUnderwriting()));
    }

    @PostMapping("/assign/{applicationId}")
    public ResponseEntity<ApiResponse<LoanApplication>> assign(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = authService.getCurrentUser(userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.ok("Assigned to underwriting",
                underwritingService.assignToUnderwriting(applicationId, user)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/report/{applicationId}")
    public ResponseEntity<ApiResponse<UnderwritingReport>> submitReport(
            @PathVariable Long applicationId,
            @RequestBody UnderwritingRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = authService.getCurrentUser(userDetails.getUsername());
            UnderwritingReport report = underwritingService.submitUnderwritingReport(applicationId, req, user);
            return ResponseEntity.ok(ApiResponse.ok("Underwriting report submitted", report));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/report/{applicationId}")
    public ResponseEntity<ApiResponse<UnderwritingReport>> getReport(@PathVariable Long applicationId) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(underwritingService.getReportForApplication(applicationId)));
        } catch (Exception e) {
            return ResponseEntity.notFound().<ApiResponse<UnderwritingReport>>build();
        }
    }
}
