package com.yufuchang.accountbook.main.mine.app.tally.module.edit.category;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.view.View;


import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.base.utils.ResUtils;
import com.yufuchang.accountbook.main.framework.BaseViewModel;
import com.yufuchang.accountbook.main.framework.ViewReliedTask;
import com.yufuchang.accountbook.main.mine.app.tally.eventbus.EventCategoryAdd;
import com.yufuchang.accountbook.main.mine.app.tally.eventbus.EventCategoryOrderChange;
import com.yufuchang.accountbook.main.mine.app.tally.eventbus.EventCategoryUpdate;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.CategoryModel;
import com.yufuchang.accountbook.main.mine.ui.widget.PopupMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;



public class CategoryManagerViewModel extends BaseViewModel implements LifecycleObserver {

    private int mType = -1;
    private CategoryRepository mRepository;

    private MutableLiveData<List<CategoryModel>> mCategoryList = new MutableLiveData<>();
    private MutableLiveData<ViewReliedTask<Activity>> mViewReliedTask = new MutableLiveData<>();

    public CategoryManagerViewModel(Application application) {
        super(application);
        mRepository = new CategoryRepository();
    }

    LiveData<List<CategoryModel>> getCategoryList() {
        return mCategoryList;
    }

    LiveData<ViewReliedTask<Activity>> getViewReliedTask() {
        return mViewReliedTask;
    }

    /**
     * 修改显示的分类类型点击
     *
     * @param type {@link CategoryModel#TYPE_EXPENSE} {@link CategoryModel#TYPE_INCOME}
     */
    void onTypeChange(int type) {
        if (type == mType) {
            return;
        }
        mType = type;
        refreshData();
    }

    /** toolbar menu sort click */
    void onMenuSortClick(Activity activity, View shareRoot) {
        CategorySortActivity.open(activity, mType, shareRoot);
    }

    /** 分类 ITEM 菜单点击 */
    public void onItemMenuClick(View anchorView, CategoryModel category) {
        mViewReliedTask.setValue(activity -> {
            new PopupMenu(activity)
                    .addMenu(0, 0, ResUtils.getString(activity, R.string.edit_category))
                    .setOnItemClickListener((popupWindow, item) -> {
                        switch (item.getId()) {
                            case 0:
                                popupWindow.dismiss();
                                CategoryEditActivity.openAsEdit(activity, category.getId());
                                break;
                            default:
                                popupWindow.dismiss();
                                break;
                        }
                    })
                    .show(anchorView);
        });
    }

    /** 添加分类点击 */
    public void onAddCategoryClick() {
        mViewReliedTask.setValue(activity -> {
            CategoryEditActivity.openAsAddNew(activity, mType);
        });
    }

    private void refreshData() {
        if (mType == CategoryModel.TYPE_EXPENSE) {
            mRepository.loadAllExpenseCategory(mCategoryList::postValue);
        }
        if (mType == CategoryModel.TYPE_INCOME) {
            mRepository.loadAllIncomeCategory(mCategoryList::postValue);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate(LifecycleOwner owner) {
        EventBus.getDefault().register(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAddCategory(EventCategoryAdd event) {
        refreshData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateCategory(EventCategoryUpdate event) {
        refreshData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSortCategory(EventCategoryOrderChange event) {
        refreshData();
    }
}
