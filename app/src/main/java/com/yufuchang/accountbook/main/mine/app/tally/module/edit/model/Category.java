package com.yufuchang.accountbook.main.mine.app.tally.module.edit.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.yufuchang.accountbook.BR;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.CategoryModel;



public class Category extends BaseObservable {

    private boolean isSelect;

    private CategoryModel internal;

    public Category(CategoryModel category) {
        this.internal = category;
    }

    @Bindable
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
        notifyPropertyChanged(BR.select);
    }

    public CategoryModel getInternal() {
        return internal;
    }

    public void setInternal(CategoryModel category) {
        this.internal = category;
    }
}
