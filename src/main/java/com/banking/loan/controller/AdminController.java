package com.banking.loan.controller;

import com.banking.loan.dto.ApiResponse;
import com.banking.loan.model.AuditLog;
import com.banking.loan.model.LoanApplication;
import com.banking.loan.model.User;
import com.banking.loan.repository.UserRepository;
import com.banking.loan.service.AuditService;
import com.banking.loan.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private LoanApplicationService loanApplicationService;
    @Autowired private AuditService auditService;
    @Autowired private UserRepository userRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> dashboard() {
        Map<String, Object> stats = new HashMap<>();
        List<LoanApplication> all = loanApplicationService.getAllApplications();
        stats.put("totalApplications", all.size());
        stats.put("totalCustomers", userRepository.findByRole(User.Role.CUSTOMER).size());
        stats.put("pendingCreditCheck", loanApplicationService.getByStatus("SUBMITTED").size());
        stats.put("pendingApproval", loanApplicationService.getByStatus("PENDING_APPROVAL").size());
        stats.put("approved", loanApplicationService.getByStatus("APPROVED").size());
        stats.put("disbursed", loanApplicationService.getByStatus("DISBURSED").size());
        stats.put("rejected", loanApplicationService.getByStatus("REJECTED").size());
        stats.put("recentApplications", all.stream().limit(10).toArray());
        return ResponseEntity.ok(ApiResponse.ok(stats));
    }

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<List<LoanApplication>>> getAllApplications(
            @RequestParam(required = false) String status) {
        List<LoanApplication> apps = (status != null && !status.trim().isEmpty())
            ? loanApplicationService.getByStatus(status)
            : loanApplicationService.getAllApplications();
        return ResponseEntity.ok(ApiResponse.ok(apps));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(
            @RequestParam(required = false) String role) {
        List<User> users = (role != null && !role.trim().isEmpty())
            ? userRepository.findByRole(User.Role.valueOf(role))
            : userRepository.findAll();
        return ResponseEntity.ok(ApiResponse.ok(users));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogs() {
        return ResponseEntity.ok(ApiResponse.ok(auditService.getAllLogs()));
    }

    @PutMapping("/users/{userId}/toggle")
    public ResponseEntity<ApiResponse<User>> toggleUserStatus(@PathVariable Long userId) {
        return userRepository.findById(userId).map(user -> {
            user.setIsActive(!user.getIsActive());
            userRepository.save(user);
            return ResponseEntity.ok(ApiResponse.ok("User status toggled", user));
        }).orElse(ResponseEntity.notFound().<ApiResponse<User>>build());
    }
}
