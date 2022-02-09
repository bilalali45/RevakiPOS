package com.revaki.revakipos;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.revaki.revakipos.beans.DishDetail;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.db.DBHelper;
import com.revaki.revakipos.helper.CommonUtils;
import com.revaki.revakipos.job.SyncMasterData;
import com.revaki.revakipos.widget.RoundCornerImageView;


import java.util.List;

public class DishListActivity extends AppCompatActivity {

    private ApplicationDAL applicationDAL;

    private SearchView svDish;
    private ListView listDish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_list);

        getSupportActionBar().setTitle("Dishes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        svDish = findViewById(R.id.svDish);
        listDish = findViewById(R.id.listDish);

        applicationDAL = new ApplicationDAL(this);


        svDish.setActivated(true);
        svDish.setQueryHint("Search here");
        svDish.onActionViewExpanded();
        svDish.setIconified(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                svDish.clearFocus();
            }
        }, 200);


        View closeButton = svDish.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svDish.setQuery("", false);
                svDish.clearFocus();

                loadDishes("");
            }
        });


        svDish.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                loadDishes(newText.trim().toLowerCase());
                return false;
            }
        });

        loadDishes("");
    }

    private void loadDishes(String dishName) {
        List<DishDetail> dishDetailList = applicationDAL.getDishDetailsWithCategory(DBHelper.COL_DISH_DETAIL_DISH_NAME + " like '%" + dishName + "%'", "");

        DishDetailAdapter dishDetailAdapter = new DishDetailAdapter(this, dishDetailList);
        listDish.setAdapter(dishDetailAdapter);
    }

    public class DishDetailAdapter extends BaseAdapter {
        Context context;
        private List<DishDetail> dishDetailList;

        public DishDetailAdapter(Context context, List<DishDetail> dishDetailList) {
            this.context = context;
            this.dishDetailList = dishDetailList;
        }

        @Override
        public int getCount() {
            return dishDetailList.size();
        }

        @Override
        public Object getItem(int position) {
            return dishDetailList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.dish_row, null);

                RoundCornerImageView ivDishImage = convertView.findViewById(R.id.ivDishImage);
                TextView tvDishName = convertView.findViewById(R.id.tvDishName);
                TextView tvCategoryName = convertView.findViewById(R.id.tvCategoryName);
                TextView tvDishPrice = convertView.findViewById(R.id.tvDishPrice);

                viewHolder = new DishDetailAdapter.ViewHolder(ivDishImage, tvDishName, tvCategoryName, tvDishPrice);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Glide.with(context)
                    .load(dishDetailList.get(position).getImageURL())
                    .centerCrop()
                    .placeholder(R.drawable.image_loading)
                    .into(viewHolder.ivDishImage);

            viewHolder.tvDishName.setText(dishDetailList.get(position).getDishName());
            viewHolder.tvCategoryName.setText(dishDetailList.get(position).getCategoryName());
            viewHolder.tvDishPrice.setText("Rs. " + CommonUtils.formatTwoDecimal(dishDetailList.get(position).getTotalPrice()));


            return convertView;
        }


        public class ViewHolder {
            RoundCornerImageView ivDishImage;
            TextView tvDishName;
            TextView tvCategoryName;
            TextView tvDishPrice;

            public ViewHolder(RoundCornerImageView ivDishImage, TextView tvDishName, TextView tvCategoryName, TextView tvDishPrice) {
                this.ivDishImage = ivDishImage;
                this.tvDishName = tvDishName;
                this.tvCategoryName = tvCategoryName;
                this.tvDishPrice = tvDishPrice;

            }
        }
    }

    private void sync() {
        SyncMasterData syncMasterData = new SyncMasterData(this);
        syncMasterData.setOnSyncListener(new SyncMasterData.OnSyncListener() {
            @Override
            public void onSyncStart() {
                lockScreenOrientation();
            }

            @Override
            public void onSyncCompleted(boolean isSuccess,String tableName) {
                unlockScreenOrientation();
                if (isSuccess) {
                    loadDishes("");
                }
            }
        });

        syncMasterData.sync(Configuration.getPlaceId(), DBHelper.TABLE_DISH_DETAIL);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sync_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_sync) {
            sync();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
