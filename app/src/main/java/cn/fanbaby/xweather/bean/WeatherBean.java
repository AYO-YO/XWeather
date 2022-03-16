package cn.fanbaby.xweather.bean;

import com.google.gson.Gson;

public class WeatherBean {

    private String high_temp;
    private String low_temp;
    private String current_temp;
    private String wt_state;
    private String day_1st_high;
    private String day_1st_low;
    private String day_2nd_high;
    private String day_2nd_low;
    private String day_3rd_high;
    private String day_3rd_low;
    private String day_1st_state;
    private String day_2nd_state;
    private String day_3rd_state;
    private String humidity;
    private String aqi;
    private String direct;
    private String power;
    private String pm25;
    private String pm10;
    private String aqi_state;
    private String time;

    public static WeatherBean objectFromData(String str) {
        return new Gson().fromJson(str, WeatherBean.class);
    }

    public String getHigh_temp() {
        return high_temp;
    }

    public void setHigh_temp(String high_temp) {
        this.high_temp = high_temp;
    }

    public String getLow_temp() {
        return low_temp;
    }

    public void setLow_temp(String low_temp) {
        this.low_temp = low_temp;
    }

    public String getCurrent_temp() {
        return current_temp;
    }

    public void setCurrent_temp(String current_temp) {
        this.current_temp = current_temp;
    }

    public String getWt_state() {
        return wt_state;
    }

    public void setWt_state(String wt_state) {
        this.wt_state = wt_state;
    }

    public String getDay_1st_high() {
        return day_1st_high;
    }

    public void setDay_1st_high(String day_1st_high) {
        this.day_1st_high = day_1st_high;
    }

    public String getDay_1st_low() {
        return day_1st_low;
    }

    public void setDay_1st_low(String day_1st_low) {
        this.day_1st_low = day_1st_low;
    }

    public String getDay_2nd_high() {
        return day_2nd_high;
    }

    public void setDay_2nd_high(String day_2nd_high) {
        this.day_2nd_high = day_2nd_high;
    }

    public String getDay_2nd_low() {
        return day_2nd_low;
    }

    public void setDay_2nd_low(String day_2nd_low) {
        this.day_2nd_low = day_2nd_low;
    }

    public String getDay_3rd_high() {
        return day_3rd_high;
    }

    public void setDay_3rd_high(String day_3rd_high) {
        this.day_3rd_high = day_3rd_high;
    }

    public String getDay_3rd_low() {
        return day_3rd_low;
    }

    public void setDay_3rd_low(String day_3rd_low) {
        this.day_3rd_low = day_3rd_low;
    }

    public String getDay_1st_state() {
        return day_1st_state;
    }

    public void setDay_1st_state(String day_1st_state) {
        this.day_1st_state = day_1st_state;
    }

    public String getDay_2nd_state() {
        return day_2nd_state;
    }

    public void setDay_2nd_state(String day_2nd_state) {
        this.day_2nd_state = day_2nd_state;
    }

    public String getDay_3rd_state() {
        return day_3rd_state;
    }

    public void setDay_3rd_state(String day_3rd_state) {
        this.day_3rd_state = day_3rd_state;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getPm10() {
        return pm10;
    }

    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }

    public String getAqi_state() {
        return aqi_state;
    }

    public void setAqi_state(String aqi_state) {
        this.aqi_state = aqi_state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
