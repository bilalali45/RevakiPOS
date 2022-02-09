package com.revaki.revakipos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.revaki.revakipos.R;
import com.revaki.revakipos.beans.ShiftRecord;
import com.revaki.revakipos.helper.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.List;

public class ShiftRegisterAdapter extends BaseAdapter {
    Context context;
    private List<ShiftRecord> shiftRecordList;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa");

    public ShiftRegisterAdapter(Context context, List<ShiftRecord> shiftRecordList) {
        this.context = context;
        this.shiftRecordList = shiftRecordList;
    }

    @Override
    public int getCount() {
        return shiftRecordList.size();
    }

    @Override
    public Object getItem(int position) {
        return shiftRecordList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.shift_register_row, null);

            TextView tvOpenDate = convertView.findViewById(R.id.tvOpenDate);
            TextView tvCloseDate = convertView.findViewById(R.id.tvCloseDate);
            TextView tvOpenTime = convertView.findViewById(R.id.tvOpenTime);
            TextView tvCloseTime = convertView.findViewById(R.id.tvCloseTime);
            TextView tvOpenAmount = convertView.findViewById(R.id.tvOpenAmount);
            TextView tvCloseAmount = convertView.findViewById(R.id.tvCloseAmount);
            TextView tvShiftStatus = convertView.findViewById(R.id.tvShiftStatus);
            TextView tvShiftType = convertView.findViewById(R.id.tvShiftType);

            viewHolder = new ViewHolder(tvOpenDate, tvCloseDate, tvOpenTime, tvCloseTime, tvOpenAmount, tvCloseAmount, tvShiftStatus, tvShiftType);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvOpenDate.setText("Open Date : " + sdf.format(shiftRecordList.get(position).getStartTime()));
        if (shiftRecordList.get(position).getFinishTime() != null) {
            viewHolder.tvCloseDate.setText("Close Date : " + sdf.format(shiftRecordList.get(position).getFinishTime()));
        } else {
            viewHolder.tvCloseDate.setText("Close Date : ");
        }
        viewHolder.tvOpenTime.setText("Open Time : " + sdft.format(shiftRecordList.get(position).getStartTime()));
        if (shiftRecordList.get(position).getFinishTime() != null) {
            viewHolder.tvCloseTime.setText("Close Time : " + sdft.format(shiftRecordList.get(position).getFinishTime()));
        } else {
            viewHolder.tvCloseTime.setText("Close Time : ");
        }
        viewHolder.tvOpenAmount.setText("Open Amount : " + CommonUtils.formatTwoDecimal(shiftRecordList.get(position).getOpeningCash()));
        viewHolder.tvCloseAmount.setText("Close Amount : " + CommonUtils.formatTwoDecimal(shiftRecordList.get(position).getClosingCash()));

        if (shiftRecordList.get(position).getStatusId() == 1) {
            viewHolder.tvShiftStatus.setText("Shift Open");
            viewHolder.tvShiftStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
        } else {
            viewHolder.tvShiftStatus.setText("Shift Close");
            viewHolder.tvShiftStatus.setTextColor(context.getResources().getColor(R.color.colorGreen));
        }
        viewHolder.tvShiftType.setText(shiftRecordList.get(position).getShiftType());

        return convertView;
    }


    public class ViewHolder {

        TextView tvOpenDate;
        TextView tvCloseDate;
        TextView tvOpenTime;
        TextView tvCloseTime;
        TextView tvOpenAmount;
        TextView tvCloseAmount;
        TextView tvShiftStatus;
        TextView tvShiftType;

        public ViewHolder(TextView tvOpenDate, TextView tvCloseDate, TextView tvOpenTime, TextView tvCloseTime, TextView tvOpenAmount, TextView tvCloseAmount, TextView tvShiftStatus, TextView tvShiftType) {
            this.tvOpenDate = tvOpenDate;
            this.tvCloseDate = tvCloseDate;
            this.tvOpenTime = tvOpenTime;
            this.tvCloseTime = tvCloseTime;
            this.tvOpenAmount = tvOpenAmount;
            this.tvCloseAmount = tvCloseAmount;
            this.tvShiftStatus = tvShiftStatus;
            this.tvShiftType = tvShiftType;
        }
    }
}
