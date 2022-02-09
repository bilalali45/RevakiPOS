package com.revaki.revakipos.helper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SpinnerItem {

    public SpinnerItem() {
    }

    public SpinnerItem(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public SpinnerItem(String value, String text, String tag) {
        this.value = value;
        this.text = text;
        this.tag = tag;
    }

    private String value;
    private String text;
    private String tag;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public static <T> List<SpinnerItem> FillSpinnerItems(List<T> itemList, String valueField, String textField, String tagField) {
        List<SpinnerItem> spinnerItems = new ArrayList<SpinnerItem>();
        SpinnerItem spinnerItem;
        Field field = null;
        for (T item : itemList) {
            try {
                spinnerItem = new SpinnerItem();

                field = item.getClass().getDeclaredField(valueField);
                field.setAccessible(true);
                spinnerItem.setValue(field.get(item).toString());

                field = item.getClass().getDeclaredField(textField);
                field.setAccessible(true);
                spinnerItem.setText(field.get(item).toString());

                field = item.getClass().getDeclaredField(tagField);
                field.setAccessible(true);
                spinnerItem.setTag(field.get(item).toString());

                spinnerItems.add(spinnerItem);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return spinnerItems;
    }

    @Override
    public String toString() {
        return text;
    }


}
