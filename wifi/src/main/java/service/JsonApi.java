package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.net.URL;

import static dao.WifiDao.add;

public class JsonApi {
    private static String API = "http://openapi.seoul.go.kr:8088/6374707a4a696b3036357669456d6a/json/TbPublicWifiInfo/";
    private static OkHttpClient okHttpClient = new OkHttpClient();

    public static int totalCnt() throws IOException {
        int cnt = 0;

        URL url = new URL(API + "1/1");
        Request.Builder builder = new Request.Builder().url(url).get();
        Response resp = okHttpClient.newCall(builder.build()).execute();

        try {
            if (resp.isSuccessful()) {
                ResponseBody body = resp.body();

                if (resp != null) {
                    JsonElement element = JsonParser.parseString(resp.toString());

                    cnt = element.getAsJsonObject().get("TbPublicWifiInfo")
                            .getAsJsonObject().get("list_total_count")
                            .getAsInt();

                    System.out.println("찾은 와이파이 개수" + cnt);
                } else {
                    System.out.println("실패" + resp.code());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnt;
    }

    public static int getPublicwifiJson() throws IOException {
        int total = totalCnt();
        int start = 1;
        int end = 1;
        int cnt = 0;

        try {
            for (int i = 0; i <= total / 1000; i++) {
                start = 1 + (1000 * i);
                end = (i + 1) * 1000;

                URL url = new URL(API + start + "/" + end);
                Request.Builder builder = new Request.Builder().url(url).get();
                Response response = okHttpClient.newCall(builder.build()).execute();

                if (response != null) {
                    JsonElement je = JsonParser.parseString(response.toString());

                    JsonArray array = je.getAsJsonArray().getAsJsonObject().get("TbPublicWifiInfo")
                            .getAsJsonObject().get("row")
                            .getAsJsonArray();
                    cnt += add(array);
                } else {
                    System.out.println("호출 실패" + response.code());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return cnt;
    }
}

