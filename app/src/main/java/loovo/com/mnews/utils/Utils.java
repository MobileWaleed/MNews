package loovo.com.mnews.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import loovo.com.mnews.repository.model.SearchFilter;

public class Utils {
    public static Map convertSearchFilterToHashMap(SearchFilter filter) {
        Map<String, String> data = new HashMap<>();
        data.put("q", filter.getKeyWord());
        data.put("sortBy", filter.getSortBy());
        if (!filter.getLang().equals("all")) {
            data.put("language", filter.getLang());
        }

        return data;
    }

    public static Date convertStringToDate(String dateString) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            date = format.parse(dateString);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getRelativeTimeFromDate(Date date) {
        return DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), 0)
                .toString();
    }
}
