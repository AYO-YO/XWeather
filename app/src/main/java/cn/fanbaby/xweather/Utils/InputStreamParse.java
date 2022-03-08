package cn.fanbaby.xweather.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamParse {
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
