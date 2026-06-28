package com.banking.loan.repository;

import com.banking.loan.model.EMISchedule;
import com.banking.loan.model.LoanDisbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EMIScheduleRepository extends JpaRepository<EMISchedule, Long> {
    List<EMISchedule> findByDisbursementOrderByInstallmentNumber(LoanDisbursement disbursement);
    List<EMISchedule> findByDisbursementAndStatus(LoanDisbursement disbursement, String status);
}
