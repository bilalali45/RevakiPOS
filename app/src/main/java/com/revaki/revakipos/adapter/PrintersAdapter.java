package com.revaki.revakipos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.revaki.revakipos.R;
import com.revaki.revakipos.beans.PrinterDetail;
import java.util.List;

public class PrintersAdapter extends BaseAdapter {

    Context context;
    List<PrinterDetail> printerDetails;

    public PrintersAdapter(Context context, List<PrinterDetail> printerDetails) {
        this.context = context;
        this.printerDetails = printerDetails;
    }

    @Override
    public int getCount() {
        return printerDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return printerDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.printer_detail_row, null);

            TextView tvTitle = convertView.findViewById(R.id.tvTitle);
            TextView tvPrinterType = convertView.findViewById(R.id.tvPrinterType);
            TextView tvPrinterName = convertView.findViewById(R.id.tvPrinterName);
            TextView tvPrinterDetail = convertView.findViewById(R.id.tvPrinterDetail);
            TextView tvPrinterSubDetail = convertView.findViewById(R.id.tvPrinterSubDetail);

            viewHolder = new ViewHolder(tvTitle, tvPrinterType, tvPrinterName,tvPrinterDetail,tvPrinterSubDetail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(printerDetails.get(position).getTitle());
        viewHolder.tvPrinterType.setText("Printer Type: "+ printerDetails.get(position).getPrinterType());
        viewHolder.tvPrinterName.setText("Printer: "+ printerDetails.get(position).getPrinterName());
        if(printerDetails.get(position).getPrinterType().equals("Network Printer")) {
            viewHolder.tvPrinterDetail.setText("Printer IP: " + printerDetails.get(position).getPrinterIp());
            viewHolder.tvPrinterSubDetail.setText("Port: " + printerDetails.get(position).getPort());
            viewHolder.tvPrinterDetail.setVisibility(View.VISIBLE);
            viewHolder.tvPrinterSubDetail.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.tvPrinterDetail.setText("");
            viewHolder.tvPrinterSubDetail.setText("");
            viewHolder.tvPrinterDetail.setVisibility(View.GONE);
            viewHolder.tvPrinterSubDetail.setVisibility(View.GONE);
        }
        return convertView;

    }


    public class ViewHolder {
        TextView tvTitle;
        TextView tvPrinterType;
        TextView tvPrinterName;
        TextView tvPrinterDetail;
        TextView tvPrinterSubDetail;

        public ViewHolder(TextView tvTitle, TextView tvPrinterType, TextView tvPrinterName, TextView tvPrinterDetail, TextView tvPrinterSubDetail) {
            this.tvTitle = tvTitle;
            this.tvPrinterType = tvPrinterType;
            this.tvPrinterName = tvPrinterName;
            this.tvPrinterDetail = tvPrinterDetail;
            this.tvPrinterSubDetail = tvPrinterSubDetail;
        }
    }

}
