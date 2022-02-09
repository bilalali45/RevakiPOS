package com.revaki.revakipos;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.revaki.revakipos.widget.IconTextView;

public class CartOptionsBottomSheetFragment extends BottomSheetDialogFragment {

    private IconTextView tvClose;
    private OnOptionItemClickListener onOptionItemClickListener;
    private TextView tvPrintPreBill;
    private TextView tvKitchenPrint;
    private TextView tvKitchenReprint;
    private TextView tvRemoveOrder;
    private TextView tvCancel;

    public void setOnItemClickListener(OnOptionItemClickListener listener) {
        this.onOptionItemClickListener = listener;
    }

    public CartOptionsBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart_options_dialog, container, false);

        initViews(view);

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvPrintPreBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionItemClickListener.onPrintPreBill(view);
                dismiss();
            }
        });

        tvKitchenPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionItemClickListener.onKitchenPrint(view);
                dismiss();
            }
        });

        tvKitchenReprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionItemClickListener.onKitchenReprint(view);
                dismiss();
            }
        });

        tvRemoveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionItemClickListener.onRemoveOrder(view);
                dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet = (FrameLayout) dialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(0);
            }
        });
    }

    private void initViews(View view) {
        tvClose = view.findViewById(R.id.tvClose);
        tvPrintPreBill = view.findViewById(R.id.tvPrintPreBill);
        tvKitchenPrint = view.findViewById(R.id.tvKitchenPrint);
        tvKitchenReprint = view.findViewById(R.id.tvKitchenReprint);
        tvRemoveOrder = view.findViewById(R.id.tvRemoveOrder);
        tvCancel = view.findViewById(R.id.tvCancel);
    }

    public interface OnOptionItemClickListener {
        public void onPrintPreBill(View view);

        public void onKitchenPrint(View view);

        public void onKitchenReprint(View view);

        public void onRemoveOrder(View view);
    }

}

