package com.library.system.service;

import com.library.system.model.Book;
import com.library.system.model.Borrower;
import com.library.system.model.Loan;
import com.library.system.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoanService {
    
    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BorrowerService borrowerService;
    
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
    
    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }
    
    public List<Loan> getLoansByBorrowerId(Long borrowerId) {
        return loanRepository.findByBorrowerId(borrowerId);
    }
    
    public List<Loan> getLoansByBookId(Long bookId) {
        return loanRepository.findByBookId(bookId);
    }
    
    public List<Loan> getActiveLoans() {
        return loanRepository.findByReturnDateIsNull();
    }
    
    public List<Loan> getOverdueLoans() {
        return loanRepository.findOverdueLoans(LocalDate.now());
    }
    
    public List<Loan> getActiveLoansByBookIsbn(String isbn) {
        return loanRepository.findActiveLoansByBookIsbn(isbn);
    }
    
    public Loan borrowBook(String isbn, Long borrowerId, int loanDays) {
        Optional<Book> optionalBook = bookService.getBookByIsbn(isbn);
        Optional<Borrower> optionalBorrower = borrowerService.getBorrowerById(borrowerId);
        
        if (optionalBook.isPresent() && optionalBorrower.isPresent()) {
            Book book = optionalBook.get();
            Borrower borrower = optionalBorrower.get();
            
            if (!book.isAvailable()) {
                throw new RuntimeException("Book is not available");
            }
            
            LocalDate borrowDate = LocalDate.now();
            LocalDate dueDate = borrowDate.plusDays(loanDays);
            
            Loan loan = new Loan(book, borrower, borrowDate, dueDate);
            loan = loanRepository.save(loan);
            
            // Update book availability
            book.borrowCopy();
            bookService.saveBook(book);
            
            return loan;
        }
        throw new RuntimeException("Book or borrower not found");
    }
    
    public Loan returnBook(Long loanId) {
        Optional<Loan> optionalLoan = loanRepository.findById(loanId);
        if (optionalLoan.isPresent()) {
            Loan loan = optionalLoan.get();
            loan.returnBook();
            
            // Update book availability
            Book book = loan.getBook();
            book.returnCopy();
            bookService.saveBook(book);
            
            return loanRepository.save(loan);
        }
        throw new RuntimeException("Loan not found");
    }
    
    public Loan returnBookByIsbn(String isbn) {
        List<Loan> activeLoans = loanRepository.findActiveLoansByBookIsbn(isbn);
        if (!activeLoans.isEmpty()) {
            Loan loan = activeLoans.get(0); // Return the first active loan
            return returnBook(loan.getId());
        }
        throw new RuntimeException("No active loan found for this book");
    }
    
    public Loan renewLoan(Long loanId, int additionalDays) {
        Optional<Loan> optionalLoan = loanRepository.findById(loanId);
        if (optionalLoan.isPresent()) {
            Loan loan = optionalLoan.get();
            if (loan.getReturnDate() == null) {
                loan.setDueDate(loan.getDueDate().plusDays(additionalDays));
                return loanRepository.save(loan);
            }
        }
        throw new RuntimeException("Loan not found or already returned");
    }
    
    public void calculateFinesForOverdueLoans() {
        List<Loan> overdueLoans = getOverdueLoans();
        for (Loan loan : overdueLoans) {
            loan.calculateFine(0.50); // $0.50 per day
            loanRepository.save(loan);
        }
    }
}


