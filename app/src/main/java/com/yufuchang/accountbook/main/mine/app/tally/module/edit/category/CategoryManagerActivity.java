package com.yufuchang.accountbook.main.mine.app.tally.module.edit.category;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;


import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.base.utils.UIUtils;
import com.yufuchang.accountbook.base.widget.MTabLayout;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.CategoryModel;
import com.yufuchang.accountbook.main.mine.ui.BaseActivity;
import com.yufuchang.accountbook.main.mine.ui.widget.recyclerview.ItemMarginDecoration;
import com.yufuchang.accountbook.module.edit.category.CategoryManagerActivityBinding;
import com.yufuchang.accountbook.module.edit.category.ItemManagerCategoryBinding;

import java.util.ArrayList;
import java.util.List;



public class CategoryManagerActivity extends BaseActivity {

    static final String EXTRA_CATEGORY_TYPE = "extra_category_type";

    private CategoryManagerActivityBinding mBinding;
    private CategoryManagerViewModel mViewModel;
    private CategoryListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.tally_module_edit_category_activity_manager);
        mViewModel = ViewModelProviders.of(this).get(CategoryManagerViewModel.class);
        getLifecycle().addObserver(mViewModel);
        initView();
        subscribeUi();
    }

    /**
     * 打开分类管理页面
     *
     * @param context      context
     * @param categoryType 分类类型
     *                     {@link CategoryModel#TYPE_EXPENSE}
     *                     {@link CategoryModel#TYPE_INCOME}
     */
    public static void open(Context context, int categoryType) {
        Intent intent = new Intent(context, CategoryManagerActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(EXTRA_CATEGORY_TYPE, categoryType);
        context.startActivity(intent);
    }

    private void initView() {
        setToolbarAsBack(v -> onBackPressed());

        int categoryType = getIntent().getIntExtra(EXTRA_CATEGORY_TYPE, CategoryModel.TYPE_EXPENSE);

        MTabLayout tabLayout = mBinding.tabLayout;
        MTabLayout.Tab tabExpense = tabLayout.newTab();
        MTabLayout.Tab tabIncome = tabLayout.newTab();
        tabExpense.setText(R.string.expense_category);
        tabIncome.setText(R.string.income_category);
        tabLayout.addTab(tabExpense, false);
        tabLayout.addTab(tabIncome, false);
        tabLayout.addOnTabSelectedListener(new MTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(MTabLayout.Tab tab) {
                if (tab == tabExpense) {
                    mViewModel.onTypeChange(CategoryModel.TYPE_EXPENSE);
                }
                if (tab == tabIncome) {
                    mViewModel.onTypeChange(CategoryModel.TYPE_INCOME);
                }
            }

            @Override
            public void onTabUnselected(MTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(MTabLayout.Tab tab) {

            }
        });
        if (categoryType == CategoryModel.TYPE_EXPENSE) {
            tabExpense.select();
        }
        if (categoryType == CategoryModel.TYPE_INCOME) {
            tabIncome.select();
        }

        RecyclerView recyclerView = mBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(self(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new CategoryListAdapter();
        recyclerView.setAdapter(mAdapter);
        int padding4 = UIUtils.dp2px(self(), 4);
        int padding16 = UIUtils.dp2px(self(), 16);
        int padding80 = UIUtils.dp2px(self(), 80);
        ItemMarginDecoration itemMarginDecoration = new ItemMarginDecoration(
                padding16, padding4, padding16, padding4);
        itemMarginDecoration.setFirstItemOffset(padding16, padding16, padding16, padding4);
        itemMarginDecoration.setLastItemOffset(padding16, padding4, padding16, padding80);
        recyclerView.addItemDecoration(itemMarginDecoration);
    }

    private void subscribeUi() {
        mBinding.setVm(mViewModel);
        mViewModel.getCategoryList().observe(this, categoryList -> {
            mAdapter.setDataList(categoryList);
        });
        mViewModel.getViewReliedTask().observe(this, task -> {
            if (task != null) {
                task.execute(self());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_tally_category_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_sort:
                mViewModel.onMenuSortClick(self(), mBinding.recyclerView);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /** 分类列表适配器 */
    private class CategoryListAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

        private List<CategoryModel> mDataList = new ArrayList<>();

        void setDataList(List<CategoryModel> list) {
            if (list == null) {
                return;
            }
            mDataList.clear();
            mDataList.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CategoryViewHolder(DataBindingUtil.inflate(LayoutInflater.from(self()),
                    R.layout.tally_module_edit_category_item_manager_category, parent, false));
        }

        @Override
        public void onBindViewHolder(CategoryViewHolder holder, int position) {
            holder.bind(mDataList.get(position));
        }
    }

    /** 分类 ITEM ViewHolder */
    private class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ItemManagerCategoryBinding mBinding;

        CategoryViewHolder(ItemManagerCategoryBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(CategoryModel category) {
            mBinding.setData(category);
            mBinding.setVm(mViewModel);
            mBinding.executePendingBindings();
        }
    }
}
