package com.revaki.revakipos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.SearchView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dk.animation.circle.CircleAnimationUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.revaki.revakipos.adapter.CustomerAdapter;
import com.revaki.revakipos.adapter.DishGridAdapter;
import com.revaki.revakipos.adapter.DishListAdapter;
import com.revaki.revakipos.adapter.TableDetailAdapter;
import com.revaki.revakipos.beans.CategoryDetail;
import com.revaki.revakipos.beans.Customer;
import com.revaki.revakipos.beans.DeliveryCompanyDetail;
import com.revaki.revakipos.beans.DishDetail;
import com.revaki.revakipos.beans.DishVariant;
import com.revaki.revakipos.beans.FloorDetail;
import com.revaki.revakipos.beans.OrderChild;
import com.revaki.revakipos.beans.OrderChildVariant;
import com.revaki.revakipos.beans.OrderMaster;
import com.revaki.revakipos.beans.PrinterDetail;
import com.revaki.revakipos.beans.SettingDetail;
import com.revaki.revakipos.beans.ShiftRecord;
import com.revaki.revakipos.beans.SyncDetail;
import com.revaki.revakipos.beans.TableDetail;
import com.revaki.revakipos.beans.User;
import com.revaki.revakipos.beans.UserDetail;
import com.revaki.revakipos.connectivity.ServiceManager;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.db.DBHelper;
import com.revaki.revakipos.helper.ActivityRequest;
import com.revaki.revakipos.helper.CommonUtils;
import com.revaki.revakipos.helper.SpinnerItem;
import com.revaki.revakipos.helper.UIHelper;
import com.revaki.revakipos.job.SyncMasterData;
import com.revaki.revakipos.job.UploadDataAlarmBroadcastReceiver;
import com.revaki.revakipos.job.UploadDataService;
import com.revaki.revakipos.utils.LocalDataManager;
import com.revaki.revakipos.utils.Logger;
import com.revaki.revakipos.utils.SessionManager;
import com.revaki.revakipos.widget.CircleImageView;
import com.revaki.revakipos.widget.IconButton;
import com.revaki.revakipos.widget.IconTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ServiceManager serviceManager = new ServiceManager();
    private ApplicationDAL applicationDAL;

    private OrderMaster orderMaster = null;

    private CircleImageView ivUserPhoto;
    private TextView tvUserFullName;
    private TextView tvUserType;
    private TextView tvUserEmail;
    private TextView tvUserContactNo;

    private LinearLayout lyTableDetail;

    private TabLayout tabFloorList;
    private TabLayout tabCategoryList;

    private GridView gridTables;
    private GridView gridDishes;
    private LinearLayout lyDishList;
    private ListView listDish;
    private Button btnProceed;
    private Button btnOptions;
    private TextView tvDropDish;

    private IconButton btnShowTable;
    private Spinner spTable;
    private Spinner spReceipt;
    private SearchView svDishes;
    private IconTextView btnDishViewMode;
    private CartFragment cartFragment;
    private AlertDialog customerDialog;
    private AlertDialog selectCustomerDialog;


    private SearchView svCustomer;
    private ListView listCustomer;

    private EditText etCustomerName;
    private EditText etContactNo;
    private EditText etAddress;
    private LinearLayout lyDeliveryDetail;
    private RadioButton rbNow;
    private RadioButton rbLater;
    private LinearLayout lyDeliveryDate;
    private TextView tvDeliveryDate;
    private TextView tvDeliveryTime;
    private Button btnChangeDate;
    private Button btnChangeTime;
    private EditText etDeliveryFee;
    private Spinner spDeliveryCompany;
    private EditText etRiderName;
    private EditText etRiderMobileNo;
    private EditText etRiderBikeNo;
    private Button btnCustomerSubmit;

    private Calendar deliveryDateCalendar;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    private SimpleDateFormat stf = new SimpleDateFormat("hh:mm aa");
    int deliveryType = 1;

    private int dishViewMode = 1;
    private FloorDetail selectedFloor = null;
    private String selectedTableId = "";
    private String selectedTableName = "";
    private String selectedCategoryId = "";
    private int numOfPerson = 0;

    private List<Customer> customerList = null;
    private List<FloorDetail> floorDetailList = null;
    private List<TableDetail> tableDetailList = null;
    private List<DishDetail> dishDetailList = null;
    private List<SpinnerItem> receiptList = null;
    private List<DeliveryCompanyDetail> deliveryCompanyDetails;
    private Customer selectedCustomer = null;
    private boolean manualCustomer = false;

    private long lastTimeBackPress = 0;
    private int timePeriodToExit = 2000;
    private Toast exitToast;

    private long syncStartTimeMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ivUserPhoto = navigationView.getHeaderView(0).findViewById(R.id.ivUserPhoto);
        tvUserFullName = navigationView.getHeaderView(0).findViewById(R.id.tvUserFullName);
        tvUserType = navigationView.getHeaderView(0).findViewById(R.id.tvUserType);
        tvUserEmail = navigationView.getHeaderView(0).findViewById(R.id.tvUserEmail);
        tvUserContactNo = navigationView.getHeaderView(0).findViewById(R.id.tvUserContactNo);

        lyTableDetail = findViewById(R.id.lyTableDetail);

        tabFloorList = findViewById(R.id.tabFloorList);
        tabCategoryList = findViewById(R.id.tabCategoryList);
        gridTables = findViewById(R.id.gridTables);


        btnShowTable = findViewById(R.id.btnShowTable);
        spTable = findViewById(R.id.spTable);
        spReceipt = findViewById(R.id.spReceipt);
        svDishes = findViewById(R.id.svDishes);
        btnDishViewMode = findViewById(R.id.btnDishViewMode);


        gridDishes = findViewById(R.id.gridDishes);
        listDish = findViewById(R.id.listDish);
        lyDishList = findViewById(R.id.lyDishList);
        btnProceed = findViewById(R.id.btnProceed);
        btnOptions = findViewById(R.id.btnOptions);
        tvDropDish = findViewById(R.id.tvDropDish);

        if (getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            cartFragment = (CartFragment) getSupportFragmentManager().findFragmentById(R.id.fm_ItemCart);
        }

        lyDishList.setVisibility(View.GONE);

        applicationDAL = new ApplicationDAL(this);

        tabFloorList.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                btnShowTable.performClick();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        tabCategoryList.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedCategoryId = tab.getTag().toString();
                loadDishes(selectedCategoryId, "");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        gridTables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spTable.setSelection(position, true);

                if (selectedFloor.isShowTable()) {
                    showTablePersonDialog();
                } else {
                    selectTable(0);
                }
            }
        });

        btnShowTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyTableDetail.setVisibility(View.GONE);
                gridTables.setVisibility(View.VISIBLE);

                loadTables(tabFloorList.getSelectedTabPosition());
            }
        });

        spTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTableId = tableDetailList.get(position).getTableId();
                selectedTableName = tableDetailList.get(position).getTableName();
                loadReceipts(selectedTableId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spReceipt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                findReceipt(receiptList.get(position).getValue(), true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        svDishes.setActivated(true);
        svDishes.setQueryHint("Search here");
        svDishes.onActionViewExpanded();
        svDishes.setIconified(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                svDishes.clearFocus();
            }
        }, 200);


        View closeButton = svDishes.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svDishes.setQuery("", false);
                svDishes.clearFocus();

                loadDishes(selectedCategoryId, "");
            }
        });


        svDishes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                loadDishes(selectedCategoryId, newText.trim().toLowerCase());

                return false;
            }
        });

        btnDishViewMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dishViewMode == 1) {
                    btnDishViewMode.setText(R.string.fa_th_list_solid);
                    gridDishes.setVisibility(View.GONE);
                    lyDishList.setVisibility(View.VISIBLE);
                    dishViewMode = 2;
                } else {
                    btnDishViewMode.setText(R.string.fa_th_large_solid);
                    dishViewMode = 1;
                    lyDishList.setVisibility(View.GONE);
                    gridDishes.setVisibility(View.VISIBLE);
                }

            }
        });

        if (btnProceed != null) {
            btnProceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (orderMaster != null) {
                        Intent intent = new Intent(MainActivity.this, CartActivity.class);
                        intent.putExtra("OrderMasterId", orderMaster.getOrderMasterId());
                        startActivityForResult(intent, ActivityRequest.REQUEST_CART);
                    } else {
                        UIHelper.showErrorDialog(MainActivity.this, "", "Cart is empty");
                    }
                }
            });
        }

        if (btnOptions != null) {
            btnOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (orderMaster != null) {
                        showBottomSheetDialogFragment();
                    } else {
                        UIHelper.showErrorDialog(MainActivity.this, "", "Cart is empty");
                    }
                }
            });
        }

        gridDishes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView ivDishImage = view.findViewById(R.id.ivDishImage);
                selectItem(ivDishImage, position);
            }
        });

        listDish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView ivDishImage = view.findViewById(R.id.ivDishImage);
                selectItem(ivDishImage, position);

            }
        });

        if (cartFragment != null) {
            cartFragment.setFragmentActionListener(new CartFragment.OnFragmentActionListener() {
                @Override
                public void onFragmentAction(String actionKey, String orderMasterId) {
                    if (actionKey.equals("RemoveOrder")) {
                        reloadCartItem();
                    } else if (actionKey.equals("Change")) {
                        findReceipt(orderMasterId, false);
                    }
                }
            });
        }

        setUserDetails();

        loadDashBoard();

        syncMasterData();

        startDataSyncService();

        syncStartTimeMillis = System.currentTimeMillis() + 10000;


    }


    private void selectItem(ImageView ivDishImage, int position) {
        List<DishVariant> dishVariants = dishDetailList.get(position).getVariants();
        if (dishVariants.size() > 0) {

            CartCustomizeFragment cartCustomizeFragment = new CartCustomizeFragment();
            cartCustomizeFragment.setFragmentActionListener(new CartCustomizeFragment.OnFragmentActionListener() {
                @Override
                public void onFragmentAction(String actionKey, Intent data) {
                    List<OrderChildVariant> variants = (List<OrderChildVariant>) data.getSerializableExtra("Variants");
                    String specialInstruction = data.getStringExtra("SpecialInstruction");
                    int quantity = data.getIntExtra("Quantity", 1);
                    int position = data.getIntExtra("Position", 0);

                    addToCart(null, variants, specialInstruction, quantity, position);
                }
            });

            Bundle bundle = new Bundle();
            bundle.putInt("Position", position);
            bundle.putString("DishName", dishDetailList.get(position).getDishName());
            bundle.putDouble("DishPrice", dishDetailList.get(position).getTotalPrice());
            bundle.putDouble("PriceStartFrom", dishDetailList.get(position).getPriceStartFrom());
            bundle.putSerializable("DishVariants", (Serializable) dishVariants);


            cartCustomizeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_up, R.anim.slide_out_up)
                    .add(android.R.id.content, cartCustomizeFragment)
                    .addToBackStack(this.getClass().getName())
                    .commit();
        } else {
            addToCart(ivDishImage, null, "", 1, position);
        }
    }

    private void startDataSyncService() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, Configuration.getSyncInterval());
        long trigger = calendar.getTimeInMillis();
        long interval = (Configuration.getSyncInterval() * 60000);

        UploadDataAlarmBroadcastReceiver.cancelAlarm(this);
        UploadDataAlarmBroadcastReceiver.setAlarm(this, trigger, interval);

        LocalBroadcastManager.getInstance(this).registerReceiver(uploadDataReceiver, new IntentFilter(UploadDataService.ACTION));
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(uploadDataReceiver);
        unregisterReceiver(networkStateReceiver);
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
                float balanceQuantity = 0;

                for (OrderChild orderChild : orderMaster.getOrderChilds()) {
                    balanceQuantity += (Float.valueOf(orderChild.getQuantity()) - Float.valueOf(orderChild.getPrintQuantity()));
                }

                if (balanceQuantity != 0) {
                    Intent intent = new Intent(MainActivity.this, PrintViewActivity.class);
                    intent.putExtra("PrintType", "KitchenPrint");
                    intent.putExtra("OrderMasterId", orderMaster.getOrderMasterId());
                    startActivityForResult(intent, ActivityRequest.REQUEST_PRINT_KITCHEN);
                } else {
                    UIHelper.showAlertDialog(MainActivity.this, "", "KOT already printed, no new items found for print.");
                }
            }

            @Override
            public void onKitchenReprint(View view) {
                showPinDialog(new OnPinSubmitListener() {
                    @Override
                    public void onSubmit(boolean isValid) {
                        if (isValid) {
                            Intent intent = new Intent(MainActivity.this, PrintViewActivity.class);
                            intent.putExtra("PrintType", "KitchenReprint");
                            intent.putExtra("OrderMasterId", orderMaster.getOrderMasterId());
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
                            UIHelper.showConfirmDialog(MainActivity.this, "Empty Cart", "Do you want to empty the shopping cart?", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    orderMaster.setStatusId(3);
                                    applicationDAL.addUpdateOrderMaster(orderMaster);
                                    loadReceipts(selectedTableId);
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
        cartOptionsBottomSheetFragment.show(getSupportFragmentManager(), cartOptionsBottomSheetFragment.getTag());
    }

    public void PreBillPrint() {
        if (orderMaster != null) {
            final Runnable preBill = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, PrintViewActivity.class);
                    intent.putExtra("PrintType", "PreBill");
                    intent.putExtra("OrderMasterId", orderMaster.getOrderMasterId());
                    startActivityForResult(intent, ActivityRequest.REQUEST_PRINT_PRE_BILL);
                }
            };

            if (Configuration.getPlaceDetail().getCardGSTPercentage().equals("0") == false && Configuration.getPlaceDetail().getGSTPercentage() != Configuration.getPlaceDetail().getCardGSTPercentage()) {
                UIHelper.showConfirmDialog(this, "", "Proceed order with cash or card?", "Card", "Cash", new DialogInterface.OnClickListener() {
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
            UIHelper.showErrorDialog(this, "", "Cart is empty");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequest.REQUEST_CART && resultCode == RESULT_OK) {
            String orderMasterId = data.getExtras().getString("OrderMasterId");
            if (orderMasterId.equals("")) {
                reloadCartItem();
            } else {
                findReceipt(orderMasterId, false);
            }
        } else if (requestCode == ActivityRequest.REQUEST_CHECKOUT && resultCode == RESULT_OK) {
            String orderMasterId = data.getExtras().getString("OrderMasterId");
            if (orderMasterId.equals("")) {
                reloadCartItem();
            } else {
                findReceipt(orderMasterId, true);
            }
        } else if (requestCode == ActivityRequest.REQUEST_TRANSACTION_LIST && resultCode == RESULT_OK) {
            String orderMasterId = data.getExtras().getString("OrderMasterId");
            String tableId = data.getExtras().getString("TableId");

            selectOrder(orderMasterId, tableId);

        } else if (requestCode == ActivityRequest.REQUEST_SYNC_DATA && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        } else if (requestCode == ActivityRequest.REQUEST_PRINT_KITCHEN) {
            if (orderMaster != null) {
                findReceipt(orderMaster.getOrderMasterId(), false);
            }
        }
    }


    interface OnPinSubmitListener {
        void onSubmit(boolean isValid);
    }

    private void showPinDialog(final OnPinSubmitListener onPinSubmitListener) {


        final EditText input = new EditText(this);
        input.setPadding(30, 30, 30, 30);
        input.setSingleLine(true);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null).create();

        alertDialog.setTitle("Enter your PIN:");
        alertDialog.setView(input);

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (input.getText().length() > 0 && Integer.valueOf(input.getText().toString()) == Configuration.getUser().getModificationKey()) {
                            alertDialog.dismiss();
                            onPinSubmitListener.onSubmit(true);
                        } else {
                            UIHelper.showShortToast(MainActivity.this, "Invalid PIN.");
                            onPinSubmitListener.onSubmit(false);
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void selectOrder(final String orderMasterId, String tableId) {
        TableDetail tableDetail = applicationDAL.getTableDetail(tableId);
        if (tableDetail != null) {
            int floorIndex = 0;
            int tableIndex = 0;

            for (FloorDetail floorDetail : floorDetailList) {
                if (floorDetail.getFloorId().equals(tableDetail.getFloorId())) {
                    break;
                }
                floorIndex++;
            }

            tabFloorList.getTabAt(floorIndex).select();

            for (TableDetail tableDetail1 : tableDetailList) {
                if (tableDetail1.getTableId().equals(tableDetail.getTableId())) {
                    break;
                }
                tableIndex++;
            }

            spTable.setSelection(tableIndex, true);
            selectTable(0);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (receiptList != null) {
                        int receiptIndex = 0;
                        for (SpinnerItem spinnerItem : receiptList) {
                            if (spinnerItem.getValue().equals(orderMasterId)) {
                                break;
                            }
                            receiptIndex++;
                        }

                        spReceipt.setSelection(receiptIndex, true);
                    }
                }
            }, 200);
        }
    }

    private void reloadCartItem() {
        loadReceipts(selectedTableId);
        if (cartFragment != null) {
            cartFragment.loadOrderMaster(null);
        }
    }


    HashMap<String, Integer> hashMapOrderItem = new HashMap<String, Integer>();

    private void loadReceipts(String tableId) {
        receiptList = applicationDAL.getReceiptList(DBHelper.COL_ORDER_MASTER_TABLE_ID + " = '" + tableId + "' and " + DBHelper.COL_ORDER_MASTER_STATUS_ID + " = 1", "");

        if (selectedFloor.getOrderTypeId() != 1 || receiptList.size() == 0 || Configuration.getPlaceDetail().isAllowTableMultipleReceipts()) {
            receiptList.add(0, new SpinnerItem("", "New Rcpt"));
        }

        ArrayAdapter<SpinnerItem> receiptItemArrayAdapter = new ArrayAdapter<SpinnerItem>(this, android.R.layout.simple_spinner_item, receiptList);
        receiptItemArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spReceipt.setAdapter(receiptItemArrayAdapter);
    }

    private void findReceipt(String orderMasterId, boolean updateFragment) {


        hashMapOrderItem.clear();
        float numOfItems = 0;
        float totalAmount = 0;
        orderMaster = null;
        selectedCustomer = null;
        manualCustomer = false;

        etCustomerName.setText("");
        etContactNo.setText("");
        etAddress.setText("");
        etDeliveryFee.setText("");
        etRiderName.setText("");
        etRiderMobileNo.setText("");
        etRiderBikeNo.setText("");

        if (orderMasterId != null && orderMasterId.equals("") == false) {
            orderMaster = applicationDAL.getOrderMaster(orderMasterId);

            if (orderMaster != null) {
                int index = 0;

                for (OrderChild orderChild : orderMaster.getOrderChilds()) {
                    hashMapOrderItem.put(orderChild.getDishId(), index);

                    numOfItems += Float.valueOf(orderChild.getQuantity());
                    index++;
                }

                numOfPerson = orderMaster.getNoOfPerson();
                totalAmount = Float.valueOf(orderMaster.getSubTotalAmount());

                if (updateFragment && cartFragment != null) {
                    cartFragment.loadOrderMaster(orderMaster.getOrderMasterId());
                }
            }
        } else {
            if (updateFragment && cartFragment != null) {
                cartFragment.loadOrderMaster(null);
            }
        }
        if (btnProceed != null) {
            btnProceed.setText(CommonUtils.formatTwoDecimal(numOfItems) + " items = Rs. " + CommonUtils.formatTwoDecimal(totalAmount));
        }

    }

    private void addToCart(View view, List<OrderChildVariant> variants, String specialInstruction, int cartQuantity, int position) {

        if (Configuration.getShiftRecordId().equals("") == false) {
            if (selectedFloor.getOrderTypeId() == 1 && Configuration.getPlaceDetail().isAllowTableMultipleReceipts() == false && orderMaster == null && receiptList.size() > 1) {
                UIHelper.showShortToast(this, "Multiple receipts not allowed.");
                return;
            }
            if (view != null) {
                new CircleAnimationUtil().attachActivity(this).
                        setTargetView(view).
                        setDestView(tvDropDish).
                        setCircleDuration(1).
                        setMoveDuration(280).
                        startAnimation();

                view.setVisibility(View.VISIBLE);
            }
            long rowId = 0;

            if (orderMaster == null) {
                int receiptNo = applicationDAL.getMaxReceiptNo() + 1;

                orderMaster = new OrderMaster();
                orderMaster.setOrderDeviceDate(CommonUtils.getDate());
                orderMaster.setCreationDeviceDatetime(new Date());
                orderMaster.setDeviceReceiptNo(receiptNo);
                orderMaster.setOrderTypeId(selectedFloor.getOrderTypeId());
                orderMaster.setTableId(selectedTableId);
                orderMaster.setTableName(selectedTableName);
                orderMaster.setNoOfPerson(numOfPerson);
                orderMaster.setPaymentTypeId(1);
                orderMaster.setSalesTaxPercent(Configuration.getPlaceDetail().getGSTPercentage());
                if (orderMaster.getOrderTypeId() == 3) {
                    orderMaster.setDeliveryFeeAmount(Configuration.getPlaceDetail().getDeliveryCharges());
                    orderMaster.setDeliveryType(1);
                }
                orderMaster.setDiscountTypeId(1);
                orderMaster.setShiftRecordId(Configuration.getShiftRecordId());
                orderMaster.setUserId(Configuration.getUserId());
                orderMaster.setUsername(Configuration.getUser().getUsername());
                orderMaster.setCheckoutDeviceDatetime(new Date());
                orderMaster.setStatusId(1);

                if (manualCustomer) {
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
                    orderMaster.setRiderName(etRiderName.getText().toString());
                    orderMaster.setRiderMobileNo(etRiderMobileNo.getText().toString());
                    orderMaster.setRiderBikeNo(etRiderBikeNo.getText().toString());

                }

                if (selectedCustomer != null) {
                    orderMaster.setCustomerId(selectedCustomer.getId());
                    orderMaster.setCustomerName(selectedCustomer.getFullName());
                    orderMaster.setCustomerContactNo(selectedCustomer.getContactNo());
                    orderMaster.setCustomerAddress(selectedCustomer.getAddress());
                }

                rowId = applicationDAL.addUpdateOrderMaster(orderMaster);
                if (rowId > 0) {
                    receiptList.add(new SpinnerItem(orderMaster.getOrderMasterId(), "Rcpt # " + receiptNo));
                    spReceipt.setSelection(receiptList.size() - 1, true);
                } else {
                    orderMaster = null;
                    selectedCustomer = null;
                    manualCustomer = false;
                }
            }

            if (orderMaster != null) {
                String dishId = dishDetailList.get(position).getDishId();
                String price = CommonUtils.parseTwoDecimal(dishDetailList.get(position).getTotalPrice());
                String dishKey = dishId;
                if (variants != null && variants.size() > 0) {
                    dishKey += UUID.randomUUID().toString();
                }

                OrderChild orderChild = new OrderChild();
                if (hashMapOrderItem.containsKey(dishKey)) {
                    int orderChildIndex = hashMapOrderItem.get(dishKey);
                    orderChild = orderMaster.getOrderChilds().get(orderChildIndex);
                }

                String quantity = CommonUtils.parseTwoDecimal(Float.valueOf(orderChild.getQuantity()) + cartQuantity);

                orderChild.setOrderMasterId(orderMaster.getOrderMasterId());
                orderChild.setDishId(dishId);
                orderChild.setDishName(dishDetailList.get(position).getDishName());
                orderChild.setApplyGST(dishDetailList.get(position).isApplyGST());
                orderChild.setApplyDiscount(dishDetailList.get(position).isApplyDiscount());
                orderChild.setQuantity(quantity);
                orderChild.setPrice(price);
                if (specialInstruction.equals("") == false) {
                    orderChild.setSpecialInstruction(specialInstruction);
                }
                orderChild.setDiscountTypeId(1);
                if (variants != null && variants.size() > 0) {
                    orderChild.setVariants(variants);
                }
                orderChild.setDatetimeStamp(new Date());
                orderChild.setStatusId(1);
                if (hashMapOrderItem.containsKey(dishKey) == false) {
                    orderMaster.getOrderChilds().add(orderChild);
                    hashMapOrderItem.put(dishKey, orderMaster.getOrderChilds().size() - 1);
                }

                orderChild.calculateValues();
                orderMaster.calculateValues();

                applicationDAL.addUpdateOrderChild(orderChild);
                applicationDAL.addUpdateOrderMaster(orderMaster);

                if (btnProceed != null) {
                    btnProceed.setText(CommonUtils.formatTwoDecimal(orderMaster.getNumberOfItems()) + " items = Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getSubTotalAmount()));
                }

                if (cartFragment != null) {
                    cartFragment.loadOrderMaster(orderMaster.getOrderMasterId());
                }
            }
        } else {
            UIHelper.showConfirmDialog(this, "", "Shift is not start, do you want to start shift?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(MainActivity.this, StartShiftActivity.class);
                    startActivityForResult(intent, ActivityRequest.REQUEST_SHIFT_START);

                }
            });
        }
    }


    private void showTablePersonDialog() {
        final Dialog tablePersonDialog = new Dialog(this);
        // tablePersonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tablePersonDialog.setContentView(R.layout.table_person_dialog);

        TextView lbl_1 = tablePersonDialog.findViewById(R.id.lbl_1);
        TextView lbl_2 = tablePersonDialog.findViewById(R.id.lbl_2);
        TextView lbl_3 = tablePersonDialog.findViewById(R.id.lbl_3);
        TextView lbl_4 = tablePersonDialog.findViewById(R.id.lbl_4);
        TextView lbl_5 = tablePersonDialog.findViewById(R.id.lbl_5);
        TextView lbl_6 = tablePersonDialog.findViewById(R.id.lbl_6);
        final EditText etNoOfPerson = tablePersonDialog.findViewById(R.id.etNoOfPerson);

        final Button btnSubmit = tablePersonDialog.findViewById(R.id.btnSubmit);
        Button btnCancel = tablePersonDialog.findViewById(R.id.btnCancel);

        View.OnClickListener onKeypadClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView) view;
                etNoOfPerson.setText(textView.getText().toString());
                btnSubmit.performClick();
            }
        };

        lbl_1.setOnClickListener(onKeypadClickListener);
        lbl_2.setOnClickListener(onKeypadClickListener);
        lbl_3.setOnClickListener(onKeypadClickListener);
        lbl_4.setOnClickListener(onKeypadClickListener);
        lbl_5.setOnClickListener(onKeypadClickListener);
        lbl_6.setOnClickListener(onKeypadClickListener);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(etNoOfPerson.getText().toString()) > 0) {
                    tablePersonDialog.dismiss();

                    selectTable(Integer.valueOf(etNoOfPerson.getText().toString()));
                } else {
                    UIHelper.showErrorDialog(MainActivity.this, "", "Enter no of persons");
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tablePersonDialog.dismiss();
            }
        });
        //  tablePersonDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        tablePersonDialog.show();

    }

    private void selectTable(int numOfPerson) {
        this.numOfPerson = numOfPerson;
        gridTables.setVisibility(View.GONE);
        lyTableDetail.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                svDishes.clearFocus();
            }
        }, 200);
    }

    private void loadDashBoard() {

        if (LocalDataManager.getInstance().getBoolean("IsSynced")) {

            UserDetail userDetail = applicationDAL.getUserDetailByUsername(Configuration.getUser().getUsername());
            if (userDetail != null) {
                Configuration.getUser().setModificationKey(userDetail.getModificationKey());
            }

            createCustomerDialog();
            createSelectCustomerDialog();

            floorDetailList = applicationDAL.getFloorDetails("", "");

            for (FloorDetail floorDetail : floorDetailList) {

                if (floorDetail.getFloorName().equals("Take Away")) {
                    floorDetail.setOrderTypeId(2);
                } else if (floorDetail.getFloorName().equals("Delivery")) {
                    floorDetail.setOrderTypeId(3);
                } else if (floorDetail.getFloorName().equals("Drive Inn")) {
                    floorDetail.setOrderTypeId(4);
                } else {
                    floorDetail.setOrderTypeId(1);
                }
                TabLayout.Tab tab = tabFloorList.newTab();
                tab.setText(floorDetail.getFloorName());
                tab.setTag(floorDetail.getFloorId());
                tabFloorList.addTab(tab);
            }

            List<CategoryDetail> categoryDetailList = applicationDAL.getCategoryDetails("", "");

            categoryDetailList.add(0, new CategoryDetail("0", "All", "", "", ""));
            for (CategoryDetail categoryDetail : categoryDetailList) {
                TabLayout.Tab tab = tabCategoryList.newTab();
                tab.setText(categoryDetail.getCategoryName());
                tab.setTag(categoryDetail.getCategoryId());
                tabCategoryList.addTab(tab);
            }

        }

    }

    private void loadTables(int floorIndex) {
        selectedFloor = floorDetailList.get(floorIndex);

        tableDetailList = applicationDAL.getTableDetails(DBHelper.COL_TABLE_DETAIL_FLOOR_ID + " = '" + selectedFloor.getFloorId() + "'", "");

        TableDetailAdapter tableDetailAdapter = new TableDetailAdapter(this, tableDetailList);
        gridTables.setAdapter(tableDetailAdapter);

        ArrayAdapter<TableDetail> tableDetailArrayAdapter = new ArrayAdapter<TableDetail>(this, android.R.layout.simple_spinner_item, tableDetailList);
        tableDetailArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTable.setAdapter(tableDetailArrayAdapter);


        if (tableDetailList.size() > 0 && floorDetailList.get(floorIndex).isShowTable() == false) {
            gridTables.performItemClick(gridTables, 0, gridTables.getItemIdAtPosition(0));
        }
    }


    private void loadDishes(String categoryId, String dishName) {
        if (categoryId.equals("0")) {
            dishDetailList = applicationDAL.getDishDetails(DBHelper.COL_DISH_DETAIL_DISH_NAME + " like '%" + dishName + "%'", "");
        } else {
            dishDetailList = applicationDAL.getDishDetails(DBHelper.COL_DISH_DETAIL_CATEGORY_ID + " = '" + categoryId + "'  COLLATE NOCASE and " + DBHelper.COL_DISH_DETAIL_DISH_NAME + " like '%" + dishName + "%'", "");
        }


        DishGridAdapter dishGridAdapter = new DishGridAdapter(this, dishDetailList);
        gridDishes.setAdapter(dishGridAdapter);

        DishListAdapter dishListAdapter = new DishListAdapter(this, dishDetailList);
        listDish.setAdapter(dishListAdapter);

    }


    private void syncMasterData() {
        if (LocalDataManager.getInstance().getBoolean("IsSynced") == false) {
            SyncMasterData syncMasterData = new SyncMasterData(this);
            syncMasterData.setOnSyncListener(new SyncMasterData.OnSyncListener() {
                @Override
                public void onSyncStart() {
                    lockScreenOrientation();
                }

                @Override
                public void onSyncCompleted(boolean isSuccess, String tableName) {
                    unlockScreenOrientation();
                    if (isSuccess) {
                        LocalDataManager.getInstance().putBoolean("IsSynced", true);
                        LocalDataManager.getInstance().putString("LastSyncedOn", CommonUtils.extractDate(new Date(), "dd/MM/yyyy hh:mm aa"));

                        applicationDAL.addUpdateSyncDetail(new SyncDetail(DBHelper.TABLE_ORDER_MASTER, "Orders", 2, 10, 0, new Date()));
                        applicationDAL.addUpdateSyncDetail(new SyncDetail(DBHelper.TABLE_SHIFT_RECORD, "Shift Records", 2, 11, 0, new Date()));

                        loadDashBoard();
                    }
                }
            });

            syncMasterData.sync(Configuration.getPlaceId(), "");
        }
    }


    private void lockScreenOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void unlockScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    private void setUserDetails() {
        User user = Configuration.getUser();
        tvUserFullName.setText(user.getFirstName() + " " + user.getLastName());
        tvUserType.setText("User");
        tvUserEmail.setText(user.getEmail());
        tvUserContactNo.setText(user.getMobileNo());
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else if (gridTables.getVisibility() == View.GONE && selectedFloor.isShowTable()) {
            btnShowTable.performClick();
        } else {
            if ((new Date().getTime() - lastTimeBackPress) < timePeriodToExit) {
                exitToast.cancel();
                setResult(RESULT_OK);
                finish();
            } else {
                exitToast = UIHelper.showShortToast(this, "Press again to exit");
                lastTimeBackPress = new Date().getTime();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        if (Configuration.getUser().getUsername().equals(Configuration.getUser().getSessionUsername())) {
            menu.findItem(R.id.action_backup_order).setVisible(false);
            menu.findItem(R.id.action_restore_order).setVisible(false);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_select_customer) {
            selectCustomerDialog.show();
        } else if (id == R.id.action_search_customer) {
            customerDialog.show();
        } else if (id == R.id.action_sync_data) {
            Intent intent = new Intent(this, SyncDataActivity.class);
            startActivityForResult(intent, ActivityRequest.REQUEST_SYNC_DATA);

            return true;
        } else if (id == R.id.action_end_shift) {
            Intent intent = new Intent(this, EndShiftActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_backup_order) {
            backupOrder();

            return true;
        } else if (id == R.id.action_restore_order) {
            restoreOrder();

            return true;
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
                selectedCustomer = customerList.get(position);

                if (orderMaster != null) {
                    orderMaster.setCustomerId(selectedCustomer.getId());
                    orderMaster.setCustomerName(selectedCustomer.getFullName());
                    orderMaster.setCustomerContactNo(selectedCustomer.getContactNo());
                    orderMaster.setCustomerAddress(selectedCustomer.getAddress());

                    applicationDAL.addUpdateOrderMaster(orderMaster);
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

    private void createSelectCustomerDialog() {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View selectCustomerDialogLayout = inflater.inflate(R.layout.select_customer_dialog, null);

        etContactNo = selectCustomerDialogLayout.findViewById(R.id.etContactNo);
        etCustomerName = selectCustomerDialogLayout.findViewById(R.id.etCustomerName);
        etAddress = selectCustomerDialogLayout.findViewById(R.id.etAddress);
        lyDeliveryDetail = selectCustomerDialogLayout.findViewById(R.id.lyDeliveryDetail);
        rbNow = selectCustomerDialogLayout.findViewById(R.id.rbNow);
        rbLater = selectCustomerDialogLayout.findViewById(R.id.rbLater);
        lyDeliveryDate = selectCustomerDialogLayout.findViewById(R.id.lyDeliveryDate);
        tvDeliveryDate = selectCustomerDialogLayout.findViewById(R.id.tvDeliveryDate);
        tvDeliveryTime = selectCustomerDialogLayout.findViewById(R.id.tvDeliveryTime);
        btnChangeDate = selectCustomerDialogLayout.findViewById(R.id.btnChangeDate);
        btnChangeTime = selectCustomerDialogLayout.findViewById(R.id.btnChangeTime);
        etDeliveryFee = selectCustomerDialogLayout.findViewById(R.id.etDeliveryFee);
        spDeliveryCompany = selectCustomerDialogLayout.findViewById(R.id.spDeliveryCompany);
        etRiderName = selectCustomerDialogLayout.findViewById(R.id.etRiderName);
        etRiderMobileNo = selectCustomerDialogLayout.findViewById(R.id.etRiderMobileNo);
        etRiderBikeNo = selectCustomerDialogLayout.findViewById(R.id.etRiderBikeNo);
        btnCustomerSubmit = selectCustomerDialogLayout.findViewById(R.id.btnCustomerSubmit);

        deliveryCompanyDetails = applicationDAL.getDeliveryCompanies("", "");
        deliveryCompanyDetails.add(0, new DeliveryCompanyDetail("1", "Home Delivery"));

        ArrayAdapter<DeliveryCompanyDetail> deliveryCompanyDetailArrayAdapter = new ArrayAdapter<DeliveryCompanyDetail>(this, android.R.layout.simple_spinner_item, deliveryCompanyDetails);
        deliveryCompanyDetailArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDeliveryCompany.setAdapter(deliveryCompanyDetailArrayAdapter);

        deliveryDateCalendar = Calendar.getInstance();

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

        tvDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnChangeDeliveryDate_onClick(view);
            }
        });

        btnChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnChangeDeliveryDate_onClick(view);
            }
        });

        tvDeliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnChangeDeliveryTime_onClick(view);
            }
        });

        btnChangeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnChangeDeliveryTime_onClick(view);
            }
        });

        btnCustomerSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manualCustomer = true;

                if (orderMaster != null) {
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
                    orderMaster.setRiderName(etRiderName.getText().toString());
                    orderMaster.setRiderMobileNo(etRiderMobileNo.getText().toString());
                    orderMaster.setRiderBikeNo(etRiderBikeNo.getText().toString());
                    orderMaster.calculateValues();

                    applicationDAL.addUpdateOrderMaster(orderMaster);
                }

                selectCustomerDialog.dismiss();
            }
        });

        selectCustomerDialog = new AlertDialog.Builder(this).create();
        selectCustomerDialog.setView(selectCustomerDialogLayout);

        selectCustomerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                if (selectedFloor.getOrderTypeId() == 3) {
                    lyDeliveryDetail.setVisibility(View.VISIBLE);
                } else {
                    lyDeliveryDetail.setVisibility(View.GONE);
                }


                if (orderMaster != null) {
                    int deliveryCompanyPosition = 0;
                    deliveryType = orderMaster.getDeliveryType();
                    if (orderMaster.getOrderTypeId() == 3) {
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
                    }

                    etCustomerName.setText(orderMaster.getCustomerName());
                    etContactNo.setText(orderMaster.getCustomerContactNo());
                    etAddress.setText(orderMaster.getCustomerAddress());
                    etDeliveryFee.setText(orderMaster.getDeliveryFeeAmount());
                    spDeliveryCompany.setSelection(deliveryCompanyPosition);
                    etRiderName.setText(orderMaster.getRiderName());
                    etRiderMobileNo.setText(orderMaster.getRiderMobileNo());
                    etRiderBikeNo.setText(orderMaster.getRiderBikeNo());

                }
            }
        });
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

    private void loadCustomer(String criteria) {
        customerList = applicationDAL.getCustomers(DBHelper.COL_CUSTOMER_FULL_NAME + " like '%" + criteria + "%' or " + DBHelper.COL_CUSTOMER_CONTACT_NO + " like '%" + criteria + "%'", "");

        CustomerAdapter customerAdapter = new CustomerAdapter(this, customerList);
        listCustomer.setAdapter(customerAdapter);
    }

    private void backupOrder() {

        boolean backupStatus = backupOrder("sdcard/RevakiPOS/RevakiOrder.bkp");
        if (backupStatus) {
            UIHelper.showShortToast(this, "Order backup completed.");
        } else {
            UIHelper.showErrorDialog(this, "", "Backup failed.");
        }
    }


    private boolean backupOrder(String path) {
        boolean status = false;
        try {
            Type type;
            Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

            List<ShiftRecord> shiftRecords = applicationDAL.getShiftRecords("", "");
            type = new TypeToken<List<ShiftRecord>>() {
            }.getType();

            String shiftJson = gson.toJson(shiftRecords, type);

            List<OrderMaster> orderMasters = applicationDAL.getOrderMasters("", "");
            type = new TypeToken<List<OrderMaster>>() {
            }.getType();

            String orderJson = gson.toJson(orderMasters, type);

            List<PrinterDetail> printerDetails = applicationDAL.getPrinterDetails("", "");
            type = new TypeToken<List<PrinterDetail>>() {
            }.getType();

            String printerJson = gson.toJson(printerDetails, type);

            String settingJson = gson.toJson(applicationDAL.getSettingDetail(), SettingDetail.class);

            JSONObject jObject = new JSONObject();
            jObject.put("ShiftRecords", new JSONArray(shiftJson));
            jObject.put("OrderMasters", new JSONArray(orderJson));
            jObject.put("PrinterDetails", new JSONArray(printerJson));
            jObject.put("SettingDetail", new JSONObject(settingJson));
            jObject.put("ShiftRecordId", Configuration.getShiftRecordId());
            jObject.put("PlaceId", Configuration.getPlaceId());

            File file = new File(path);
            if (!file.exists()) {
                File directory = new File(file.getParent());
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                file.createNewFile();
            }

            BufferedWriter buf = new BufferedWriter(new FileWriter(file));
            buf.write(Base64.encodeToString(jObject.toString().getBytes(), Base64.NO_WRAP));
            buf.close();
            status = true;

        } catch (Exception ex) {
            Logger.writeError(ex);
        }

        return status;
    }

    private void restoreOrder() {

        UIHelper.showConfirmDialog(this, "Restore", "Are you sure to restore, all existing data will be replaced by this restore?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {

                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    InputStream inputStream = new FileInputStream("sdcard/RevakiPOS/RevakiOrder.bkp");
                    byte[] stream = new byte[inputStream.available()];

                    inputStream.read(stream);
                    inputStream.close();

                    JSONObject jObject = new JSONObject(new String(Base64.decode(stream, Base64.NO_WRAP)));

                    if (Configuration.getPlaceId().equals(jObject.getString("PlaceId"))) {
                        type = new TypeToken<List<ShiftRecord>>() {
                        }.getType();

                        List<ShiftRecord> shiftRecords = gson.fromJson(jObject.getJSONArray("ShiftRecords").toString(), type);

                        type = new TypeToken<List<OrderMaster>>() {
                        }.getType();

                        List<OrderMaster> orderMasters = gson.fromJson(jObject.getJSONArray("OrderMasters").toString(), type);

                        List<PrinterDetail> printerDetails = new ArrayList<PrinterDetail>();

                        SettingDetail settingDetail = new SettingDetail();

                        type = new TypeToken<List<PrinterDetail>>() {
                        }.getType();

                        if (jObject.has("PrinterDetails")) {
                            printerDetails = gson.fromJson(jObject.getJSONArray("PrinterDetails").toString(), type);
                        }

                        if (jObject.has("SettingDetail")) {
                            settingDetail = gson.fromJson(jObject.getJSONObject("SettingDetail").toString(), SettingDetail.class);
                        }

                        boolean backupStatus = backupOrder("sdcard/RevakiPOS/Temp/RevakiOrder_" + CommonUtils.extractDate(new Date(), "ddMMyyhhmmss") + ".bkp");
                        if (backupStatus) {
                            applicationDAL.clearOrderData();

                            for (ShiftRecord shiftRecord : shiftRecords) {
                                applicationDAL.addShiftRecord(shiftRecord);
                            }

                            for (OrderMaster orderMaster : orderMasters) {
                                applicationDAL.addOrder(orderMaster);
                            }

                            for (PrinterDetail printerDetail : printerDetails) {
                                applicationDAL.addPrinterDetail(printerDetail);
                            }

                            if (settingDetail != null) {
                                applicationDAL.addUpdateSettingDetail(settingDetail);
                            }

                            Configuration.setShiftRecordId(jObject.getString("ShiftRecordId"));

                            UIHelper.showShortToast(MainActivity.this, "Order restore successfully.");

                        } else {
                            UIHelper.showErrorDialog(MainActivity.this, "", "Restore failed.");
                        }
                    } else {
                        UIHelper.showErrorDialog(MainActivity.this, "", "Restore failed, place not match.");
                    }
                } catch (Exception ex) {
                    Logger.writeError(ex);
                    UIHelper.showErrorDialog(MainActivity.this, "", "Restore failed.");
                }

            }
        });


    }

    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (System.currentTimeMillis() > syncStartTimeMillis) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager != null) {
                        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                        if (networkInfo != null) {
                            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                                Intent serviceIntent = new Intent(context, UploadDataService.class);
                                serviceIntent.putExtra("startCode", UploadDataService.AUTO_START);
                                context.startService(serviceIntent);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
            }
        }
    };

    private BroadcastReceiver uploadDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", 0);
            int startCode = intent.getIntExtra("startCode", 0);
            String syncDetail = intent.getStringExtra("syncDetail");
            if (resultCode == UploadDataService.RESULT_STARTED) {

                if (startCode == UploadDataService.MANUAL_START) {
                    UIHelper.showShortToast(MainActivity.this, "Syncing now...");
                }
            } else if (resultCode == UploadDataService.RESULT_COMPLETED || resultCode == UploadDataService.RESULT_FAILED) {
                if (startCode == UploadDataService.MANUAL_START) {
                    UIHelper.showShortToast(MainActivity.this, syncDetail);
                }
            }
        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_categories) {
            Intent intent = new Intent(this, CategoryListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_dishes) {
            Intent intent = new Intent(this, DishListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_transactions) {
            Intent intent = new Intent(this, TransactionListActivity.class);
            startActivityForResult(intent, ActivityRequest.REQUEST_TRANSACTION_LIST);
        } else if (id == R.id.nav_shift_register) {
            Intent intent = new Intent(this, ShiftRegisterActivity.class);
            intent.putExtra("ReportType", 1);
            startActivity(intent);
        } else if (id == R.id.nav_shift_summary) {
            Intent intent = new Intent(this, ShiftRegisterActivity.class);
            intent.putExtra("ReportType", 2);
            startActivity(intent);
        } else if (id == R.id.nav_day_register) {
            Intent intent = new Intent(this, DayRegisterActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_void_report) {
            Intent intent = new Intent(this, ShiftRegisterActivity.class);
            intent.putExtra("ReportType", 3);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_print_test) {
            Intent intent = new Intent(this, PrintViewActivity.class);
            intent.putExtra("PrintType", "PrintTest");
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {

        UIHelper.showConfirmDialog(this, "Log Out", "Are you sure to log out?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SessionManager.getInstance().logout();

                Configuration.setLogin(false);
                Configuration.setOfflineLogin(false);
                finish();

            }
        });
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_F1) {

        } else if (event.getKeyCode() == KeyEvent.KEYCODE_F4) {
            spReceipt.performClick();
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_F5) {
            spReceipt.setSelection(0, true);
            // } else if (event.getKeyCode() == KeyEvent.KEYCODE_F6) {
            //    PreBillPrint();
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_F12) {


        } else if (event.getKeyCode() == KeyEvent.KEYCODE_F9) {

        }

        return super.onKeyUp(keyCode, event);
    }
}
