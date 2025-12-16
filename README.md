# BookStore
A Spring Boot–based RESTful web service to manage a **Book Store** with support for **Books, Authors, Customers, and Purchased Books**, providing full **CRUD operations**, documentation, testing, logging, and code quality analysis

##  Use Case Overview
The application supports the following use cases:

###  Book
- Create, retrieve, update, delete books
- A book can exist without an author
- A book can have one or more authors

###  Author
- Create authors with or without books
- An author can be associated with multiple books

###  Customer
- Create, retrieve, update, delete customers
- Customers can purchase books
- View all customers
- View a customer along with purchased books

###  Purchased Books
- Track customer purchases
- Maintain purchase history

##  Architecture

The application follows **Layered Architecture**:

### Layers:
- **Controller**
- **Service**
- **Repository**
- **DTO**
- **Entity**
- **Exception**

##  Technology Stack

| Concern | Technology        |
|------|-------------------|
| Framework | Spring Boot       |
| Build Tool | Maven             |
| Database | Postgresql        |
| ORM | Spring Data JPA (Hibernate) |
| API Docs | Swagger           |
| Logging | SLF4J + Logback   |
| Testing | JUnit 5, Mockito  |
| Code Coverage | JaCoCo            |
| Code Quality | SonarQube         |

##  Entity Relationships

- **Book ↔ Author** → Many-to-Many
- **Customer ↔ Book** → One-to-Many (via `Purchase` entity)

> A separate `Purchase` entity is used to support extensibility (date, customerId (customer  Purchased),bookId(which book purchased)). 