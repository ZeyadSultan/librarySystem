# Library Management System

## Overview
The Library Management System is a RESTful API built using the Spring Boot framework. It provides functionalities to manage authors, books, customers, and borrowing records in a library.

## Entities

### Author
- **id:** Unique identifier for the author.
- **name:** Name of the author.
- **birthDate:** Date of birth of the author.
- **nationality:** Nationality of the author.

### Book
- **id:** Unique identifier for the book.
- **title:** Title of the book.
- **authorId:** Foreign key referencing the author of the book.
- **isbn:** ISBN (International Standard Book Number) of the book.
- **publicationDate:** Publication date of the book.
- **genre:** Genre of the book.
- **available:** Indicates whether the book is currently available for borrowing.

### Customer/User
- **id:** Unique identifier for the customer/user.
- **name:** Name of the customer/user.
- **email:** Email address of the customer/user.
- **address:** Address of the customer/user.
- **phoneNumber:** Phone number of the customer/user.
- **password:** Password for the customer/user (encrypted).

### Borrowing Record
- **id:** Unique identifier for the borrowing record.
- **userId:** Foreign key referencing the user who borrowed the book.
- **bookId:** Foreign key referencing the book that was borrowed.
- **borrowDate:** Date when the book was borrowed.
- **returnDate:** Date when the book is expected to be returned.

## Endpoints

1. **Authors:**
   - `GET /authors`: Retrieve all authors.
   - `GET /authors/{id}`: Retrieve an author by ID.
   - `POST /authors`: Create a new author.
   - `PUT /authors/{id}`: Update an existing author.
   - `DELETE /authors/{id}`: Delete an author by ID.

2. **Books:**
   - `GET /books`: Retrieve all books.
   - `GET /books/{id}`: Retrieve a book by ID.
   - `POST /books`: Create a new book.
   - `PUT /books/{id}`: Update an existing book.
   - `DELETE /books/{id}`: Delete a book by ID.
   - `GET /books/search?title={title}`: Search for books by title.
   - `GET /books/search?author={author}`: Search for books by author.
   - `GET /books/search?isbn={isbn}`: Search for books by ISBN.

3. **Customers/Users:**
   - `GET /customers`: Retrieve all customers/users.
   - `GET /customers/{id}`: Retrieve a customer/user by ID.
   - `POST /customers`: Create a new customer/user.
   - `PUT /customers/{id}`: Update an existing customer/user.
   - `DELETE /customers/{id}`: Delete a customer/user by ID.

4. **Borrowing Records:**
   - `GET /borrowings`: Retrieve all borrowing records.
   - `GET /borrowings/{id}`: Retrieve a borrowing record by ID.
   - `POST /borrowings`: Create a new borrowing record.
   - `PUT /borrowings/{id}`: Update an existing borrowing record.
   - `DELETE /borrowings/{id}`: Delete a borrowing record by ID.
   - `GET /borrowings/search?userId={userId}`: Retrieve borrowing records for a specific user.
   - `GET /borrowings/search?bookId={bookId}`: Retrieve borrowing records for a specific book.

## SQL Database
A SQL database H2 is used to store and retrieve data for the entities. Implement CRUD (Create, Read, Update, Delete) operations to manage data.

## Validation and Exception Handling
Validation checks are implemented to ensure data integrity, and exceptions are handled gracefully.

## Documentation with Swagger
API documentation is generated using Swagger to describe the endpoints, request parameters, response formats, etc.

## Clean Code and SOLID Principles
The project follows clean code practices and adheres to SOLID principles to ensure code maintainability, readability, and scalability.
