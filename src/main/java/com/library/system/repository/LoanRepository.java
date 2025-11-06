package com.library.system.repository;

import com.library.system.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    
    List<Loan> findByBorrowerId(Long borrowerId);
    
    List<Loan> findByBookId(Long bookId);
    
    List<Loan> findByReturnDateIsNull();
    
    List<Loan> findByReturnDateIsNullAndDueDateBefore(LocalDate date);
    
    @Query("SELECT l FROM Loan l WHERE l.borrower.id = :borrowerId AND l.returnDate IS NULL")
    List<Loan> findActiveLoansByBorrowerId(@Param("borrowerId") Long borrowerId);
    
    @Query("SELECT l FROM Loan l WHERE l.book.id = :bookId AND l.returnDate IS NULL")
    List<Loan> findActiveLoansByBookId(@Param("bookId") Long bookId);
    
    @Query("SELECT l FROM Loan l WHERE l.returnDate IS NULL AND l.dueDate < :currentDate")
    List<Loan> findOverdueLoans(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT l FROM Loan l WHERE l.book.isbn = :isbn AND l.returnDate IS NULL")
    List<Loan> findActiveLoansByBookIsbn(@Param("isbn") String isbn);
    
    Optional<Loan> findByBookIdAndBorrowerIdAndReturnDateIsNull(Long bookId, Long borrowerId);
}


