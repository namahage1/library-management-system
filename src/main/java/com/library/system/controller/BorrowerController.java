package com.library.system.controller;

import com.library.system.model.Borrower;
import com.library.system.service.BorrowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/borrowers")
@CrossOrigin(origins = "*")
public class BorrowerController {
    
    @Autowired
    private BorrowerService borrowerService;
    
    @GetMapping
    public List<Borrower> getAllBorrowers() {
        return borrowerService.getAllBorrowers();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Borrower> getBorrowerById(@PathVariable Long id) {
        Optional<Borrower> borrower = borrowerService.getBorrowerById(id);
        return borrower.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Borrower> getBorrowerByEmail(@PathVariable String email) {
        Optional<Borrower> borrower = borrowerService.getBorrowerByEmail(email);
        return borrower.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public List<Borrower> searchBorrowers(@RequestParam String keyword) {
        return borrowerService.searchBorrowers(keyword);
    }
    
    @GetMapping("/active")
    public List<Borrower> getActiveBorrowers() {
        return borrowerService.getActiveBorrowers();
    }
    
    @PostMapping
    public Borrower createBorrower(@RequestBody Borrower borrower) {
        return borrowerService.saveBorrower(borrower);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Borrower> updateBorrower(@PathVariable Long id, @RequestBody Borrower borrowerDetails) {
        Borrower updatedBorrower = borrowerService.updateBorrower(id, borrowerDetails);
        return updatedBorrower != null ? ResponseEntity.ok(updatedBorrower) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrower(@PathVariable Long id) {
        borrowerService.deleteBorrower(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}/active-loans-count")
    public ResponseEntity<Integer> getActiveLoansCount(@PathVariable Long id) {
        int count = borrowerService.getActiveLoansCount(id);
        return ResponseEntity.ok(count);
    }
}


