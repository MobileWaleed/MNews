package loovo.com.mnews.db;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import loovo.com.mnews.db.entity.Source;

public class DataTypeConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static Source stringToItem(String data) {


        Type type = new TypeToken<Source>() {
        }.getType();

        return gson.fromJson(data, type);
    }

    @TypeConverter
    public static String ItemToString(Source someObject) {
        return gson.toJson(someObject);
    }
}