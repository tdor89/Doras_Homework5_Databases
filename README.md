
# Homework 5: Java CRUD with MySQL and MongoDB

This Java Maven project demonstrates Create, Read, Update, and Delete (CRUD) operations using MySQL and MongoDB.

MySQL operations use JDBC, while MongoDB operations use the MongoDB Java Driver.

## Dataset

Goodreads Books Dataset from Kaggle:
https://www.kaggle.com/datasets/jealousleopard/goodreadsbooks

The original dataset contained 11,127 books. Four malformed records were excluded during the SQL import, leaving 11,123 books.

## Project Files

- `Main.java` - Runs the CRUD demonstrations for both databases.
- `MySQLDatabase.java` - Implements CRUD operations using JDBC.
- `MongoDBDatabase.java` - Implements CRUD operations using the MongoDB Java Driver and imports data from MySQL.
- `pom.xml` - Contains Maven dependencies.
- `goodreads.sql` - MySQL database export containing the table structure and 11,123 records.

## Requirements

- Java JDK 26
- Maven
- MySQL Server 8.4
- MongoDB Atlas or local MongoDB

## Database Setup

### MySQL

Import `goodreads.sql` using the MySQL command-line client:

```sql
SOURCE C:/path/to/goodreads.sql;
```

This creates the `goodreads` database and populates the `books` table.

The Java application connects to MySQL at `localhost:3306` using the `root` account.

### MongoDB

Create a MongoDB database using MongoDB Atlas or a local MongoDB server.

The `importBooks()` method in `MongoDBDatabase.java` reads the Goodreads records from MySQL and inserts them into the MongoDB `goodreads.books` collection.

To populate MongoDB, temporarily add the following call to `Main.java` after creating the MongoDBDatabase object:

```java
mongodb.importBooks();
```

Run the program once, then remove or comment out the import call.

## Configuration

In IntelliJ, open Run > Edit Configurations and set these environment variables:

- `MYSQL_PASSWORD` - MySQL root password.
- `MONGODB_URI` - MongoDB connection string containing the database username and password.

Credentials are not stored in the source code.

## Running the Program

Run `Main.java` in IntelliJ.

The program performs the following operations in both MySQL and MongoDB:

1. CREATE - Insert a test book.
2. READ - Retrieve the test book.
3. UPDATE - Change the book's average rating.
4. READ - Verify the updated rating.
5. DELETE - Remove the test book.
6. READ - Verify the book was deleted.

The test book uses ID 999999 and is deleted after the demonstration, leaving the original Goodreads records unchanged.
