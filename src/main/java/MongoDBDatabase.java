
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MongoDBDatabase {

    // MongoDB connection settings
    private static final String MONGODB_URI =
            System.getenv("MONGODB_URI");

    private static final String DATABASE = "goodreads";
    private static final String COLLECTION = "books";

    // Import Goodreads books from MySQL into MongoDB
    public void importBooks() {

        String mysqlURL =
                "jdbc:mysql://localhost:3306/goodreads";

        String sql = "SELECT * FROM books";

        try (Connection connection =
                     DriverManager.getConnection(
                             mysqlURL,
                             "root",
                             System.getenv("MYSQL_PASSWORD"));
             Statement statement =
                     connection.createStatement();
             ResultSet results =
                     statement.executeQuery(sql);
             MongoClient client =
                     MongoClients.create(MONGODB_URI)) {

            MongoCollection<Document> collection =
                    client.getDatabase(DATABASE)
                            .getCollection(COLLECTION);

            // Avoid importing the same books twice
            if (collection.countDocuments() > 0) {
                System.out.println(
                        "MongoDB already contains books. Import skipped."
                );
                return;
            }

            List<Document> batch = new ArrayList<>();
            int count = 0;

            while (results.next()) {

                Document book = new Document()
                        .append("bookID", results.getInt("bookID"))
                        .append("title", results.getString("title"))
                        .append("authors", results.getString("authors"))
                        .append("average_rating",
                                results.getDouble("average_rating"))
                        .append("isbn", results.getString("isbn"))
                        .append("isbn13", results.getString("isbn13"))
                        .append("language_code",
                                results.getString("language_code"))
                        .append("num_pages",
                                results.getInt("num_pages"))
                        .append("ratings_count",
                                results.getInt("ratings_count"))
                        .append("text_reviews_count",
                                results.getInt("text_reviews_count"))
                        .append("publication_date",
                                results.getString("publication_date"))
                        .append("publisher",
                                results.getString("publisher"));

                batch.add(book);

                // Insert 500 books at a time
                if (batch.size() == 500) {
                    collection.insertMany(batch);
                    count += batch.size();
                    batch.clear();
                }
            }

            // Insert remaining books
            if (!batch.isEmpty()) {
                collection.insertMany(batch);
                count += batch.size();
            }

            System.out.println(
                    "Books imported into MongoDB: " + count
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CREATE: Insert a new book
    public void createBook(int bookID, String title,
                           String authors, double rating) {

        try (MongoClient client =
                     MongoClients.create(MONGODB_URI)) {

            MongoCollection<Document> collection =
                    client.getDatabase(DATABASE)
                            .getCollection(COLLECTION);

            // Check if the book already exists
            Document existingBook = collection.find(
                    new Document("bookID", bookID)
            ).first();

            if (existingBook != null) {
                System.out.println("Book already exists.");
                return;
            }

            Document book = new Document()
                    .append("bookID", bookID)
                    .append("title", title)
                    .append("authors", authors)
                    .append("average_rating", rating);

            collection.insertOne(book);

            System.out.println("Book added to MongoDB!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // READ: Find a book by its ID
    public void readBook(int bookID) {

        try (MongoClient client =
                     MongoClients.create(MONGODB_URI)) {

            MongoCollection<Document> collection =
                    client.getDatabase(DATABASE)
                            .getCollection(COLLECTION);

            Document book = collection.find(
                    new Document("bookID", bookID)
            ).first();

            if (book != null) {
                System.out.println("\nBook found in MongoDB:");
                System.out.println("ID: " +
                        book.getInteger("bookID"));
                System.out.println("Title: " +
                        book.getString("title"));
                System.out.println("Authors: " +
                        book.getString("authors"));
                System.out.println("Rating: " +
                        book.getDouble("average_rating"));
                System.out.println("ISBN: " +
                        book.getString("isbn"));
            } else {
                System.out.println("Book not found in MongoDB.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE: Change a book's average rating
    public void updateBook(int bookID, double newRating) {

        try (MongoClient client =
                     MongoClients.create(MONGODB_URI)) {

            MongoCollection<Document> collection =
                    client.getDatabase(DATABASE)
                            .getCollection(COLLECTION);

            Document filter =
                    new Document("bookID", bookID);

            Document update =
                    new Document("$set",
                            new Document("average_rating", newRating));

            long rows = collection.updateOne(
                    filter, update
            ).getMatchedCount();

            System.out.println("Books updated: " + rows);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE: Remove a book by its ID
    public void deleteBook(int bookID) {

        try (MongoClient client =
                     MongoClients.create(MONGODB_URI)) {

            MongoCollection<Document> collection =
                    client.getDatabase(DATABASE)
                            .getCollection(COLLECTION);

            long rows = collection.deleteOne(
                    new Document("bookID", bookID)
            ).getDeletedCount();

            System.out.println("Books deleted: " + rows);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
