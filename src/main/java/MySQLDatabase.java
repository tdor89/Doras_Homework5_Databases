
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLDatabase {

    // MySQL connection settings
    private static final String URL =
            "jdbc:mysql://localhost:3306/goodreads";
    private static final String USER = "root";
    private static final String PASSWORD =
            System.getenv("MYSQL_PASSWORD");

    // Test the connection and count the books
    public void testConnection() {

        String sql = "SELECT COUNT(*) FROM books";

        try (Connection connection =
                     DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            System.out.println("Connected to MySQL!");

            if (results.next()) {
                System.out.println(
                        "Total books: " + results.getInt(1)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CREATE: Insert a new book
    public void createBook(int bookID, String title,
                           String authors, double rating) {

        String sql = "INSERT INTO books " +
                "(bookID, title, authors, average_rating) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection =
                     DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, bookID);
            statement.setString(2, title);
            statement.setString(3, authors);
            statement.setDouble(4, rating);

            int rows = statement.executeUpdate();

            System.out.println("Books added: " + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ: Find a book by its ID
    public void readBook(int bookID) {

        String sql = "SELECT * FROM books WHERE bookID = ?";

        try (Connection connection =
                     DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, bookID);

            try (ResultSet results = statement.executeQuery()) {

                if (results.next()) {
                    System.out.println("\nBook found:");
                    System.out.println("ID: " +
                            results.getInt("bookID"));
                    System.out.println("Title: " +
                            results.getString("title"));
                    System.out.println("Authors: " +
                            results.getString("authors"));
                    System.out.println("Rating: " +
                            results.getDouble("average_rating"));
                    System.out.println("ISBN: " +
                            results.getString("isbn"));
                } else {
                    System.out.println("Book not found.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // UPDATE: Change a book's average rating
    public void updateBook(int bookID, double newRating) {

        String sql = "UPDATE books " +
                "SET average_rating = ? WHERE bookID = ?";

        try (Connection connection =
                     DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setDouble(1, newRating);
            statement.setInt(2, bookID);

            int rows = statement.executeUpdate();

            System.out.println("Books updated: " + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // DELETE: Remove a book by its ID
    public void deleteBook(int bookID) {

        String sql = "DELETE FROM books WHERE bookID = ?";

        try (Connection connection =
                     DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, bookID);

            int rows = statement.executeUpdate();

            System.out.println("Books deleted: " + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
