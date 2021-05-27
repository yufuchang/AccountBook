package com.yufuchang.accountbook.main.mine.app.tally.module.detail;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.support.v7.app.AlertDialog;


import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.base.common.Callback;
import com.yufuchang.accountbook.base.common.IError;
import com.yufuchang.accountbook.main.framework.BaseViewModel;
import com.yufuchang.accountbook.main.mine.app.tally.common.utils.TallyUtils;
import com.yufuchang.accountbook.main.mine.app.tally.eventbus.EventRecordDelete;
import com.yufuchang.accountbook.main.mine.app.tally.eventbus.EventRecordUpdate;
import com.yufuchang.accountbook.main.mine.app.tally.module.edit.RecordEditActivity;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.Record;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class RecordDetailViewModel extends BaseViewModel implements LifecycleObserver {

    public static final int TYPE_EXPENSE = 0;
    public static final int TYPE_INCOME = 1;

    static final String EXTRA_RECORD_ID = "extra_record_id";
    static final String EXTRA_RECORD_TYPE = "extra_record_type";

    private boolean mDataModified;
    private int mType;
    private long mRecordId;
    private Object mRecord;

    private MutableLiveData<RecordData> mRecordData = new MutableLiveData<>();
    private RecordDetailRepository mRepository;

    public RecordDetailViewModel(Application application) {
        super(application);
        mRepository = new RecordDetailRepository();
    }

    LiveData<RecordData> getRecordData() {
        return mRecordData;
    }

    /** 修改点击 */
    public void onUpdateClick(Activity activity) {
        if (mType == TYPE_EXPENSE) {
            RecordEditActivity.openAsUpdateExpense(activity, mRecordId);
        } else {
            RecordEditActivity.openAsUpdateIncome(activity, mRecordId);
        }
    }

    /** 删除点击 */
    void onDeleteClick(Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.dialog_title_delete_confirm)
                .setNegativeButton(R.string.dialog_btn_cancel, null)
                .setPositiveButton(R.string.dialog_btn_delete, (dialog, which) -> {
                    if (mType == TYPE_EXPENSE) {
                        mRepository.deleteExpense(mRecordId, result -> {
                            activity.finish();
                            EventBus.getDefault().post(new EventRecordDelete((Record) mRecord));
                        });
                    } else {
                        mRepository.deleteIncome(mRecordId, result -> {
                            activity.finish();
                            EventBus.getDefault().post(new EventRecordDelete((Record) mRecord));
                        });
                    }
                }).show();
    }

    boolean onBackPressed(Activity activity) {
        if (mDataModified) {
            activity.finish();
            return true;
        }
        return false;
    }

    private void refreshData() {
        if (mType == TYPE_EXPENSE) {
            mRepository.queryExpense(mRecordId, new Callback<Record, IError>() {
                @Override
                public void success(Record expense) {
                    mRecord = expense;
                    RecordData recordData = new RecordData();
                    recordData.setType(TYPE_EXPENSE);
                    recordData.setRecordId(expense.getId());
                    recordData.setAmount("¥" + TallyUtils.formatDisplayMoney(expense.getAmount()));
                    recordData.setCategoryIcon(expense.getCategoryIcon());
                    recordData.setCategoryName(expense.getCategoryName());
                    recordData.setDesc(expense.getDesc());
                    recordData.setTime(TallyUtils.formatDisplayTime(expense.getTime()));
                    mRecordData.setValue(recordData);
                }

                @Override
                public void failure(IError iError) {
                    showToastShort(iError.msg());
                }
            });
        } else {
            mRepository.queryIncome(mRecordId, new Callback<Record, IError>() {
                @Override
                public void success(Record income) {
                    mRecord = income;
                    RecordData recordData = new RecordData();
                    recordData.setType(TYPE_INCOME);
                    recordData.setRecordId(income.getId());
                    recordData.setAmount(TallyUtils.formatDisplayMoney(income.getAmount()));
                    recordData.setCategoryIcon(income.getCategoryIcon());
                    recordData.setCategoryName(income.getCategoryName());
                    recordData.setDesc(income.getDesc());
                    recordData.setTime("¥" + TallyUtils.formatDisplayTime(income.getTime()));
                    mRecordData.setValue(recordData);
                }

                @Override
                public void failure(IError iError) {
                    showToastShort(iError.msg());
                }
            });
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate(LifecycleOwner owner) {
        Activity activity = (Activity) owner;
        Intent intent = activity.getIntent();
        mType = intent.getIntExtra(EXTRA_RECORD_TYPE, 0);
        mRecordId = intent.getLongExtra(EXTRA_RECORD_ID, 0);
        refreshData();

        EventBus.getDefault().register(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventRecordUpdate(EventRecordUpdate event) {
        mDataModified = true;
        if (event.getRecord() != null && event.getRecord().getId() == mRecordId) {
            refreshData();
        }
    }
}
