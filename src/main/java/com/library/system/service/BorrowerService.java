package com.library.system.service;

import com.library.system.model.Borrower;
import com.library.system.repository.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BorrowerService {
    
    @Autowired
    private BorrowerRepository borrowerRepository;
    
    public List<Borrower> getAllBorrowers() {
        return borrowerRepository.findAll();
    }
    
    public Optional<Borrower> getBorrowerById(Long id) {
        return borrowerRepository.findById(id);
    }
    
    public Optional<Borrower> getBorrowerByEmail(String email) {
        return borrowerRepository.findByEmail(email);
    }
    
    public List<Borrower> searchBorrowers(String keyword) {
        return borrowerRepository.searchBorrowers(keyword);
    }
    
    public List<Borrower> getActiveBorrowers() {
        return borrowerRepository.findByIsActiveTrue();
    }
    
    public Borrower saveBorrower(Borrower borrower) {
        return borrowerRepository.save(borrower);
    }
    
    public Borrower updateBorrower(Long id, Borrower borrowerDetails) {
        Optional<Borrower> optionalBorrower = borrowerRepository.findById(id);
        if (optionalBorrower.isPresent()) {
            Borrower borrower = optionalBorrower.get();
            borrower.setFirstName(borrowerDetails.getFirstName());
            borrower.setLastName(borrowerDetails.getLastName());
            borrower.setEmail(borrowerDetails.getEmail());
            borrower.setPhoneNumber(borrowerDetails.getPhoneNumber());
            borrower.setAddress(borrowerDetails.getAddress());
            borrower.setIsActive(borrowerDetails.getIsActive());
            return borrowerRepository.save(borrower);
        }
        return null;
    }
    
    public void deleteBorrower(Long id) {
        borrowerRepository.deleteById(id);
    }
    
    public int getActiveLoansCount(Long borrowerId) {
        return borrowerRepository.countActiveLoansByBorrowerId(borrowerId);
    }
}


