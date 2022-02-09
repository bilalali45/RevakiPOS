package com.revaki.revakipos;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.revaki.revakipos.beans.CategoryDetail;
import com.revaki.revakipos.beans.PrinterDetail;
import com.revaki.revakipos.beans.SettingDetail;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.db.DBHelper;
import com.revaki.revakipos.widget.RoundCornerImageView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class KitchenPrinterCategoryActivity extends AppCompatActivity {


    private ApplicationDAL applicationDAL;

    private SearchView svCategory;
    private ListView listCategory;

    List<CategoryDetail> categoryDetailList = null;
    CategoryDetailAdapter categoryDetailAdapter = null;
    private List<PrinterDetail> printerDetails = null;
    private SettingDetail settingDetail = null;

    private HashMap<String, String> kitchenPrinterCategories = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_printer_category);

        getSupportActionBar().setTitle("Kitchen Printer Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        svCategory = findViewById(R.id.svCategory);
        listCategory = findViewById(R.id.listCategory);

        applicationDAL = new ApplicationDAL(this);


        svCategory.setActivated(true);
        svCategory.setQueryHint("Search here");
        svCategory.onActionViewExpanded();
        svCategory.setIconified(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                svCategory.clearFocus();
            }
        }, 200);


        View closeButton = svCategory.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svCategory.setQuery("", false);
                svCategory.clearFocus();

                loadCategories("");
            }
        });


        svCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                loadCategories(newText.trim().toLowerCase());
                return false;
            }
        });

        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                int selectedKitchenPrinterIndex = 0;
                for (int i = 0; i < printerDetails.size(); i++) {
                    if (printerDetails.get(i).getPrinterId().equals(categoryDetailList.get(position).getType())) {
                        selectedKitchenPrinterIndex = i;
                        break;
                    }
                }

                ArrayAdapter<PrinterDetail> printerDetailArrayAdapter = new ArrayAdapter<PrinterDetail>(KitchenPrinterCategoryActivity.this, android.R.layout.simple_list_item_single_choice, printerDetails);

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(KitchenPrinterCategoryActivity.this);
                builder.setTitle("Select Printer");
                builder.setSingleChoiceItems(printerDetailArrayAdapter, selectedKitchenPrinterIndex, null);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (printerDetails.size() > 0) {
                            int selectedKitchenPrinterIndex = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                            kitchenPrinterCategories.put(categoryDetailList.get(position).getCategoryId(), printerDetails.get(selectedKitchenPrinterIndex).getPrinterId());
                            categoryDetailList.get(position).setType(printerDetails.get(selectedKitchenPrinterIndex).getPrinterId());
                            categoryDetailList.get(position).setDiscription(printerDetails.get(selectedKitchenPrinterIndex).getTitle());
                            categoryDetailAdapter.notifyDataSetChanged();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", null);

                builder.show();
            }
        });

        printerDetails = applicationDAL.getPrinterDetails("", "");
        settingDetail = applicationDAL.getSettingDetail();

        Type type;
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        type = new TypeToken<HashMap<String, String>>() {
        }.getType();

        kitchenPrinterCategories = gson.fromJson(settingDetail.getKitchenPrinterCategories(), type);

        loadCategories("");
    }


    private PrinterDetail getPrinterDetailById(String printerId) {
        PrinterDetail printerDetail = null;
        for (int i = 0; i < printerDetails.size(); i++) {
            if (printerDetails.get(i).getPrinterId().equals(printerId)) {
                printerDetail = printerDetails.get(i);
                break;
            }
        }
        return printerDetail;
    }

    private void loadCategories(String categoryName) {
        categoryDetailList = applicationDAL.getCategoryDetails(DBHelper.COL_CATEGORY_DETAIL_CATEGORY_NAME + " like '%" + categoryName + "%'", "");

        for (CategoryDetail categoryDetail : categoryDetailList) {
            categoryDetail.setType("");
            categoryDetail.setDiscription("");

            if (kitchenPrinterCategories.containsKey(categoryDetail.getCategoryId())) {
                String printerId = kitchenPrinterCategories.get(categoryDetail.getCategoryId());
                PrinterDetail printerDetail = getPrinterDetailById(printerId);
                if (printerDetail != null) {
                    categoryDetail.setType(printerId);
                    categoryDetail.setDiscription(printerDetail.getTitle());
                }
            }
        }

        categoryDetailAdapter = new CategoryDetailAdapter(this, categoryDetailList);
        listCategory.setAdapter(categoryDetailAdapter);
    }

    public class CategoryDetailAdapter extends BaseAdapter {
        Context context;
        private List<CategoryDetail> categoryDetailList;

        public CategoryDetailAdapter(Context context, List<CategoryDetail> categoryDetailList) {
            this.context = context;
            this.categoryDetailList = categoryDetailList;
        }

        @Override
        public int getCount() {
            return categoryDetailList.size();
        }

        @Override
        public Object getItem(int position) {
            return categoryDetailList.get(position);
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
                convertView = layoutInflater.inflate(R.layout.category_row, null);

                RoundCornerImageView ivCategoryImage = convertView.findViewById(R.id.ivCategoryImage);
                TextView tvCategoryName = convertView.findViewById(R.id.tvCategoryName);
                TextView tvSubTitle = convertView.findViewById(R.id.tvSubTitle);

                viewHolder = new CategoryDetailAdapter.ViewHolder(ivCategoryImage, tvCategoryName, tvSubTitle);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Glide.with(context)
                    .load(categoryDetailList.get(position).getImageURL())
                    .centerCrop()
                    .placeholder(R.drawable.image_loading)
                    .into(viewHolder.ivCategoryImage);

            viewHolder.tvCategoryName.setText(categoryDetailList.get(position).getCategoryName());
            viewHolder.tvSubTitle.setText(categoryDetailList.get(position).getDiscription());

            return convertView;
        }


        public class ViewHolder {
            RoundCornerImageView ivCategoryImage;
            TextView tvCategoryName;
            TextView tvSubTitle;

            public ViewHolder(RoundCornerImageView ivCategoryImage, TextView tvCategoryName, TextView tvSubTitle) {
                this.ivCategoryImage = ivCategoryImage;
                this.tvCategoryName = tvCategoryName;
                this.tvSubTitle = tvSubTitle;
            }
        }
    }

    @Override
    public void onBackPressed() {

        Type type;
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        type = new TypeToken<HashMap<String, String>>() {
        }.getType();

        String kitchenPrinterCategoriesData = gson.toJson(kitchenPrinterCategories, type);

        settingDetail.setKitchenPrinterCategories(kitchenPrinterCategoriesData);

        applicationDAL.addUpdateSettingDetail(settingDetail);
        super.onBackPressed();
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


}
