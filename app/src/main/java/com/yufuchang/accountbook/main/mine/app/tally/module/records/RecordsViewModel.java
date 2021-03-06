package com.yufuchang.accountbook.main.mine.app.tally.module.records;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;


import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.base.common.Callback;
import com.yufuchang.accountbook.base.common.IError;
import com.yufuchang.accountbook.base.utils.ArrayUtils;
import com.yufuchang.accountbook.base.utils.ResUtils;
import com.yufuchang.accountbook.base.utils.UIUtils;
import com.yufuchang.accountbook.base.utils.WrappedInt;
import com.yufuchang.accountbook.main.framework.BaseViewModel;
import com.yufuchang.accountbook.main.framework.ViewReliedTask;
import com.yufuchang.accountbook.main.mine.app.tally.common.utils.BaseLoadDelegate;
import com.yufuchang.accountbook.main.mine.app.tally.eventbus.EventRecordAdd;
import com.yufuchang.accountbook.main.mine.app.tally.eventbus.EventRecordDelete;
import com.yufuchang.accountbook.main.mine.app.tally.eventbus.EventRecordUpdate;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.Record;
import com.yufuchang.accountbook.main.mine.app.tally.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class RecordsViewModel extends BaseViewModel implements LifecycleObserver {

    private String mToolbarTitleBase = "";

    /** ?????????????????? */
    private MutableLiveData<CharSequence> mToolbarTitle = new MutableLiveData<>();
    /** ???????????? */
    private MutableLiveData<List<Object>> mRecordList = new MutableLiveData<>();
    /** ???????????? activity ????????? */
    private MutableLiveData<ViewReliedTask<Activity>> mViewReliedTask = new MutableLiveData<>();

    /** ???????????? */
    private RecordQuery mQuery = new RecordQuery.Builder()
            .setType(RecordQuery.TYPE_ALL)
            .setStartTime(0)
            .setEndTime(System.currentTimeMillis())
            .build();
    private RecordsRepository mRepository;
    /** ?????????????????????????????????????????????????????????????????? */
    private BaseLoadDelegate<Record> mLoadDelegate;

    public RecordsViewModel(Application application) {
        super(application);
        mToolbarTitleBase = ResUtils.getString(application, R.string.tally_toolbar_title_records);
        mRepository = new RecordsRepository();
        mLoadDelegate = new BaseLoadDelegate<Record>(15) {
            @Override
            public void requestData(int page, int pageSize, Callback<List<Record>, IError> callback) {
                mRepository.queryRecords(page, pageSize, mQuery, callback);
            }

            @Override
            public void onRequestFinish(RequestType type, List<Record> dataList, @Nullable IError error) {
                super.onRequestFinish(type, dataList, error);
                if (error != null) {
                    showToastShort(error.msg());
                    return;
                }
                // ???????????????????????????
                mRecordList.postValue(formatRecordList(dataList));
            }
        };
    }

    LiveData<Boolean> getRefreshing() {
        return mLoadDelegate.getRefreshing();
    }

    LiveData<Boolean> getLoadingMore() {
        return mLoadDelegate.getLoadingMore();
    }

    LiveData<Integer> getLoadingStatus() {
        return mLoadDelegate.getLoadingStatus();
    }

    LiveData<CharSequence> getToolbarTitle() {
        return mToolbarTitle;
    }

    LiveData<List<Object>> getRecordList() {
        return mRecordList;
    }

    /** ?????????????????????????????????????????? */
    void setQuery(RecordQuery query) {
        if (query == null) {
            return;
        }
        mQuery = query;
        boolean refreshUseLoadMode = mRecordList.getValue() == null || mRecordList.getValue().isEmpty();
        if (refreshUseLoadMode) {
            mLoadDelegate.load();
        } else {
            mLoadDelegate.refresh();
        }

        // ????????? Toolbar Title
        // ?????????????????????????????????
        String dateRange = mQuery.getStartTime() <= 0 ?
                "" : DateUtils.formatDisplayDateRange(getApplication(), mQuery.getStartTime(), mQuery.getEndTime());

        SpannableStringBuilder titleBuilder = new SpannableStringBuilder();
        titleBuilder.append(mToolbarTitleBase).append(" ");
        if (!TextUtils.isEmpty(dateRange)) {
            titleBuilder.append(createAbsoluteSizeSpan(dateRange, null, 14,
                    ResUtils.getColor(getApplication(), R.color.appTextColorSecondary)));
        }
        mToolbarTitle.setValue(titleBuilder);
    }

    void load() {
        mLoadDelegate.load();
    }

    void refresh() {
        mLoadDelegate.refresh();
    }

    void loadMore() {
        mLoadDelegate.loadMore();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private method
    ///////////////////////////////////////////////////////////////////////////

    /**
     * ????????????????????? span
     *
     * @param text     ??????
     * @param typeface ??????
     * @param sizeDip  ?????? dp
     * @param color    ??????
     */
    private SpannableString createAbsoluteSizeSpan(String text, Typeface typeface, int sizeDip, int color) {
        SpannableString tipSpan = new SpannableString(text);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(UIUtils.dp2px(getApplication(), sizeDip)) {
            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setFakeBoldText(false);
                if (color != 0) {
                    textPaint.setColor(color);
                }
                if (typeface != null) {
                    textPaint.setTypeface(typeface);
                }
            }
        };
        tipSpan.setSpan(sizeSpan, 0, tipSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return tipSpan;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param source ???????????????
     * @return ???????????????????????????
     */
    private List<Object> formatRecordList(List<Record> source) {
        List<Object> currentDisplayList = mRecordList.getValue() == null ? new ArrayList<>() : mRecordList.getValue();
        if (source == null || source.isEmpty()) {
            return currentDisplayList;
        }
        currentDisplayList.clear();

        WrappedInt yearTemp = new WrappedInt(-1);
        WrappedInt monthTemp = new WrappedInt(-1);
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        // ????????????????????????????????????????????????
        ArrayUtils.forEach(source, (count, index, item) -> {
            long time = item.getTime();
            calendar.setTimeInMillis(time);
            int selfYear = calendar.get(Calendar.YEAR);
            int selfMonth = calendar.get(Calendar.MONTH) + 1;
            if (yearTemp.get() != selfYear || monthTemp.get() != selfMonth) {
                currentDisplayList.add(new RecordsDateTitle(selfYear, selfMonth));
                yearTemp.set(selfYear);
                monthTemp.set(selfMonth);
            }
            currentDisplayList.add(item);
        });

        return currentDisplayList;
    }

    ///////////////////////////////////////////////////////////////////////////
    // LifeCycle
    ///////////////////////////////////////////////////////////////////////////

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate(LifecycleOwner owner) {
        Activity activity = (Activity) owner;
        Intent intent = activity.getIntent();
        RecordQuery query = intent.getParcelableExtra(RecordsActivity.EXTRA_QUERY);
        mQuery = query == null ? new RecordQuery.Builder()
                .setType(RecordQuery.TYPE_ALL)
                .setStartTime(0)
                .setEndTime(System.currentTimeMillis())
                .build() : query;
        setQuery(mQuery);

        EventBus.getDefault().register(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        EventBus.getDefault().unregister(this);
    }

    ///////////////////////////////////////////////////////////////////////////
    // EventBus
    ///////////////////////////////////////////////////////////////////////////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRecordAdd(EventRecordAdd event) {
        mLoadDelegate.refreshAllBackground();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRecordUpdate(EventRecordUpdate event) {
        mLoadDelegate.refreshAllBackground();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRecordDelete(EventRecordDelete event) {
        mLoadDelegate.refreshAllBackground();
    }
}
