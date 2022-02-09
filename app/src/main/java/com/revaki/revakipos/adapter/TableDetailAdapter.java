package com.revaki.revakipos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.revaki.revakipos.R;
import com.revaki.revakipos.beans.TableDetail;

import java.util.List;

public class TableDetailAdapter extends BaseAdapter {
    Context context;
    private List<TableDetail> tableDetailList;

    public TableDetailAdapter(Context context, List<TableDetail> tableDetailList) {
        this.context = context;
        this.tableDetailList = tableDetailList;
    }

    @Override
    public int getCount() {
        return tableDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return tableDetailList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.table_detail_item, null);

            TextView tvTableStatus = convertView.findViewById(R.id.tvTableStatus);
            TextView tvTableTitle = convertView.findViewById(R.id.tvTableTitle);
            TextView tvTableCapacity = convertView.findViewById(R.id.tvTableCapacity);

            viewHolder = new ViewHolder(tvTableStatus, tvTableTitle, tvTableCapacity);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTableTitle.setText(tableDetailList.get(position).getTableName());
        viewHolder.tvTableCapacity.setText("Capacity : " + tableDetailList.get(position).getCapacity());
        if (tableDetailList.get(position).getActiveBillCount() > 0) {
            viewHolder.tvTableStatus.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.tvTableStatus.setVisibility(View.GONE);
        }

        return convertView;
    }


    public class ViewHolder {
        TextView tvTableStatus;
        TextView tvTableTitle;
        TextView tvTableCapacity;

        public ViewHolder(TextView tvTableStatus, TextView tvTableTitle, TextView tvTableCapacity) {
            this.tvTableStatus = tvTableStatus;
            this.tvTableTitle = tvTableTitle;
            this.tvTableCapacity = tvTableCapacity;

        }
    }
}

