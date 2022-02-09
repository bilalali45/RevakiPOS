package com.revaki.revakipos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.revaki.revakipos.R;
import com.revaki.revakipos.beans.OrderMaster;
import com.revaki.revakipos.helper.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransactionAdapter extends BaseAdapter {
    Context context;
    private List<OrderMaster> orderMasterList;

    public TransactionAdapter(Context context, List<OrderMaster> orderMasterList) {
        this.context = context;
        this.orderMasterList = orderMasterList;
    }

    @Override
    public int getCount() {
        return orderMasterList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderMasterList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.transaction_row, null);

            TextView tvReceiptNo = convertView.findViewById(R.id.tvReceiptNo);
            TextView tvTableName = convertView.findViewById(R.id.tvTableName);
            TextView tvWaiterName = convertView.findViewById(R.id.tvWaiterName);
            TextView tvCustomerName = convertView.findViewById(R.id.tvCustomerName);
            TextView tvNetAmount = convertView.findViewById(R.id.tvNetAmount);

            viewHolder = new ViewHolder(tvReceiptNo, tvTableName, tvWaiterName, tvCustomerName, tvNetAmount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvReceiptNo.setText("Receipt # " + orderMasterList.get(position).getDeviceReceiptNo());
        viewHolder.tvTableName.setText("Table : " + orderMasterList.get(position).getTableName());
        viewHolder.tvWaiterName.setText("Waiter : " + orderMasterList.get(position).getWaiterName());
        viewHolder.tvCustomerName.setText(orderMasterList.get(position).getCustomerName());
        viewHolder.tvNetAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(orderMasterList.get(position).getTotalAmount()));

        if (orderMasterList.get(position).getWaiterName().equals("") == false) {
            viewHolder.tvWaiterName.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvWaiterName.setVisibility(View.GONE);
        }

        if (orderMasterList.get(position).getCustomerName() != null && orderMasterList.get(position).getCustomerName().length() > 0) {
            viewHolder.tvCustomerName.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvCustomerName.setVisibility(View.GONE);
        }

        return convertView;
    }


    public class ViewHolder {

        TextView tvReceiptNo;
        TextView tvTableName;
        TextView tvWaiterName;
        TextView tvCustomerName;
        TextView tvNetAmount;

        public ViewHolder(TextView tvReceiptNo, TextView tvTableName, TextView tvWaiterName, TextView tvCustomerName, TextView tvNetAmount) {
            this.tvReceiptNo = tvReceiptNo;
            this.tvTableName = tvTableName;
            this.tvWaiterName = tvWaiterName;
            this.tvCustomerName = tvCustomerName;
            this.tvNetAmount = tvNetAmount;


        }
    }
}