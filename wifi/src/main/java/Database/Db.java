package Database;


import java.sql.*;

public class Db {


    public static Connection connDB(){

        String url = "jdbc:mariadb://192.168.0.25:3306/localhost";
        String id = "publicwifi";
        String password = "zerobasse";
        Connection connection = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(url, id, password);

            if (connection != null) {
                System.out.println("데이터베이스에 연결되었습니다.");
            } else {
                System.out.println("데이터베이스 연결에 실패했습니다.");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionClose(connection, null, null); // 연결을 닫습니다.
        }

        return connection;
    }

    public static void connectionClose(Connection connection, PreparedStatement ps, ResultSet rst) {
        try {
            // ResultSet 닫기
            if (rst != null && !rst.isClosed()) {
                rst.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            // PreparedStatement 닫기
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            // Connection 닫기
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("데이터베이스 연결이 닫혔습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
