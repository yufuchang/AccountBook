package com.yufuchang.accountbook.main.mine.app.tally.ui.dialog;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.dialog.MenuDialogItemBinding;

import java.util.ArrayList;
import java.util.List;



class MenuDialogAdapter extends RecyclerView.Adapter<MenuDialogAdapter.Vh> {

    private MenuDialogViewModel mViewModel;
    private List<MenuDialogItem> mMenuList = new ArrayList<>();

    MenuDialogAdapter(MenuDialogViewModel viewModel) {
        mViewModel = viewModel;
    }

    void setMenuList(List<MenuDialogItem> menuList) {
        if (menuList == null) {
            return;
        }
        mMenuList.clear();
        mMenuList.addAll(menuList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMenuList.size();
    }

    @Override
    public Vh onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.tally_dialog_menu_item_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(Vh holder, int position) {
        holder.bind(mMenuList.get(position));
    }

    class Vh extends RecyclerView.ViewHolder {
        private MenuDialogItemBinding mBinding;

        Vh(MenuDialogItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(MenuDialogItem item) {
            mBinding.setItem(item);
            mBinding.setVm(mViewModel);
            mBinding.executePendingBindings();
        }
    }
}
