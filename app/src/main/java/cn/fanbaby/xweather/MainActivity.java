package cn.fanbaby.xweather;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView tv_city, tv_update_time, tv_wt_state, tv_temp_high, tv_temp_low, tv_temp_current, tv_pm25_num, tv_pm25_state, tv_time_1st, tv_time_2nd, tv_time_3rd, tv_time_4th, tv_time_5th, tv_time_6th, tv_time_7th, tv_temp_1st, tv_temp_2nd, tv_temp_3rd, tv_temp_4th, tv_temp_5th, tv_temp_6th, tv_temp_7th, tv_temp_feature_high_1st, tv_temp_feature_high_2nd, tv_temp_feature_high_3rd, tv_temp_feature_low_1st, tv_temp_feature_low_2nd, tv_temp_feature_low_3rd, tv_humidity, tv_aqi, tv_direct, tv_power, tv_pm10;
    private ImageView iv_pull_arrow, iv_wt_icon, iv_icon_time_1st, iv_icon_time_2nd, iv_icon_time_3rd, iv_icon_time_4th, iv_icon_time_5th, iv_icon_time_6th, iv_icon_time_7th, iv_icon_feature_1st, iv_icon_feature_2nd, iv_icon_feature_3rd;
    private Python py;
    private String city, updateTime, currentState, currentTemp, highTemp, lowTemp, pm25, airState, day_1st_high, day_2nd_high, day_3rd_high, day_1st_low, day_2nd_low, day_3rd_low, day_1st_state, day_2nd_state, day_3rd_state, humidity, aqi, direct, power, pm10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initPython();
        city = "新乡";
        getData(city);
        updateData();
    }

    private void updateData() {
        tv_city.setText(city);
        tv_update_time.setText(updateTime);
        tv_wt_state.setText(currentState);
        
    }

    private void initPython() {
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
    }

    void init() {
        tv_city = findViewById(R.id.tv_wt_city); // 城市
        tv_update_time = findViewById(R.id.tv_wt_update_time); // 更新时间
        tv_wt_state = findViewById(R.id.tv_wt_weather_state); // 天气状态
        tv_temp_high = findViewById(R.id.tv_wt_high_temp); // 最高温度
        tv_temp_low = findViewById(R.id.tv_wt_low_temp); // 最低温度
        tv_temp_current = findViewById(R.id.tv_wt_current_temp); // 当前温度
        tv_pm25_num = findViewById(R.id.tv_wt_pm25_num); // pm2.5值
        tv_pm25_state = findViewById(R.id.tv_wt_pm25_state);  // pm2.5污染情况
        tv_time_1st = findViewById(R.id.tv_wt_1st_three); // 未来3小时
        tv_time_2nd = findViewById(R.id.tv_wt_2nd_three); // 未来6小时
        tv_time_3rd = findViewById(R.id.tv_wt_3rd_three); // 未来9小时
        tv_time_4th = findViewById(R.id.tv_wt_4th_three); // 未来12小时
        tv_time_5th = findViewById(R.id.tv_wt_5th_three); // 未来15小时
        tv_time_6th = findViewById(R.id.tv_wt_6th_three); // 未来18小时
        tv_time_7th = findViewById(R.id.tv_wt_7th_three); // 未来21小时
        tv_temp_1st = findViewById(R.id.tv_wt_1st_temp); // 未来3小时温度
        tv_temp_2nd = findViewById(R.id.tv_wt_2nd_temp); // 未来6小时温度
        tv_temp_3rd = findViewById(R.id.tv_wt_3rd_temp); // 未来9小时温度
        tv_temp_4th = findViewById(R.id.tv_wt_4th_temp); // 未来12小时温度
        tv_temp_5th = findViewById(R.id.tv_wt_5th_temp); // 未来15小时温度
        tv_temp_6th = findViewById(R.id.tv_wt_6th_temp); // 未来18小时温度
        tv_temp_7th = findViewById(R.id.tv_wt_7th_temp); // 未来21小时温度
        tv_temp_feature_high_1st = findViewById(R.id.tv_wt_feature_1st_high_temp); // 明天高温
        tv_temp_feature_high_2nd = findViewById(R.id.tv_wt_feature_2nd_high_temp); // 后天高温
        tv_temp_feature_high_3rd = findViewById(R.id.tv_wt_feature_3rd_high_temp); // 大后天高温
        tv_temp_feature_low_1st = findViewById(R.id.tv_wt_feature_1st_low_temp); // 明天低温
        tv_temp_feature_low_2nd = findViewById(R.id.tv_wt_feature_2nd_low_temp); // 后天低温
        tv_temp_feature_low_3rd = findViewById(R.id.tv_wt_feature_3rd_low_temp); // 大后天低温
        tv_humidity = findViewById(R.id.tv_wt_humidity); // 湿度
        tv_aqi = findViewById(R.id.tv_wt_aqi); // 空气质量
        tv_direct = findViewById(R.id.tv_wt_direct); // 风向
        tv_power = findViewById(R.id.tv_wt_power); // 风力
        tv_pm10 = findViewById(R.id.tv_wt_pm10); // pm10
        iv_pull_arrow = findViewById(R.id.iv_pull_arrow); // 下拉箭头
        iv_wt_icon = findViewById(R.id.iv_wt_wt_icon); // 天气图标
        iv_icon_time_1st = findViewById(R.id.iv_wt_1st_three); // 未来3小时图标
        iv_icon_time_2nd = findViewById(R.id.iv_wt_2nd_three); // 未来6小时图标
        iv_icon_time_3rd = findViewById(R.id.iv_wt_3rd_three);// 未来9小时图标
        iv_icon_time_4th = findViewById(R.id.iv_wt_4th_three); // 未来12小时图标
        iv_icon_time_5th = findViewById(R.id.iv_wt_5th_three); // 未来15小时图标
        iv_icon_time_6th = findViewById(R.id.iv_wt_6th_three); // 未来18小时图标
        iv_icon_time_7th = findViewById(R.id.iv_wt_7th_three); // 未来21小时图标
        iv_icon_feature_1st = findViewById(R.id.iv_wt_feature_1st_state); // 明天天气图标
        iv_icon_feature_2nd = findViewById(R.id.iv_wt_feature_2nd_state); // 后天天气图标
        iv_icon_feature_3rd = findViewById(R.id.iv_wt_feature_3rd_state); // 大后天天气图标
        listen();
    }

    private void listen() {
    }

    private String getWeather(String city, String need) {
        return py.getModule("py_utils").callAttr("getWeather", city, need).toString();
    }

    private String getAQI(String city, String need) {
        return py.getModule("py_utils").callAttr("getAQI", city, need).toString();
    }

    private void getData(String city) {
        updateTime = getAQI(city, "time");
        currentState = getWeather(city, "state");
        currentTemp = getWeather(city, "current_temp");
        highTemp = getWeather(city, "high_temp");
        lowTemp = getWeather(city, "low_temp");
        pm25 = getAQI(city, "pm25");
        airState = getAQI(city, "quality");
        day_1st_high = getWeather(city, "day_1st_high");
        day_2nd_high = getWeather(city, "day_2nd_high");
        day_3rd_high = getWeather(city, "day_3rd_high");
        day_1st_low = getWeather(city, "day_1st_low");
        day_2nd_low = getWeather(city, "day_2nd_low");
        day_3rd_low = getWeather(city, "day_3rd_low");
        day_1st_state = getWeather(city, "day_1st_state");
        day_2nd_state = getWeather(city, "day_2nd_state");
        day_3rd_state = getWeather(city, "day_3rd_state");
        humidity = getWeather(city, "humidity");
        aqi = getWeather(city, "aqi");
        direct = getWeather(city, "direct");
        power = getWeather(city, "power");
        pm10 = getAQI(city, "pm10");
    }

    private InputStream requestJuheApi(String url, String method) {
        Gson gson = new Gson();
        InputStream in = null;
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod(method);
            conn.setConnectTimeout(5000);
            conn.connect();
            in = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

}