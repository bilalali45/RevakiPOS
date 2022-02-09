package com.revaki.revakipos.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class DataRow extends Hashtable<String, String> {

    public String getString(String key) {
        return get(key);
    }

    public int getInt(String key) {
        return Integer.valueOf("0" + get(key));
    }

    public float getFloat(String key) {
        return Float.valueOf("0" + get(key));
    }

    public double getDouble(String key) {
        return Double.valueOf("0" + get(key));
    }

    public String getDouble(String key, String format) {
        return String.format(format, Double.valueOf("0" + get(key)));
    }

    public Long getLong(String key) {
        return Long.valueOf("0" + get(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.valueOf(get(key));
    }

    public Date getDate(String key) {
        return new Date(Long.valueOf(get(key)));
    }

    public String getDate(String key, String format) {
        return new SimpleDateFormat(format).format(Long.valueOf(get(key)));
    }
}