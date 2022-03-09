package cn.fanbaby.xweather.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamParse {
    /**
     * 将字节流转换为字符流，常用于网络请求到的数据和文件读取
     *
     * @param in 字节流
     * @return 字符串
     */
    public static String inputStream_to_String(InputStream in) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inReader = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(inReader);

        String temp;
        while (true) {
            try {
                if ((temp = br.readLine()) == null)
                    break;
                else {
                    stringBuilder.append(temp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}
