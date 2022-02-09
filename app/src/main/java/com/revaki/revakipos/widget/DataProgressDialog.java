package com.revaki.revakipos.widget;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.revaki.revakipos.R;

import java.util.Hashtable;

public class DataProgressDialog extends AlertDialog {
    Context context;
    LinearLayout lyDataProgressDialog;
    LinearLayout lyDataProgressDialogContent;
    Button btnPositiveButton;
    private String positiveButtonText = "OK";
    Hashtable<String, DataProgressItem> hashtable;


    public DataProgressDialog(Context context) {
        super(context);
        this.context = context;


        ScrollView scrollView = new ScrollView(context);
        lyDataProgressDialog = new LinearLayout(context);
        lyDataProgressDialogContent = new LinearLayout(context);
        btnPositiveButton = new Button(context, null, R.attr.borderlessButtonStyle);
        hashtable = new Hashtable<String, DataProgressItem>();

        lyDataProgressDialog.setOrientation(LinearLayout.VERTICAL);
        lyDataProgressDialogContent.setOrientation(LinearLayout.VERTICAL);
        int dialogPadding = context.getResources().getDimensionPixelOffset(R.dimen.data_progress_dialog_padding);
        lyDataProgressDialog.setPadding(dialogPadding, dialogPadding, dialogPadding, dialogPadding);

        btnPositiveButton.setTextColor(context.getResources().getColor(R.color.accent_material_light));
        btnPositiveButton.setText(positiveButtonText);
        btnPositiveButton.setVisibility(View.GONE);

        lyDataProgressDialog.addView(lyDataProgressDialogContent);
        lyDataProgressDialog.addView(btnPositiveButton);

        scrollView.addView(lyDataProgressDialog);
        this.setView(scrollView);
    }

    public DataProgressItem addDataProgressItem(String key) {
        DataProgressItem dataProgressItem = null;

        if (hashtable.containsKey(key)) {
            dataProgressItem = hashtable.get(key);
        } else {
            dataProgressItem = new DataProgressItem();
            lyDataProgressDialogContent.addView(dataProgressItem.dataProgressItemView);
            hashtable.put(key, dataProgressItem);
        }

        return dataProgressItem;
    }

    public void setPositiveButton(CharSequence text, View.OnClickListener listener) {
        if (listener == null) {
            listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataProgressDialog.this.dismiss();
                }
            };
        }
        btnPositiveButton.setOnClickListener(listener);
    }

    public void showPositiveButton() {
        if (!btnPositiveButton.hasOnClickListeners()) {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataProgressDialog.this.dismiss();
                }
            };
            btnPositiveButton.setOnClickListener(listener);
        }
        btnPositiveButton.setVisibility(View.VISIBLE);
    }

    public void hidePositiveButton() {
        btnPositiveButton.setVisibility(View.GONE);
    }


    public String getPositiveButtonText() {
        return positiveButtonText;
    }

    public void setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
    }

    public class DataProgressItem {

        View dataProgressItemView;
        ProgressBar pbProgress;
        ImageButton ibProgress;
        TextView tvProgress;
        String progress = "";

        DataProgressItem() {
            LayoutInflater inflater =  LayoutInflater.from(context);
            dataProgressItemView = inflater.inflate(R.layout.data_progress_item, null);

            pbProgress = (ProgressBar) dataProgressItemView.findViewById(R.id.pbProgress);
            ibProgress = (ImageButton) dataProgressItemView.findViewById(R.id.ibProgress);
            tvProgress = (TextView) dataProgressItemView.findViewById(R.id.tvProgress);

            ibProgress.setVisibility(View.GONE);

            View.OnClickListener DataProgressItemDetail = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] values = (String[]) view.getTag();

                    String progress = values[0];
                    String detail = values[1];
                    if (!detail.equals("")) {
                        Builder alertDialogBuilder = new Builder(context);
                        alertDialogBuilder.setMessage(detail);
                        alertDialogBuilder.setPositiveButton("OK", null);
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            };

            ibProgress.setOnClickListener(DataProgressItemDetail);
        }

        public void setProgress(String progress, String message, String detail) {
            this.progress = progress;
            tvProgress.setText(message);
            if (progress.equals("Running")) {
                ibProgress.setVisibility(View.GONE);
                pbProgress.setVisibility(View.VISIBLE);
            } else if (progress.equals("Completed")) {
                ibProgress.setImageResource(R.drawable.ic_success);
                pbProgress.setVisibility(View.GONE);
                ibProgress.setTag(new String[]{progress, detail});
                ibProgress.setVisibility(View.VISIBLE);
            } else if (progress.equals("Failed")) {
                ibProgress.setImageResource(R.drawable.ic_error);
                pbProgress.setVisibility(View.GONE);
                ibProgress.setVisibility(View.VISIBLE);
                ibProgress.setTag(new String[]{progress, detail});
            }
        }

        public String getProgress() {
            return this.progress;
        }


    }
}
