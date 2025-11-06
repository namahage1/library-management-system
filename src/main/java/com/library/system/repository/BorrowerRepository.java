package com.library.system.repository;

import com.library.system.model.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    
    Optional<Borrower> findByEmail(String email);
    
    List<Borrower> findByFirstNameContainingIgnoreCase(String firstName);
    
    List<Borrower> findByLastNameContainingIgnoreCase(String lastName);
    
    List<Borrower> findByIsActiveTrue();
    
    @Query("SELECT b FROM Borrower b WHERE b.firstName LIKE %:keyword% OR b.lastName LIKE %:keyword% OR b.email LIKE %:keyword%")
    List<Borrower> searchBorrowers(@Param("keyword") String keyword);
    
    @Query("SELECT COUNT(l) FROM Loan l WHERE l.borrower.id = :borrowerId AND l.returnDate IS NULL")
    int countActiveLoansByBorrowerId(@Param("borrowerId") Long borrowerId);
}


