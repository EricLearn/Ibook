package com.example.eric.Http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Eric
 */

public class DataParseUtil {

    private DataParseUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 当业务层未指定返回数据的类型时，通过匹配的方式确定，不过最好业务层指定
     * 默认指定为 object  要更换 自己指定
     * @param result
     * @return
     */
    @Deprecated
    public static HttpTypeHelper.BodyDataType matchDataTypeOfResult(String result) {

        HttpTypeHelper.BodyDataType type = HttpTypeHelper.BodyDataType.UNKNOWN;
        if (result != null) {
            if (result.startsWith("[")) {
                type = HttpTypeHelper.BodyDataType.JSON_ARRAY;
            }
            else if (result.startsWith("{")) {
                type = HttpTypeHelper.BodyDataType.JSON_OBJECT;
            }
            else if (result.startsWith("<")) {
            }
            else {
                type = HttpTypeHelper.BodyDataType.STRING;
            }
        }
        return type;
    }

    public static <T> T parseObject(String string, Class<T> clazz) {
        return new Gson().fromJson(string, clazz);
    }

    public static <T> ArrayList<T> parseToArrayList(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);
        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    public static <T> List<T> parseToList(String json, Class<T[]> clazz) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }

    public static Object parseXml(String json, Class<?> clazz) {
        try {
            if (!TextUtils.isEmpty(json) && clazz != null) {
                //Serializer serializer = new Persister();
                //InputStreamReader is = new InputStreamReader(new ByteArrayInputStream(json.getBytes("UTF-8")), "utf-8");
                //return serializer.read(clazz, is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
