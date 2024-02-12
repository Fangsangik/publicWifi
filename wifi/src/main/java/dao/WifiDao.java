package dao;

import Database.Db;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.WifiDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WifiDao {

    public static Connection con = null;
    public static PreparedStatement prst = null;
    public static ResultSet rst = null;

    public static int add(JsonArray array) {
        String url = "jdbc:mariadb://192.168.0.25:3306/localhost";
        String userId = "publicwifi";
        String pw = "zerobasse";

        int cnt = 0;

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            con = DriverManager.getConnection(url, userId, pw);
            String sql = "";

            prst = con.prepareStatement(sql);

            for (int i = 0; i < array.size(); i++) {
                JsonObject data = array.get(i).getAsJsonObject();

                prst.setString(1, data.get("X_SWIFI_MGR_NO").getAsString());
                prst.setString(2, data.get("X_SWIFI_WRDOFC").getAsString());
                prst.setString(3, data.get("X_SWIFI_MAIN_NM").getAsString());
                prst.setString(4, data.get("X_SWIFI_ADRES1").getAsString());
                prst.setString(5, data.get("X_SWIFI_ADRES2").getAsString());
                prst.setString(6, data.get("X_SWIFI_INSTL_FLOOR").getAsString());
                prst.setString(7, data.get("X_SWIFI_INSTL_TY").getAsString());
                prst.setString(8, data.get("X_SWIFI_INSTL_MBY").getAsString());
                prst.setString(9, data.get("X_SWIFI_SVC_SE").getAsString());
                prst.setString(10, data.get("X_SWIFI_CMCWR").getAsString());
                prst.setString(11, data.get("X_SWIFI_CNSTC_YEAR").getAsString());
                prst.setString(12, data.get("X_SWIFI_INOUT_DOOR").getAsString());
                prst.setString(13, data.get("X_SWIFI_REMARS3").getAsString());
                prst.setString(14, data.get("LAT").getAsString());
                prst.setString(15, data.get("LNT").getAsString());
                prst.setString(16, data.get("WORK_DTTM").getAsString());

                prst.addBatch();
                prst.clearParameters();

                if ((i + 1) % 1000 == 0){
                    int[] rst = prst.executeBatch();
                    cnt += rst.length;
                    con.commit();

                }
            }
            prst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (prst != null && !prst.isClosed()) {
                    prst.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return cnt;
    }

    public void delete() {

        String url = "jdbc:mariadb://192.168.0.25:3306/localhost";
        String userId = "publicwifi";
        String pw = "zerobasse";

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            con = DriverManager.getConnection(url, userId, pw);
            String sql = "";

            prst = con.prepareStatement(sql);
            System.out.println("sql 전달 성공");

            int affected = prst.executeUpdate();

            if (affected > 0) {
                System.out.println("실행성공");
            } else {
                System.out.println("실행실패");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rst != null && !rst.isClosed()) {
                    rst.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (prst != null && !prst.isClosed()) {
                    prst.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<WifiDto> getNearestWifi(String lat, String len) {
        con = null;
        prst = null;
        rst = null;

        List<WifiDto> list = new ArrayList<>();

        try {
            con = Db.connDB();
            String sql = "";

            prst = con.prepareStatement(sql);
            prst.setDouble(1, Double.parseDouble(lat));
            prst.setDouble(2, Double.parseDouble(len));
            prst.setDouble(3, Double.parseDouble(lat));

            rst = prst.executeQuery();

            while (rst.next()) {
                WifiDto dto = WifiDto.builder()
                        .distance(rst.getDouble("distance"))
                        .xSwifiMgrNo(rst.getString("x_swifi_mgr_no"))
                        .xSwifiWrdofc(rst.getString("x_swifi_wrdofc"))
                        .xSwifiMainNm(rst.getString("x_swifi_main_nm"))
                        .xSwifiAdres1(rst.getString("x_swifi_adres1"))
                        .xSwifiAdres2(rst.getString("x_swifi_adres2"))
                        .xSwifiInstlFloor(rst.getString("x_swifi_instl_floor"))
                        .xSwifiInstlTy(rst.getString("x_swifi_instl_ty"))
                        .xSwifiInstlMby(rst.getString("x_swifi_instl_mby"))
                        .xSwifiSvcSe(rst.getString("x_swifi_svc_se"))
                        .xSwifiCmcwr(rst.getString("x_swifi_cmcwr"))
                        .xSwifiCnstcYear(rst.getString("x_swifi_cnstc_year"))
                        .xSwifiInoutDoor(rst.getString("x_swifi_inout_door"))
                        .xSwifiRemars3(rst.getString("x_swifi_remars3"))
                        .lat(rst.getString("lat"))
                        .lnt(rst.getString("lnt"))
                        .workDttm(String.valueOf(rst.getTimestamp("work_dttm").toLocalDateTime()))
                        .build();
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Db.connectionClose(con, prst, rst);
        }

        return list;
    }
}
