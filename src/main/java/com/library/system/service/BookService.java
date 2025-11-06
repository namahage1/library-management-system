package com.library.system.service;

import com.library.system.model.Book;
import com.library.system.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
    
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
    
    public List<Book> searchBooks(String keyword) {
        return bookRepository.searchBooks(keyword);
    }
    
    public List<Book> getAvailableBooks() {
        return bookRepository.findAvailableBooks();
    }
    
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }
    
    public Book updateBook(Long id, Book bookDetails) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setDescription(bookDetails.getDescription());
            book.setPublicationDate(bookDetails.getPublicationDate());
            book.setTotalCopies(bookDetails.getTotalCopies());
            book.setAvailableCopies(bookDetails.getAvailableCopies());
            return bookRepository.save(book);
        }
        return null;
    }
    
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
    
    public boolean isBookAvailable(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        return book.isPresent() && book.get().isAvailable();
    }
    
    public void borrowBook(String isbn) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.borrowCopy();
            bookRepository.save(book);
        }
    }
    
    public void returnBook(String isbn) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.returnCopy();
            bookRepository.save(book);
        }
    }
}


