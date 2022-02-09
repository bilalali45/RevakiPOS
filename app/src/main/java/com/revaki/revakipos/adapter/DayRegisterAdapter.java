package com.revaki.revakipos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.revaki.revakipos.R;
import com.revaki.revakipos.beans.DayRecord;
import com.revaki.revakipos.helper.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.List;

public class DayRegisterAdapter  extends BaseAdapter {
    Context context;
    private List<DayRecord> dayRecordList;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

    public DayRegisterAdapter(Context context, List<DayRecord> dayRecordList) {
        this.context = context;
        this.dayRecordList = dayRecordList;
    }

    @Override
    public int getCount() {
        return dayRecordList.size();
    }

    @Override
    public Object getItem(int position) {
        return dayRecordList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.day_register_row, null);

            TextView tvShiftDate = convertView.findViewById(R.id.tvShiftDate);
            TextView tvNoOfShift = convertView.findViewById(R.id.tvNoOfShift);

            viewHolder = new ViewHolder(tvShiftDate, tvNoOfShift);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvShiftDate.setText("Shift Date : " + sdf.format(dayRecordList.get(position).getShiftDate()));
        viewHolder.tvNoOfShift.setText("No. of Shifts : " + dayRecordList.get(position).getNoOfShift());

        return convertView;
    }


    public class ViewHolder {

        TextView tvShiftDate;
        TextView tvNoOfShift;

        public ViewHolder(TextView tvShiftDate, TextView tvNoOfShift) {
            this.tvShiftDate = tvShiftDate;
            this.tvNoOfShift = tvNoOfShift;
        }
    }
}
