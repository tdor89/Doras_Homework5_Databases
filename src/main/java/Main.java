
public class Main {

    public static void main(String[] args) {

        MySQLDatabase mysql = new MySQLDatabase();
        MongoDBDatabase mongodb = new MongoDBDatabase();

        int bookID = 999999;
        String title = "Homework 5 Test Book";
        String authors = "Test Author";
        double rating = 4.50;

        //
        // MySQL CRUD Operations
        //

        System.out.println("=== MySQL CRUD Operations ===");

        // CREATE: Insert a new book
        mysql.createBook(bookID, title, authors, rating);

        // READ: Retrieve the book
        mysql.readBook(bookID);

        // UPDATE: Change the book's rating
        mysql.updateBook(bookID, 4.90);

        // READ: Verify the updated rating
        mysql.readBook(bookID);

        // DELETE: Remove the test book
        mysql.deleteBook(bookID);

        // READ: Verify deletion
        mysql.readBook(bookID);


        //
        // MongoDB CRUD Operations
        //

        System.out.println("\n=== MongoDB CRUD Operations ===");

        // CREATE: Insert a new document
        mongodb.createBook(bookID, title, authors, rating);

        // READ: Retrieve the document
        mongodb.readBook(bookID);

        // UPDATE: Change the book's rating
        mongodb.updateBook(bookID, 4.90);

        // READ: Verify the updated rating
        mongodb.readBook(bookID);

        // DELETE: Remove the test document
        mongodb.deleteBook(bookID);

        // READ: Verify deletion
        mongodb.readBook(bookID);

        System.out.println("\nAll CRUD operations completed.");
    }
}
