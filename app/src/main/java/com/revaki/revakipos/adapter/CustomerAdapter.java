package com.revaki.revakipos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.revaki.revakipos.R;
import com.revaki.revakipos.beans.Customer;
import com.revaki.revakipos.widget.RoundCornerImageView;

import java.util.List;

public class CustomerAdapter extends BaseAdapter {
    Context context;
    private List<Customer> customerList;

    public CustomerAdapter(Context context, List<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
    }

    @Override
    public int getCount() {
        return customerList.size();
    }

    @Override
    public Object getItem(int position) {
        return customerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.customer_row, null);

            TextView tvFullName = convertView.findViewById(R.id.tvFullName);
            TextView tvContactNo = convertView.findViewById(R.id.tvContactNo);
            TextView tvAddress = convertView.findViewById(R.id.tvAddress);
            TextView tvRewardBalance = convertView.findViewById(R.id.tvRewardBalance);

            viewHolder = new ViewHolder(tvFullName, tvContactNo, tvAddress, tvRewardBalance);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.tvFullName.setText(customerList.get(position).getFullName());
        viewHolder.tvContactNo.setText(customerList.get(position).getContactNo());
        viewHolder.tvAddress.setText(customerList.get(position).getAddress());
        viewHolder.tvRewardBalance.setText(customerList.get(position).getRewardBalance());


        return convertView;
    }


    public class ViewHolder {

        TextView tvFullName;
        TextView tvContactNo;
        TextView tvAddress;
        TextView tvRewardBalance;

        public ViewHolder(TextView tvFullName, TextView tvContactNo, TextView tvAddress, TextView tvRewardBalance) {
            this.tvFullName = tvFullName;
            this.tvContactNo = tvContactNo;
            this.tvAddress = tvAddress;
            this.tvRewardBalance = tvRewardBalance;
        }
    }
}