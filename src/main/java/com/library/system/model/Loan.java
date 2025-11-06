package com.library.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    @NotNull(message = "Book is required")
    private Book book;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", nullable = false)
    @NotNull(message = "Borrower is required")
    private Borrower borrower;
    
    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;
    
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    
    @Column(name = "return_date")
    private LocalDate returnDate;
    
    @Column(name = "fine_amount")
    private Double fineAmount = 0.0;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LoanStatus status = LoanStatus.ACTIVE;
    
    @Column(name = "notes")
    private String notes;
    
    // Constructors
    public Loan() {}
    
    public Loan(Book book, Borrower borrower, LocalDate borrowDate, LocalDate dueDate) {
        this.book = book;
        this.borrower = borrower;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
    }
    
    public Borrower getBorrower() {
        return borrower;
    }
    
    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }
    
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    
    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public Double getFineAmount() {
        return fineAmount;
    }
    
    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }
    
    public LoanStatus getStatus() {
        return status;
    }
    
    public void setStatus(LoanStatus status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    // Helper methods
    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }
    
    public boolean isReturned() {
        return returnDate != null;
    }
    
    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return LocalDate.now().toEpochDay() - dueDate.toEpochDay();
    }
    
    public void calculateFine(double dailyFineRate) {
        if (isOverdue()) {
            fineAmount = getDaysOverdue() * dailyFineRate;
        }
    }
    
    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.status = LoanStatus.RETURNED;
        if (isOverdue()) {
            calculateFine(0.50); // $0.50 per day overdue
        }
    }
    
    // Enum for loan status
    public enum LoanStatus {
        ACTIVE, RETURNED, OVERDUE
    }
}


