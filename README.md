# Library Management System

A modern, full-stack library management system with barcode scanning capabilities built with Spring Boot and vanilla JavaScript.

## Features

### 📚 Book Management
- **Barcode Scanning**: Scan book ISBNs using phone camera
- **Manual Input**: Enter book ISBN/ID manually
- **Book Search**: Search books by title, author, or ISBN
- **Book Information**: View detailed book information including availability

### 👥 Borrower Management
- **Borrower Search**: Find borrowers by name or email
- **Borrower Information**: View borrower details and loan history

### 📖 Loan Management
- **Borrow Books**: Check out books to borrowers
- **Return Books**: Process book returns
- **Renew Loans**: Extend loan periods
- **Overdue Tracking**: Monitor overdue books with fine calculations
- **Active Loans**: View all currently active loans

### 🎨 Modern UI
- **Responsive Design**: Works on desktop, tablet, and mobile
- **Modern Styling**: Clean, professional interface with smooth animations
- **Real-time Updates**: Live updates when books are borrowed/returned
- **Toast Notifications**: User-friendly feedback messages

## Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (lightweight, in-memory)
- **Maven** for dependency management

### Frontend
- **HTML5**
- **CSS3** with modern features (Grid, Flexbox, Custom Properties)
- **Vanilla JavaScript** (ES6+)
- **QuaggaJS** for barcode scanning
- **Font Awesome** for icons
- **Google Fonts** (Inter)

## Project Structure

```
library-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/library/system/
│   │   │   ├── controller/          # REST Controllers
│   │   │   ├── service/            # Business Logic
│   │   │   ├── repository/         # Data Access Layer
│   │   │   ├── model/              # JPA Entities
│   │   │   ├── config/             # Configuration Classes
│   │   │   └── LibraryManagementSystemApplication.java
│   │   └── resources/
│   │       ├── static/             # Frontend Files
│   │       │   ├── index.html
│   │       │   ├── styles.css
│   │       │   └── script.js
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- Modern web browser with camera support

### Installation & Running

1. **Clone or download the project**
   ```bash
   cd library-management-system
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Open your browser and go to: `http://localhost:8080`
   - The application will automatically load sample data

### Database Access
- **H2 Console**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:librarydb`
- **Username**: `sa`
- **Password**: `password`

## API Endpoints

### Books
- `GET /api/books` - Get all books
- `GET /api/books/{id}` - Get book by ID
- `GET /api/books/isbn/{isbn}` - Get book by ISBN
- `GET /api/books/search?keyword={keyword}` - Search books
- `GET /api/books/available` - Get available books
- `POST /api/books` - Create new book
- `PUT /api/books/{id}` - Update book
- `DELETE /api/books/{id}` - Delete book

### Borrowers
- `GET /api/borrowers` - Get all borrowers
- `GET /api/borrowers/{id}` - Get borrower by ID
- `GET /api/borrowers/email/{email}` - Get borrower by email
- `GET /api/borrowers/search?keyword={keyword}` - Search borrowers
- `POST /api/borrowers` - Create new borrower
- `PUT /api/borrowers/{id}` - Update borrower
- `DELETE /api/borrowers/{id}` - Delete borrower

### Loans
- `GET /api/loans` - Get all loans
- `GET /api/loans/active` - Get active loans
- `GET /api/loans/overdue` - Get overdue loans
- `GET /api/loans/book/isbn/{isbn}` - Get loans for a book
- `POST /api/loans/borrow` - Borrow a book
- `POST /api/loans/{id}/return` - Return a book
- `POST /api/loans/return-by-isbn` - Return book by ISBN
- `POST /api/loans/{id}/renew` - Renew a loan

## Usage Guide

### Scanning Books
1. Click "Start Camera" to enable barcode scanning
2. Position the book's barcode within the camera frame
3. The system will automatically detect and process the ISBN
4. Book information will be displayed automatically

### Manual Book Search
1. Enter the book ISBN or ID in the input field
2. Click "Search" or press Enter
3. View book details and loan information

### Borrowing Books
1. Search for a book using scan or manual input
2. If the book is available, search for a borrower
3. Select the borrower from the results
4. Click "Borrow Book" to complete the transaction

### Returning Books
1. Scan or search for the book to return
2. If the book is currently borrowed, return information will be displayed
3. Click "Return Book" to process the return
4. Any overdue fines will be calculated automatically

### Managing Loans
- **Active Loans Tab**: View all currently borrowed books
- **Overdue Tab**: View books that are past their due date
- **Renew Loans**: Extend loan periods for active loans
- **Return Books**: Process returns directly from the loan lists

## Sample Data

The application comes with sample data including:
- **Books**: Effective Java, Head First Design Patterns, Clean Code, Spring Boot in Action
- **Borrowers**: John Doe, Jane Smith, Bob Johnson
- **Sample Loan**: One active loan for demonstration

## Browser Compatibility

- **Chrome/Chromium**: Full support including camera access
- **Firefox**: Full support including camera access
- **Safari**: Full support including camera access
- **Edge**: Full support including camera access

## Mobile Support

The application is fully responsive and works on mobile devices:
- Touch-friendly interface
- Mobile-optimized camera scanning
- Responsive design adapts to different screen sizes

## Development

### Adding New Features
1. **Backend**: Add new endpoints in controllers, implement business logic in services
2. **Frontend**: Extend the JavaScript LibraryManager class with new methods
3. **Database**: Modify entities and repositories as needed

### Customization
- **Styling**: Modify `styles.css` for visual changes
- **API Base URL**: Change `apiBase` in `script.js` for different environments
- **Fine Rates**: Modify fine calculation in the Loan entity and service

## Troubleshooting

### Camera Issues
- Ensure browser has camera permissions
- Use HTTPS in production for camera access
- Check browser compatibility for camera features

### API Issues
- Verify Spring Boot application is running on port 8080
- Check browser console for CORS or network errors
- Ensure all required dependencies are installed

## License

This project is open source and available under the MIT License.

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.


