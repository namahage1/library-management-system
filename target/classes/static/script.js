// Library Management System JavaScript
class LibraryManager {
    constructor() {
        this.apiBase = 'http://localhost:8080/api';
        this.selectedBorrower = null;
        this.currentBook = null;
        this.isScanning = false;
        
        this.initializeEventListeners();
        this.loadActiveLoans();
        this.loadOverdueLoans();
    }
    
    initializeEventListeners() {
        // Tab navigation
        document.querySelectorAll('.nav-btn').forEach(btn => {
            btn.addEventListener('click', (e) => this.switchTab(e.target.dataset.tab));
        });
        
        // Camera controls
        document.getElementById('start-camera').addEventListener('click', () => this.startCamera());
        document.getElementById('stop-camera').addEventListener('click', () => this.stopCamera());
        
        // Manual search
        document.getElementById('search-book').addEventListener('click', () => this.searchBookByIsbn());
        document.getElementById('book-isbn').addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.searchBookByIsbn();
        });
        
        // Book actions
        document.getElementById('return-book').addEventListener('click', () => this.returnBook());
        document.getElementById('renew-loan').addEventListener('click', () => this.renewLoan());
        document.getElementById('borrow-book').addEventListener('click', () => this.borrowBook());
        
        // Borrower search
        document.getElementById('search-borrower').addEventListener('click', () => this.searchBorrower());
        document.getElementById('borrower-search').addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.searchBorrower();
        });
        
        // General search
        document.getElementById('search-books').addEventListener('click', () => this.searchBooks());
        document.getElementById('search-keyword').addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.searchBooks();
        });
    }
    
    switchTab(tabName) {
        // Update nav buttons
        document.querySelectorAll('.nav-btn').forEach(btn => btn.classList.remove('active'));
        document.querySelector(`[data-tab="${tabName}"]`).classList.add('active');
        
        // Update tab content
        document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));
        document.getElementById(`${tabName}-tab`).classList.add('active');
        
        // Load data for specific tabs
        if (tabName === 'loans') {
            this.loadActiveLoans();
        } else if (tabName === 'overdue') {
            this.loadOverdueLoans();
        }
    }
    
    async startCamera() {
        try {
            this.showLoading();
            
            const stream = await navigator.mediaDevices.getUserMedia({ 
                video: { 
                    facingMode: 'environment',
                    width: { ideal: 640 },
                    height: { ideal: 480 }
                } 
            });
            
            const video = document.getElementById('video');
            video.srcObject = stream;
            
            this.isScanning = true;
            document.getElementById('start-camera').disabled = true;
            document.getElementById('stop-camera').disabled = false;
            
            // Initialize QuaggaJS for barcode scanning
            Quagga.init({
                inputStream: {
                    name: "Live",
                    type: "LiveStream",
                    target: video,
                    constraints: {
                        width: 640,
                        height: 480,
                        facingMode: "environment"
                    }
                },
                decoder: {
                    readers: [
                        "code_128_reader",
                        "ean_reader",
                        "ean_8_reader",
                        "code_39_reader",
                        "code_39_vin_reader",
                        "codabar_reader",
                        "upc_reader",
                        "upc_e_reader",
                        "i2of5_reader"
                    ]
                },
                locate: true
            }, (err) => {
                if (err) {
                    console.error('QuaggaJS initialization error:', err);
                    this.showToast('Camera initialization failed', 'error');
                    return;
                }
                Quagga.start();
            });
            
            Quagga.onDetected((data) => {
                if (this.isScanning) {
                    const code = data.codeResult.code;
                    this.stopCamera();
                    this.searchBookByIsbn(code);
                }
            });
            
            this.hideLoading();
            this.showToast('Camera started successfully', 'success');
            
        } catch (error) {
            console.error('Camera access error:', error);
            this.hideLoading();
            this.showToast('Camera access denied or not available', 'error');
        }
    }
    
    stopCamera() {
        if (this.isScanning) {
            Quagga.stop();
            const video = document.getElementById('video');
            if (video.srcObject) {
                video.srcObject.getTracks().forEach(track => track.stop());
                video.srcObject = null;
            }
            
            this.isScanning = false;
            document.getElementById('start-camera').disabled = false;
            document.getElementById('stop-camera').disabled = true;
        }
    }
    
    async searchBookByIsbn(isbn = null) {
        const isbnInput = document.getElementById('book-isbn');
        const searchIsbn = isbn || isbnInput.value.trim();
        
        if (!searchIsbn) {
            this.showToast('Please enter a book ISBN or ID', 'warning');
            return;
        }
        
        try {
            this.showLoading();
            const response = await fetch(`${this.apiBase}/books/isbn/${searchIsbn}`);
            
            if (response.ok) {
                const book = await response.json();
                this.currentBook = book;
                this.displayBookInfo(book);
                this.loadBookLoanInfo(searchIsbn);
            } else if (response.status === 404) {
                this.showToast('Book not found', 'error');
                this.hideBookInfo();
            } else {
                throw new Error('Failed to fetch book');
            }
        } catch (error) {
            console.error('Error searching book:', error);
            this.showToast('Error searching for book', 'error');
        } finally {
            this.hideLoading();
        }
    }
    
    async loadBookLoanInfo(isbn) {
        try {
            const response = await fetch(`${this.apiBase}/loans/book/isbn/${isbn}`);
            const loans = await response.json();
            
            const activeLoan = loans.find(loan => !loan.returnDate);
            
            if (activeLoan) {
                this.displayLoanInfo(activeLoan);
            } else {
                this.displayBorrowSection();
            }
        } catch (error) {
            console.error('Error loading loan info:', error);
        }
    }
    
    displayBookInfo(book) {
        document.getElementById('book-title').textContent = book.title;
        document.getElementById('book-author').textContent = `by ${book.author}`;
        document.getElementById('book-isbn-display').textContent = `ISBN: ${book.isbn}`;
        document.getElementById('book-description').textContent = book.description || 'No description available';
        
        const statusBadge = document.getElementById('availability-status');
        if (book.availableCopies > 0) {
            statusBadge.textContent = 'Available';
            statusBadge.className = 'status-badge available';
        } else {
            statusBadge.textContent = 'Borrowed';
            statusBadge.className = 'status-badge borrowed';
        }
        
        document.getElementById('book-info').style.display = 'block';
    }
    
    displayLoanInfo(loan) {
        document.getElementById('borrower-name').textContent = `${loan.borrower.firstName} ${loan.borrower.lastName}`;
        document.getElementById('borrower-email').textContent = loan.borrower.email;
        document.getElementById('borrow-date').textContent = new Date(loan.borrowDate).toLocaleDateString();
        document.getElementById('due-date').textContent = new Date(loan.dueDate).toLocaleDateString();
        
        const isOverdue = new Date(loan.dueDate) < new Date() && !loan.returnDate;
        const statusElement = document.getElementById('loan-status');
        
        if (isOverdue) {
            statusElement.textContent = 'Overdue';
            statusElement.className = 'status-badge overdue';
            
            // Show fine information
            const daysOverdue = Math.ceil((new Date() - new Date(loan.dueDate)) / (1000 * 60 * 60 * 24));
            const fineAmount = daysOverdue * 0.50;
            document.getElementById('fine-amount').textContent = `$${fineAmount.toFixed(2)}`;
            document.getElementById('fine-info').style.display = 'block';
        } else {
            statusElement.textContent = 'Active';
            statusElement.className = 'status-badge borrowed';
            document.getElementById('fine-info').style.display = 'none';
        }
        
        document.getElementById('loan-info').style.display = 'block';
        document.getElementById('borrow-section').style.display = 'none';
    }
    
    displayBorrowSection() {
        document.getElementById('loan-info').style.display = 'none';
        document.getElementById('borrow-section').style.display = 'block';
    }
    
    hideBookInfo() {
        document.getElementById('book-info').style.display = 'none';
    }
    
    async searchBorrower() {
        const searchTerm = document.getElementById('borrower-search').value.trim();
        
        if (!searchTerm) {
            this.showToast('Please enter a search term', 'warning');
            return;
        }
        
        try {
            this.showLoading();
            const response = await fetch(`${this.apiBase}/borrowers/search?keyword=${encodeURIComponent(searchTerm)}`);
            const borrowers = await response.json();
            
            this.displayBorrowerResults(borrowers);
        } catch (error) {
            console.error('Error searching borrowers:', error);
            this.showToast('Error searching borrowers', 'error');
        } finally {
            this.hideLoading();
        }
    }
    
    displayBorrowerResults(borrowers) {
        const resultsContainer = document.getElementById('borrower-results');
        resultsContainer.innerHTML = '';
        
        if (borrowers.length === 0) {
            resultsContainer.innerHTML = '<p>No borrowers found</p>';
            return;
        }
        
        borrowers.forEach(borrower => {
            const borrowerDiv = document.createElement('div');
            borrowerDiv.className = 'borrower-item';
            borrowerDiv.innerHTML = `
                <h5>${borrower.firstName} ${borrower.lastName}</h5>
                <p>Email: ${borrower.email}</p>
                <p>Phone: ${borrower.phoneNumber || 'N/A'}</p>
            `;
            
            borrowerDiv.addEventListener('click', () => {
                document.querySelectorAll('.borrower-item').forEach(item => item.classList.remove('selected'));
                borrowerDiv.classList.add('selected');
                this.selectedBorrower = borrower;
                document.getElementById('borrow-book').disabled = false;
            });
            
            resultsContainer.appendChild(borrowerDiv);
        });
    }
    
    async borrowBook() {
        if (!this.selectedBorrower || !this.currentBook) {
            this.showToast('Please select a borrower', 'warning');
            return;
        }
        
        try {
            this.showLoading();
            const response = await fetch(`${this.apiBase}/loans/borrow?isbn=${this.currentBook.isbn}&borrowerId=${this.selectedBorrower.id}&loanDays=14`, {
                method: 'POST'
            });
            
            if (response.ok) {
                this.showToast('Book borrowed successfully', 'success');
                this.loadBookLoanInfo(this.currentBook.isbn);
                this.loadActiveLoans();
            } else {
                throw new Error('Failed to borrow book');
            }
        } catch (error) {
            console.error('Error borrowing book:', error);
            this.showToast('Error borrowing book', 'error');
        } finally {
            this.hideLoading();
        }
    }
    
    async returnBook() {
        if (!this.currentBook) {
            this.showToast('No book selected', 'warning');
            return;
        }
        
        try {
            this.showLoading();
            const response = await fetch(`${this.apiBase}/loans/return-by-isbn?isbn=${this.currentBook.isbn}`, {
                method: 'POST'
            });
            
            if (response.ok) {
                this.showToast('Book returned successfully', 'success');
                this.loadBookLoanInfo(this.currentBook.isbn);
                this.loadActiveLoans();
                this.loadOverdueLoans();
            } else {
                throw new Error('Failed to return book');
            }
        } catch (error) {
            console.error('Error returning book:', error);
            this.showToast('Error returning book', 'error');
        } finally {
            this.hideLoading();
        }
    }
    
    async renewLoan() {
        if (!this.currentBook) {
            this.showToast('No book selected', 'warning');
            return;
        }
        
        try {
            this.showLoading();
            const response = await fetch(`${this.apiBase}/loans/book/isbn/${this.currentBook.isbn}`);
            const loans = await response.json();
            const activeLoan = loans.find(loan => !loan.returnDate);
            
            if (activeLoan) {
                const renewResponse = await fetch(`${this.apiBase}/loans/${activeLoan.id}/renew?additionalDays=14`, {
                    method: 'POST'
                });
                
                if (renewResponse.ok) {
                    this.showToast('Loan renewed successfully', 'success');
                    this.loadBookLoanInfo(this.currentBook.isbn);
                    this.loadActiveLoans();
                } else {
                    throw new Error('Failed to renew loan');
                }
            }
        } catch (error) {
            console.error('Error renewing loan:', error);
            this.showToast('Error renewing loan', 'error');
        } finally {
            this.hideLoading();
        }
    }
    
    async searchBooks() {
        const keyword = document.getElementById('search-keyword').value.trim();
        
        if (!keyword) {
            this.showToast('Please enter a search term', 'warning');
            return;
        }
        
        try {
            this.showLoading();
            const response = await fetch(`${this.apiBase}/books/search?keyword=${encodeURIComponent(keyword)}`);
            const books = await response.json();
            
            this.displaySearchResults(books);
        } catch (error) {
            console.error('Error searching books:', error);
            this.showToast('Error searching books', 'error');
        } finally {
            this.hideLoading();
        }
    }
    
    displaySearchResults(books) {
        const resultsContainer = document.getElementById('search-results');
        resultsContainer.innerHTML = '';
        
        if (books.length === 0) {
            resultsContainer.innerHTML = '<p>No books found</p>';
            return;
        }
        
        books.forEach(book => {
            const bookDiv = document.createElement('div');
            bookDiv.className = 'book-item';
            bookDiv.innerHTML = `
                <h4>${book.title}</h4>
                <p><strong>Author:</strong> ${book.author}</p>
                <p><strong>ISBN:</strong> ${book.isbn}</p>
                <p><strong>Available Copies:</strong> ${book.availableCopies}/${book.totalCopies}</p>
                <p><strong>Description:</strong> ${book.description || 'No description available'}</p>
            `;
            
            bookDiv.addEventListener('click', () => {
                this.currentBook = book;
                this.displayBookInfo(book);
                this.loadBookLoanInfo(book.isbn);
                this.switchTab('scan');
            });
            
            resultsContainer.appendChild(bookDiv);
        });
    }
    
    async loadActiveLoans() {
        try {
            const response = await fetch(`${this.apiBase}/loans/active`);
            const loans = await response.json();
            
            this.displayLoans(loans, 'active-loans');
        } catch (error) {
            console.error('Error loading active loans:', error);
        }
    }
    
    async loadOverdueLoans() {
        try {
            const response = await fetch(`${this.apiBase}/loans/overdue`);
            const loans = await response.json();
            
            this.displayLoans(loans, 'overdue-loans');
        } catch (error) {
            console.error('Error loading overdue loans:', error);
        }
    }
    
    displayLoans(loans, containerId) {
        const container = document.getElementById(containerId);
        container.innerHTML = '';
        
        if (loans.length === 0) {
            container.innerHTML = '<p>No loans found</p>';
            return;
        }
        
        loans.forEach(loan => {
            const loanDiv = document.createElement('div');
            loanDiv.className = 'loan-item';
            
            const isOverdue = new Date(loan.dueDate) < new Date() && !loan.returnDate;
            const daysOverdue = isOverdue ? Math.ceil((new Date() - new Date(loan.dueDate)) / (1000 * 60 * 60 * 24)) : 0;
            
            loanDiv.innerHTML = `
                <h4>${loan.book.title}</h4>
                <div class="loan-meta">
                    <div class="meta-item">
                        <strong>Borrower:</strong> ${loan.borrower.firstName} ${loan.borrower.lastName}
                    </div>
                    <div class="meta-item">
                        <strong>Email:</strong> ${loan.borrower.email}
                    </div>
                    <div class="meta-item">
                        <strong>Borrowed:</strong> ${new Date(loan.borrowDate).toLocaleDateString()}
                    </div>
                    <div class="meta-item">
                        <strong>Due:</strong> ${new Date(loan.dueDate).toLocaleDateString()}
                    </div>
                    ${isOverdue ? `<div class="meta-item"><strong>Days Overdue:</strong> ${daysOverdue}</div>` : ''}
                </div>
                <div class="loan-actions">
                    <button class="btn btn-success" onclick="libraryManager.returnLoanById(${loan.id})">
                        <i class="fas fa-undo"></i> Return
                    </button>
                    ${!isOverdue ? `<button class="btn btn-warning" onclick="libraryManager.renewLoanById(${loan.id})">
                        <i class="fas fa-refresh"></i> Renew
                    </button>` : ''}
                </div>
            `;
            
            container.appendChild(loanDiv);
        });
    }
    
    async returnLoanById(loanId) {
        try {
            this.showLoading();
            const response = await fetch(`${this.apiBase}/loans/${loanId}/return`, {
                method: 'POST'
            });
            
            if (response.ok) {
                this.showToast('Book returned successfully', 'success');
                this.loadActiveLoans();
                this.loadOverdueLoans();
            } else {
                throw new Error('Failed to return book');
            }
        } catch (error) {
            console.error('Error returning book:', error);
            this.showToast('Error returning book', 'error');
        } finally {
            this.hideLoading();
        }
    }
    
    async renewLoanById(loanId) {
        try {
            this.showLoading();
            const response = await fetch(`${this.apiBase}/loans/${loanId}/renew?additionalDays=14`, {
                method: 'POST'
            });
            
            if (response.ok) {
                this.showToast('Loan renewed successfully', 'success');
                this.loadActiveLoans();
                this.loadOverdueLoans();
            } else {
                throw new Error('Failed to renew loan');
            }
        } catch (error) {
            console.error('Error renewing loan:', error);
            this.showToast('Error renewing loan', 'error');
        } finally {
            this.hideLoading();
        }
    }
    
    showLoading() {
        document.getElementById('loading').style.display = 'flex';
    }
    
    hideLoading() {
        document.getElementById('loading').style.display = 'none';
    }
    
    showToast(message, type = 'info') {
        const toastContainer = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        
        const icon = type === 'success' ? 'check-circle' : 
                    type === 'error' ? 'exclamation-circle' : 
                    type === 'warning' ? 'exclamation-triangle' : 'info-circle';
        
        toast.innerHTML = `
            <i class="fas fa-${icon}"></i>
            <span>${message}</span>
        `;
        
        toastContainer.appendChild(toast);
        
        setTimeout(() => {
            toast.remove();
        }, 5000);
    }
}

// Initialize the application when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.libraryManager = new LibraryManager();
});


