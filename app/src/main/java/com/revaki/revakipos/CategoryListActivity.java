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
import com.revaki.revakipos.beans.CategoryDetail;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.db.DBHelper;
import com.revaki.revakipos.job.SyncMasterData;

import com.revaki.revakipos.widget.RoundCornerImageView;

import java.util.List;

public class CategoryListActivity extends AppCompatActivity {

    private ApplicationDAL applicationDAL;

    private SearchView svCategory;
    private ListView listCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        getSupportActionBar().setTitle("Categories");
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

        loadCategories("");
    }

    private void loadCategories(String categoryName) {
        List<CategoryDetail> categoryDetailList = applicationDAL.getCategoryDetails(DBHelper.COL_CATEGORY_DETAIL_CATEGORY_NAME + " like '%" + categoryName + "%'", "");

        CategoryDetailAdapter categoryDetailAdapter = new CategoryDetailAdapter(this, categoryDetailList);
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

                viewHolder = new CategoryDetailAdapter.ViewHolder(ivCategoryImage, tvCategoryName);
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


            return convertView;
        }


        public class ViewHolder {
            RoundCornerImageView ivCategoryImage;
            TextView tvCategoryName;

            public ViewHolder(RoundCornerImageView ivCategoryImage, TextView tvCategoryName) {
                this.ivCategoryImage = ivCategoryImage;
                this.tvCategoryName = tvCategoryName;


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
                    loadCategories("");
                }
            }
        });

        syncMasterData.sync(Configuration.getPlaceId(), DBHelper.TABLE_CATEGORY_DETAIL);
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
