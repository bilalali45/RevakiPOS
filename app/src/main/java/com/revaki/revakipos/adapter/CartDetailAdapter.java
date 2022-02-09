package com.revaki.revakipos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.revaki.revakipos.R;
import com.revaki.revakipos.beans.OrderChild;
import com.revaki.revakipos.beans.OrderChildVariant;
import com.revaki.revakipos.beans.VariantDetail;
import com.revaki.revakipos.helper.CommonUtils;
import com.revaki.revakipos.widget.IconTextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class CartDetailAdapter extends BaseAdapter {
    Context context;
    private List<OrderChild> orderChildList;
    private OnItemClickListener onItemClickListener;
    SimpleDateFormat sdfDatetimeSTamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");

    public CartDetailAdapter(Context context, List<OrderChild> orderChildList) {
        this.context = context;
        this.orderChildList = orderChildList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.cart_detail_row, null);

            TextView tvQuantity = convertView.findViewById(R.id.tvQuantity);
            IconTextView btnPlus = convertView.findViewById(R.id.btnPlus);
            IconTextView btnMinus = convertView.findViewById(R.id.btnMinus);
            TextView tvDishName = convertView.findViewById(R.id.tvDishName);
            TextView tvDishPrice = convertView.findViewById(R.id.tvDishPrice);
            TextView tvNetAmount = convertView.findViewById(R.id.tvNetAmount);
            TextView tvAmount = convertView.findViewById(R.id.tvAmount);
            LinearLayout lyDishOptions = convertView.findViewById(R.id.lyDishOptions);
            LinearLayout lyItem = convertView.findViewById(R.id.lyItem);
            TextView tvNoOfItem = convertView.findViewById(R.id.tvNoOfItem);
            LinearLayout lySpecialInstruction = convertView.findViewById(R.id.lySpecialInstruction);
            TextView tvSpecialInstruction = convertView.findViewById(R.id.tvSpecialInstruction);
            LinearLayout lyDiscount = convertView.findViewById(R.id.lyDiscount);
            TextView tvDiscount = convertView.findViewById(R.id.tvDiscount);
            TextView tvDatetimeStamp = convertView.findViewById(R.id.tvDatetimeStamp);
            TextView tvVariantText = convertView.findViewById(R.id.tvVariantText);
            TextView tvVariantPrice = convertView.findViewById(R.id.tvVariantPrice);

            tvAmount.setPaintFlags(tvAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (parent.getTag() != null) {
                        View lyView = ((View) parent.getTag());

                        lyView.findViewById(R.id.lyDishOptions).setVisibility(View.GONE);
                        lyView.setBackgroundColor(Color.TRANSPARENT);
                    }
                    if (parent.getTag() != view) {
                        view.findViewById(R.id.lyDishOptions).setVisibility(View.VISIBLE);
                        view.setBackgroundColor(Color.parseColor("#f7f7f8"));
                        parent.setTag(view);
                    } else {
                        parent.setTag(null);
                    }
                }
            });


            viewHolder = new ViewHolder(tvQuantity, tvDishName, tvDishPrice, tvNetAmount, tvAmount, lyDishOptions, lyItem, tvNoOfItem, btnPlus, btnMinus, lySpecialInstruction, tvSpecialInstruction, lyDiscount, tvDiscount, tvDatetimeStamp, tvVariantText, tvVariantPrice);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (onItemClickListener != null) {
            viewHolder.btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onPlusClick(view, position, view.getId());
                }
            });

            viewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onMinusClick(view, position, view.getId());
                }
            });

            viewHolder.lyItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onQuantityClick(view, position, view.getId());
                }
            });

            viewHolder.lyDiscount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onDiscountClick(view, position, view.getId());
                }
            });

            viewHolder.lySpecialInstruction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onSpecialInstructionClick(view, position, view.getId());
                }
            });
        }

        float price = CommonUtils.parseFloat(orderChildList.get(position).getPrice());

        for (OrderChildVariant orderChildVariant : orderChildList.get(position).getVariants()) {
            for (VariantDetail variantDetail : orderChildVariant.getData()) {
                price += variantDetail.getPrice();
            }
        }

        viewHolder.tvQuantity.setText(CommonUtils.formatTwoDecimal(orderChildList.get(position).getQuantity()));
        viewHolder.tvDishName.setText(orderChildList.get(position).getDishName());
        viewHolder.tvDishPrice.setText("Rs. " + CommonUtils.formatTwoDecimal(price));
        viewHolder.tvNetAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(orderChildList.get(position).getTotalAmount()));
        viewHolder.tvNoOfItem.setText(CommonUtils.formatTwoDecimal(orderChildList.get(position).getQuantity()) + " Item");
        viewHolder.tvSpecialInstruction.setText(orderChildList.get(position).getSpecialInstruction());
        viewHolder.tvDiscount.setText("Rs. " + CommonUtils.formatTwoDecimal(orderChildList.get(position).getDiscountAmount()));

        float quantity = Float.valueOf(orderChildList.get(position).getQuantity());
        if (quantity > 1) {
            viewHolder.tvNoOfItem.setText(CommonUtils.formatTwoDecimal(orderChildList.get(position).getQuantity()) + " Items");
        }

        if (orderChildList.get(position).getSpecialInstruction().equals("") == false) {
            viewHolder.tvSpecialInstruction.setText(orderChildList.get(position).getSpecialInstruction());
        } else {
            viewHolder.tvSpecialInstruction.setText("Add Special Instruction");
        }

        if (Float.valueOf(orderChildList.get(position).getDiscountAmount()) > 0) {
            viewHolder.tvAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(Float.valueOf(orderChildList.get(position).getQuantity()) * Float.valueOf(orderChildList.get(position).getPrice())));
        } else {
            viewHolder.tvDiscount.setText("Discount");
            viewHolder.tvAmount.setText("");
        }

        if (orderChildList.get(position).getDatetimeStamp() != null) {
            viewHolder.tvDatetimeStamp.setText(sdfDatetimeSTamp.format(orderChildList.get(position).getDatetimeStamp()));
            viewHolder.tvDatetimeStamp.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvDatetimeStamp.setText("");
            viewHolder.tvDatetimeStamp.setVisibility(View.GONE);
        }
        if (orderChildList.get(position).getVariants().size() > 0) {
            String variantText = "";
            String variantPrice = "";
            for (OrderChildVariant orderChildVariant : orderChildList.get(position).getVariants()) {
                for (VariantDetail variantDetail : orderChildVariant.getData()) {
                    if (variantText.length() > 0) {
                        variantText += "\n";
                        variantPrice += "\n";
                    }
                    variantText += variantDetail.getText();
                    variantPrice += "Rs. " + CommonUtils.formatTwoDecimal(quantity * variantDetail.getPrice());
                }
            }
            viewHolder.tvVariantText.setText(variantText);
            viewHolder.tvVariantPrice.setText(variantPrice);
            viewHolder.tvVariantText.setVisibility(View.VISIBLE);
            viewHolder.tvVariantPrice.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvVariantText.setText("");
            viewHolder.tvVariantPrice.setText("");
            viewHolder.tvVariantText.setVisibility(View.GONE);
            viewHolder.tvVariantPrice.setVisibility(View.GONE);
        }


        if (parent.getTag() != convertView) {
            viewHolder.lyDishOptions.setVisibility(View.GONE);
        }
        return convertView;
    }


    public class ViewHolder {
        TextView tvQuantity;
        IconTextView btnPlus;
        IconTextView btnMinus;
        TextView tvDishName;
        TextView tvDishPrice;
        TextView tvNetAmount;
        TextView tvAmount;
        LinearLayout lyDishOptions;
        LinearLayout lyItem;
        TextView tvNoOfItem;
        LinearLayout lySpecialInstruction;
        TextView tvSpecialInstruction;
        LinearLayout lyDiscount;
        TextView tvDiscount;
        TextView tvDatetimeStamp;
        TextView tvVariantText;
        TextView tvVariantPrice;


        public ViewHolder(TextView tvQuantity, TextView tvDishName, TextView tvDishPrice, TextView tvNetAmount, TextView tvAmount, LinearLayout lyDishOptions, LinearLayout lyItem, TextView tvNoOfItem, IconTextView btnPlus, IconTextView btnMinus, LinearLayout lySpecialInstruction, TextView tvSpecialInstruction, LinearLayout lyDiscount, TextView tvDiscount, TextView tvDatetimeStamp, TextView tvVariantText, TextView tvVariantPrice) {
            this.tvQuantity = tvQuantity;
            this.btnPlus = btnPlus;
            this.btnMinus = btnMinus;
            this.tvDishName = tvDishName;
            this.tvDishPrice = tvDishPrice;
            this.tvNetAmount = tvNetAmount;
            this.tvAmount = tvAmount;
            this.lyDishOptions = lyDishOptions;
            this.lyItem = lyItem;
            this.tvNoOfItem = tvNoOfItem;
            this.lySpecialInstruction = lySpecialInstruction;
            this.tvSpecialInstruction = tvSpecialInstruction;
            this.lyDiscount = lyDiscount;
            this.tvDiscount = tvDiscount;
            this.tvDatetimeStamp = tvDatetimeStamp;
            this.tvVariantPrice = tvVariantPrice;
            this.tvVariantText = tvVariantText;
        }
    }

    public interface OnItemClickListener {
        public void onPlusClick(View view, int position, long id);

        public void onMinusClick(View view, int position, long id);

        public void onQuantityClick(View view, int position, long id);

        public void onDiscountClick(View view, int position, long id);

        public void onSpecialInstructionClick(View view, int position, long id);

    }
}
