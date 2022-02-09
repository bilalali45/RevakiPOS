package com.revaki.revakipos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.revaki.revakipos.beans.DeliveryCompanyDetail;
import com.revaki.revakipos.beans.DishDetail;
import com.revaki.revakipos.beans.OrderChild;
import com.revaki.revakipos.beans.OrderChildVariant;
import com.revaki.revakipos.beans.OrderMaster;
import com.revaki.revakipos.beans.PlaceDetail;
import com.revaki.revakipos.beans.PrinterDetail;
import com.revaki.revakipos.beans.PrinterModel;
import com.revaki.revakipos.beans.SettingDetail;
import com.revaki.revakipos.beans.VariantDetail;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.db.DataRow;
import com.revaki.revakipos.db.DataTable;
import com.revaki.revakipos.helper.CommonUtils;
import com.revaki.revakipos.helper.UIHelper;
import com.revaki.revakipos.printer.EpsonThermalPrinter;
import com.revaki.revakipos.printer.Printer;
import com.revaki.revakipos.printer.SunmiV1Printer;
import com.revaki.revakipos.printer.WifiThermalPrinter;
import com.revaki.revakipos.utils.BackgroundRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class PrintViewActivity extends AppCompatActivity {

    private ApplicationDAL applicationDAL;

    private Button btnPrint;
    private WebView wvPrintPreview;
    private SettingDetail settingDetail = null;
    private HashMap<String, String> kitchenPrinterCategories = new HashMap<String, String>();
    private PrinterDetail printerDetail = null;
    private PrinterModel printerModel = null;
    private PlaceDetail placeDetail = null;
    String orderMasterId = null;
    private String shiftRecordId = null;
    private String shiftType = null;
    private Date shiftDate = null;
    String printType = null;
    int printMode = 1;
    int printNoOfCopies = 1;
    int checkoutMode = 0;
    boolean isPrinterConfigured = false;
    boolean isPrintRequest = false;
    Printer.PaperSize paperSize = Printer.PaperSize.Paper_80mm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnPrint = findViewById(R.id.btnPrint);

        wvPrintPreview = findViewById(R.id.wvPrintPreview);

        printType = getIntent().getStringExtra("PrintType");
        printMode = getIntent().getIntExtra("PrintMode", 1);
        checkoutMode = getIntent().getIntExtra("CheckoutMode", 0);

        applicationDAL = new ApplicationDAL(this);
        placeDetail = Configuration.getPlaceDetail();
        settingDetail = applicationDAL.getSettingDetail();

        Type type;
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        type = new TypeToken<HashMap<String, String>>() {
        }.getType();

        kitchenPrinterCategories = gson.fromJson(settingDetail.getKitchenPrinterCategories(), type);


        if (printType.equals("KitchenPrint") || printType.equals("KitchenReprint")) {
            printNoOfCopies = settingDetail.getKitchenPintCopy();
            if (settingDetail.getKitchenPrinterId().length() > 0) {
                printerDetail = applicationDAL.getPrinterDetail(settingDetail.getKitchenPrinterId());
                printerModel = Printer.getPrinterModel(printerDetail.getPrinterBrand(), printerDetail.getPrinterModel(), printerDetail.getPrinterType());
            }
        } else {
            if (settingDetail.getDefaultPrinterId().length() > 0) {
                printerDetail = applicationDAL.getPrinterDetail(settingDetail.getDefaultPrinterId());
                printerModel = Printer.getPrinterModel(printerDetail.getPrinterBrand(), printerDetail.getPrinterModel(), printerDetail.getPrinterType());
            }
        }

        if (printerDetail != null && printerModel != null) {
            isPrinterConfigured = true;
        } else {
            printMode = 1;
        }

        if (printType.equals("PrintTest")) {
            getSupportActionBar().setTitle("Print Test");
        } else if (printType.equals("PreBill")) {
            getSupportActionBar().setTitle("Pre Bill");
            orderMasterId = getIntent().getStringExtra("OrderMasterId");
            if (isPrinterConfigured && settingDetail.isShowPreBillPreview() == false) {
                printMode = 2;
            }
        } else if (printType.equals("PostBill")) {
            getSupportActionBar().setTitle("Post Bill");
            orderMasterId = getIntent().getStringExtra("OrderMasterId");
            if (printMode != 3 && isPrinterConfigured && settingDetail.isShowPostBillPreview() == false) {
                printMode = 2;
            }
        } else if (printType.equals("KitchenPrint")) {
            getSupportActionBar().setTitle("Kitchen Print");
            orderMasterId = getIntent().getStringExtra("OrderMasterId");
            if (isPrinterConfigured && settingDetail.isShowKitchenPrintPreview() == false) {
                printMode = 2;
            }
        } else if (printType.equals("KitchenReprint")) {
            getSupportActionBar().setTitle("Kitchen Reprint");
            orderMasterId = getIntent().getStringExtra("OrderMasterId");
            if (isPrinterConfigured && settingDetail.isShowKitchenPrintPreview() == false) {
                printMode = 2;
            }
        } else if (printType.equals("ShiftRegister")) {
            getSupportActionBar().setTitle("Shift Register");
            shiftRecordId = getIntent().getStringExtra("ShiftRecordId");
            shiftType = getIntent().getStringExtra("ShiftType");
        } else if (printType.equals("DayRegister")) {
            getSupportActionBar().setTitle("Day Register");
            shiftDate = CommonUtils.parseDate(getIntent().getStringExtra("ShiftDate"), "dd-MMM-yyyy");
        } else if (printType.equals("ShiftSummary")) {
            getSupportActionBar().setTitle("Shift Summary");
            shiftRecordId = getIntent().getStringExtra("ShiftRecordId");
            shiftType = getIntent().getStringExtra("ShiftType");
        } else if (printType.equals("VoidReport")) {
            getSupportActionBar().setTitle("Void Report");
            shiftRecordId = getIntent().getStringExtra("ShiftRecordId");
            shiftType = getIntent().getStringExtra("ShiftType");
        }


        print(printMode);

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPrinterConfigured) {
                    print(2);
                }
            }
        });

    }

    private void print(int printMode) {
        if (isPrintRequest == false) {
            isPrintRequest = true;

            sbPreview = new StringBuilder();
            if (printType.equals("PrintTest")) {
                testPrintView(printMode);
            } else if (printType.equals("PreBill")) {
                printPreBill(orderMasterId, printMode);
            } else if (printType.equals("PostBill")) {
                printPostBill(orderMasterId, printMode);
            } else if (printType.equals("KitchenPrint")) {
                kitchenPrint(orderMasterId, false, printMode);
            } else if (printType.equals("KitchenReprint")) {
                kitchenPrint(orderMasterId, true, printMode);
            } else if (printType.equals("ShiftRegister")) {
                printShiftRegister(shiftRecordId, shiftType, printMode);
            } else if (printType.equals("DayRegister")) {
                printDayRegister(shiftDate, printMode);
            } else if (printType.equals("ShiftSummary")) {
                printShiftSummary(shiftRecordId, shiftType, printMode);
            } else if (printType.equals("VoidReport")) {
                printVoidReport(shiftRecordId, shiftType, printMode);
            }

        }
    }

    private void printSuccess() {
        isPrintRequest = false;
        if (printMode == 2) {
            finish();
        }
    }

    private void printFailed() {
        isPrintRequest = false;
        print(1);
        UIHelper.showErrorDialog(this, "", "Print Failed.");
    }


    private void testPrintView(int printMode) {
        int totalCharLength = 40;
        int itemColumnLength = 22;
        int priceColumnLength = 6;
        int qtyColumnLength = 5;
        int totalColumnLength = 7;

        if (paperSize == Printer.PaperSize.Paper_58mm) {
            totalCharLength = 34;
            itemColumnLength = 18;
            priceColumnLength = 6;
            qtyColumnLength = 4;
            totalColumnLength = 6;
        }

        List<String> commands = new ArrayList<String>();


        commands.add("<C>");
        commands.add("<B>");
        commands.add("<DW>");
        commands.add("<DH>");
        commands.add("Heading # Double Size");
        commands.add("</DH>");
        commands.add("</DW>");

        commands.add("<DW>");
        commands.add("Heading # Double Width");
        commands.add("</DW>");

        commands.add("<DH>");
        commands.add("Heading # Double Height");
        commands.add("</DH>");
        commands.add("</B>");

        commands.add("<IMG>/RevakiPOS/logo.jpg</IMG>");
        commands.add("<BR>");
        commands.add("Normal Text");

        commands.add("<SC>");
        commands.add("Small Text");
        commands.add("</SC>");

        commands.add("<U>");
        commands.add("Underline Text");
        commands.add("</U>");

        commands.add("<B>");
        commands.add("Bold Text");
        commands.add("</B>");

        commands.add(CommonUtils.repeat("-", totalCharLength));
        commands.add("<R>");
        commands.add("Right Text");
        commands.add("<L>");
        commands.add("Left Text");
        commands.add("<C>");
        commands.add(CommonUtils.repeat("-", totalCharLength));
        commands.add("<BR>");
        commands.add("<BR>");
        commands.add("<CP>");

        printRecord(printMode, commands, printNoOfCopies);
    }

    private void printPreBill(String orderMasterId, int printMode) {
        int totalCharLength = 40;
        int itemColumnLength = 22;
        int priceColumnLength = 6;
        int qtyColumnLength = 5;
        int totalColumnLength = 7;

        if (paperSize == Printer.PaperSize.Paper_58mm) {
            totalCharLength = 34;
            itemColumnLength = 18;
            priceColumnLength = 6;
            qtyColumnLength = 4;
            totalColumnLength = 6;
        }

        OrderMaster orderMaster = applicationDAL.getOrderMaster(orderMasterId);

        int previousCharLength = 0;
        String dishNameLine1 = "";
        String dishNameLine2 = "";
        List<String> commands = new ArrayList<String>();

        commands.add("<C>");
        commands.add("");
        if (placeDetail.getPrintLogoImage().length() > 0) {
            commands.add("<IMG>" + placeDetail.getPrintLogoImage() + "</IMG>");
        }
        commands.add("<B>");

        if (placeDetail.getPrintLogoName().length() > 0) {
            commands.add("<DW>");
            commands.add(placeDetail.getPrintLogoName());
            commands.add("</DW>");
        }


        if (placeDetail.getContactInfo().length() > 0) {
            commands.add("Contact Info: " + placeDetail.getContactInfo());
        }

        if (placeDetail.getAddress().length() > 0) {
            String address = "Address : " + placeDetail.getAddress();
            int addressPrintLength = 0;
            int addressEndLength = totalCharLength;
            while (address.length() > addressPrintLength) {
                if ((address.length() - addressPrintLength) < totalCharLength) {
                    addressEndLength = (address.length() - addressPrintLength);
                }
                commands.add(address.substring(addressPrintLength, addressPrintLength + addressEndLength));
                addressPrintLength += addressEndLength;
            }
        }
        if (placeDetail.getEmail().length() > 0) {
            commands.add("Email: " + placeDetail.getEmail());
        }
        if (placeDetail.getPhone().length() > 0) {
            commands.add("Phone: " + placeDetail.getPhone());
        }
        commands.add(CommonUtils.repeat(".", totalCharLength));
        commands.add("Bill Type : Pre Bill");
        if (placeDetail.getWebsite().length() > 0) {
            commands.add("Website: " + placeDetail.getWebsite());
        }

        if (placeDetail.getNTN().length() > 0) {
            commands.add("SNTN: " + placeDetail.getNTN());
        }
        if (placeDetail.getSTRN().length() > 0) {
            commands.add("STRN: " + placeDetail.getSTRN());
        }
        commands.add("</B>");

        previousCharLength = ("Date: " + CommonUtils.extractDate(orderMaster.getCreationDeviceDatetime(), "dd-MMM-yyyy")).length();
        commands.add("Date: " + CommonUtils.extractDate(orderMaster.getCreationDeviceDatetime(), "dd-MMM-yyyy") + CommonUtils.padLeft("Receipt# " + orderMaster.getDeviceReceiptNo(), totalCharLength - previousCharLength));
        previousCharLength = ("Time: " + CommonUtils.extractDate(orderMaster.getCreationDeviceDatetime(), "hh:mm aa")).length();
        commands.add("Time: " + CommonUtils.extractDate(orderMaster.getCreationDeviceDatetime(), "hh:mm aa") + CommonUtils.padLeft("User: " + orderMaster.getUsername(), totalCharLength - previousCharLength));
        if (orderMaster.getWaiterName() != null && orderMaster.getWaiterName().length() > 0) {
            commands.add(CommonUtils.padRight("Waiter: " + orderMaster.getWaiterName(), totalCharLength));
        }
        if (orderMaster.getNoOfPerson() > 0) {
            commands.add("No. of Persons: " + CommonUtils.formatTwoDecimal(orderMaster.getNoOfPerson()));
        }
        commands.add("<BR>");
        commands.add("<B>");
        commands.add("Table: " + orderMaster.getTableName());
        commands.add(CommonUtils.repeat("-", totalCharLength));
        commands.add(CommonUtils.padRight("Item(s)", itemColumnLength) + CommonUtils.padRight("Price", priceColumnLength) + CommonUtils.padRight("Qty", qtyColumnLength) + CommonUtils.padLeft("Total", totalColumnLength));
        commands.add(CommonUtils.repeat("-", totalCharLength));
        commands.add("</B>");
        for (OrderChild orderChild : orderMaster.getOrderChilds()) {
            dishNameLine1 = CommonUtils.padRight(orderChild.getDishName(), itemColumnLength - 1);
            if (dishNameLine1.length() > itemColumnLength - 1) {
                dishNameLine2 = dishNameLine1.substring(itemColumnLength - 1);
                dishNameLine1 = dishNameLine1.substring(0, itemColumnLength - 1);

            }
            commands.add(CommonUtils.padRight(dishNameLine1, itemColumnLength) + CommonUtils.padRight((orderChild.getPrice().equals("0") ? "" : orderChild.getPrice()), priceColumnLength) + CommonUtils.padRight(orderChild.getQuantity(), qtyColumnLength) + CommonUtils.padLeft((orderChild.getNetAmount().equals("0") ? "" : CommonUtils.formatTwoDecimal(orderChild.getNetAmount())), totalColumnLength));
            if (dishNameLine2.length() > 0) {
                commands.add(CommonUtils.padRight(dishNameLine2, totalCharLength));
            }
            dishNameLine2 = "";
            if (Float.valueOf(orderChild.getDiscountAmount()) > 0) {
                commands.add(CommonUtils.padLeft("", itemColumnLength) + CommonUtils.padRight("Disc.", priceColumnLength) + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderChild.getDiscountAmount()), qtyColumnLength + totalColumnLength));
            }

            for (OrderChildVariant orderChildVariant : orderChild.getVariants()) {
                for (VariantDetail variantDetail : orderChildVariant.getData()) {
                    commands.add(CommonUtils.padLeft("-", qtyColumnLength) + CommonUtils.padRight(variantDetail.getText() + " (" + CommonUtils.formatTwoDecimal(variantDetail.getPrice()) + ")", itemColumnLength + priceColumnLength) + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(Float.valueOf(orderChild.getQuantity()) * variantDetail.getPrice()), totalColumnLength));
                }
            }
        }
        commands.add(CommonUtils.repeat("-", totalCharLength));
        commands.add("<B>");
        commands.add("Total: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getSubTotalAmount()), totalCharLength - 7));
        commands.add("<BR>");
        if (orderMaster.getDiscountAmount().length() > 0 && orderMaster.getDiscountAmount().equals("0") == false) {
            previousCharLength = ("Discount " + orderMaster.getDiscountPercentage() + "%: ").length();
            commands.add("Discount " + orderMaster.getDiscountPercentage() + "%: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getDiscountAmount()), totalCharLength - previousCharLength));
        }
        if (orderMaster.getProductDiscountAmount().length() > 0 && orderMaster.getProductDiscountAmount().equals("0") == false) {
            commands.add("Product Wise Discount: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getProductDiscountAmount()), totalCharLength - 23));
        }
        if (orderMaster.getSalesTaxAmount().length() > 0 && orderMaster.getSalesTaxAmount().equals("0") == false) {
            previousCharLength = (placeDetail.getGSTTitle() + " " + orderMaster.getSalesTaxPercent() + "%: ").length();
            commands.add(placeDetail.getGSTTitle() + " " + orderMaster.getSalesTaxPercent() + "%: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getSalesTaxAmount()), totalCharLength - previousCharLength));
        }
        if (orderMaster.getOrderTypeId() == 3 && orderMaster.getDeliveryFeeAmount().length() > 0 && orderMaster.getDeliveryFeeAmount().equals("0") == false) {
            commands.add("Delivery Fee Amount: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getDeliveryFeeAmount()), totalCharLength - 21));
        }
        commands.add("<DH>");
        commands.add("Total Amount: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()), totalCharLength - 14));
        commands.add("</DH>");
        if (orderMaster.getCashAmount().length() > 0 && orderMaster.getCashAmount().equals("0") == false) {
            commands.add("Cash: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getCashAmount()), totalCharLength - 6));
        }
        if (orderMaster.getCardAmount().length() > 0 && orderMaster.getCardAmount().equals("0") == false) {
            commands.add("Card: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getCardAmount()), totalCharLength - 6));
        }
        if (orderMaster.getChangeAmount().length() > 0 && orderMaster.getChangeAmount().equals("0") == false) {
            commands.add("Change: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getChangeAmount()), totalCharLength - 8));
        }
        if (orderMaster.getOrderTypeId() == 3) {
            commands.add(CommonUtils.repeat("-", totalCharLength));
            if (orderMaster.getDeliveryCompany() != null && orderMaster.getDeliveryCompany().length() > 0) {
                DeliveryCompanyDetail deliveryCompanyDetail = applicationDAL.getDeliveryCompany(orderMaster.getDeliveryCompany());
                if (deliveryCompanyDetail != null) {
                    commands.add("Delivery Company: " + CommonUtils.padLeft(deliveryCompanyDetail.getDeliveryCompany(), totalCharLength - 18));
                }
            }
            if (orderMaster.getRiderName().length() > 0) {
                commands.add("Rider Name: " + CommonUtils.padLeft(orderMaster.getRiderName(), totalCharLength - 12));
            }
            if (orderMaster.getRiderMobileNo().length() > 0) {
                commands.add("Rider Mobile No.: " + CommonUtils.padLeft(orderMaster.getRiderMobileNo(), totalCharLength - 18));
            }
        }
        commands.add("</B>");
        commands.add(CommonUtils.repeat("-", totalCharLength));
        if (orderMaster.getCustomerName().length() > 0) {
            commands.add("<BR>");
            commands.add("<B>");
            commands.add(CommonUtils.padRight("Customer Details", totalCharLength));
            commands.add("</B>");
            commands.add("Name: " + CommonUtils.padLeft(orderMaster.getCustomerName(), totalCharLength - 6));
            commands.add("Contact No.: " + CommonUtils.padLeft(orderMaster.getCustomerContactNo(), totalCharLength - 13));
            if (orderMaster.getCustomerAddress().length() > 0) {
                commands.add("Address:     " + CommonUtils.padLeft(orderMaster.getCustomerAddress(), totalCharLength - 13));
            }
        }
        if (placeDetail.getBottomText().length() > 0) {
            commands.add(placeDetail.getBottomText());
        }
        commands.add("<BR>");
        commands.add("<BR>");
        commands.add("<BR>");
        commands.add("<BR>");
        commands.add("<CP>");

        printRecord(printMode, commands, printNoOfCopies);
    }


    private void printPostBill(String orderMasterId, int printMode) {
        int totalCharLength = 40;
        int itemColumnLength = 22;
        int priceColumnLength = 6;
        int qtyColumnLength = 5;
        int totalColumnLength = 7;

        if (paperSize == Printer.PaperSize.Paper_58mm) {
            totalCharLength = 34;
            itemColumnLength = 18;
            priceColumnLength = 6;
            qtyColumnLength = 4;
            totalColumnLength = 6;
        }

        OrderMaster orderMaster = applicationDAL.getOrderMaster(orderMasterId);

        int previousCharLength = 0;
        String dishNameLine1 = "";
        String dishNameLine2 = "";
        List<String> commands = new ArrayList<String>();

        commands.add("<C>");
        commands.add("");
        if (placeDetail.getPrintLogoImage().length() > 0) {
            commands.add("<IMG>" + placeDetail.getPrintLogoImage() + "</IMG>");
        }
        commands.add("<B>");

        if (placeDetail.getPrintLogoName().length() > 0) {
            commands.add("<DW>");
            commands.add(placeDetail.getPrintLogoName());
            commands.add("</DW>");
        }
        if (placeDetail.getContactInfo().length() > 0) {
            commands.add("Contact Info: " + placeDetail.getContactInfo());
        }

        if (placeDetail.getAddress().length() > 0) {
            String address = "Address : " + placeDetail.getAddress();
            int addressPrintLength = 0;
            int addressEndLength = totalCharLength;
            while (address.length() > addressPrintLength) {
                if ((address.length() - addressPrintLength) < totalCharLength) {
                    addressEndLength = (address.length() - addressPrintLength);
                }
                commands.add(address.substring(addressPrintLength, addressPrintLength + addressEndLength));
                addressPrintLength += addressEndLength;
            }
        }
        if (placeDetail.getEmail().length() > 0) {
            commands.add("Email: " + placeDetail.getEmail());
        }
        if (placeDetail.getPhone().length() > 0) {
            commands.add("Phone: " + placeDetail.getPhone());
        }

        commands.add(CommonUtils.repeat(".", totalCharLength));
        commands.add("Bill Type : Post Bill");
        if (placeDetail.getWebsite().length() > 0) {
            commands.add("Website: " + placeDetail.getWebsite());
        }
        if (placeDetail.getNTN().length() > 0) {
            commands.add("SNTN: " + placeDetail.getNTN());
        }
        if (placeDetail.getSTRN().length() > 0) {
            commands.add("STRN: " + placeDetail.getSTRN());
        }
        commands.add("</B>");

        previousCharLength = ("Date: " + CommonUtils.extractDate(orderMaster.getCreationDeviceDatetime(), "dd-MMM-yyyy")).length();
        commands.add("Date: " + CommonUtils.extractDate(orderMaster.getCreationDeviceDatetime(), "dd-MMM-yyyy") + CommonUtils.padLeft("Receipt# " + orderMaster.getDeviceReceiptNo(), totalCharLength - previousCharLength));
        previousCharLength = ("Time: " + CommonUtils.extractDate(orderMaster.getCreationDeviceDatetime(), "hh:mm aa")).length();
        commands.add("Time: " + CommonUtils.extractDate(orderMaster.getCreationDeviceDatetime(), "hh:mm aa") + CommonUtils.padLeft("User: " + orderMaster.getUsername(), totalCharLength - previousCharLength));
        commands.add(CommonUtils.padRight("Checkout Datetime:  " + CommonUtils.extractDate(orderMaster.getCheckoutDeviceDatetime(), "dd-MMM-yyyy hh:mm aa"), totalCharLength));
        if (orderMaster.getWaiterName() != null && orderMaster.getWaiterName().length() > 0) {
            commands.add(CommonUtils.padRight("Waiter: " + orderMaster.getWaiterName(), totalCharLength));
        }
        if (orderMaster.getNoOfPerson() > 0) {
            commands.add("No. of Persons: " + CommonUtils.formatTwoDecimal(orderMaster.getNoOfPerson()));
        }
        commands.add("<BR>");
        commands.add("<B>");
        commands.add("Table: " + orderMaster.getTableName());
        commands.add(CommonUtils.repeat("-", totalCharLength));
        commands.add(CommonUtils.padRight("Item(s)", itemColumnLength) + CommonUtils.padRight("Price", priceColumnLength) + CommonUtils.padRight("Qty", qtyColumnLength) + CommonUtils.padLeft("Total", totalColumnLength));
        commands.add(CommonUtils.repeat("-", totalCharLength));
        commands.add("</B>");
        for (OrderChild orderChild : orderMaster.getOrderChilds()) {
            dishNameLine1 = CommonUtils.padRight(orderChild.getDishName(), itemColumnLength - 1);
            if (dishNameLine1.length() > itemColumnLength - 1) {
                dishNameLine2 = dishNameLine1.substring(itemColumnLength - 1);
                dishNameLine1 = dishNameLine1.substring(0, itemColumnLength - 1);

            }
            commands.add(CommonUtils.padRight(dishNameLine1, itemColumnLength) + CommonUtils.padRight((orderChild.getPrice().equals("0") ? "" : orderChild.getPrice()), priceColumnLength) + CommonUtils.padRight(orderChild.getQuantity(), qtyColumnLength) + CommonUtils.padLeft((orderChild.getNetAmount().equals("0") ? "" : CommonUtils.formatTwoDecimal(orderChild.getNetAmount())), totalColumnLength));
            if (dishNameLine2.length() > 0) {
                commands.add(CommonUtils.padRight(dishNameLine2, totalCharLength));
            }
            dishNameLine2 = "";

            if (Float.valueOf(orderChild.getDiscountAmount()) > 0) {
                commands.add(CommonUtils.padLeft("", itemColumnLength) + CommonUtils.padRight("Disc.", priceColumnLength) + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderChild.getDiscountAmount()), qtyColumnLength + totalColumnLength));
            }

            for (OrderChildVariant orderChildVariant : orderChild.getVariants()) {
                for (VariantDetail variantDetail : orderChildVariant.getData()) {
                    commands.add(CommonUtils.padLeft("-", qtyColumnLength) + CommonUtils.padRight(variantDetail.getText() + " (" + CommonUtils.formatTwoDecimal(variantDetail.getPrice()) + ")", itemColumnLength + priceColumnLength) + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(Float.valueOf(orderChild.getQuantity()) * variantDetail.getPrice()), totalColumnLength));
                }
            }
        }
        commands.add(CommonUtils.repeat("-", totalCharLength));
        commands.add("<B>");
        commands.add("Total: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getSubTotalAmount()), totalCharLength - 7));
        commands.add("<BR>");
        if (orderMaster.getDiscountAmount().length() > 0 && orderMaster.getDiscountAmount().equals("0") == false) {
            previousCharLength = ("Discount " + orderMaster.getDiscountPercentage() + "%: ").length();
            commands.add("Discount " + orderMaster.getDiscountPercentage() + "%: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getDiscountAmount()), totalCharLength - previousCharLength));
        }
        if (orderMaster.getProductDiscountAmount().length() > 0 && orderMaster.getProductDiscountAmount().equals("0") == false) {
            commands.add("Product Wise Discount: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getProductDiscountAmount()), totalCharLength - 23));
        }
        if (orderMaster.getSalesTaxAmount().length() > 0 && orderMaster.getSalesTaxAmount().equals("0") == false) {
            previousCharLength = (placeDetail.getGSTTitle() + " " + orderMaster.getSalesTaxPercent() + "%: ").length();
            commands.add(placeDetail.getGSTTitle() + " " + orderMaster.getSalesTaxPercent() + "%: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getSalesTaxAmount()), totalCharLength - previousCharLength));
        }
        if (orderMaster.getTip().length() > 0 && orderMaster.getTip().equals("0") == false) {
            commands.add("Tip: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getTip()), totalCharLength - 5));
        }
        if (orderMaster.getOrderTypeId() == 3 && orderMaster.getDeliveryFeeAmount().length() > 0 && orderMaster.getDeliveryFeeAmount().equals("0") == false) {
            commands.add("Delivery Fee Amount: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getDeliveryFeeAmount()), totalCharLength - 21));
        }
        commands.add("<DH>");
        commands.add("Total Amount: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()), totalCharLength - 14));
        commands.add("</DH>");
        if (orderMaster.getCashAmount().length() > 0 && orderMaster.getCashAmount().equals("0") == false) {
            commands.add("Cash: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getCashAmount()), totalCharLength - 6));
        }
        if (orderMaster.getCardAmount().length() > 0 && orderMaster.getCardAmount().equals("0") == false) {
            commands.add("Card: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getCardAmount()), totalCharLength - 6));
        }
        if (orderMaster.getChangeAmount().length() > 0 && orderMaster.getChangeAmount().equals("0") == false) {
            commands.add("Change: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(orderMaster.getChangeAmount()), totalCharLength - 8));
        }
        if (orderMaster.getOrderTypeId() == 3) {
            commands.add(CommonUtils.repeat("-", totalCharLength));
            if (orderMaster.getDeliveryCompany() != null && orderMaster.getDeliveryCompany().length() > 0) {
                DeliveryCompanyDetail deliveryCompanyDetail = applicationDAL.getDeliveryCompany(orderMaster.getDeliveryCompany());
                if (deliveryCompanyDetail != null) {
                    commands.add("Delivery Company: " + CommonUtils.padLeft(deliveryCompanyDetail.getDeliveryCompany(), totalCharLength - 18));
                }
            }
            if (orderMaster.getRiderName().length() > 0) {
                commands.add("Rider Name: " + CommonUtils.padLeft(orderMaster.getRiderName(), totalCharLength - 12));
            }
            if (orderMaster.getRiderMobileNo().length() > 0) {
                commands.add("Rider Mobile No.: " + CommonUtils.padLeft(orderMaster.getRiderMobileNo(), totalCharLength - 18));
            }
        }
        commands.add("</B>");
        commands.add(CommonUtils.repeat("-", totalCharLength));
        if (orderMaster.getCustomerName().length() > 0) {
            commands.add("<BR>");
            commands.add("<B>");
            commands.add(CommonUtils.padRight("Customer Details", totalCharLength));
            commands.add("</B>");
            commands.add("Name: " + CommonUtils.padLeft(orderMaster.getCustomerName(), totalCharLength - 6));
            commands.add("Contact No.: " + CommonUtils.padLeft(orderMaster.getCustomerContactNo(), totalCharLength - 13));
            if (orderMaster.getCustomerAddress().length() > 0) {
                commands.add("Address:     " + CommonUtils.padLeft(orderMaster.getCustomerAddress(), totalCharLength - 13));
            }
        }
        if (placeDetail.getBottomText().length() > 0) {
            commands.add(placeDetail.getBottomText());
        }
        commands.add("<BR>");
        commands.add("<BR>");
        commands.add("<BR>");
        commands.add("<BR>");
        commands.add("<CP>");
        if (checkoutMode == 1 && settingDetail.isOpenCashDrawerAfterCheckout() && orderMaster.getCashAmount().length() > 0 && orderMaster.getCashAmount().equals("0") == false) {
            commands.add("<OCD>");
        }
        printRecord(printMode, commands, printNoOfCopies);
    }

    private void kitchenPrint(String orderMasterId, boolean isReprint, int printMode) {
        boolean printStatus = false;
        int totalCharLength = 40;
        int itemColumnLength = 34;
        int qtyColumnLength = 6;

        if (paperSize == Printer.PaperSize.Paper_58mm) {
            totalCharLength = 34;
            itemColumnLength = 28;
            qtyColumnLength = 6;
        }

        HashMap<String, String> dishMap = new HashMap<String, String>();
        List<DishDetail> dishDetailList = applicationDAL.getDishDetails("", "");

        for (DishDetail dishDetail : dishDetailList) {
            dishMap.put(dishDetail.getDishId(), dishDetail.getCategoryId());
        }

        OrderMaster orderMasterRecord = applicationDAL.getOrderMaster(orderMasterId);

        TreeMap<String, OrderMaster> orderMap = new TreeMap<String, OrderMaster>();


        String kitchenPrinterId = "";
        String categoryId = "";
        float quantity;

        boolean isPrintSummary = placeDetail.isPrintKitchenSummary();

        for (OrderChild orderChild : orderMasterRecord.getOrderChilds()) {

            categoryId = dishMap.get(orderChild.getDishId());

            if (kitchenPrinterCategories.containsKey(categoryId)) {
                kitchenPrinterId = kitchenPrinterCategories.get(categoryId);
            }

            PrinterDetail categoryPrinterDetail = applicationDAL.getPrinterDetail(kitchenPrinterId);

            if (categoryPrinterDetail == null) {
                kitchenPrinterId = settingDetail.getKitchenPrinterId();
            }

            OrderMaster orderMapMaster;
            if (orderMap.containsKey(kitchenPrinterId)) {
                orderMapMaster = orderMap.get(kitchenPrinterId);
            } else {
                orderMapMaster = new OrderMaster();

                orderMapMaster.setCreationDeviceDatetime(orderMasterRecord.getCreationDeviceDatetime());
                orderMapMaster.setDeviceReceiptNo(orderMasterRecord.getDeviceReceiptNo());
                orderMapMaster.setTableName(orderMasterRecord.getTableName());
                orderMapMaster.setUsername(orderMasterRecord.getUsername());
                orderMapMaster.setWaiterName(orderMasterRecord.getWaiterName());

                orderMap.put(kitchenPrinterId, orderMapMaster);
            }

            quantity = Float.valueOf(orderChild.getQuantity());
            if (isReprint == false) {
                quantity = quantity - Float.valueOf(orderChild.getPrintQuantity());
            }
            if (quantity != 0) {
                orderMapMaster.getOrderChilds().add(orderChild);
            }
        }

        List<KitchenPrintManager> kitchenPrintManagers = new ArrayList<KitchenPrintManager>();

        for (String printerId : orderMap.keySet()) {
            PrinterDetail categoryPrinterDetail = applicationDAL.getPrinterDetail(printerId);
            if (categoryPrinterDetail != null) {
                KitchenPrintManager kitchenPrintManager = null;
                for (KitchenPrintManager kpm : kitchenPrintManagers) {
                    if (kpm.getPrinterDetail().getPrinterKey().equals(categoryPrinterDetail.getPrinterKey())) {
                        kitchenPrintManager = kpm;
                        break;
                    }
                }
                if (kitchenPrintManager == null) {
                    kitchenPrintManager = new KitchenPrintManager();
                    kitchenPrintManagers.add(kitchenPrintManager);
                }
                kitchenPrintManager.setPrinterDetail(categoryPrinterDetail);
                kitchenPrintManager.getOrderMasters().add(orderMap.get(printerId));
            }
        }

        if (isPrintSummary) {
            PrinterDetail categoryPrinterDetail = applicationDAL.getPrinterDetail(settingDetail.getKitchenPrinterId());
            if (categoryPrinterDetail != null) {
                KitchenPrintManager kitchenPrintManager = new KitchenPrintManager();
                kitchenPrintManager.setPrinterDetail(categoryPrinterDetail);
                kitchenPrintManager.getOrderMasters().add(orderMasterRecord);

                kitchenPrintManagers.add(0, kitchenPrintManager);
            }
        }

        for (KitchenPrintManager kitchenPrintManager : kitchenPrintManagers) {
            printerDetail = kitchenPrintManager.getPrinterDetail();
            if (printerDetail != null) {
                printerModel = Printer.getPrinterModel(printerDetail.getPrinterBrand(), printerDetail.getPrinterModel(), printerDetail.getPrinterType());
            }

            int previousCharLength = 0;
            String dishNameLine1 = "";
            String dishNameLine2 = "";
            List<String> commands = new ArrayList<String>();

            for (OrderMaster orderMaster : kitchenPrintManager.getOrderMasters()) {
                if (orderMaster.getOrderChilds().size() > 0) {
                    commands.add("<C>");
                    commands.add("<DW>");
                    commands.add("<B>");
                    if (isReprint) {
                        commands.add("KOT Reprint");
                    }
                    if (isPrintSummary) {
                        commands.add("KOT Summary");
                    }
                    commands.add("Receipt # " + orderMaster.getDeviceReceiptNo());
                    commands.add(orderMaster.getTableName());
                    commands.add("</B>");
                    commands.add("</DW>");
                    commands.add("<BR>");

                    previousCharLength = ("Date: " + CommonUtils.extractDate(new Date(), "dd-MMM-yyyy")).length();
                    commands.add("Date: " + CommonUtils.extractDate(new Date(), "dd-MMM-yyyy") + CommonUtils.padLeft("Table# " + orderMaster.getTableName(), totalCharLength - previousCharLength));
                    previousCharLength = ("Time: " + CommonUtils.extractDate(new Date(), "hh:mm aa")).length();
                    commands.add("Time: " + CommonUtils.extractDate(new Date(), "hh:mm aa") + CommonUtils.padLeft("User: " + orderMaster.getUsername(), totalCharLength - previousCharLength));
                    if (orderMaster.getWaiterName() != null && orderMaster.getWaiterName().length() > 0) {
                        commands.add(CommonUtils.padRight("Waiter: " + orderMaster.getWaiterName(), totalCharLength));
                    }
                    commands.add("<B>");
                    commands.add(CommonUtils.repeat("-", totalCharLength));
                    commands.add(CommonUtils.padRight("Item(s)", itemColumnLength) + CommonUtils.padRight("Qty", qtyColumnLength));
                    commands.add(CommonUtils.repeat("-", totalCharLength));
                    commands.add("</B>");
                    commands.add("<DH>");


                    for (OrderChild orderChild : orderMaster.getOrderChilds()) {
                        quantity = Float.valueOf(orderChild.getQuantity());
                        if (isReprint == false) {
                            quantity = quantity - Float.valueOf(orderChild.getPrintQuantity());
                        }
                        if (quantity != 0) {
                            dishNameLine1 = CommonUtils.padRight(orderChild.getDishName(), itemColumnLength - 1);

                            if (dishNameLine1.length() > itemColumnLength - 1) {
                                dishNameLine2 = dishNameLine1.substring(itemColumnLength - 1);
                                dishNameLine1 = dishNameLine1.substring(0, itemColumnLength - 1);

                            }
                            commands.add(CommonUtils.padRight(dishNameLine1, itemColumnLength) + CommonUtils.padRight(CommonUtils.parseTwoDecimal(quantity), qtyColumnLength));
                            if (dishNameLine2.length() > 0) {
                                commands.add(CommonUtils.padRight(dishNameLine2, totalCharLength));
                            }
                            dishNameLine2 = "";

                            for (OrderChildVariant orderChildVariant : orderChild.getVariants()) {
                                for (VariantDetail variantDetail : orderChildVariant.getData()) {
                                    commands.add(CommonUtils.padLeft("-", 5) + CommonUtils.padRight(variantDetail.getText(), itemColumnLength));
                                }
                            }

                            if (quantity < 0) {
                                commands.add("</DH>");
                                commands.add("<B>");
                                commands.add(CommonUtils.padRight("Note: Item/Quantity is modify", totalCharLength));
                                commands.add("</B>");
                                commands.add("<DH>");
                            }
                            if (orderChild.getSpecialInstruction().length() > 0) {
                                commands.add("</DH>");
                                commands.add(CommonUtils.padRight(orderChild.getSpecialInstruction(), totalCharLength));
                                commands.add("<DH>");
                            }
                        }
                    }
                    commands.add("</DH>");
                    commands.add(CommonUtils.repeat("-", totalCharLength));
                    commands.add("<BR>");
                    commands.add("<BR>");
                    commands.add("<BR>");
                    commands.add("<BR>");
                    commands.add("<BR>");
                    commands.add("<BR>");
                    commands.add("<BR>");
                    commands.add("<BR>");
                    commands.add("<CP>");
                }
            }

            printStatus = printRecord(printMode, commands, printNoOfCopies);
            if (isPrintSummary == false) {
                if (printStatus && isReprint == false && printMode == 2) {

                    for (OrderMaster orderMaster : kitchenPrintManager.getOrderMasters()) {
                        for (OrderChild orderChild : orderMaster.getOrderChilds()) {
                            orderChild.setPrintQuantity(orderChild.getQuantity());
                            applicationDAL.addUpdateOrderChild(orderChild);
                        }
                    }
                    UIHelper.showAlertDialog(PrintViewActivity.this, "", "Print Successful.");
                }
            } else {
                isPrintSummary = false;
            }
        }
    }

    private HashMap<String, List<VariantSummary>> getOrderItemVariants(DataTable dataTable) {
        HashMap<String, List<VariantSummary>> orderItemVariants = new HashMap<String, List<VariantSummary>>();

        Gson gson = new Gson();
        String dishId;
        double quantity;
        List<VariantSummary> variantSummaries;
        List<OrderChildVariant> orderChildVariants;

        for (DataRow dataRow : dataTable) {
            if (dataRow.getString("variant_data").equals("[]") == false) {
                dishId = dataRow.getString("dish_id");
                quantity = Double.valueOf(dataRow.getString("quantity"));
                orderChildVariants = gson.fromJson(dataRow.getString("variant_data"), new TypeToken<List<OrderChildVariant>>() {
                }.getType());

                if (orderItemVariants.containsKey(dishId) == false) {
                    variantSummaries = new ArrayList<VariantSummary>();
                    orderItemVariants.put(dishId, variantSummaries);
                } else {
                    variantSummaries = orderItemVariants.get(dishId);
                }

                for (OrderChildVariant orderChildVariant : orderChildVariants) {
                    for (VariantDetail variantDetail : orderChildVariant.getData()) {
                        VariantSummary variantSummary = new VariantSummary();
                        for (VariantSummary vs : variantSummaries) {
                            if (vs.getId().equals(variantDetail.getId())) {
                                variantSummary = vs;
                                break;
                            }
                        }

                        if (variantSummary.getId() == null) {
                            variantSummaries.add(variantSummary);
                        }

                        variantSummary.setId(variantDetail.getId());
                        variantSummary.setText(variantDetail.getText());
                        variantSummary.setPrice(variantDetail.getPrice());
                        variantSummary.setQuantity(variantSummary.getQuantity() + quantity);
                        variantSummary.setAmount(variantSummary.getAmount() + (quantity * variantDetail.getPrice()));
                        variantSummary.setVariantId(orderChildVariant.getId());
                        variantSummary.setVariantKey(orderChildVariant.getKey());
                    }
                }
            }
        }

        return orderItemVariants;
    }

    private void printShiftRegister(String shiftRecordId, String shiftType, int printMode) {
        int totalCharLength = 40;
        int itemColumnLength = 26;
        int qtyColumnLength = 6;
        int amountColumnLength = 8;

        if (paperSize == Printer.PaperSize.Paper_58mm) {
            totalCharLength = 34;
            itemColumnLength = 22;
            qtyColumnLength = 5;
            amountColumnLength = 7;
        }
        DataTable shiftRegisterItems = applicationDAL.getShiftRegisterItems(shiftRecordId);
        DataTable shiftRegisterItemVariants = applicationDAL.getShiftRegisterItemVariants(shiftRecordId);
        HashMap<String, List<VariantSummary>> orderItemVariants = getOrderItemVariants(shiftRegisterItemVariants);
        DataRow shiftRegisterSummary = applicationDAL.getShiftRegisterSummary(shiftRecordId, 2);
        DataTable shiftRegisterCardSummary = applicationDAL.getShiftRegisterCardSummary(shiftRecordId);
        DataTable shiftRegisterDeliverySummary = applicationDAL.getShiftRegisterDeliverySummary(shiftRecordId);
        DataTable shiftRegisterOrderTypeSummary = applicationDAL.getShiftRegisterOrderTypeSummary(shiftRecordId);

        int previousCharLength = 0;
        String categoryName = "";
        String dishNameLine1 = "";
        String dishNameLine2 = "";
        List<String> commands = new ArrayList<String>();

        if (shiftRegisterSummary != null) {

            commands.add("<C>");
            commands.add("<B>");
            if (placeDetail.getPrintLogoName().length() > 0) {
                commands.add("<DW>");
                commands.add(placeDetail.getPrintLogoName());
                commands.add("</DW>");
            }
            commands.add("<BR>");
            commands.add("Shift Wise Sales");
            commands.add(shiftType);
            commands.add("<BR>");
            commands.add("</B>");


            previousCharLength = ("Open Date: " + shiftRegisterSummary.getDate("start_time", "dd-MMM-yyyy")).length();
            commands.add("Open Date: " + shiftRegisterSummary.getDate("start_time", "dd-MMM-yyyy") + CommonUtils.padLeft("Date: " + (shiftRegisterSummary.getLong("finish_time") != 0 ? shiftRegisterSummary.getDate("finish_time", "dd-MMM-yyyy") : ""), totalCharLength - previousCharLength));
            previousCharLength = ("Open Time: " + shiftRegisterSummary.getDate("start_time", "hh:mm aa")).length();
            commands.add("Open Time: " + shiftRegisterSummary.getDate("start_time", "hh:mm aa") + CommonUtils.padLeft("Time: " + (shiftRegisterSummary.getLong("finish_time") != 0 ? shiftRegisterSummary.getDate("finish_time", "hh:mm aa") + "   " : ""), totalCharLength - previousCharLength));
            previousCharLength = ("Open Amount: " + CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("opening_cash"))).length();
            commands.add("Open Amount: " + CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("opening_cash")) + CommonUtils.padLeft("Close Amount: " + CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("closing_cash")), totalCharLength - previousCharLength));
            commands.add("<B>");
            commands.add(CommonUtils.repeat("-", totalCharLength));
            commands.add(CommonUtils.padRight("Qty", qtyColumnLength) + CommonUtils.padRight("Item(s)", itemColumnLength) + CommonUtils.padLeft("Total", amountColumnLength));
            commands.add(CommonUtils.repeat("-", totalCharLength));
            commands.add("</B>");
            for (DataRow dataRow : shiftRegisterItems) {
                if (categoryName.equals(dataRow.getString("category_name")) == false) {
                    categoryName = dataRow.getString("category_name");
                    commands.add("<B>");
                    commands.add(categoryName);
                    commands.add("</B>");
                }
                dishNameLine1 = CommonUtils.padRight(dataRow.getString("dish_name"), itemColumnLength - 1);
                if (dishNameLine1.length() > itemColumnLength - 1) {
                    dishNameLine2 = dishNameLine1.substring(itemColumnLength - 1);
                    dishNameLine1 = dishNameLine1.substring(0, itemColumnLength - 1);

                }
                commands.add(CommonUtils.padRight(dataRow.getString("quantity"), qtyColumnLength) + CommonUtils.padRight(dishNameLine1, itemColumnLength) + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dataRow.getString("amount")), amountColumnLength));

                if (dishNameLine2.length() > 0) {
                    commands.add(CommonUtils.padRight(CommonUtils.padLeft("", 6) + dishNameLine2, totalCharLength));
                }
                dishNameLine2 = "";

                if (orderItemVariants.containsKey(dataRow.getString("dish_id"))) {
                    List<VariantSummary> variantSummaries = orderItemVariants.get(dataRow.getString("dish_id"));
                    for (VariantSummary variantSummary : variantSummaries) {
                        commands.add(CommonUtils.padLeft("(" + CommonUtils.formatTwoDecimal(variantSummary.getQuantity()) + ") ", qtyColumnLength) + CommonUtils.padRight(variantSummary.getText() + " (" + CommonUtils.formatTwoDecimal(variantSummary.getAmount()) + ")", itemColumnLength + amountColumnLength));
                    }
                }

                if (Float.valueOf(dataRow.getString("discount_amount")) > 0) {
                    commands.add(CommonUtils.padLeft("Discount: ", qtyColumnLength + itemColumnLength) + CommonUtils.padLeft("-" + CommonUtils.formatTwoDecimal(dataRow.getString("discount_amount")), amountColumnLength));
                }
            }
            commands.add("<B>");
            commands.add(CommonUtils.repeat("-", totalCharLength));
            if (shiftRegisterSummary.getString("product_discount_amount").equals("0") == false) {
                previousCharLength = ("Product Wise Discount: ").length();
                commands.add("Product Wise Discount: " + CommonUtils.padLeft("-" + CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("product_discount_amount")), totalCharLength - previousCharLength));
            }
            commands.add(CommonUtils.padLeft("Total: " + CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("sub_total_amount")), totalCharLength));
            commands.add("<BR>");
            previousCharLength = ("Discount: ").length();
            commands.add("Discount: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("discount_amount")), totalCharLength - previousCharLength));
            previousCharLength = ("Net Sale: ").length();
            commands.add("Net Sale: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("net_sale")), totalCharLength - previousCharLength));
            if (shiftRegisterSummary.getString("sales_tax_amount").equals("0") == false) {
                previousCharLength = (placeDetail.getGSTTitle() + ": ").length();
                commands.add(placeDetail.getGSTTitle() + ": " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("sales_tax_amount")), totalCharLength - previousCharLength));
            }
            if (shiftRegisterSummary.getString("tip_amount").equals("0") == false) {
                previousCharLength = ("Tip Amount: ").length();
                commands.add("Tip Amount: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("tip_amount")), totalCharLength - previousCharLength));
            }
            if (shiftRegisterSummary.getString("delivery_fee_amount").equals("0") == false) {
                previousCharLength = ("Delivery Fee Amount: ").length();
                commands.add("Delivery Fee Amount: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("delivery_fee_amount")), totalCharLength - previousCharLength));
            }
            commands.add(CommonUtils.repeat("-", totalCharLength));
            commands.add("<DH>");
            previousCharLength = ("Total: ").length();
            commands.add("Total: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("total_amount")), totalCharLength - previousCharLength));
            if (shiftRegisterSummary.getString("delivery_fee_amount").equals("0") == false) {
                previousCharLength = ("Cash without Delivery: ").length();
                commands.add("Cash without Delivery: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getDouble("cash_amount") - shiftRegisterSummary.getDouble("delivery_fee_amount")), totalCharLength - previousCharLength));
            }
            previousCharLength = ("Cash: ").length();
            commands.add("Cash: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("cash_amount")), totalCharLength - previousCharLength));
            previousCharLength = ("Card: ").length();
            commands.add("Card: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("card_amount")), totalCharLength - previousCharLength));
            commands.add("</DH>");
            commands.add(CommonUtils.repeat("-", totalCharLength));
            previousCharLength = ("No. of Cash Transaction: ").length();
            commands.add("No. of Cash Transaction: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("no_of_cash_transactions")), totalCharLength - previousCharLength));
            previousCharLength = ("No. of Card Transaction: ").length();
            commands.add("No. of Card Transaction: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("no_of_card_transactions")), totalCharLength - previousCharLength));
            for (DataRow dataRow : shiftRegisterCardSummary) {
                previousCharLength = ("  -" + dataRow.getString("card_type") + ": ").length();
                commands.add("  -" + dataRow.getString("card_type") + ": " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dataRow.getInt("no_of_transactions")), totalCharLength - previousCharLength));
            }
            commands.add(CommonUtils.repeat("-", totalCharLength));

            int totalOrderTypeTransaction = 0;
            double totalOrderTypeAmount = 0;
            commands.add(CommonUtils.padRight("Order Details", totalCharLength));

            for (DataRow dataRow : shiftRegisterOrderTypeSummary) {
                previousCharLength = ("  -" + dataRow.getString("table_name") + ": ").length();
                commands.add("  -" + dataRow.getString("table_name") + ": " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dataRow.getInt("no_of_transactions")) + " " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dataRow.getFloat("total_amount")), 8), totalCharLength - previousCharLength));
                totalOrderTypeTransaction += dataRow.getInt("no_of_transactions");
                totalOrderTypeAmount += dataRow.getFloat("total_amount");
            }
            commands.add("<DH>");
            previousCharLength = ("Total Orders: ").length();
            commands.add("Total Orders: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(totalOrderTypeTransaction) + " " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(totalOrderTypeAmount), 8), totalCharLength - previousCharLength));
            commands.add("</DH>");
            commands.add(CommonUtils.repeat("-", totalCharLength));

            if (shiftRegisterDeliverySummary.size() > 0) {
                int totalDeliveryTransaction = 0;
                double totalDeliveryAmount = 0;
                commands.add(CommonUtils.padRight("Company Reference", totalCharLength));
                for (DataRow dataRow : shiftRegisterDeliverySummary) {
                    previousCharLength = ("  -" + dataRow.getString("delivery_company_name") + ": ").length();
                    commands.add("  -" + dataRow.getString("delivery_company_name") + ": " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dataRow.getInt("no_of_transactions")) + " " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dataRow.getFloat("total_amount")), 8), totalCharLength - previousCharLength));
                    totalDeliveryTransaction += dataRow.getInt("no_of_transactions");
                    totalDeliveryAmount += dataRow.getFloat("total_amount");
                }
                previousCharLength = ("Total Reference: ").length();
                commands.add("<DH>");
                commands.add("Total Reference: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(totalDeliveryTransaction) + " " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(totalDeliveryAmount), 8), totalCharLength - previousCharLength));
                commands.add("</DH>");
                commands.add(CommonUtils.repeat("-", totalCharLength));
            }

            if (shiftRegisterSummary.getLong("finish_time") == 0) {
                commands.add("<C>");
                commands.add("Shift need to be close");
            }
            commands.add("</B>");
            commands.add("<BR>");
            commands.add("<BR>");
            commands.add("<BR>");
            commands.add("<BR>");
            commands.add("<CP>");

            printRecord(printMode, commands, printNoOfCopies);
        }
    }

    private void printDayRegister(Date shiftDate, int printMode) {
        int totalCharLength = 40;
        int itemColumnLength = 26;
        int qtyColumnLength = 6;
        int amountColumnLength = 8;

        if (paperSize == Printer.PaperSize.Paper_58mm) {
            totalCharLength = 34;
            itemColumnLength = 22;
            qtyColumnLength = 5;
            amountColumnLength = 7;
        }
        DataTable dayRegisterItems = applicationDAL.getDayRegisterItems(shiftDate);
        DataRow dayRegisterSummary = applicationDAL.getDayRegisterSummary(shiftDate);

        int previousCharLength = 0;
        String dishNameLine1 = "";
        String dishNameLine2 = "";
        List<String> commands = new ArrayList<String>();

        if (dayRegisterSummary != null) {

            commands.add("<C>");
            commands.add("<B>");
            if (placeDetail.getPrintLogoName().length() > 0) {
                commands.add("<DW>");
                commands.add(placeDetail.getPrintLogoName());
                commands.add("</DW>");
            }
            commands.add("<BR>");
            commands.add("Day Wise Sales");
            commands.add("<BR>");
            commands.add("</B>");


            commands.add("Shift Date: " + dayRegisterSummary.getDate("start_time", "dd-MMM-yyyy"));
            previousCharLength = ("Open Time: " + dayRegisterSummary.getDate("start_time", "hh:mm aa")).length();
            commands.add("Open Time: " + dayRegisterSummary.getDate("start_time", "hh:mm aa") + CommonUtils.padLeft("Time: " + (dayRegisterSummary.getLong("finish_time") != 0 ? dayRegisterSummary.getDate("finish_time", "hh:mm aa") + "   " : ""), totalCharLength - previousCharLength));
            previousCharLength = ("Open Amount: " + CommonUtils.formatTwoDecimal(dayRegisterSummary.getString("opening_cash"))).length();
            commands.add("Open Amount: " + CommonUtils.formatTwoDecimal(dayRegisterSummary.getString("opening_cash")) + CommonUtils.padLeft("Close Amount: " + CommonUtils.formatTwoDecimal(dayRegisterSummary.getString("closing_cash")), totalCharLength - previousCharLength));
            commands.add("<B>");
            commands.add(CommonUtils.repeat("-", totalCharLength));
            commands.add(CommonUtils.padRight("Qty", qtyColumnLength) + CommonUtils.padRight("Item(s)", itemColumnLength) + CommonUtils.padLeft("Total", amountColumnLength));
            commands.add(CommonUtils.repeat("-", totalCharLength));
            commands.add("</B>");
            for (DataRow dataRow : dayRegisterItems) {
                dishNameLine1 = CommonUtils.padRight(dataRow.getString("dish_name"), itemColumnLength - 1);
                if (dishNameLine1.length() > itemColumnLength - 1) {
                    dishNameLine2 = dishNameLine1.substring(itemColumnLength - 1);
                    dishNameLine1 = dishNameLine1.substring(0, itemColumnLength - 1);

                }
                commands.add(CommonUtils.padRight(dataRow.getString("quantity"), qtyColumnLength) + CommonUtils.padRight(dishNameLine1, itemColumnLength) + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dataRow.getString("amount")), amountColumnLength));
                if (dishNameLine2.length() > 0) {
                    commands.add(CommonUtils.padRight(CommonUtils.padLeft("", 6) + dishNameLine2, totalCharLength));
                }
                dishNameLine2 = "";
                if (Float.valueOf(dataRow.getString("discount_amount")) > 0) {
                    commands.add(CommonUtils.padLeft("Discount: ", qtyColumnLength + itemColumnLength) + CommonUtils.padLeft("-" + CommonUtils.formatTwoDecimal(dataRow.getString("discount_amount")), amountColumnLength));
                }
            }
            commands.add("<B>");
            commands.add(CommonUtils.repeat("-", totalCharLength));
            if (dayRegisterSummary.getString("product_discount_amount").equals("0") == false) {
                previousCharLength = ("Product Wise Discount: ").length();
                commands.add("Product Wise Discount: " + CommonUtils.padLeft("-" + CommonUtils.formatTwoDecimal(dayRegisterSummary.getString("product_discount_amount")), totalCharLength - previousCharLength));
            }
            commands.add(CommonUtils.padLeft("Total: " + CommonUtils.formatTwoDecimal(dayRegisterSummary.getString("sub_total_amount")), totalCharLength));
            commands.add("<BR>");
            previousCharLength = ("Discount: ").length();
            commands.add("Discount: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dayRegisterSummary.getString("discount_amount")), totalCharLength - previousCharLength));
            previousCharLength = ("Gross Sale: ").length();
            commands.add("Gross Sale: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dayRegisterSummary.getString("net_sale")), totalCharLength - previousCharLength));
            if (dayRegisterSummary.getString("sales_tax_amount").equals("0") == false) {
                previousCharLength = (placeDetail.getGSTTitle() + ": ").length();
                commands.add(placeDetail.getGSTTitle() + ": " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dayRegisterSummary.getString("sales_tax_amount")), totalCharLength - previousCharLength));
            }
            if (dayRegisterSummary.getString("tip_amount").equals("0") == false) {
                previousCharLength = ("Tip Amount: ").length();
                commands.add("Tip Amount: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dayRegisterSummary.getString("tip_amount")), totalCharLength - previousCharLength));
            }
            if (dayRegisterSummary.getString("delivery_fee_amount").equals("0") == false) {
                previousCharLength = ("Delivery Fee Amount: ").length();
                commands.add("Delivery Fee Amount: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dayRegisterSummary.getString("delivery_fee_amount")), totalCharLength - previousCharLength));
            }
            commands.add(CommonUtils.repeat("-", totalCharLength));
            commands.add("<DH>");
            previousCharLength = ("Total: ").length();
            commands.add("Total: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dayRegisterSummary.getString("total_amount")), totalCharLength - previousCharLength));
            if (dayRegisterSummary.getString("delivery_fee_amount").equals("0") == false) {
                previousCharLength = ("Cash without Delivery: ").length();
                commands.add("Cash without Delivery: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dayRegisterSummary.getDouble("cash_amount") - dayRegisterSummary.getDouble("delivery_fee_amount")), totalCharLength - previousCharLength));
            }
            previousCharLength = ("Cash: ").length();
            commands.add("Cash: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dayRegisterSummary.getString("cash_amount")), totalCharLength - previousCharLength));
            previousCharLength = ("Card: ").length();
            commands.add("Card: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dayRegisterSummary.getString("card_amount")), totalCharLength - previousCharLength));
            commands.add("</DH>");
            commands.add(CommonUtils.repeat("-", totalCharLength));
            commands.add("</B>");
            commands.add("<BR>");
            commands.add("<BR>");
            commands.add("<BR>");
            commands.add("<BR>");
            commands.add("<CP>");

            printRecord(printMode, commands, printNoOfCopies);
        }
    }

    private void printShiftSummary(String shiftRecordId, String shiftType, int printMode) {
        int totalCharLength = 46;
        int rcptColumnLength = 15;
        int noOfItemColumnLength = 21;
        int amountColumnLength = 10;

        if (paperSize == Printer.PaperSize.Paper_58mm) {
            totalCharLength = 34;
            rcptColumnLength = 10;
            noOfItemColumnLength = 16;
            amountColumnLength = 8;
        }
        DataTable shiftRegisterItemsSummary = applicationDAL.getShiftRegisterItemsSummary(shiftRecordId, 2);
        DataRow shiftRegisterSummary = applicationDAL.getShiftRegisterSummary(shiftRecordId, 2);
        int noOfitems = 0;

        int previousCharLength = 0;

        List<String> commands = new ArrayList<String>();

        if (shiftRegisterSummary != null) {

            commands.add("<C>");
            commands.add("<B>");
            if (placeDetail.getPrintLogoName().length() > 0) {
                commands.add("<DW>");
                commands.add(placeDetail.getPrintLogoName());
                commands.add("</DW>");
            }
            commands.add("<BR>");
            commands.add("Shift Wise Sales Summary");
            commands.add(shiftType);
            commands.add("<BR>");
            commands.add("</B>");


            previousCharLength = ("Open Date: " + shiftRegisterSummary.getDate("start_time", "dd-MMM-yyyy")).length();
            commands.add("Open Date: " + shiftRegisterSummary.getDate("start_time", "dd-MMM-yyyy") + CommonUtils.padLeft("Date: " + (shiftRegisterSummary.getLong("finish_time") != 0 ? shiftRegisterSummary.getDate("finish_time", "dd-MMM-yyyy") : ""), totalCharLength - previousCharLength));
            previousCharLength = ("Open Time: " + shiftRegisterSummary.getDate("start_time", "hh:mm aa")).length();
            commands.add("Open Time: " + shiftRegisterSummary.getDate("start_time", "hh:mm aa") + CommonUtils.padLeft("Time: " + (shiftRegisterSummary.getLong("finish_time") != 0 ? shiftRegisterSummary.getDate("finish_time", "hh:mm aa") + "   " : ""), totalCharLength - previousCharLength));
            previousCharLength = ("Open Amount: " + CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("opening_cash"))).length();
            commands.add("Open Amount: " + CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("opening_cash")) + CommonUtils.padLeft("Close Amount: " + CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("closing_cash")), totalCharLength - previousCharLength));
            commands.add("<B>");
            commands.add(CommonUtils.repeat("-", totalCharLength));
            commands.add(CommonUtils.padRight("Receipt#", rcptColumnLength) + CommonUtils.padRight("No of Item(s)", noOfItemColumnLength) + CommonUtils.padLeft("Total", amountColumnLength));
            commands.add(CommonUtils.repeat("-", totalCharLength));
            commands.add("</B>");
            for (DataRow dataRow : shiftRegisterItemsSummary) {
                noOfitems += dataRow.getInt("no_of_items");
                commands.add(CommonUtils.padRight("Rcpt# " + dataRow.getString("device_receipt_no"), rcptColumnLength) + CommonUtils.padRight(" " + CommonUtils.formatTwoDecimal(dataRow.getInt("no_of_items")), noOfItemColumnLength) + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dataRow.getString("amount")), amountColumnLength));
            }
            commands.add("<B>");
            commands.add(CommonUtils.repeat("-", totalCharLength));
            if (shiftRegisterSummary.getString("product_discount_amount").equals("0") == false) {
                previousCharLength = ("Product Wise Discount: ").length();
                commands.add("Product Wise Discount: " + CommonUtils.padLeft("-" + CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("product_discount_amount")), totalCharLength - previousCharLength));
            }
            commands.add(CommonUtils.padRight("Total:  " + CommonUtils.formatTwoDecimal(shiftRegisterItemsSummary.size()), rcptColumnLength) + CommonUtils.padRight(" " + CommonUtils.formatTwoDecimal(noOfitems), noOfItemColumnLength) + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("sub_total_amount")), amountColumnLength));
            commands.add("<BR>");
            previousCharLength = ("Discount: ").length();
            commands.add("Discount: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("discount_amount")), totalCharLength - previousCharLength));
            previousCharLength = ("Net Sale: ").length();
            commands.add("Net Sale: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("net_sale")), totalCharLength - previousCharLength));
            if (shiftRegisterSummary.getString("sales_tax_amount").equals("0") == false) {
                previousCharLength = ("Sales Tax: ").length();
                commands.add("Sales Tax: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("sales_tax_amount")), totalCharLength - previousCharLength));
            }
            if (shiftRegisterSummary.getString("tip_amount").equals("0") == false) {
                previousCharLength = ("Tip Amount: ").length();
                commands.add("Tip Amount: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("tip_amount")), totalCharLength - previousCharLength));
            }
            if (shiftRegisterSummary.getString("delivery_fee_amount").equals("0") == false) {
                previousCharLength = ("Delivery Fee Amount: ").length();
                commands.add("Delivery Fee Amount: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("delivery_fee_amount")), totalCharLength - previousCharLength));
            }
            commands.add(CommonUtils.repeat("-", totalCharLength));
            commands.add("<DH>");
            previousCharLength = ("Net Total: ").length();
            commands.add("Net Total: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("total_amount")), totalCharLength - previousCharLength));
            if (shiftRegisterSummary.getString("delivery_fee_amount").equals("0") == false) {
                previousCharLength = ("Cash without Delivery: ").length();
                commands.add("Cash without Delivery: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getDouble("cash_amount") - shiftRegisterSummary.getDouble("delivery_fee_amount")), totalCharLength - previousCharLength));
            }
            previousCharLength = ("Cash: ").length();
            commands.add("Cash: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("cash_amount")), totalCharLength - previousCharLength));
            previousCharLength = ("Card: ").length();
            commands.add("Card: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("card_amount")), totalCharLength - previousCharLength));


            float cashInHand = 0;
            cashInHand = (shiftRegisterSummary.getFloat("opening_cash") + shiftRegisterSummary.getFloat("cash_amount"));
            previousCharLength = ("Cash In Hand: ").length();
            commands.add("Cash In Hand: " + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(cashInHand), totalCharLength - previousCharLength));

            commands.add("</DH>");
            commands.add(CommonUtils.repeat("-", totalCharLength));
            if (shiftRegisterSummary.getLong("finish_time") == 0) {
                commands.add("<C>");
                commands.add("Shift need to be close");
            }
            commands.add("</B>");
            commands.add("<BR>");
            commands.add("<BR>");
            commands.add("<BR>");
            commands.add("<BR>");
            commands.add("<CP>");

            printRecord(printMode, commands, printNoOfCopies);
        }

    }

    private void printVoidReport(String shiftRecordId, String shiftType, int printMode) {
        int totalCharLength = 46;
        int rcptColumnLength = 15;
        int noOfItemColumnLength = 21;
        int amountColumnLength = 10;

        if (paperSize == Printer.PaperSize.Paper_58mm) {
            totalCharLength = 34;
            rcptColumnLength = 10;
            noOfItemColumnLength = 16;
            amountColumnLength = 8;
        }
        DataTable shiftRegisterItemsSummary = applicationDAL.getShiftRegisterItemsSummary(shiftRecordId, 3);
        DataRow shiftRegisterSummary = applicationDAL.getShiftRegisterSummary(shiftRecordId, 3);
        int noOfitems = 0;

        int previousCharLength = 0;

        List<String> commands = new ArrayList<String>();

        if (shiftRegisterSummary != null) {

            commands.add("<C>");
            commands.add("<B>");
            if (placeDetail.getPrintLogoName().length() > 0) {
                commands.add("<DW>");
                commands.add(placeDetail.getPrintLogoName());
                commands.add("</DW>");
            }
            commands.add("<BR>");
            commands.add("Void Report");
            commands.add(shiftType);
            commands.add("<BR>");
            commands.add("</B>");


            previousCharLength = ("Open Date: " + shiftRegisterSummary.getDate("start_time", "dd-MMM-yyyy")).length();
            commands.add("Open Date: " + shiftRegisterSummary.getDate("start_time", "dd-MMM-yyyy") + CommonUtils.padLeft("Date: " + (shiftRegisterSummary.getLong("finish_time") != 0 ? shiftRegisterSummary.getDate("finish_time", "dd-MMM-yyyy") : ""), totalCharLength - previousCharLength));
            previousCharLength = ("Open Time: " + shiftRegisterSummary.getDate("start_time", "hh:mm aa")).length();
            commands.add("Open Time: " + shiftRegisterSummary.getDate("start_time", "hh:mm aa") + CommonUtils.padLeft("Time: " + (shiftRegisterSummary.getLong("finish_time") != 0 ? shiftRegisterSummary.getDate("finish_time", "hh:mm aa") + "   " : ""), totalCharLength - previousCharLength));
            commands.add("<B>");
            commands.add(CommonUtils.repeat("-", totalCharLength));
            commands.add(CommonUtils.padRight("Receipt#", rcptColumnLength) + CommonUtils.padRight("No of Item(s)", noOfItemColumnLength) + CommonUtils.padLeft("Total", amountColumnLength));
            commands.add(CommonUtils.repeat("-", totalCharLength));
            commands.add("</B>");
            for (DataRow dataRow : shiftRegisterItemsSummary) {
                noOfitems += dataRow.getInt("no_of_items");
                commands.add(CommonUtils.padRight("Rcpt# " + dataRow.getString("device_receipt_no"), rcptColumnLength) + CommonUtils.padRight(" " + CommonUtils.formatTwoDecimal(dataRow.getInt("no_of_items")), noOfItemColumnLength) + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(dataRow.getString("amount")), amountColumnLength));
            }
            commands.add("<B>");
            commands.add(CommonUtils.repeat("-", totalCharLength));
            if (shiftRegisterSummary.getString("product_discount_amount").equals("0") == false) {
                previousCharLength = ("Product Wise Discount: ").length();
                commands.add("Product Wise Discount: " + CommonUtils.padLeft("-" + CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("product_discount_amount")), totalCharLength - previousCharLength));
            }
            commands.add(CommonUtils.padRight("Total:  " + CommonUtils.formatTwoDecimal(shiftRegisterItemsSummary.size()), rcptColumnLength) + CommonUtils.padRight(" " + CommonUtils.formatTwoDecimal(noOfitems), noOfItemColumnLength) + CommonUtils.padLeft(CommonUtils.formatTwoDecimal(shiftRegisterSummary.getString("sub_total_amount")), amountColumnLength));
            commands.add(CommonUtils.repeat("-", totalCharLength));
            if (shiftRegisterSummary.getLong("finish_time") == 0) {
                commands.add("<C>");
                commands.add("Shift need to be close");
            }
            commands.add("</B>");
            commands.add("<BR>");
            commands.add("<BR>");
            commands.add("<BR>");
            commands.add("<BR>");
            commands.add("<CP>");

            printRecord(printMode, commands, printNoOfCopies);
        }

    }

    private boolean printRecord(int printMode, List<String> commands, int noOfCopies) {
        boolean status = false;

        if (printMode == 1 || printMode == 3) {
            status = previewRecord(commands, noOfCopies);
        } else if (printMode == 2) {
            if (printerDetail.getPrinterBrand().equals("BayLan") || printerDetail.getPrinterBrand().equals("BlackCopper")) {
                status = WifiPrintRecord(printerDetail.getPrinterIp(), printerDetail.getPort(), commands, noOfCopies);
            } else if (printerDetail.getPrinterBrand().equals("Epson")) {
                status = EpsonPrintRecord(printerDetail.getPrinterIp(), printerDetail.getPort(), commands, noOfCopies);
            } else if (printerDetail.getPrinterBrand().equals("Sunmi")) {
                status = SunmiV1PrintRecord(commands);
            }

            if (status == false) {
                printFailed();
            }
        }
        return status;
    }

    StringBuilder sbPreview = new StringBuilder();

    private boolean previewRecord(List<String> commands, int noOfCopies) {

        String alignTagOpen = "";
        String alignTagClose = "";
        String imageName = "";

        for (int i = 0; i < noOfCopies; i++) {

            for (String command : commands) {
                String formattedCommand = command.toUpperCase();

                if (formattedCommand.equals("<L>")) {
                    sbPreview.append(alignTagClose);
                    alignTagOpen = "<left>";
                    alignTagClose = "</left>";
                    sbPreview.append(alignTagOpen);
                } else if (formattedCommand.equals("<C>")) {
                    sbPreview.append(alignTagClose);
                    alignTagOpen = "<center>";
                    alignTagClose = "</center>";
                    sbPreview.append(alignTagOpen);
                } else if (formattedCommand.equals("<R>")) {

                } else if (formattedCommand.equals("<DW>")) {
                    sbPreview.append("<font size=\"5\">");
                } else if (formattedCommand.equals("</DW>")) {
                    sbPreview.append("</font>");
                } else if (formattedCommand.equals("<DH>")) {

                } else if (formattedCommand.equals("</DH>")) {

                } else if (formattedCommand.equals("<B>")) {
                    sbPreview.append("<b>");
                } else if (formattedCommand.equals("</B>")) {
                    sbPreview.append("</b>");
                } else if (formattedCommand.equals("<SC>")) {

                } else if (formattedCommand.equals("</SC>")) {

                } else if (formattedCommand.equals("<BR>")) {
                    sbPreview.append("<br>");
                } else if (formattedCommand.equals("<CP>")) {

                } else if (formattedCommand.startsWith("<IMG>")) {
                    imageName = command.replace("<IMG>", "").replace("</IMG>", "");
                    //imageName = "file://" + Environment.getExternalStorageDirectory().getPath() + imageName;
                    sbPreview.append("<img src='" + imageName + "'/>");
                    sbPreview.append("<br>");
                } else if (formattedCommand.startsWith("<BARCODE>")) {

                } else {
                    sbPreview.append(command);
                    sbPreview.append("<br>");
                }
            }
        }

        if (alignTagClose.length() > 0) {
            sbPreview.append(alignTagClose);
        }

        wvPrintPreview.loadDataWithBaseURL("", "<html><body><pre>" + sbPreview.toString() + "</pre></body></html>", "text/html; charset=utf-8", "UTF-8", "");

        isPrintRequest = false;
        return true;
    }


    private boolean WifiPrintRecord(String printerIp, int port, List<String> commands, int noOfCopies) {
        boolean status = false;

        try {
            WifiThermalPrinter printer = new WifiThermalPrinter();

            boolean isConnected = printer.connect(printerIp, port);
            if (isConnected) {
                String formattedCommand = "";
                String imageName = "";
                String barcode = "";

                printer.initializeTextMsg();

                for (int i = 0; i < noOfCopies; i++) {

                    for (String command : commands) {
                        formattedCommand = command.toUpperCase();

                        if (formattedCommand.equals("<L>")) {
                            printer.setTextAlign(Printer.TextAlign.ALIGN_LEFT);
                        } else if (formattedCommand.equals("<C>")) {
                            printer.setTextAlign(Printer.TextAlign.ALIGN_CENTER);
                        } else if (formattedCommand.equals("<R>")) {
                            printer.setTextAlign(Printer.TextAlign.ALIGN_RIGHT);
                        } else if (formattedCommand.equals("<DW>")) {
                            printer.doubleWidthOn();
                        } else if (formattedCommand.equals("</DW>")) {
                            printer.doubleWidthOff();
                        } else if (formattedCommand.equals("<DH>")) {
                            printer.doubleHeightOn();
                        } else if (formattedCommand.equals("</DH>")) {
                            printer.doubleHeightOff();
                        } else if (formattedCommand.equals("<B>")) {
                            printer.boldOn();
                        } else if (formattedCommand.equals("</B>")) {
                            printer.boldOff();
                        } else if (formattedCommand.equals("<I>")) {
                            printer.italicOn();
                        } else if (formattedCommand.equals("</I>")) {
                            printer.italicOff();
                        } else if (formattedCommand.equals("<U>")) {
                            printer.underlineOn();
                        } else if (formattedCommand.equals("</U>")) {
                            printer.underlineOff();
                        } else if (formattedCommand.equals("<SC>")) {
                            printer.smallCharacterOn();
                        } else if (formattedCommand.equals("</SC>")) {
                            printer.smallCharacterOff();
                        } else if (formattedCommand.equals("<BR>")) {
                            printer.addNewLine();
                        } else if (formattedCommand.equals("<CP>")) {
                            printer.cutPaper();
                        } else if (formattedCommand.equals("<OCD>")) {
                            printer.openCashDrawer();
                        } else if (formattedCommand.startsWith("<IMG>")) {
                            imageName = command.replace("<IMG>", "").replace("</IMG>", "");
                            //imageName = Environment.getExternalStorageDirectory().getPath() + imageName;
                            //Bitmap bitmap = BitmapFactory.decodeFile(imageName);

                            Bitmap bitmap = loadBitmap(imageName);
                            if (bitmap != null) {
                                printer.printImage(bitmap);
                            }
                        } else if (formattedCommand.startsWith("<BARCODE>")) {
                            barcode = command.replace("<BARCODE>", "").replace("</BARCODE>", "");
                            printer.printBarcode(GetBarcodeType(barcode), GetBarcodeText(barcode));
                        } else {
                            printer.writeLine(command);
                        }
                    }
                }

                printer.writeTextMsgAsync();

                printer.disconnect();

                status = true;

                printSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    private boolean EpsonPrintRecord(String printerIp, int port, List<String> commands, int noOfCopies) {
        boolean status = false;

        try {
            EpsonThermalPrinter printer = new EpsonThermalPrinter(this, Integer.valueOf(printerModel.getModelValue()));

            boolean isConnected = printer.connect(printerIp, port);
            if (isConnected) {
                String formattedCommand = "";
                String imageName = "";
                String barcode = "";

                for (int i = 0; i < noOfCopies; i++) {

                    for (String command : commands) {
                        formattedCommand = command.toUpperCase();

                        if (formattedCommand.equals("<L>")) {
                            printer.setTextAlign(Printer.TextAlign.ALIGN_LEFT);
                        } else if (formattedCommand.equals("<C>")) {
                            printer.setTextAlign(Printer.TextAlign.ALIGN_CENTER);
                        } else if (formattedCommand.equals("<R>")) {
                            printer.setTextAlign(Printer.TextAlign.ALIGN_RIGHT);
                        } else if (formattedCommand.equals("<DW>")) {
                            printer.doubleWidthOn();
                        } else if (formattedCommand.equals("</DW>")) {
                            printer.doubleWidthOff();
                        } else if (formattedCommand.equals("<DH>")) {
                            printer.doubleHeightOn();
                        } else if (formattedCommand.equals("</DH>")) {
                            printer.doubleHeightOff();
                        } else if (formattedCommand.equals("<B>")) {
                            printer.boldOn();
                        } else if (formattedCommand.equals("</B>")) {
                            printer.boldOff();
                        } else if (formattedCommand.equals("<I>")) {
                            printer.italicOn();
                        } else if (formattedCommand.equals("</I>")) {
                            printer.italicOff();
                        } else if (formattedCommand.equals("<U>")) {
                            printer.underlineOn();
                        } else if (formattedCommand.equals("</U>")) {
                            printer.underlineOff();
                        } else if (formattedCommand.equals("<SC>")) {
                            printer.smallCharacterOn();
                        } else if (formattedCommand.equals("</SC>")) {
                            printer.smallCharacterOff();
                        } else if (formattedCommand.equals("<BR>")) {
                            printer.addNewLine();
                        } else if (formattedCommand.equals("<CP>")) {
                            printer.cutPaper();
                        } else if (formattedCommand.startsWith("<IMG>")) {
                            imageName = command.replace("<IMG>", "").replace("</IMG>", "");
                            //imageName = Environment.getExternalStorageDirectory().getPath() + imageName;
                            //Bitmap bitmap = BitmapFactory.decodeFile(imageName);

                            Bitmap bitmap = loadBitmap(imageName);
                            if (bitmap != null) {
                                printer.printImage(bitmap);
                            }
                        } else if (formattedCommand.startsWith("<BARCODE>")) {
                            //barcode = command.replace("<BARCODE>", "").replace("</BARCODE>", "");

                        } else {
                            printer.writeLine(command);
                        }
                    }

                    printer.senData();
                }

                printer.disconnect();

                status = true;

                printSuccess();

            } else {
                if (printer.getLastErrorMsg().equals("") == false) {
                    UIHelper.showShortToast(this, printer.getLastErrorMsg() + " Print Failed.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }


    private boolean SunmiV1PrintRecord(final List<String> commands) {
        boolean status = false;

        try {
            final SunmiV1Printer printer = new SunmiV1Printer(this);

            printer.setOnPrinterStateChangeListener(new Printer.OnPrinterStateChangeListener() {
                @Override
                public void onStateChange(Printer.PrinterState printerState) {
                    if (printerState == printerState.CONNECTED) {
                        String formattedCommand = "";

                        printer.setFontSize(22);

                        for (String command : commands) {
                            formattedCommand = command.toUpperCase();

                            if (formattedCommand.equals("<L>")) {
                                printer.setTextAlign(Printer.TextAlign.ALIGN_LEFT);
                            } else if (formattedCommand.equals("<C>")) {
                                printer.setTextAlign(Printer.TextAlign.ALIGN_CENTER);
                            } else if (formattedCommand.equals("<R>")) {
                                printer.setTextAlign(Printer.TextAlign.ALIGN_RIGHT);
                            } else if (formattedCommand.equals("<DW>")) {
                                printer.setFontSize(30);
                            } else if (formattedCommand.equals("</DW>")) {
                                printer.setFontSize(22);
                            } else if (formattedCommand.equals("<DH>")) {
                                printer.setFontSize(30);
                            } else if (formattedCommand.equals("</DH>")) {
                                printer.setFontSize(22);
                            } else if (formattedCommand.equals("<B>")) {
                                printer.boldOn();
                            } else if (formattedCommand.equals("</B>")) {
                                printer.boldOff();
                            } else if (formattedCommand.equals("<I>")) {

                            } else if (formattedCommand.equals("</I>")) {

                            } else if (formattedCommand.equals("<U>")) {
                                printer.underlineOn();
                            } else if (formattedCommand.equals("</U>")) {
                                printer.underlineOff();
                            } else if (formattedCommand.equals("<SC>")) {
                                printer.setFontSize(19);
                            } else if (formattedCommand.equals("</SC>")) {
                                printer.setFontSize(22);
                            } else if (formattedCommand.equals("<BR>")) {
                                printer.addNewLine();
                            } else if (formattedCommand.equals("<CP>")) {

                            } else if (formattedCommand.startsWith("<IMG>")) {

                            } else if (formattedCommand.startsWith("<BARCODE>")) {

                            } else {
                                printer.writeLine(command);
                            }
                        }

                        printer.disconnect();

                        printSuccess();
                    } else if (printerState == printerState.FAILED) {
                        printFailed();
                    }
                }
            });

            printer.connectAsync();

            status = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }


    private Printer.BarcodeType GetBarcodeType(String BarcodeText) {
        Printer.BarcodeType barcodeType = Printer.BarcodeType.QR_CODE;
        if (BarcodeText.startsWith("<CODE39>")) {
            barcodeType = Printer.BarcodeType.CODE39;
        } else if (BarcodeText.startsWith("<CODE128>")) {
            barcodeType = Printer.BarcodeType.CODE128;
        } else if (BarcodeText.startsWith("<CODABAR>")) {
            barcodeType = Printer.BarcodeType.CODABAR;
        }
        return barcodeType;
    }


    private Bitmap loadBitmap(final String url) {
        Bitmap bitmap = null;
        bitmap = new BackgroundRequest<String, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(PrintViewActivity.this)
                            .asBitmap()
                            .load(url)
                            .submit().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }
        }.executeAndWait();

        return bitmap;
    }

    private String GetBarcodeText(String BarcodeText) {
        return BarcodeText.replace("<QRCODE>", "").replace("</QRCODE>", "").replace("<CODE39>", "").replace("</CODE39>", "").replace("<CODE128>", "").replace("</CODE128>", "").replace("<CODABAR>", "").replace("</CODABAR>", "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class KitchenPrintManager {
        private PrinterDetail printerDetail;
        private List<OrderMaster> orderMasters = new ArrayList<OrderMaster>();

        public PrinterDetail getPrinterDetail() {
            return printerDetail;
        }

        public void setPrinterDetail(PrinterDetail printerDetail) {
            this.printerDetail = printerDetail;
        }

        public List<OrderMaster> getOrderMasters() {
            return orderMasters;
        }

        public void setOrderMasters(List<OrderMaster> orderMasters) {
            this.orderMasters = orderMasters;
        }
    }

    public class VariantSummary {
        private String VariantKey;
        private String VariantId;
        private String Id;
        private String Text;
        private double Quantity;
        private double Price;
        private double Amount;

        public String getVariantKey() {
            return VariantKey;
        }

        public void setVariantKey(String variantKey) {
            VariantKey = variantKey;
        }

        public String getVariantId() {
            return VariantId;
        }

        public void setVariantId(String variantId) {
            VariantId = variantId;
        }

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getText() {
            return Text;
        }

        public void setText(String text) {
            Text = text;
        }

        public double getQuantity() {
            return Quantity;
        }

        public void setQuantity(double quantity) {
            Quantity = quantity;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double price) {
            Price = price;
        }

        public double getAmount() {
            return Amount;
        }

        public void setAmount(double amount) {
            Amount = amount;
        }
    }

}
