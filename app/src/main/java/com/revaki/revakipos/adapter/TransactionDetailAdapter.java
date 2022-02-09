package com.revaki.revakipos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.revaki.revakipos.R;
import com.revaki.revakipos.beans.OrderChild;
import com.revaki.revakipos.beans.OrderChildVariant;
import com.revaki.revakipos.beans.OrderMaster;
import com.revaki.revakipos.beans.VariantDetail;
import com.revaki.revakipos.helper.CommonUtils;

import java.util.List;

public class TransactionDetailAdapter extends BaseAdapter {
    Context context;
    private List<OrderChild> orderChildList;

    public TransactionDetailAdapter(Context context, List<OrderChild> orderChildList) {
        this.context = context;
        this.orderChildList = orderChildList;
    }

    @Override
    public int getCount() {
        return orderChildList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderChildList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.transaction_detail_row, null);

            TextView tvQuantity = convertView.findViewById(R.id.tvQuantity);
            TextView tvDishName = convertView.findViewById(R.id.tvDishName);
            TextView tvDishPrice = convertView.findViewById(R.id.tvDishPrice);
            TextView tvDishAmount = convertView.findViewById(R.id.tvDishAmount);

            viewHolder = new ViewHolder(tvQuantity, tvDishName, tvDishPrice, tvDishAmount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        float price = CommonUtils.parseFloat(orderChildList.get(position).getPrice());

        for(OrderChildVariant orderChildVariant: orderChildList.get(position).getVariants()) {
            for (VariantDetail variantDetail : orderChildVariant.getData()) {
                price += variantDetail.getPrice();
            }
        }

        viewHolder.tvQuantity.setText(CommonUtils.formatTwoDecimal(orderChildList.get(position).getQuantity()));
        viewHolder.tvDishName.setText(orderChildList.get(position).getDishName());
        viewHolder.tvDishPrice.setText("Rs. " + CommonUtils.formatTwoDecimal(price));
        viewHolder.tvDishAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(orderChildList.get(position).getTotalAmount()));


        return convertView;
    }


    public class ViewHolder {
        TextView tvQuantity;
        TextView tvDishName;
        TextView tvDishPrice;
        TextView tvDishAmount;

        public ViewHolder(TextView tvQuantity, TextView tvDishName, TextView tvDishPrice, TextView tvDishAmount) {
            this.tvQuantity = tvQuantity;
            this.tvDishName = tvDishName;
            this.tvDishPrice = tvDishPrice;
            this.tvDishAmount = tvDishAmount;


        }
    }
}