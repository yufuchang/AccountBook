package com.yufuchang.accountbook.main.mine.app.tally.module.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.base.utils.CommonUtils;
import com.yufuchang.accountbook.module.search.ItemClearAllBinding;
import com.yufuchang.accountbook.module.search.ItemSearchHistoryBinding;

import java.util.ArrayList;
import java.util.List;


class SearchHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_NORMAL = 1;
    private static final int ITEM_TYPE_BOTTOM = 2;
    private static final String ITEM_CLEAR_ALL = "ITEM_CLEAR_ALL";

    private LayoutInflater mInflater;
    private SearchViewModel mViewModel;
    private List<String> mItems = new ArrayList<>();

    SearchHistoryAdapter(Context context, SearchViewModel viewModel) {
        mInflater = LayoutInflater.from(context);
        mViewModel = viewModel;
    }

    void refresh(List<String> items) {
        mItems.clear();
        mItems.addAll(items);
        if (!mItems.isEmpty()) {
            mItems.add(ITEM_CLEAR_ALL);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return CommonUtils.isEqual(mItems.get(position), ITEM_CLEAR_ALL) ? ITEM_TYPE_BOTTOM : ITEM_TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_NORMAL) {
            return new ItemHistoryVh(DataBindingUtil.inflate(mInflater,
                    R.layout.tally_module_search_item_history, parent, false));
        } else {
            return new ItemClearAllVh(DataBindingUtil.inflate(mInflater,
                    R.layout.tally_module_search_item_clear_all, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHistoryVh) {
            ((ItemHistoryVh) holder).bind(mItems.get(position));
        } else if (holder instanceof ItemClearAllVh) {
            ((ItemClearAllVh) holder).bind();
        }
    }

    class ItemHistoryVh extends RecyclerView.ViewHolder {
        private ItemSearchHistoryBinding mBinding;

        ItemHistoryVh(ItemSearchHistoryBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(String history) {
            mBinding.setText(history);
            mBinding.setVm(mViewModel);
            mBinding.executePendingBindings();
        }
    }

    class ItemClearAllVh extends RecyclerView.ViewHolder {
        private ItemClearAllBinding mBinding;

        ItemClearAllVh(ItemClearAllBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind() {
            mBinding.setVm(mViewModel);
            mBinding.executePendingBindings();
        }
    }
}
