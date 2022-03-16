package cn.fanbaby.xweather.Utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class GetWeather {

    public static String jsonStr(String URL) {
        try {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.connect();
            InputStream in = conn.getInputStream();
            return InputStreamParse.inputStream_to_String(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String get(String jsonStr, String key) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsonStr);
            return obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCity(String x, String y) {
        String URL = "https://api.2fanbaby.cn/getcity?x=" + x + "&y=" + y;
        String jsonStr = GetWeather.jsonStr(URL);
        return jsonStr;
    }
}
