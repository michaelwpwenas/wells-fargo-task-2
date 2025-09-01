# Wells Fargo Task 2 Repo
Contains Everything you need to get started on task 2 of Forage's Wells Fargo software engineering program

This project is a **Spring Boot application** designed for handling banking transaction data with **JPA entities** and **RESTful APIs**. It models accounts and transactions, persists them in a relational database, and exposes CRUD endpoints for integration.

## Features

- **Spring Boot 3** application with embedded Tomcat
- **JPA/Hibernate** entity mapping for `Account` and `Transaction`
- **Spring Data JPA Repositories** for database access
- **REST API Controllers** for Accounts and Transactions
- **H2 in-memory database** (configurable to MySQL/Postgres)
- **Lombok** for reducing boilerplate code

## Prerequisites

- **Java 17** or higher
- **Maven 3.9+**

(Optional)
- MySQL/PostgreSQL if you want to use a persistent DB instead of H2

## Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/wells-fargo-imt.git
   cd wells-fargo-imt
   ```

2. Build and run:
   ```bash
   mvn spring-boot:run
   ```

3. The application will start on:
   ```
   http://localhost:8080
   ```

## REST API Endpoints

### Accounts
- `GET /accounts` → list all accounts
- `GET /accounts/{id}` → get account by ID
- `POST /accounts` → create new account
- `PUT /accounts/{id}` → update account
- `DELETE /accounts/{id}` → delete account

### Transactions
- `GET /transactions` → list all transactions
- `GET /transactions/{id}` → get transaction by ID
- `POST /transactions` → create new transaction
- `PUT /transactions/{id}` → update transaction
- `DELETE /transactions/{id}` → delete transaction

## Example JSON

### Create Account
```json
{
  "accountNumber": "123456789",
  "accountHolder": "John Doe",
  "balance": 5000.0
}
```

### Create Transaction
```json
{
  "accountId": 1,
  "amount": 250.75,
  "type": "DEBIT"
}
```

## Database Configuration

Modify `application.properties` (H2 default):
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

For MySQL:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/wellsfargo
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

## Technologies Used

- Spring Boot
- Spring Data JPA
- H2/MySQL/Postgres
- Lombok
- Maven

## License

This project is licensed under the MIT License.

