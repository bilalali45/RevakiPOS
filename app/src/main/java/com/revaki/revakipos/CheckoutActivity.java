package com.revaki.revakipos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.revaki.revakipos.adapter.CustomerAdapter;
import com.revaki.revakipos.beans.Customer;
import com.revaki.revakipos.beans.DeliveryCompanyDetail;
import com.revaki.revakipos.beans.OrderCardDetail;
import com.revaki.revakipos.beans.OrderMaster;
import com.revaki.revakipos.beans.SettingDetail;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.db.DBHelper;
import com.revaki.revakipos.db.DataRow;
import com.revaki.revakipos.helper.ActivityRequest;
import com.revaki.revakipos.helper.CommonUtils;
import com.revaki.revakipos.helper.UIHelper;
import com.revaki.revakipos.widget.IconTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private ApplicationDAL applicationDAL;

    String orderMasterId = null;
    OrderMaster orderMaster = null;

    private TextView tvNetAmount;
    private TextView tvSubtotal;
    private TextView tvDiscount;
    private TextView tvSaleTax;
    private TextView tvDeliveryFeeAmount;
    private TextView tvChangeAmount;
    private EditText etTipAmount;
    private EditText etCashAmount;
    private EditText etCardAmount;
    private LinearLayout lyCard;
    private LinearLayout lyCardType;
    private EditText etCardNumber;
    private EditText etCustomerName;
    private EditText etContactNo;
    private EditText etAddress;
    private LinearLayout lyDeliveryDetail;
    private RadioButton rbNow;
    private RadioButton rbLater;
    private LinearLayout lyDeliveryDate;
    private TextView tvDeliveryDate;
    private TextView tvDeliveryTime;
    private EditText etDeliveryFee;
    private Spinner spDeliveryCompany;
    private EditText etRiderName;
    private EditText etRiderMobileNo;
    private EditText etRiderBikeNo;
    private Button btnSplitCard;
    private Button btnPay;
    private RelativeLayout ryCartFinish;
    private TextView tvFinishAmount;
    private RelativeLayout btnReceipt;
    private LinearLayout lyCheckout;
    int mode = 1;
    float changeAmount = 0;
    int deliveryType = 1;
    private Calendar deliveryDateCalendar;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    private SimpleDateFormat stf = new SimpleDateFormat("hh:mm aa");

    private ImageView selectedCardType = null;

    private SearchView svCustomer;
    private ListView listCustomer;

    private AlertDialog customerDialog;
    private AlertDialog cardSplitDialog;

    private List<Customer> customerList = null;
    private List<DeliveryCompanyDetail> deliveryCompanyDetails;

    private EditText etNoOfSplit;
    private LinearLayout lySplitContainer;

    private SettingDetail settingDetail;

    private int noOfCardSplit;
    private float splitAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        getSupportActionBar().setTitle("Checkout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        createCardSplitDialog();
        createCustomerDialog();

        deliveryDateCalendar = Calendar.getInstance();

        lyCard.setVisibility(View.GONE);
        rbNow.setChecked(true);
        tvDeliveryDate.setText(sdf.format(deliveryDateCalendar.getTime()));
        tvDeliveryTime.setText(stf.format(deliveryDateCalendar.getTime()));

        selectedCardType = (ImageView) lyCardType.getChildAt(0);

        for (int index = 0; index < lyCardType.getChildCount(); index++) {
            lyCardType.getChildAt(index).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageView imageView = (ImageView) view;

                    if (selectedCardType != null) {
                        selectedCardType.setBackground(getResources().getDrawable(R.drawable.bg_card));
                    }
                    selectedCardType = imageView;
                    imageView.setBackground(getResources().getDrawable(R.drawable.bg_card_selected));

                }
            });
        }


        applicationDAL = new ApplicationDAL(this);

        orderMasterId = getIntent().getStringExtra("OrderMasterId");
        orderMaster = applicationDAL.getOrderMaster(orderMasterId);

        int deliveryCompanyPosition = 0;
        deliveryCompanyDetails = applicationDAL.getDeliveryCompanies("", "");
        deliveryCompanyDetails.add(0, new DeliveryCompanyDetail("1", "Home Delivery"));
        settingDetail = applicationDAL.getSettingDetail();

        ArrayAdapter<DeliveryCompanyDetail> deliveryCompanyDetailArrayAdapter = new ArrayAdapter<DeliveryCompanyDetail>(this, android.R.layout.simple_spinner_item, deliveryCompanyDetails);
        deliveryCompanyDetailArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDeliveryCompany.setAdapter(deliveryCompanyDetailArrayAdapter);

        if (orderMaster.getOrderTypeId() == 3) {
            deliveryType = orderMaster.getDeliveryType();
            lyDeliveryDetail.setVisibility(View.VISIBLE);
            if (orderMaster.getDeliveryType() == 1) {
                rbNow.setChecked(true);
                lyDeliveryDate.setVisibility(View.GONE);
            } else if (orderMaster.getDeliveryType() == 2) {
                rbLater.setChecked(true);
                lyDeliveryDate.setVisibility(View.VISIBLE);

                if (orderMaster.getDeliveryDate() != null) {
                    deliveryDateCalendar.setTime(orderMaster.getDeliveryDate());
                    tvDeliveryDate.setText(sdf.format(deliveryDateCalendar.getTime()));
                    tvDeliveryTime.setText(stf.format(deliveryDateCalendar.getTime()));
                }
            }

            for (int i = 0; i < deliveryCompanyDetails.size(); i++) {
                if (deliveryCompanyDetails.get(i).getId().equals(orderMaster.getDeliveryCompany())) {
                    deliveryCompanyPosition = i;
                    break;
                }
            }
        } else {
            lyDeliveryDetail.setVisibility(View.GONE);
        }

        float saleTaxAmount = Float.valueOf(orderMaster.getSalesTaxAmount());
        float discountAmount = Float.valueOf(orderMaster.getDiscountAmount());
        float deliveryFeeAmount = Float.valueOf(orderMaster.getDeliveryFeeAmount());
        changeAmount = Float.valueOf(orderMaster.getChangeAmount());

        tvSubtotal.setText("Subtotal: Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getSubTotalAmount()));


        if (discountAmount > 0) {
            tvDiscount.setText("Discount: Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getDiscountAmount()));
        } else {
            tvDiscount.setText("");
            tvDiscount.setVisibility(View.GONE);
        }
        if (saleTaxAmount > 0) {
            tvSaleTax.setText("Sale tax " + CommonUtils.formatTwoDecimal(orderMaster.getSalesTaxPercent()) + "%: Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getSalesTaxAmount()));
            tvSaleTax.setVisibility(View.VISIBLE);
        } else {
            tvSaleTax.setText("");
            tvSaleTax.setVisibility(View.GONE);
        }
        if (deliveryFeeAmount > 0) {
            tvDeliveryFeeAmount.setText("Delivery Fee: Rs. " + CommonUtils.formatTwoDecimal(deliveryFeeAmount));
            tvDeliveryFeeAmount.setVisibility(View.VISIBLE);
        } else {
            tvDeliveryFeeAmount.setText("");
            tvDeliveryFeeAmount.setVisibility(View.GONE);
        }
        tvNetAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));
        btnPay.setText("Charge Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));
        tvFinishAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));


        if (orderMaster.getCashAmount().length() > 0 && orderMaster.getCashAmount().equals("0") == false) {
            etCashAmount.setText(orderMaster.getCashAmount());
        }
        if (orderMaster.getCardAmount().length() > 0 && orderMaster.getCardAmount().equals("0") == false) {
            noOfCardSplit = orderMaster.getCardNoOfSplit();

            etCardAmount.setText(orderMaster.getCardAmount());
            etCardNumber.setText(orderMaster.getCardNumber());
            selectCardType(orderMaster.getCardType());
            lyCard.setVisibility(View.VISIBLE);
            if (noOfCardSplit > 0) {
                etNoOfSplit.setText(String.valueOf(noOfCardSplit));
                splitCardUI(noOfCardSplit);
            }
        }
        if (changeAmount < 0) {
            changeAmount = 0;
        }
        tvChangeAmount.setText("Change: Rs. " + CommonUtils.formatTwoDecimal(changeAmount));

        if (orderMaster.getTip().length() > 0 && orderMaster.getTip().equals("0") == false) {
            etTipAmount.setText(orderMaster.getTip());
        }
        etCustomerName.setText(orderMaster.getCustomerName());
        etContactNo.setText(orderMaster.getCustomerContactNo());
        etAddress.setText(orderMaster.getCustomerAddress());
        etDeliveryFee.setText(orderMaster.getDeliveryFeeAmount());
        spDeliveryCompany.setSelection(deliveryCompanyPosition);
        etRiderName.setText(orderMaster.getRiderName());
        etRiderMobileNo.setText(orderMaster.getRiderMobileNo());
        etRiderBikeNo.setText(orderMaster.getRiderBikeNo());

        etDeliveryFee.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus == false) {
                    orderMaster.setDeliveryFeeAmount(CommonUtils.formatTwoDecimal(etDeliveryFee.getText().toString()));
                    orderMaster.calculateValues();

                    float deliveryFeeAmount = Float.valueOf(orderMaster.getDeliveryFeeAmount());

                    if (deliveryFeeAmount > 0) {
                        tvDeliveryFeeAmount.setText("Delivery Fee: Rs. " + CommonUtils.formatTwoDecimal(deliveryFeeAmount));
                        tvDeliveryFeeAmount.setVisibility(View.VISIBLE);
                    } else {
                        tvDeliveryFeeAmount.setText("");
                        tvDeliveryFeeAmount.setVisibility(View.GONE);
                    }

                    tvNetAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));
                    btnPay.setText("Charge Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));
                    tvFinishAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));

                    calculateChange();
                }
            }
        });

        etTipAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus == false) {
                    orderMaster.setTip(CommonUtils.formatTwoDecimal(etTipAmount.getText().toString()));
                    orderMaster.calculateValues();

                    tvNetAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));
                    btnPay.setText("Charge Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));
                    tvFinishAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));

                    calculateChange();
                }
            }
        });

        etCashAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                if (etCashAmount.hasFocus()) {
                    calculateChange();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etCardAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                if (etCardAmount.hasFocus()) {
                    calculateChange();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && editable.toString().equals("0") == false) {
                    lyCard.setVisibility(View.VISIBLE);
                } else {
                    lyCard.setVisibility(View.GONE);
                    etCardNumber.setText("");
                }
            }
        });

        etContactNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus == false) {
                    DataRow customerRow = applicationDAL.getCustomerByContactNo(etContactNo.getText().toString());

                    if (customerRow != null) {
                        etCustomerName.setText(customerRow.getString(DBHelper.COL_ORDER_MASTER_CUSTOMER_NAME));
                        etAddress.setText(customerRow.getString(DBHelper.COL_ORDER_MASTER_CUSTOMER_ADDRESS));
                    }
                }
            }
        });

        rbNow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rbNow.isChecked()) {
                    deliveryType = 1;
                    lyDeliveryDate.setVisibility(View.GONE);
                }
            }
        });

        rbLater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rbLater.isChecked()) {
                    deliveryType = 2;
                    lyDeliveryDate.setVisibility(View.VISIBLE);
                }
            }
        });

        btnSplitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardSplitDialog.show();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode == 1) {
                    if (validateFields()) {
                        update();

                        ryCartFinish.setVisibility(View.VISIBLE);
                        lyCheckout.setVisibility(View.GONE);
                        btnReceipt.setVisibility(View.VISIBLE);
                        btnPay.setText("Start a new sale");
                        mode = 2;
                        if (settingDetail.isAutoPrintAfterCheckout()) {
                            btnReceipt.performClick();
                        }

                    }
                } else if (mode == 2) {
                    Intent intent = new Intent();
                    intent.putExtra("OrderMasterId", "");
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        btnReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckoutActivity.this, PrintViewActivity.class);
                intent.putExtra("PrintType", "PostBill");
                intent.putExtra("OrderMasterId", orderMaster.getOrderMasterId());
                intent.putExtra("CheckoutMode", 1);

                startActivityForResult(intent, ActivityRequest.REQUEST_PRINT_POST_BILL);

            }
        });

        if (Configuration.getPlaceDetail().getCardGSTPercentage().equals("0") == false && Configuration.getPlaceDetail().getGSTPercentage() != Configuration.getPlaceDetail().getCardGSTPercentage()) {
            if (orderMaster.getPaymentTypeId() > 1) {
                etCashAmount.setEnabled(false);
            } else {
                etCardAmount.setEnabled(false);
                btnSplitCard.setEnabled(false);
            }
        }
    }

    private void selectCardType(String cardType) {
        for (int index = 0; index < lyCardType.getChildCount(); index++) {
            if (lyCardType.getChildAt(index).getTag().equals(cardType)) {
                lyCardType.getChildAt(index).performClick();
            }
        }
    }

    public void btnChangeDeliveryDate_onClick(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        deliveryDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        deliveryDateCalendar.set(Calendar.MONTH, month);
                        deliveryDateCalendar.set(Calendar.YEAR, year);

                        tvDeliveryDate.setText(sdf.format(deliveryDateCalendar.getTime()));
                    }
                }, deliveryDateCalendar.get(Calendar.YEAR), deliveryDateCalendar.get(Calendar.MONTH), deliveryDateCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    public void btnChangeDeliveryTime_onClick(View view) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        deliveryDateCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        deliveryDateCalendar.set(Calendar.MINUTE, minute);

                        tvDeliveryTime.setText(stf.format(deliveryDateCalendar.getTime()));
                    }
                }, deliveryDateCalendar.get(Calendar.HOUR_OF_DAY), deliveryDateCalendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();

    }

    @Override
    public void onBackPressed() {
        if (mode == 2) {
            Intent intent = new Intent();
            intent.putExtra("OrderMasterId", "");
            setResult(RESULT_OK, intent);
        } else {
            updateCustomerDetails();
            Intent intent = new Intent();
            intent.putExtra("OrderMasterId", orderMasterId);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    private void updateCustomerDetails() {

        orderMaster.setCashAmount(CommonUtils.parseTwoDecimal(etCashAmount.getText().toString()));
        orderMaster.setCardAmount(CommonUtils.parseTwoDecimal(etCardAmount.getText().toString()));
        orderMaster.setChangeAmount(CommonUtils.parseTwoDecimal(changeAmount));
        orderMaster.setCardNoOfSplit(noOfCardSplit);
        orderMaster.setCardNumber(etCardNumber.getText().toString());
        orderMaster.setCardType(selectedCardType.getTag().toString());
        orderMaster.setCustomerName(etCustomerName.getText().toString());
        orderMaster.setCustomerContactNo(etContactNo.getText().toString());
        if (orderMaster.getOrderTypeId() == 3) {
            orderMaster.setDeliveryType(deliveryType);
            if (deliveryType == 2) {
                orderMaster.setDeliveryDate(deliveryDateCalendar.getTime());
            }
            orderMaster.setDeliveryCompany(deliveryCompanyDetails.get(spDeliveryCompany.getSelectedItemPosition()).getId());
        }
        orderMaster.setCustomerAddress(etAddress.getText().toString());
        orderMaster.setDeliveryFeeAmount(CommonUtils.parseTwoDecimal(etDeliveryFee.getText().toString()));
        orderMaster.setTip(CommonUtils.parseTwoDecimal(etTipAmount.getText().toString()));
        orderMaster.setRiderName(etRiderName.getText().toString());
        orderMaster.setRiderMobileNo(etRiderMobileNo.getText().toString());
        orderMaster.setRiderBikeNo(etRiderBikeNo.getText().toString());

        if (orderMaster.getCardAmount().length() > 0 && orderMaster.getCardAmount().equals("0")) {
            orderMaster.setCardNumber("");
            orderMaster.setCardType("");
        }

        orderMaster.calculateValues();
        applicationDAL.addUpdateOrderMaster(orderMaster);

    }

    private void update() {

        orderMaster.setCashAmount(CommonUtils.parseTwoDecimal(etCashAmount.getText().toString()));
        orderMaster.setCardAmount(CommonUtils.parseTwoDecimal(etCardAmount.getText().toString()));
        orderMaster.setChangeAmount(CommonUtils.parseTwoDecimal(changeAmount));
        orderMaster.setCardNoOfSplit(noOfCardSplit);
        orderMaster.setCardNumber(etCardNumber.getText().toString());
        orderMaster.setCardType(selectedCardType.getTag().toString());
        orderMaster.setCustomerName(etCustomerName.getText().toString());
        orderMaster.setCustomerContactNo(etContactNo.getText().toString());
        if (orderMaster.getOrderTypeId() == 3) {
            orderMaster.setDeliveryType(deliveryType);
            if (deliveryType == 2) {
                orderMaster.setDeliveryDate(deliveryDateCalendar.getTime());
            }
            orderMaster.setDeliveryCompany(deliveryCompanyDetails.get(spDeliveryCompany.getSelectedItemPosition()).getId());
        }
        orderMaster.setCustomerAddress(etAddress.getText().toString());
        orderMaster.setDeliveryFeeAmount(CommonUtils.parseTwoDecimal(etDeliveryFee.getText().toString()));
        orderMaster.setTip(CommonUtils.parseTwoDecimal(etTipAmount.getText().toString()));
        orderMaster.setRiderName(etRiderName.getText().toString());
        orderMaster.setRiderMobileNo(etRiderMobileNo.getText().toString());
        orderMaster.setRiderBikeNo(etRiderBikeNo.getText().toString());

        if (orderMaster.getCardAmount().length() > 0 && orderMaster.getCardAmount().equals("0")) {
            orderMaster.setCardNumber("");
            orderMaster.setCardType("");
        }
        for (int i = 0; i < lySplitContainer.getChildCount(); i++) {

            Spinner spCardType = lySplitContainer.getChildAt(i).findViewById(R.id.spCardType);
            TextView etCardNumber = lySplitContainer.getChildAt(i).findViewById(R.id.etCardNumber);

            OrderCardDetail orderCardDetail = new OrderCardDetail();
            orderCardDetail.setOrderMasterId(orderMasterId);
            orderCardDetail.setGuestSplitNo(i + 1);
            orderCardDetail.setCardAmount(CommonUtils.parseTwoDecimal(splitAmount));
            orderCardDetail.setCardNumber(etCardNumber.getText().toString());
            orderCardDetail.setCardType(spCardType.getSelectedItem().toString());

            applicationDAL.addUpdateOrderCardDetail(orderCardDetail);
        }

        orderMaster.setCheckoutDeviceDatetime(new Date());
        orderMaster.setStatusId(2);

        orderMaster.calculateValues();
        applicationDAL.addUpdateOrderMaster(orderMaster);

    }

    private void calculateChange() {

        float totalAmount = Float.valueOf(orderMaster.getTotalAmount());
        float cashAmount = Float.valueOf("0" + etCashAmount.getText().toString());
        float cardAmount = Float.valueOf("0" + etCardAmount.getText().toString());
        changeAmount = (cashAmount + cardAmount) - (totalAmount);

        if (changeAmount < 0) {
            changeAmount = 0;
        }
        tvChangeAmount.setText("Change: Rs. " + CommonUtils.formatTwoDecimal(changeAmount));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_search_customer) {
            customerDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createCustomerDialog() {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customerDialogLayout = inflater.inflate(R.layout.customer_dialog, null);

        svCustomer = customerDialogLayout.findViewById(R.id.svCustomer);
        listCustomer = customerDialogLayout.findViewById(R.id.listCustomer);

        svCustomer.setActivated(true);
        svCustomer.setQueryHint("Search customer...");
        svCustomer.onActionViewExpanded();
        svCustomer.setIconified(false);

        View closeButton = svCustomer.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svCustomer.setQuery("", false);
                svCustomer.clearFocus();

                loadCustomer("");
            }
        });


        svCustomer.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                loadCustomer(newText.trim().toLowerCase());
                return false;
            }
        });

        listCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer selectedCustomer = customerList.get(position);

                if (orderMaster != null) {
                    orderMaster.setCustomerId(selectedCustomer.getId());
                    orderMaster.setCustomerName(selectedCustomer.getFullName());
                    orderMaster.setCustomerContactNo(selectedCustomer.getContactNo());
                    orderMaster.setCustomerAddress(selectedCustomer.getAddress());

                    applicationDAL.addUpdateOrderMaster(orderMaster);

                    etCustomerName.setText(orderMaster.getCustomerName());
                    etContactNo.setText(orderMaster.getCustomerContactNo());
                    etAddress.setText(orderMaster.getCustomerAddress());
                }

                customerDialog.dismiss();
            }
        });

        customerDialog = new AlertDialog.Builder(this).create();
        customerDialog.setView(customerDialogLayout);

        customerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                svCustomer.setQuery("", false);
                svCustomer.requestFocus();
                loadCustomer("");
            }
        });

    }

    private void createCardSplitDialog() {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cardSplitDialogLayout = inflater.inflate(R.layout.card_split_dialog, null);

        etNoOfSplit = cardSplitDialogLayout.findViewById(R.id.etNoOfSplit);
        lySplitContainer = cardSplitDialogLayout.findViewById(R.id.lySplitContainer);
        Button btnSubmit = cardSplitDialogLayout.findViewById(R.id.btnSubmit);

        etNoOfSplit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                int noOfCardSplit = Integer.valueOf("0" + etNoOfSplit.getText().toString());
                splitCardUI(noOfCardSplit);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int noOfCardSplit = Integer.valueOf("0" + etNoOfSplit.getText().toString());
                int splitRowDiff = lySplitContainer.getChildCount() - noOfCardSplit;

                for (int i = 0; i < splitRowDiff; i++) {
                    lySplitContainer.removeViewAt(lySplitContainer.getChildCount() - 1);
                }

                CheckoutActivity.this.noOfCardSplit = noOfCardSplit;

                splitCardUI(noOfCardSplit);
                cardSplitDialog.dismiss();

            }
        });

        cardSplitDialog = new AlertDialog.Builder(this).create();
        cardSplitDialog.setView(cardSplitDialogLayout);
        cardSplitDialog.setCancelable(false);
    }


    private void splitCardUI(int noOfCardSplit) {
        float totalAmount = Float.valueOf(orderMaster.getTotalAmount());
        float cashAmount = Float.valueOf("0" + etCashAmount.getText().toString());
        float cardAmount = totalAmount - cashAmount;
        splitAmount = 0;

        if (cardAmount > 0 && noOfCardSplit > 0) {
            splitAmount = (totalAmount - cashAmount) / noOfCardSplit;

            for (int i = lySplitContainer.getChildCount(); i < noOfCardSplit; i++) {
                LayoutInflater inflater = (LayoutInflater) CheckoutActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View cardSplitRow = inflater.inflate(R.layout.card_split_row, null);

                lySplitContainer.addView(cardSplitRow);
            }
            for (int i = 0; i < lySplitContainer.getChildCount(); i++) {
                TextView tvSplitAmount = lySplitContainer.getChildAt(i).findViewById(R.id.tvSplitAmount);
                if (i < noOfCardSplit) {
                    tvSplitAmount.setText("Rs." + CommonUtils.formatTwoDecimal(splitAmount));
                } else {
                    tvSplitAmount.setText("Rs.0");
                }
            }
        }

        if (noOfCardSplit > 0) {
            etCardAmount.setText(CommonUtils.parseTwoDecimal(cardAmount));
            etCardAmount.setEnabled(false);
            selectCardType("Split");
            lyCard.setVisibility(View.GONE);
        } else {
            etCardAmount.setText("");
            etCardAmount.setEnabled(true);
            selectCardType("Visa");
        }
    }

    private void loadCustomer(String criteria) {
        customerList = applicationDAL.getCustomers(DBHelper.COL_CUSTOMER_FULL_NAME + " like '%" + criteria + "%' or " + DBHelper.COL_CUSTOMER_CONTACT_NO + " like '%" + criteria + "%'", "");

        CustomerAdapter customerAdapter = new CustomerAdapter(this, customerList);
        listCustomer.setAdapter(customerAdapter);
    }

    private void initViews() {
        tvNetAmount = findViewById(R.id.tvNetAmount);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvSaleTax = findViewById(R.id.tvSaleTax);
        tvDeliveryFeeAmount = findViewById(R.id.tvDeliveryFeeAmount);
        tvChangeAmount = findViewById(R.id.tvChangeAmount);
        etTipAmount = findViewById(R.id.etTipAmount);
        etCashAmount = findViewById(R.id.etCashAmount);
        etCardAmount = findViewById(R.id.etCardAmount);
        lyCard = findViewById(R.id.lyCard);
        lyCardType = findViewById(R.id.lyCardType);
        etCardNumber = findViewById(R.id.etCardNumber);
        etContactNo = findViewById(R.id.etContactNo);
        etCustomerName = findViewById(R.id.etCustomerName);
        etAddress = findViewById(R.id.etAddress);
        lyDeliveryDetail = findViewById(R.id.lyDeliveryDetail);
        rbNow = findViewById(R.id.rbNow);
        rbLater = findViewById(R.id.rbLater);
        lyDeliveryDate = findViewById(R.id.lyDeliveryDate);
        tvDeliveryDate = findViewById(R.id.tvDeliveryDate);
        tvDeliveryTime = findViewById(R.id.tvDeliveryTime);
        etDeliveryFee = findViewById(R.id.etDeliveryFee);
        spDeliveryCompany = findViewById(R.id.spDeliveryCompany);
        etRiderName = findViewById(R.id.etRiderName);
        etRiderMobileNo = findViewById(R.id.etRiderMobileNo);
        etRiderBikeNo = findViewById(R.id.etRiderBikeNo);
        btnSplitCard = findViewById(R.id.btnSplitCard);
        btnPay = findViewById(R.id.btnPay);
        ryCartFinish = findViewById(R.id.ryCartFinish);
        btnReceipt = findViewById(R.id.btnReceipt);
        lyCheckout = findViewById(R.id.lyCheckout);
        tvFinishAmount = findViewById(R.id.tvFinishAmount);
    }

    private boolean validateFields() {
        orderMaster.setTip(CommonUtils.formatTwoDecimal(etTipAmount.getText().toString()));
        orderMaster.setDeliveryFeeAmount(CommonUtils.formatTwoDecimal(etDeliveryFee.getText().toString()));
        orderMaster.calculateValues();

        tvNetAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));
        btnPay.setText("Charge Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));
        tvFinishAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));

        calculateChange();

        float totalAmount = Float.valueOf(orderMaster.getTotalAmount());
        float cashAmount = Float.valueOf("0" + etCashAmount.getText().toString());
        float cardAmount = Float.valueOf("0" + etCardAmount.getText().toString());
        float changeAmount = (cashAmount + cardAmount) - totalAmount;

        if (changeAmount < 0) {
            UIHelper.showShortToast(this, "Invalid Amount");
            return false;
        }

        if (cardAmount > 0 && cardAmount > totalAmount) {
            UIHelper.showShortToast(this, "Card amount must be equal to total bill amount.");
            return false;
        }


        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.checkout_main, menu);
        return true;
    }


}
