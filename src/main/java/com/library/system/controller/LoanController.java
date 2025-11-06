package com.library.system.controller;

import com.library.system.model.Loan;
import com.library.system.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "*")
public class LoanController {
    
    @Autowired
    private LoanService loanService;
    
    @GetMapping
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        Optional<Loan> loan = loanService.getLoanById(id);
        return loan.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/borrower/{borrowerId}")
    public List<Loan> getLoansByBorrowerId(@PathVariable Long borrowerId) {
        return loanService.getLoansByBorrowerId(borrowerId);
    }
    
    @GetMapping("/book/{bookId}")
    public List<Loan> getLoansByBookId(@PathVariable Long bookId) {
        return loanService.getLoansByBookId(bookId);
    }
    
    @GetMapping("/active")
    public List<Loan> getActiveLoans() {
        return loanService.getActiveLoans();
    }
    
    @GetMapping("/overdue")
    public List<Loan> getOverdueLoans() {
        return loanService.getOverdueLoans();
    }
    
    @GetMapping("/book/isbn/{isbn}")
    public List<Loan> getActiveLoansByBookIsbn(@PathVariable String isbn) {
        return loanService.getActiveLoansByBookIsbn(isbn);
    }
    
    @PostMapping("/borrow")
    public ResponseEntity<Loan> borrowBook(@RequestParam String isbn, 
                                          @RequestParam Long borrowerId,
                                          @RequestParam(defaultValue = "14") int loanDays) {
        try {
            Loan loan = loanService.borrowBook(isbn, borrowerId, loanDays);
            return ResponseEntity.ok(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/return")
    public ResponseEntity<Loan> returnBook(@PathVariable Long id) {
        try {
            Loan loan = loanService.returnBook(id);
            return ResponseEntity.ok(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/return-by-isbn")
    public ResponseEntity<Loan> returnBookByIsbn(@RequestParam String isbn) {
        try {
            Loan loan = loanService.returnBookByIsbn(isbn);
            return ResponseEntity.ok(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/renew")
    public ResponseEntity<Loan> renewLoan(@PathVariable Long id, 
                                         @RequestParam(defaultValue = "14") int additionalDays) {
        try {
            Loan loan = loanService.renewLoan(id, additionalDays);
            return ResponseEntity.ok(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/calculate-fines")
    public ResponseEntity<Void> calculateFinesForOverdueLoans() {
        loanService.calculateFinesForOverdueLoans();
        return ResponseEntity.ok().build();
    }
}


