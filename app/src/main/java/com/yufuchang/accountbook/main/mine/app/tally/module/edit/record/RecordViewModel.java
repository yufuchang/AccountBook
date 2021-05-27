package com.yufuchang.accountbook.main.mine.app.tally.module.edit.record;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.DialogInterface;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;


import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.base.utils.ArrayUtils;
import com.yufuchang.accountbook.base.utils.CommonUtils;
import com.yufuchang.accountbook.base.utils.ResUtils;
import com.yufuchang.accountbook.base.utils.UIUtils;
import com.yufuchang.accountbook.main.framework.ViewReliedTask;
import com.yufuchang.accountbook.main.mine.app.tally.common.RecordType;
import com.yufuchang.accountbook.main.mine.app.tally.data.CategoryIconHelper;
import com.yufuchang.accountbook.main.mine.app.tally.eventbus.EventCategoryAdd;
import com.yufuchang.accountbook.main.mine.app.tally.eventbus.EventCategoryOrderChange;
import com.yufuchang.accountbook.main.mine.app.tally.eventbus.EventCategoryUpdate;
import com.yufuchang.accountbook.main.mine.app.tally.eventbus.EventRecordAdd;
import com.yufuchang.accountbook.main.mine.app.tally.eventbus.EventRecordUpdate;
import com.yufuchang.accountbook.main.mine.app.tally.module.edit.category.CategoryManagerActivity;
import com.yufuchang.accountbook.main.mine.app.tally.module.edit.model.Category;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.CategoryModel;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.Record;
import com.yufuchang.accountbook.main.mine.app.tally.ui.dialog.TextEditDialog;
import com.yufuchang.accountbook.main.mine.app.tally.utils.DatePickUtils;
import com.yufuchang.accountbook.main.mine.utils.AndroidUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class RecordViewModel extends AndroidViewModel implements LifecycleObserver {

    private DecimalFormat mAmountFormat = new DecimalFormat("0.00");
    private SimpleDateFormat mDateFormat;

    private RecordType mType;
    private long mRecordId;
    private long mDate;
    private double mAmount;
    private Record mRecord;
    private Category mCategorySettingItem;
    private RecordRepository mRepository;

    /** 币种单位 */
    private ObservableField<String> mAmountUnit = new ObservableField<>("¥");
    /** 金额 */
    private ObservableField<String> mAmountText = new ObservableField<>("");
    /** 说明信息 */
    private ObservableField<String> mDesc = new ObservableField<>("");
    /** 时间 */
    private ObservableField<String> mDateText = new ObservableField<>("");
    /** 当前选择的分类 */
    private MutableLiveData<Category> mCurrentSelectCategory = new MutableLiveData<>();
    /** 支付分类列表 */
    private MutableLiveData<List<Category>> mCategoryList = new MutableLiveData<>();

    private MutableLiveData<ViewReliedTask<Activity>> mActivityRelayTask = new MutableLiveData<>();

    public RecordViewModel(Application application) {
        super(application);
        CategoryModel setting = new CategoryModel();
        setting.setIcon(CategoryIconHelper.IC_NAME_SETTING);
        setting.setName(ResUtils.getString(application, R.string.setting));
        mCategorySettingItem = new Category(setting);

        mDateFormat = new SimpleDateFormat(ResUtils.getString(
                application, R.string.date_format_y_m_d) + " HH:mm", Locale.getDefault());
        mRepository = new RecordRepository();
        mAmountText.set("0");
        mDate = System.currentTimeMillis();
        mDateText.set(mDateFormat.format(new Date(mDate)));
    }

    public ObservableField<String> getAmountUnit() {
        return mAmountUnit;
    }

    public ObservableField<String> getAmountText() {
        return mAmountText;
    }

    public ObservableField<String> getDesc() {
        return mDesc;
    }

    public ObservableField<String> getDateText() {
        return mDateText;
    }

    LiveData<Category> getCurrentSelectCategory() {
        return mCurrentSelectCategory;
    }

    LiveData<List<Category>> getCategoryList() {
        return mCategoryList;
    }

    LiveData<ViewReliedTask<Activity>> getActivityRelayTask() {
        return mActivityRelayTask;
    }

    /** 消费分类点击 */
    public void onCategoryClick(Category category) {
        if (category == mCategorySettingItem) {
            mActivityRelayTask.setValue(activity -> {
                if (mType == RecordType.EXPENSE) {
                    CategoryManagerActivity.open(activity, CategoryModel.TYPE_EXPENSE);
                }
                if (mType == RecordType.INCOME) {
                    CategoryManagerActivity.open(activity, CategoryModel.TYPE_INCOME);
                }
            });
            return;
        }
        makeCategorySelect(mCategoryList.getValue(), category.getInternal().getUniqueName());
    }

    /** 消费说明点击 */
    public void onDescClick(Activity activity) {
        String desc = mDesc.get();
        TextEditDialog dialog = new TextEditDialog(activity)
                .setTitle(ResUtils.getString(activity, R.string.tally_add_record_note))
                .setHint(ResUtils.getString(activity, R.string.tally_expense_note))
                .setContent(desc)
                .setListener(new TextEditDialog.Listener() {
                    @Override
                    public void onPositiveClick(EditText editText, DialogInterface dialog, String text) {
                        mDesc.set(text);
                        UIUtils.hideSoftKeyboard(activity, editText);
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegativeClick(EditText editText, DialogInterface dialog) {
                        UIUtils.hideSoftKeyboard(activity, editText);
                        dialog.dismiss();
                    }
                });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /** 消费记录时间点击 */
    public void onDateClick(Activity activity) {
        DatePickUtils.showDatePickDialog(activity,
                mDate,
                new DatePickUtils.OnDatePickListener() {

                    @Override
                    public void onDatePick(DialogInterface dialog,
                                           int year,
                                           int month,
                                           int dayOfMonth) {
                    }

                    @Override
                    public void onConfirmClick(DialogInterface dialog, long timeInMills) {
                        mDate = timeInMills;
                        mDateText.set(mDateFormat.format(new Date(mDate)));
                    }
                });
    }

    /** 键盘数字点击 */
    public void onNumberClick(String number) {
        String amount = mAmountText.get();
        amount = TextUtils.isEmpty(amount) ? "" : amount;
        if ("0".equals(amount)) {
            amount = "";
        }
        amount += number;
        mAmountText.set(amount);
        mAmount = CommonUtils.string2float(amount, 0);
    }

    /** 键盘删除点击 */
    public void onDeleteClick() {
        String amount = mAmountText.get();
        amount = TextUtils.isEmpty(amount) ? "" : amount;
        if (!TextUtils.isEmpty(amount)) {
            amount = amount.substring(0, amount.length() - 1);
        }
        if (TextUtils.isEmpty(amount)) {
            amount = "0";
        }
        mAmountText.set(amount);
        mAmount = CommonUtils.string2float(amount, 0);
    }

    /** 键盘清楚点击 */
    public void onClearClick() {
        mAmountText.set("0");
        mAmount = 0;
    }

    /** 键盘 . 点击 */
    public void onDotClick() {
        String amount = mAmountText.get();
        amount = TextUtils.isEmpty(amount) ? "" : amount;
        if (!amount.contains(".")) {
            amount = amount + ".";
        }
        mAmountText.set(amount);
        mAmount = CommonUtils.string2float(amount, 0);
    }

    /** 确定点击 */
    public void onEnterClick() {
        saveData();
    }

    private void setRecordId(long recordId) {
        if (recordId <= 0) {
            return;
        }
        mRecordId = recordId;
        mRepository.queryRecordById(mRecordId, record -> {
            if (record == null) {
                return;
            }
            mRecord = record;
            mAmount = mRecord.getAmount();
            mDate = mRecord.getTime();
            mDesc.set(mRecord.getDesc());
            mDateText.set(mDateFormat.format(new Date(mDate)));
            mAmountText.set(CommonUtils.removeOddDecimal(mAmountFormat.format(mAmount)));

            String categoryUniqueName = mRecord.getCategoryUniqueName();
            Category category = ArrayUtils.findFirst(mCategoryList.getValue(),
                    c -> CommonUtils.isEqual(c.getInternal().getUniqueName(), categoryUniqueName));
            if (category != null) {
                mCurrentSelectCategory.setValue(category);
            }
        });
    }

    private void initData() {
        // 查询所有分类
        mRepository.queryAllCategory(mType, categoryList -> {

            if (categoryList == null || categoryList.isEmpty()) {
                return;
            }
            List<Category> displayList = new ArrayList<>(categoryList.size());
            for (CategoryModel category : categoryList) {
                displayList.add(new Category(category));
            }
            // 分类设置ITEM
            displayList.add(mCategorySettingItem);
            mCategoryList.setValue(displayList);

            if (mRecord == null) {
                Category select = displayList.get(0);
                makeCategorySelect(displayList, select.getInternal().getUniqueName());
            } else {
                makeCategorySelect(displayList, mRecord.getCategoryUniqueName());
            }
        });
    }

    private void refreshCategoryList() {
        // 查询所有分类
        mRepository.queryAllCategory(mType, categoryList -> {

            if (categoryList == null || categoryList.isEmpty()) {
                return;
            }
            List<Category> displayList = new ArrayList<>(categoryList.size());
            for (CategoryModel category : categoryList) {
                displayList.add(new Category(category));
            }

            // 当前选择的分类
            List<Category> currentCategoryList = mCategoryList.getValue();
            Category selectCategory = ArrayUtils.findFirst(currentCategoryList, Category::isSelect);
            String selectCategoryUniqueName = selectCategory == null || selectCategory.getInternal() == null
                    ? "" : selectCategory.getInternal().getUniqueName();

            // 分类设置ITEM
            displayList.add(mCategorySettingItem);
            mCategoryList.setValue(displayList);
            makeCategorySelect(displayList, selectCategoryUniqueName);
        });
    }

    /** 保存数据 */
    private void saveData() {
        Category category = mCurrentSelectCategory.getValue();
        if (category == null) {
            Toast.makeText(getApplication(), "no category", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isNewRecord = mRecord == null;
        Record record;
        if (mRecord != null) {
            record = mRecord;
        } else {
            record = new Record();
            record.setType(mType == RecordType.EXPENSE ? Record.TYPE_EXPENSE : Record.TYPE_INCOME);
            record.setSyncId(AndroidUtils.generateUUID());
        }
        record.setAmount(mAmount);
        record.setTime(mDate);
        record.setDesc(TextUtils.isEmpty(mDesc.get()) ? "" : mDesc.get());
        record.setCategoryIcon(category.getInternal().getIcon());
        record.setCategoryName(category.getInternal().getName());
        record.setCategoryUniqueName(category.getInternal().getUniqueName());

        if (isNewRecord) {
            mRepository.saveRecord(record, result -> {
                if (result.isOk()) {
                    record.setId(result.data());
                    EventBus.getDefault().post(new EventRecordAdd(record));
                    mActivityRelayTask.setValue(Activity::finish);
                }
            });
        } else {
            mRepository.updateExpense(record, result -> {
                if (result.isOk()) {
                    EventBus.getDefault().post(new EventRecordUpdate(record));
                    mActivityRelayTask.setValue(Activity::finish);
                }
            });
        }
    }

    private void makeCategorySelect(List<Category> list, String categoryUniqueName) {
        if (list == null) {
            return;
        }
        Category select = null;
        for (Category category : list) {
            boolean isSelect = CommonUtils.isEqual(category.getInternal().getUniqueName(), categoryUniqueName);
            category.setSelect(isSelect);
            if (isSelect) {
                select = category;
            }
        }
        if (select != null) {
            select.setSelect(true);
            mCurrentSelectCategory.setValue(select);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // LifeCycle
    ///////////////////////////////////////////////////////////////////////////

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate(LifecycleOwner owner) {
        RecordEditFragment fragment = (RecordEditFragment) owner;
        Bundle arguments = fragment.getArguments();
        long recordId = arguments.getLong(RecordEditFragment.EXTRA_RECORD_ID, -1);
        mType = (RecordType) arguments.getSerializable(RecordEditFragment.EXTRA_RECORD_TYPE);
        mCategorySettingItem.getInternal().setType(mType == RecordType.EXPENSE
                ? CategoryModel.TYPE_EXPENSE : CategoryModel.TYPE_INCOME);
        setRecordId(recordId);
        initData();

        EventBus.getDefault().register(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAddCategory(EventCategoryAdd event) {
        refreshCategoryList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateCategory(EventCategoryUpdate event) {
        refreshCategoryList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCategoryOrderChange(EventCategoryOrderChange event) {
        if (event.getCategoryType() == CategoryModel.TYPE_EXPENSE && mType == RecordType.EXPENSE) {
            refreshCategoryList();
        }
        if (event.getCategoryType() == CategoryModel.TYPE_INCOME && mType == RecordType.INCOME) {
            refreshCategoryList();
        }
    }
}
