package cn.fanbaby.xweather.Utils;

import cn.fanbaby.xweather.R;


public class StateToIcon {
    /**
     * 将状态信息映射为drawable resource
     *
     * @param state 天气状态、
     * @return R.drawable.x
     */
    public static int stateToIcon(String state) {
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
                return R.drawable.ic_dongyu;
            case "强沙尘暴":
            case "沙尘暴":
                return R.drawable.ic_icon_shachenbao;
            case "浮尘":
                return R.drawable.ic_icon_fuchen;
            case "扬沙":
                return R.drawable.ic_icon_yangsha;
            case "霾":
                return R.drawable.ic_wumai;
            default:
                return R.drawable.ic_icon_wushuju;
        }
        return R.drawable.ic_icon_wushuju;
    }
}
