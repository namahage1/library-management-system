# Setup Instructions for Library Management System

## Prerequisites Installation

### 1. Install Java 17 or higher
- Download from: https://adoptium.net/
- Or use Oracle JDK: https://www.oracle.com/java/technologies/downloads/
- Verify installation: `java -version`

### 2. Install Maven
- Download from: https://maven.apache.org/download.cgi
- Extract and add to PATH
- Verify installation: `mvn -version`

### 3. Alternative: Use Maven Wrapper (if available)
- The project includes Maven wrapper scripts
- Run: `./mvnw clean install` (Linux/Mac) or `mvnw.cmd clean install` (Windows)

## Running the Application

### Method 1: Using Maven
```bash
# Navigate to project directory
cd library-management-system

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Method 2: Using IDE
1. Open the project in IntelliJ IDEA, Eclipse, or VS Code
2. Import as Maven project
3. Run `LibraryManagementSystemApplication.java`

### Method 3: Using Maven Wrapper
```bash
# Windows
mvnw.cmd clean install
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw clean install
./mvnw spring-boot:run
```

## Accessing the Application

1. **Web Application**: http://localhost:8080
2. **H2 Database Console**: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:librarydb`
   - Username: `sa`
   - Password: `password`

## Features Available

✅ **Barcode Scanning**: Use phone camera to scan book ISBNs
✅ **Manual Input**: Enter book ISBN/ID manually
✅ **Book Search**: Search by title, author, or ISBN
✅ **Borrower Management**: Search and select borrowers
✅ **Loan Management**: Borrow, return, and renew books
✅ **Overdue Tracking**: Monitor overdue books with fines
✅ **Responsive Design**: Works on desktop and mobile
✅ **Real-time Updates**: Live updates for all operations

## Sample Data Included

The application comes with pre-loaded sample data:
- **Books**: Effective Java, Head First Design Patterns, Clean Code, Spring Boot in Action
- **Borrowers**: John Doe, Jane Smith, Bob Johnson
- **Sample Loan**: One active loan for testing

## Testing the Application

1. **Start the application** using one of the methods above
2. **Open browser** and navigate to http://localhost:8080
3. **Test barcode scanning**:
   - Click "Start Camera"
   - Allow camera permissions
   - Scan a barcode or use manual input
4. **Test book operations**:
   - Search for books
   - Borrow books to borrowers
   - Return books
   - View active and overdue loans

## Troubleshooting

### Common Issues:
1. **Port 8080 already in use**: Change port in `application.properties`
2. **Camera not working**: Ensure HTTPS in production, check browser permissions
3. **Build errors**: Ensure Java 17+ and Maven are properly installed
4. **CORS errors**: Check that backend is running on localhost:8080

### Browser Compatibility:
- Chrome/Chromium: Full support
- Firefox: Full support  
- Safari: Full support
- Edge: Full support

## Next Steps

Once the application is running:
1. Test all features thoroughly
2. Customize styling if needed
3. Add more sample data
4. Deploy to production server
5. Configure production database (PostgreSQL, MySQL, etc.)


