package dao;

import Database.Db;
import dto.HistoryDto;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HistoryDao {
    public static Connection connection;
    public static PreparedStatement prst;
    public static ResultSet rst;

    public void search(String lat, String len) {
        connection = null;
        prst = null;
        rst = null;

        String driver = "org.mariadb.jdbc.Driver";

        String url = "jdbc:mariadb://192.168.0.25:3306/localhost";
        String userId = "publicwifi";
        String pw = "zerobasse";


        try {
            Class.forName(driver);

            connection = DriverManager.getConnection(url, userId, pw);
            String sql = "";
            prst = connection.prepareStatement(sql);

            prst.setString(1, lat);
            prst.setString(2,len);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (prst != null && !prst.isClosed()) {
                    prst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (rst != null && !rst.isClosed()) {
                    rst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<HistoryDto> historyDtoList(){
        List<HistoryDto> list = new ArrayList<>();

        connection = null;
        prst = null;
        rst = null;

        try {
            connection = Db.connDB();
            String sql = "";

            prst = connection.prepareStatement(sql);
            rst = prst.executeQuery();

            while (rst.next()){
                HistoryDto dto = new HistoryDto();
                rst.getInt("id");
                rst.getString("lat");
                rst.getString("len");

                list.add(dto);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            Db.connectionClose(connection, prst, rst);
        }

        return list;
    }

}
