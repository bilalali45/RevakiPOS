package com.revaki.revakipos;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.revaki.revakipos.adapter.CartDetailAdapter;
import com.revaki.revakipos.beans.OrderChild;
import com.revaki.revakipos.beans.OrderMaster;
import com.revaki.revakipos.beans.WaiterDetail;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.helper.ActivityRequest;
import com.revaki.revakipos.helper.CommonUtils;
import com.revaki.revakipos.helper.UIHelper;
import com.revaki.revakipos.widget.IconTextView;

import java.util.List;


public class CartFragment extends Fragment {

    private ApplicationDAL applicationDAL;

    String orderMasterId = null;
    OrderMaster orderMaster = null;

    private ListView listCart;
    private TextView tvSubtotal;
    private TextView tvDiscount;
    private IconTextView tvRemoveDiscount;
    private LinearLayout lySaleTax;
    private TextView tvSaleTaxPercent;
    private TextView tvSaleTaxAmount;
    private TextView tvDeliveryFeeAmount;
    private TextView tvTotal;
    private ImageButton btnPrintKOT;
    private ImageButton btnPrintPreBill;
    private ImageButton btnWaiter;
    private Button btnCheckout;
    private Button btnOptions;

    private List<OrderChild> orderChildList;
    private CartDetailAdapter cartDetailAdapter;

    private OnFragmentActionListener mFragmentActionListener = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listCart = getView().findViewById(R.id.listCart);
        tvSubtotal = getView().findViewById(R.id.tvSubtotal);
        tvDiscount = getView().findViewById(R.id.tvDiscount);
        tvRemoveDiscount = getView().findViewById(R.id.tvRemoveDiscount);
        lySaleTax = getView().findViewById(R.id.lySaleTax);
        tvSaleTaxPercent = getView().findViewById(R.id.tvSaleTaxPercent);
        tvSaleTaxAmount = getView().findViewById(R.id.tvSaleTaxAmount);
        tvDeliveryFeeAmount = getView().findViewById(R.id.tvDeliveryFeeAmount);
        tvTotal = getView().findViewById(R.id.tvTotal);
        btnPrintKOT = getView().findViewById(R.id.btnPrintKOT);
        btnPrintPreBill = getView().findViewById(R.id.btnPrintPreBill);
        btnWaiter = getView().findViewById(R.id.btnWaiter);
        btnCheckout = getView().findViewById(R.id.btnCheckout);
        btnOptions = getView().findViewById(R.id.btnOptions);

        lySaleTax.setVisibility(View.GONE);
        tvRemoveDiscount.setVisibility(View.GONE);
        tvDeliveryFeeAmount.setVisibility(View.GONE);

        applicationDAL = new ApplicationDAL(getActivity());
        orderMasterId = getActivity().getIntent().getStringExtra("OrderMasterId");

        loadOrderMaster(orderMasterId);


        tvRemoveDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCartDiscount("0", "0", 1);
            }
        });

        tvDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderMasterId != null) {
                    Intent intent = new Intent(getActivity(), CartDiscountActivity.class);
                    intent.putExtra("Title", "Total");
                    intent.putExtra("DiscountTypeId", orderMaster.getDiscountTypeId());
                    intent.putExtra("DiscountAmount", orderMaster.getDiscountAmount());
                    intent.putExtra("DiscountPercentage", orderMaster.getDiscountPercentage());
                    intent.putExtra("Amount", orderMaster.getSubTotalAmount());
                    startActivityForResult(intent, ActivityRequest.REQUEST_CART_DISCOUNT);
                } else {
                    UIHelper.showErrorDialog(getActivity(), "", "Cart is empty");
                }
            }
        });

        btnPrintKOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KitchenPrint();
            }
        });


        btnPrintPreBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreBillPrint();
            }
        });

        btnWaiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWaiterDialog();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderMasterId != null) {
                    Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                    intent.putExtra("OrderMasterId", orderMasterId);
                    getActivity().startActivityForResult(intent, ActivityRequest.REQUEST_CHECKOUT);
                } else {
                    UIHelper.showErrorDialog(getActivity(), "", "Cart is empty");
                }
            }
        });

        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderMasterId != null) {
                    showBottomSheetDialogFragment();
                } else {
                    UIHelper.showErrorDialog(getActivity(), "", "Cart is empty");
                }
            }
        });


    }

    int selectedWaiterIndex;

    private void showWaiterDialog() {
        if (orderMasterId != null) {

            final List<WaiterDetail> waiterDetails = applicationDAL.getWaiterDetail("", "");

            if (waiterDetails.size() > 0) {

                for (int i = 0; i < waiterDetails.size(); i++) {
                    if (waiterDetails.get(i).getWaiterId().equals(orderMaster.WaiterId)) {
                        selectedWaiterIndex = i;
                        break;
                    }
                }

                ArrayAdapter<WaiterDetail> waiterDetailArrayAdapter = new ArrayAdapter<WaiterDetail>(getActivity(), android.R.layout.simple_list_item_single_choice, waiterDetails);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Waiter");
                builder.setSingleChoiceItems(waiterDetailArrayAdapter, selectedWaiterIndex, null);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        selectedWaiterIndex = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        orderMaster.setWaiterId(waiterDetails.get(selectedWaiterIndex).getWaiterId());
                        orderMaster.setWaiterName(waiterDetails.get(selectedWaiterIndex).getWaiterName());

                        applicationDAL.addUpdateOrderMaster(orderMaster);
                    }
                });

                builder.setNegativeButton("Cancel", null);

                builder.show();
            } else {
                UIHelper.showShortToast(getActivity(), "Waiter not found.");
            }

        } else {
            UIHelper.showErrorDialog(getActivity(), "", "Cart is empty");
        }
    }


    public void showBottomSheetDialogFragment() {
        CartOptionsBottomSheetFragment cartOptionsBottomSheetFragment = new CartOptionsBottomSheetFragment();
        cartOptionsBottomSheetFragment.setOnItemClickListener(new CartOptionsBottomSheetFragment.OnOptionItemClickListener() {
            @Override
            public void onPrintPreBill(View view) {
                PreBillPrint();
            }

            @Override
            public void onKitchenPrint(View view) {
                KitchenPrint();
            }

            @Override
            public void onKitchenReprint(View view) {
                showPinDialog(new OnPinSubmitListener() {
                    @Override
                    public void onSubmit(boolean isValid) {
                        if (isValid) {
                            Intent intent = new Intent(getActivity(), PrintViewActivity.class);
                            intent.putExtra("PrintType", "KitchenReprint");
                            intent.putExtra("OrderMasterId", orderMasterId);
                            startActivityForResult(intent, ActivityRequest.REQUEST_PRINT_KITCHEN);
                        }
                    }
                });
            }

            @Override
            public void onRemoveOrder(View view) {

                OnPinSubmitListener onPinSubmitListener = new OnPinSubmitListener() {
                    @Override
                    public void onSubmit(boolean isValid) {
                        if (isValid) {
                            UIHelper.showConfirmDialog(getActivity(), "Empty Cart", "Do you want to empty the shopping cart?", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    orderMaster.setStatusId(3);
                                    applicationDAL.addUpdateOrderMaster(orderMaster);
                                    if (getActivity().getIntent().getStringExtra("OrderMasterId") != null) {
                                        Intent intent = new Intent();
                                        intent.putExtra("OrderMasterId", "");
                                        getActivity().setResult(Activity.RESULT_OK, intent);
                                        getActivity().finish();
                                    } else if (mFragmentActionListener != null) {
                                        mFragmentActionListener.onFragmentAction("RemoveOrder", "");
                                    }
                                }
                            });
                        }
                    }
                };

                float printQuantity = 0;

                for (OrderChild orderChild : orderMaster.getOrderChilds()) {
                    printQuantity += Float.valueOf(orderChild.getPrintQuantity());
                }

                if (printQuantity == 0) {
                    onPinSubmitListener.onSubmit(true);
                } else {
                    showPinDialog(onPinSubmitListener);
                }
            }
        });
        cartOptionsBottomSheetFragment.show(getActivity().getSupportFragmentManager(), cartOptionsBottomSheetFragment.getTag());
    }


    private void KitchenPrint() {
        if (orderMasterId != null) {

            float balanceQuantity = 0;

            for (OrderChild orderChild : orderMaster.getOrderChilds()) {
                balanceQuantity += (Float.valueOf(orderChild.getQuantity()) - Float.valueOf(orderChild.getPrintQuantity()));
            }

            if (balanceQuantity != 0) {
                Intent intent = new Intent(getActivity(), PrintViewActivity.class);
                intent.putExtra("PrintType", "KitchenPrint");
                intent.putExtra("OrderMasterId", orderMasterId);
                startActivityForResult(intent, ActivityRequest.REQUEST_PRINT_KITCHEN);
            } else {
                UIHelper.showAlertDialog(getActivity(), "", "KOT already printed, no new items found for print.");
            }
        } else {
            UIHelper.showErrorDialog(getActivity(), "", "Cart is empty");
        }
    }


    public void PreBillPrint() {
        if (orderMasterId != null) {

            final Runnable preBill = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getActivity(), PrintViewActivity.class);
                    intent.putExtra("PrintType", "PreBill");
                    intent.putExtra("OrderMasterId", orderMasterId);
                    startActivityForResult(intent, ActivityRequest.REQUEST_PRINT_PRE_BILL);
                }
            };

            if (Configuration.getPlaceDetail().getCardGSTPercentage().equals("0") == false && Configuration.getPlaceDetail().getGSTPercentage() != Configuration.getPlaceDetail().getCardGSTPercentage()) {
                UIHelper.showConfirmDialog(getActivity(), "", "Proceed order with cash or card?", "Card", "Cash", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        orderMaster.setCashAmount("0");
                        orderMaster.setPaymentTypeId(2);
                        orderMaster.setSalesTaxPercent(Configuration.getPlaceDetail().getCardGSTPercentage());
                        orderMaster.calculateValues();
                        applicationDAL.addUpdateOrderMaster(orderMaster);

                        preBill.run();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        orderMaster.setCardAmount("0");
                        orderMaster.setPaymentTypeId(1);
                        orderMaster.setSalesTaxPercent(Configuration.getPlaceDetail().getGSTPercentage());
                        orderMaster.calculateValues();
                        applicationDAL.addUpdateOrderMaster(orderMaster);

                        preBill.run();
                    }
                });
            } else {
                preBill.run();
            }

        } else {
            UIHelper.showErrorDialog(getActivity(), "", "Cart is empty");
        }
    }

    public void loadOrderMaster(String orderMasterId) {
        this.orderMasterId = orderMasterId;

        orderMaster = new OrderMaster();
        if (orderMasterId != null) {
            orderMaster = applicationDAL.getOrderMaster(orderMasterId);
        }
        orderChildList = orderMaster.getOrderChilds();

        cartDetailAdapter = new CartDetailAdapter(getActivity(), orderChildList);
        cartDetailAdapter.setOnItemClickListener(onCartItemClickListener);
        listCart.setAdapter(cartDetailAdapter);

        calculateAmounts(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityRequest.REQUEST_CART_ITEM_DISCOUNT && resultCode == Activity.RESULT_OK) {
            updateCartItemDiscount(data.getExtras().getString("DiscountAmount"), data.getExtras().getString("DiscountPercentage"), data.getExtras().getInt("Position", 0), data.getExtras().getInt("DiscountTypeId"));
        } else if (requestCode == ActivityRequest.REQUEST_CART_DISCOUNT && resultCode == Activity.RESULT_OK) {
            updateCartDiscount(data.getExtras().getString("DiscountAmount"), data.getExtras().getString("DiscountPercentage"), data.getExtras().getInt("DiscountTypeId"));

        } else if (requestCode == ActivityRequest.REQUEST_CART_EDIT_QUANTITY && resultCode == Activity.RESULT_OK) {
            updateCartItemQuantity(data.getExtras().getString("Value"), data.getExtras().getInt("Position"));
        } else if (requestCode == ActivityRequest.REQUEST_PRINT_KITCHEN) {
            loadOrderMaster(orderMasterId);
            mFragmentActionListener.onFragmentAction("Change", orderMasterId);
        }
    }

    private void updateCartItemDiscount(String DiscountAmount, String DiscountPercentage, int Position, int DiscountTypeId) {
        orderChildList.get(Position).setDiscountAmount(DiscountAmount);
        orderChildList.get(Position).setDiscountPercentage(DiscountPercentage);
        orderChildList.get(Position).setDiscountTypeId(DiscountTypeId);

        orderChildList.get(Position).calculateValues();
        orderMaster.calculateValues();

        applicationDAL.addUpdateOrderChild(orderChildList.get(Position));
        applicationDAL.addUpdateOrderMaster(orderMaster);

        cartDetailAdapter.notifyDataSetChanged();

        calculateAmounts(true);
    }

    private void updateCartItemQuantity(String quantity, int position) {
        orderChildList.get(position).setQuantity(quantity);
        orderChildList.get(position).calculateValues();
        orderMaster.calculateValues();

        applicationDAL.addUpdateOrderChild(orderChildList.get(position));
        applicationDAL.addUpdateOrderMaster(orderMaster);

        cartDetailAdapter.notifyDataSetChanged();

        calculateAmounts(true);
    }


    private void updateCartDiscount(String DiscountAmount, String DiscountPercentage, int DiscountTypeId) {
        orderMaster.setDiscountAmount(DiscountAmount);
        orderMaster.setDiscountPercentage(DiscountPercentage);
        orderMaster.setDiscountTypeId(DiscountTypeId);
        orderMaster.calculateValues();

        applicationDAL.addUpdateOrderMaster(orderMaster);

        calculateAmounts(true);
    }

    private CartDetailAdapter.OnItemClickListener onCartItemClickListener = new CartDetailAdapter.OnItemClickListener() {
        @Override
        public void onPlusClick(View view, int position, long id) {
            float quantity = Float.valueOf(orderChildList.get(position).getQuantity()) + 1;

            orderChildList.get(position).setQuantity(CommonUtils.parseTwoDecimal(quantity));
            orderChildList.get(position).calculateValues();
            orderMaster.calculateValues();

            applicationDAL.addUpdateOrderChild(orderChildList.get(position));
            applicationDAL.addUpdateOrderMaster(orderMaster);

            cartDetailAdapter.notifyDataSetChanged();
            calculateAmounts(true);
        }

        @Override
        public void onMinusClick(View view, final int position, long id) {

            OnPinSubmitListener onPinSubmitListener = new OnPinSubmitListener() {
                @Override
                public void onSubmit(boolean isValid) {
                    if (isValid) {
                        float quantity = Float.valueOf(orderChildList.get(position).getQuantity()) - 1;

                        if (quantity > 0) {
                            orderChildList.get(position).setQuantity(CommonUtils.parseTwoDecimal(quantity));
                            orderChildList.get(position).calculateValues();
                            orderMaster.calculateValues();

                            applicationDAL.addUpdateOrderChild(orderChildList.get(position));
                            applicationDAL.addUpdateOrderMaster(orderMaster);

                        } else {
                            String primaryKey = orderChildList.get(position).getOrderChildId();
                            orderChildList.remove(position);
                            orderMaster.calculateValues();

                            applicationDAL.deleteOrderChild(primaryKey);
                            applicationDAL.addUpdateOrderMaster(orderMaster);

                            if (orderChildList.size() == 0) {
                                applicationDAL.deleteOrderMaster(orderMasterId);
                                if (getActivity().getIntent().getStringExtra("OrderMasterId") != null) {
                                    Intent intent = new Intent();
                                    intent.putExtra("OrderMasterId", "");
                                    getActivity().setResult(Activity.RESULT_OK, intent);
                                    getActivity().finish();
                                } else if (mFragmentActionListener != null) {
                                    mFragmentActionListener.onFragmentAction("RemoveOrder", "");
                                }
                            }
                        }

                        cartDetailAdapter.notifyDataSetChanged();
                        calculateAmounts(true);
                    }
                }
            };

            float balanceQuantity = Float.valueOf(orderChildList.get(position).getQuantity()) - Float.valueOf(orderChildList.get(position).getPrintQuantity());
            if (balanceQuantity > 0) {
                onPinSubmitListener.onSubmit(true);
            } else {
                showPinDialog(onPinSubmitListener);
            }
        }

        @Override
        public void onQuantityClick(final View view, final int position, long id) {

            OnPinSubmitListener onPinSubmitListener = new OnPinSubmitListener() {
                @Override
                public void onSubmit(boolean isValid) {
                    if (isValid) {
                        Intent intent = new Intent(getActivity(), EditCartQuantityPriceActivity.class);
                        intent.putExtra("Title", "Edit quantity");
                        intent.putExtra("Value", orderChildList.get(position).getQuantity());
                        intent.putExtra("Position", position);

                        startActivityForResult(intent, ActivityRequest.REQUEST_CART_EDIT_QUANTITY);
                    }
                }
            };

            float printQuantity = Float.valueOf(orderChildList.get(position).getPrintQuantity());
            if (printQuantity == 0) {
                onPinSubmitListener.onSubmit(true);
            } else {
                showPinDialog(onPinSubmitListener);
            }
        }

        @Override
        public void onDiscountClick(View view, int position, long id) {
            Intent intent = new Intent(getActivity(), CartDiscountActivity.class);
            intent.putExtra("Title", orderChildList.get(position).getQuantity() + "x" + " " + orderChildList.get(position).getDishName());
            intent.putExtra("DiscountTypeId", orderChildList.get(position).getDiscountTypeId());
            intent.putExtra("DiscountAmount", orderChildList.get(position).getDiscountAmount());
            intent.putExtra("DiscountPercentage", orderChildList.get(position).getDiscountPercentage());
            intent.putExtra("Amount", orderChildList.get(position).getAmount());
            intent.putExtra("Position", position);

            startActivityForResult(intent, ActivityRequest.REQUEST_CART_ITEM_DISCOUNT);
        }

        @Override
        public void onSpecialInstructionClick(View view, final int position, long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Special Instruction");

            final EditText etSpecialInstruction = new EditText(getActivity());
            etSpecialInstruction.setPadding(30, 30, 30, 30);
            builder.setView(etSpecialInstruction);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    orderChildList.get(position).setSpecialInstruction(etSpecialInstruction.getText().toString());
                    applicationDAL.addUpdateOrderChild(orderChildList.get(position));

                    calculateAmounts(true);
                    View lyView = ((View) listCart.getTag());
                    TextView tvSpecialInstruction = lyView.findViewById(R.id.tvSpecialInstruction);

                    if (orderChildList.get(position).getSpecialInstruction().equals("") == false) {
                        tvSpecialInstruction.setText(orderChildList.get(position).getSpecialInstruction());
                    } else {
                        tvSpecialInstruction.setText("Add Special Instruction");
                    }
                }
            });

            builder.setNegativeButton("Cancel", null);
            builder.show();
        }
    };


    interface OnPinSubmitListener {
        void onSubmit(boolean isValid);
    }

    private void showPinDialog(final OnPinSubmitListener onPinSubmitListener) {


        final EditText input = new EditText(getActivity());
        input.setPadding(30, 30, 30, 30);
        input.setSingleLine(true);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getActivity())
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null).create();

        alertDialog.setTitle("Enter your PIN:");
        alertDialog.setView(input);

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((android.app.AlertDialog) alertDialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (input.getText().length() > 0 && Integer.valueOf(input.getText().toString()) == Configuration.getUser().getModificationKey()) {
                            alertDialog.dismiss();
                            onPinSubmitListener.onSubmit(true);
                        } else {
                            UIHelper.showShortToast(getActivity(), "Invalid PIN.");
                            onPinSubmitListener.onSubmit(false);
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void calculateAmounts(boolean updateParent) {
        float saleTaxAmount = Float.valueOf(orderMaster.getSalesTaxAmount());
        float discountAmount = Float.valueOf(orderMaster.getDiscountAmount());
        float deliveryFeeAmount = Float.valueOf(orderMaster.getDeliveryFeeAmount());

        tvSubtotal.setText("Subtotal: Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getSubTotalAmount()));
        if (saleTaxAmount > 0) {
            lySaleTax.setVisibility(View.VISIBLE);
            tvSaleTaxPercent.setText("Sale tax " + CommonUtils.formatTwoDecimal(orderMaster.getSalesTaxPercent()) + "%: ");
            tvSaleTaxAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(saleTaxAmount));
        } else {
            lySaleTax.setVisibility(View.GONE);
            tvSaleTaxPercent.setText("");
            tvSaleTaxAmount.setText("");
        }

        if (discountAmount > 0) {
            tvDiscount.setText("Discount: (" + orderMaster.getDiscountPercentage() + "%) Rs. " + CommonUtils.formatTwoDecimal(discountAmount));
            tvDiscount.setTextColor(getResources().getColor(R.color.colorRed));
            tvRemoveDiscount.setVisibility(View.VISIBLE);
        } else {
            tvDiscount.setText("Add Discount");
            tvDiscount.setTextColor(Color.parseColor("#c7c8ca"));
            tvRemoveDiscount.setVisibility(View.GONE);
        }

        if (deliveryFeeAmount > 0) {
            tvDeliveryFeeAmount.setText("Delivery Fee: Rs. " + CommonUtils.formatTwoDecimal(deliveryFeeAmount));
            tvDeliveryFeeAmount.setVisibility(View.VISIBLE);
        } else {
            tvDeliveryFeeAmount.setText("");
            tvDeliveryFeeAmount.setVisibility(View.GONE);
        }

        tvTotal.setText("Total: Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));
        btnCheckout.setText(CommonUtils.formatTwoDecimal(orderMaster.getNumberOfItems()) + " items = Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));

        if (updateParent && mFragmentActionListener != null) {
            mFragmentActionListener.onFragmentAction("Change", orderMasterId);
        }
    }

    public void setFragmentActionListener(OnFragmentActionListener l) {
        mFragmentActionListener = l;
    }

    public interface OnFragmentActionListener {

        void onFragmentAction(String actionKey, String orderMasterId);
    }
}
