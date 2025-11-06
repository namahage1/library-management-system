package com.library.system.config;

import com.library.system.model.Book;
import com.library.system.model.Borrower;
import com.library.system.model.Loan;
import com.library.system.repository.BookRepository;
import com.library.system.repository.BorrowerRepository;
import com.library.system.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private BorrowerRepository borrowerRepository;
    
    @Autowired
    private LoanRepository loanRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize sample books
        if (bookRepository.count() == 0) {
            Book book1 = new Book("978-0134685991", "Effective Java", "Joshua Bloch", 
                                "A comprehensive guide to Java programming best practices", 
                                LocalDate.of(2017, 12, 27), 3);
            Book book2 = new Book("978-0596009205", "Head First Design Patterns", "Eric Freeman", 
                                "A brain-friendly guide to design patterns", 
                                LocalDate.of(2004, 10, 25), 2);
            Book book3 = new Book("978-0132350884", "Clean Code", "Robert C. Martin", 
                                "A Handbook of Agile Software Craftsmanship", 
                                LocalDate.of(2008, 8, 1), 4);
            Book book4 = new Book("978-0134685992", "Spring Boot in Action", "Craig Walls", 
                                "A comprehensive guide to Spring Boot development", 
                                LocalDate.of(2019, 3, 15), 2);
            
            bookRepository.save(book1);
            bookRepository.save(book2);
            bookRepository.save(book3);
            bookRepository.save(book4);
        }
        
        // Initialize sample borrowers
        if (borrowerRepository.count() == 0) {
            Borrower borrower1 = new Borrower("John", "Doe", "john.doe@email.com", 
                                            "+1234567890", "123 Main St, City, State");
            Borrower borrower2 = new Borrower("Jane", "Smith", "jane.smith@email.com", 
                                            "+1234567891", "456 Oak Ave, City, State");
            Borrower borrower3 = new Borrower("Bob", "Johnson", "bob.johnson@email.com", 
                                            "+1234567892", "789 Pine Rd, City, State");
            
            borrowerRepository.save(borrower1);
            borrowerRepository.save(borrower2);
            borrowerRepository.save(borrower3);
        }
        
        // Initialize sample loans
        if (loanRepository.count() == 0) {
            Book book1 = bookRepository.findByIsbn("978-0134685991").orElse(null);
            Borrower borrower1 = borrowerRepository.findByEmail("john.doe@email.com").orElse(null);
            
            if (book1 != null && borrower1 != null) {
                Loan loan1 = new Loan(book1, borrower1, LocalDate.now().minusDays(5), 
                                    LocalDate.now().plusDays(9));
                loanRepository.save(loan1);
                
                // Update book availability
                book1.borrowCopy();
                bookRepository.save(book1);
            }
        }
    }
}


