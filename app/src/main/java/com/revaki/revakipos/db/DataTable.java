package com.revaki.revakipos.db;

import android.database.Cursor;

import java.util.ArrayList;

public class DataTable extends ArrayList<DataRow> {

    public void fillByCursor(Cursor cursor) {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DataRow dataRow = new DataRow();
            for (int i = 0; i < cursor.getColumnNames().length; i++) {
                dataRow.put(cursor.getColumnName(i), cursor.getString(i));
            }
            add(dataRow);
            cursor.moveToNext();
        }
    }

    private Object getValueAt(Cursor cursor, int colIndex) {
        int type = cursor.getType(colIndex);
        switch (type) {
            case Cursor.FIELD_TYPE_STRING:
                return cursor.getString(colIndex);
            case Cursor.FIELD_TYPE_INTEGER:
                return cursor.getInt(colIndex);
            case Cursor.FIELD_TYPE_FLOAT:
                return cursor.getFloat(colIndex);
            case Cursor.FIELD_TYPE_BLOB:
                return cursor.getBlob(colIndex);
            case Cursor.FIELD_TYPE_NULL:
                return null;
            default:
                return null;
        }
    }


}