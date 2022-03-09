package cn.fanbaby.xweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.fanbaby.xweather.Utils.pulltorefresh.PullToRefreshScrollView;

public class MainActivity extends AppCompatActivity {
    private static final int OPEN_SET_REQUEST_CODE = 100;
    // 三个位置相关的权限
    private final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS};
    private TextView tv_city, tv_update_time, tv_wt_state, tv_temp_high, tv_temp_low, tv_temp_current, tv_pm25_num, tv_pm25_state, tv_time_1st, tv_time_2nd, tv_time_3rd, tv_time_4th, tv_time_5th, tv_time_6th, tv_time_7th, tv_temp_1st, tv_temp_2nd, tv_temp_3rd, tv_temp_4th, tv_temp_5th, tv_temp_6th, tv_temp_7th, tv_temp_feature_high_1st, tv_temp_feature_high_2nd, tv_temp_feature_high_3rd, tv_temp_feature_low_1st, tv_temp_feature_low_2nd, tv_temp_feature_low_3rd, tv_humidity, tv_aqi, tv_direct, tv_power, tv_pm10;
    private ImageView iv_pull_arrow, iv_wt_icon, iv_icon_time_1st, iv_icon_time_2nd, iv_icon_time_3rd, iv_icon_time_4th, iv_icon_time_5th, iv_icon_time_6th, iv_icon_time_7th, iv_icon_feature_1st, iv_icon_feature_2nd, iv_icon_feature_3rd;
    private Python py;
    private String city, updateTime, currentState, currentTemp, highTemp, lowTemp, pm25, airState, day_1st_high, day_2nd_high, day_3rd_high, day_1st_low, day_2nd_low, day_3rd_low, day_1st_state, day_2nd_state, day_3rd_state, humidity, aqi, direct, power, pm10, hour_next_3, hour_next_6, hour_next_9, hour_next_12, hour_next_15, hour_next_18, hour_next_21;
    private PullToRefreshScrollView rs;
    private LocationManager locationManager;
    private String locationProvider;
    private double userX;
    private double userY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initPython();
        // 子线程，用于更新数据
        new Thread(() -> {
            city = getCity();
            if (!city.equals("定位失败"))
                getData(city);
        }).start();
    }

    private void initPermissions() {
        if (lacksPermission(permissions)) {//判断是否拥有权限
            //请求权限，第二参数权限String数据，第三个参数是请求码便于在onRequestPermissionsResult 方法中根据code进行判断
            ActivityCompat.requestPermissions(this, permissions, OPEN_SET_REQUEST_CODE);
        }
    }

    //如果返回true表示缺少权限
    public boolean lacksPermission(String[] permissions) {
        for (String permission : permissions) {
            //判断是否缺少权限，true=缺少权限
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case OPEN_SET_REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "未获取权限", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(this, "未获取权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 获取城市
     *
     * @return 当前城市
     */
    private String getCity() {
        try {
            getLocation();
            return py.getModule("py_utils").callAttr("getCity", userX, userY).toString();
        } catch (Exception e) {
            return "定位失败";
        }
    }

    private void getLocation() {
        if (initLocation()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                initPermissions();
                return;
            }
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location != null) {
                userX = location.getLatitude();
                userY = location.getLongitude();
            }
        }
    }

    private boolean initLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            // GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            // Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Looper.prepare();
            Toast.makeText(getApplicationContext(), "位置服务不可用，请确保手机打开了位置信息并赋予软件位置权限", Toast.LENGTH_SHORT).show();
            Looper.loop();
            return false;
        }
        return true;
    }

    /**
     * 更新数据
     */
    private void updateData() {
        updateText();
        updateImage();
    }

    /**
     * 更新图片相关UI显示
     */
    private void updateImage() {
        runOnUiThread(() -> {
            iv_wt_icon.setImageResource(stateToIcon(currentState));
            iv_icon_feature_1st.setImageResource(stateToIcon(day_1st_state));
            iv_icon_feature_2nd.setImageResource(stateToIcon(day_2nd_state));
            iv_icon_feature_3rd.setImageResource(stateToIcon(day_3rd_state));
        });
    }

    /**
     * 将状态信息映射为drawable resource
     *
     * @param state 天气状态、
     * @return R.drawable.x
     */
    private int stateToIcon(String state) {
        switch (state) {
            case "晴":
                return R.drawable.ic_icon_qingtian;
            case "多云":
                return R.drawable.ic_icon_duoyun;
            case "阴":
                return R.drawable.ic_icon_yintian;
            case "阵雨":
                return R.drawable.ic_icon_zhenyu;
            case "雷阵雨":
                return R.drawable.ic_icon_leizhenyu;
            case "雨夹雪":
                break;
            case "小雨":
            case "小到中雨":
                return R.drawable.ic_icon_xiaoyu;
            case "中到大雨":
            case "中雨":
                return R.drawable.ic_icon_zhongyu;
            case "大雨":
                return R.drawable.ic_icon_dayu;
            case "暴雨":
            case "大暴雨":
            case "特大暴雨":
            case "大到暴雨":
            case "暴雨到大暴雨":
            case "大暴雨到特大暴雨":
                return R.drawable.ic_icon_baoyu;
            case "阵雪":
                return R.drawable.ic_icon_zhenxue;
            case "小雪":
                return R.drawable.ic_icon_xiaoxue;
            case "小到中雪":
            case "中雪":
                return R.drawable.ic_icon_zhongxue;
            case "中到大雪":
            case "大雪":
                return R.drawable.ic_icon_daxue;
            case "大到暴雪":
            case "暴雪":
                return R.drawable.ic_icon_baoxue;
            case "雾":
                return R.drawable.ic_icon_wu;
            case "冻雨":
                break;
            case "强沙尘暴":
            case "沙尘暴":
                return R.drawable.ic_icon_shachenbao;
            case "浮尘":
                return R.drawable.ic_icon_fuchen;
            case "扬沙":
                return R.drawable.ic_icon_yangsha;
            case "霾":
                break;
            default:
                return R.drawable.ic_icon_wushuju;
        }
        return R.drawable.ic_icon_wushuju;
    }

    /**
     * 更新页面文本
     */
    private void updateText() {
        runOnUiThread(
                () -> {
                    tv_city.setText(city);
                    tv_update_time.setText(updateTime);
                    tv_wt_state.setText(currentState);
                    tv_temp_current.setText(currentTemp);
                    tv_temp_high.setText(highTemp);
                    tv_temp_low.setText(lowTemp);
                    tv_pm25_num.setText(pm25);
                    tv_pm25_state.setText(airState);
                    tv_temp_feature_high_1st.setText(day_1st_high);
                    tv_temp_feature_high_2nd.setText(day_2nd_high);
                    tv_temp_feature_high_3rd.setText(day_3rd_high);
                    tv_temp_feature_low_1st.setText(day_1st_low);
                    tv_temp_feature_low_2nd.setText(day_2nd_low);
                    tv_temp_feature_low_3rd.setText(day_3rd_low);
                    tv_humidity.setText(humidity);
                    tv_aqi.setText(aqi);
                    tv_direct.setText(direct);
                    tv_power.setText(power);
                    tv_pm10.setText(pm10);
                    tv_time_1st.setText(hour_next_3);
                    tv_time_2nd.setText(hour_next_6);
                    tv_time_3rd.setText(hour_next_9);
                    tv_time_4th.setText(hour_next_12);
                    tv_time_5th.setText(hour_next_15);
                    tv_time_6th.setText(hour_next_18);
                    tv_time_7th.setText(hour_next_21);
                }
        );
    }

    /**
     * 初始化Python
     */
    private void initPython() {
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        py = Python.getInstance();
    }

    /**
     * 初始化控件信息
     */
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
        rs = findViewById(R.id.rs_wt_pull_refresh);
        // 初始化位置信息
        userX = 0;
        userY = 0;
        listen();
    }

    /**
     * 监听器
     */
    private void listen() {
        rs.setOnRefreshListener(refreshView -> {
            getData(city);
            Toast.makeText(this, "刷新成功！", Toast.LENGTH_SHORT).show();
            rs.onRefreshComplete();
        });
    }

    /**
     * 获取对应城市的对应天气情况
     *
     * @param city 城市
     * @param need 需要的天气状况，例如highTemp
     * @return 查询的天气情况
     */
    private String getWeather(String city, String need) {
        return py.getModule("py_utils").callAttr("getWeather", city, need).toString();
    }

    /**
     * 获取对应城市的空气指数
     *
     * @param city 城市
     * @param need 需要的空气指数内容，例如pm2.5
     * @return 查询道德空气指数
     */
    private String getAQI(String city, String need) {
        return py.getModule("py_utils").callAttr("getAQI", city, need).toString();
    }

    /**
     * 获取数据
     *
     * @param city 城市
     */
    private void getData(String city) {
        // 获取更新数据的时间
        updateTime = getAQI(city, "time");
        currentState = getWeather(city, "state");
        currentTemp = getWeather(city, "current_temp");
        highTemp = getWeather(city, "high_temp");
        lowTemp = getWeather(city, "low_temp");
        pm25 = getAQI(city, "pm25");
        airState = getAQI(city, "state");
        // 获取未来3天的最高温、最低温和天气情况
        day_1st_high = getWeather(city, "day_1st_high") + "℃";
        day_2nd_high = getWeather(city, "day_2nd_high") + "℃";
        day_3rd_high = getWeather(city, "day_3rd_high") + "℃";
        day_1st_low = getWeather(city, "day_1st_low") + "℃";
        day_2nd_low = getWeather(city, "day_2nd_low") + "℃";
        day_3rd_low = getWeather(city, "day_3rd_low") + "℃";
        day_1st_state = getWeather(city, "day_1st_state");
        day_2nd_state = getWeather(city, "day_2nd_state");
        day_3rd_state = getWeather(city, "day_3rd_state");
        // 获取空气质量
        humidity = getWeather(city, "humidity");
        aqi = getWeather(city, "aqi");
        direct = getWeather(city, "direct");
        power = getWeather(city, "power");
        pm10 = getAQI(city, "pm10");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
        // 格式化时间以取得获取数据的小时数
        Date time = new Date();
        try {
            time = sdf.parse(updateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int updateHour = 0;
        if (time != null) {
            updateHour = time.getHours();
        }
        // 计算未来一天，每隔3小时的小时数
        hour_next_3 = (updateHour + 3) % 24 + ":00";
        hour_next_6 = (updateHour + 6) % 24 + ":00";
        hour_next_9 = (updateHour + 9) % 24 + ":00";
        hour_next_12 = (updateHour + 12) % 24 + ":00";
        hour_next_15 = (updateHour + 15) % 24 + ":00";
        hour_next_18 = (updateHour + 18) % 24 + ":00";
        hour_next_21 = (updateHour + 21) % 24 + ":00";
        // 将获取到的数据更新
        updateData();
    }
}