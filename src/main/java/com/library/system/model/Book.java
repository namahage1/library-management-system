package com.library.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "ISBN is required")
    @Column(unique = true, nullable = false)
    private String isbn;
    
    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;
    
    @NotBlank(message = "Author is required")
    @Column(nullable = false)
    private String author;
    
    @Column(length = 500)
    private String description;
    
    @NotNull(message = "Publication date is required")
    @Column(name = "publication_date")
    private LocalDate publicationDate;
    
    @Column(name = "total_copies")
    private Integer totalCopies = 1;
    
    @Column(name = "available_copies")
    private Integer availableCopies = 1;
    
    @Column(name = "created_at")
    private LocalDate createdAt = LocalDate.now();
    
    // Constructors
    public Book() {}
    
    public Book(String isbn, String title, String author, String description, 
                LocalDate publicationDate, Integer totalCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.description = description;
        this.publicationDate = publicationDate;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getPublicationDate() {
        return publicationDate;
    }
    
    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }
    
    public Integer getTotalCopies() {
        return totalCopies;
    }
    
    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }
    
    public Integer getAvailableCopies() {
        return availableCopies;
    }
    
    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }
    
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    
    // Helper methods
    public boolean isAvailable() {
        return availableCopies > 0;
    }
    
    public void borrowCopy() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }
    
    public void returnCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }
}


