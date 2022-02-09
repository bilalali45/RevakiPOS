package com.revaki.revakipos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.revaki.revakipos.R;
import com.revaki.revakipos.beans.DishDetail;
import com.revaki.revakipos.widget.RoundCornerImageView;

import java.util.ArrayList;
import java.util.List;

public class DishGridAdapter extends BaseAdapter {

    Context context;
    List<DishDetail> dishDetailList;

    public DishGridAdapter(Context context, List<DishDetail> dishDetailList) {
        this.context = context;
        this.dishDetailList = dishDetailList;
    }

    @Override
    public int getCount() {
        return dishDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return dishDetailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.dish_detail_item, null);

            ImageView ivDishImage = convertView.findViewById(R.id.ivDishImage);
            TextView tvDishName = convertView.findViewById(R.id.tvDishName);
            TextView tvDishPrice = convertView.findViewById(R.id.tvDishPrice);

            viewHolder = new ViewHolder(ivDishImage, tvDishName, tvDishPrice);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context)
                .load(dishDetailList.get(position).getImageURL())
                .centerCrop()
                .placeholder(R.drawable.image_loading)
                .into(viewHolder.ivDishImage);

        viewHolder.tvDishName.setText(dishDetailList.get(position).getDishName());

        if (dishDetailList.get(position).getTotalPrice() > 0) {
            viewHolder.tvDishPrice.setText("Rs. " + dishDetailList.get(position).getTotalPrice());
        } else {
            viewHolder.tvDishPrice.setText("Rs. " + dishDetailList.get(position).getPriceStartFrom());
        }

        return convertView;
    }


    public class ViewHolder {
        ImageView ivDishImage;
        TextView tvDishName;
        TextView tvDishPrice;

        public ViewHolder(ImageView ivDishImage, TextView tvDishName, TextView tvDishPrice) {
            this.ivDishImage = ivDishImage;
            this.tvDishName = tvDishName;
            this.tvDishPrice = tvDishPrice;

        }
    }

}
