package api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class OpenApi {

    static String key = "6374707a4a696b3036357669456d6a";

    public static String getApi() {
        String request = null;

        try {
            StringBuilder sb = new StringBuilder("http://openapi.seoul.go.kr:8088/");
            sb.append(URLEncoder.encode(key, "UTF-8"));
            sb.append("/json/TbPublicWifiInfo/1/5");

            URL url = new URL(sb.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            System.out.println("Response code: " + connection.getResponseCode());
            BufferedReader br;

            if (connection.getResponseCode() >= 200 && connection.getResponseCode() <= 300) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            StringBuilder sb1 = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb1.append(line);
            }
            br.close();

            connection.disconnect();
            request = sb1.toString();
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
        return request;
    }
}