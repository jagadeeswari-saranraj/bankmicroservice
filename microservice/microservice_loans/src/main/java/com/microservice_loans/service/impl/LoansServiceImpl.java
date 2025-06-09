package com.microservice_loans.service.impl;

import com.microservice_loans.constants.LoansConstants;
import com.microservice_loans.dto.LoansDto;
import com.microservice_loans.entity.Loans;
import com.microservice_loans.exception.LoanAlreadyExistsException;
import com.microservice_loans.exception.ResourceNotFoundException;
import com.microservice_loans.mapper.LoansMapper;
import com.microservice_loans.repository.LoansRepository;
import com.microservice_loans.service.ILoansService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class LoansServiceImpl implements ILoansService {

    private LoansRepository loansRepository;

    public LoansServiceImpl(LoansRepository loansRepository) {
        this.loansRepository = loansRepository;
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> optionalLoans= loansRepository.findByMobileNumber(mobileNumber);
        if(optionalLoans.isPresent()){
            throw new LoanAlreadyExistsException("Loan already registered with given mobileNumber "+mobileNumber);
        }
        loansRepository.save(createNewLoan(mobileNumber));
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new loan details
     */
    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }

    /**
     * @param mobileNumber - Input mobile Number
     * @return Loan Details based on a given mobileNumber
     */
    @Override
    public LoansDto fetchLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
        return LoansMapper.mapToLoansDto(loans, new LoansDto());
    }

    /**
     * @param loansDto - LoansDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    @Override
    public boolean updateLoan(LoansDto loansDto) {
        Loans loans = loansRepository.findByMobileNumber(loansDto.getLoanNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "LoanNumber", loansDto.getLoanNumber())
        );
        LoansMapper.mapToLoans(loansDto, loans);
        loansRepository.save(loans);
        return true;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of loan details is successful or not
     */
    @Override
    public boolean deleteLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
        loansRepository.deleteById(loans.getLoanId());
        return true;
    }
}
