package util;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class DataTypeChange {
    public static String time2String(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return simpleDateFormat.format(new Date(time));
    }

    public static String map2String(Map<String, Object> map) {
        if (map.isEmpty()) {
            return "";
        }
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        StringBuilder builder = new StringBuilder();
        for(Map.Entry entry:entries) {
            builder.append(entry.getKey()).append("=").append(URLEncoder.encode(String.valueOf(entry.getValue()))).append("&");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public static String size2String(long size) {
        double result = size / 1024.0 / 1024;
        String string = Double.parseDouble(new DecimalFormat("#.###").format(result)) + "MB";
        return string;
    }
}
