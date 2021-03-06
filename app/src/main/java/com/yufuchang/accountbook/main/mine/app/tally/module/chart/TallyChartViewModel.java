package com.yufuchang.accountbook.main.mine.app.tally.module.chart;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Pair;


import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.base.common.Callback;
import com.yufuchang.accountbook.base.common.IError;
import com.yufuchang.accountbook.base.common.SimpleCallback;
import com.yufuchang.accountbook.base.utils.ArrayUtils;
import com.yufuchang.accountbook.base.utils.LogUtils;
import com.yufuchang.accountbook.base.utils.ResUtils;
import com.yufuchang.accountbook.base.utils.WrappedLong;
import com.yufuchang.accountbook.base.utils.WrappedObject;
import com.yufuchang.accountbook.main.framework.BaseViewModel;
import com.yufuchang.accountbook.main.framework.ViewReliedTask;
import com.yufuchang.accountbook.main.mine.app.tally.common.utils.TallyUtils;
import com.yufuchang.accountbook.main.mine.app.tally.module.chart.data.CategoryData;
import com.yufuchang.accountbook.main.mine.app.tally.module.chart.data.DailyData;
import com.yufuchang.accountbook.main.mine.app.tally.module.chart.data.Month;
import com.yufuchang.accountbook.main.mine.app.tally.module.chart.data.MonthlyData;
import com.yufuchang.accountbook.main.mine.app.tally.module.chart.data.MonthlyDataList;
import com.yufuchang.accountbook.main.mine.app.tally.module.chart.data.MonthlyEntryData;
import com.yufuchang.accountbook.main.mine.app.tally.module.records.RecordQuery;
import com.yufuchang.accountbook.main.mine.app.tally.module.records.RecordsActivity;
import com.yufuchang.accountbook.main.mine.app.tally.ui.widget.MonthSelectDialog;
import com.yufuchang.accountbook.main.mine.app.tally.utils.DateUtils;
import com.yufuchang.accountbook.main.mine.app.tally.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;



public class TallyChartViewModel extends BaseViewModel implements LifecycleObserver {

    private static final String TAG = LogUtils.makeLogTag(TallyChartViewModel.class);

    /** ?????????????????? */
    private Calendar mStartDate;
    /** ?????????????????? */
    private Calendar mEndDate;

    private List<Month> mSelectableMonthList = new ArrayList<>();

    /** ???????????? */
    private ObservableField<String> mCurrentDateText = new ObservableField<>("");
    /** ???????????? */
    private ObservableField<String> mExpenseTotalAmountText = new ObservableField<>("");
    /** ???????????? */
    private ObservableField<String> mIncomeTotalAmountText = new ObservableField<>("");
    /** ???????????????????????? */
    private ObservableBoolean mDisplayDailyChart = new ObservableBoolean(true);
    /** ??????????????????????????? */
    private ObservableBoolean mDisplayExpenseChart = new ObservableBoolean(true);

    private List<DailyData> mDailyExpenseList = new ArrayList<>();
    private List<DailyData> mDailyIncomeList = new ArrayList<>();
    private List<MonthlyData> mMonthlyExpenseList = new ArrayList<>();
    private List<MonthlyData> mMonthlyIncomeList = new ArrayList<>();
    private List<CategoryData> mCategoryDailyExpenseList = new ArrayList<>();
    private List<CategoryData> mCategoryDailyIncomeList = new ArrayList<>();
    private List<CategoryData> mCategoryYearlyExpenseList = new ArrayList<>();
    private List<CategoryData> mCategoryYearlyIncomeList = new ArrayList<>();

    /** ???????????????????????? */
    private MutableLiveData<MonthlyDataList> mObservableMonthlyDataList = new MutableLiveData<>();
    /** ???????????????????????? */
    private MutableLiveData<List<DailyData>> mObservableDailyExpenseList = new MutableLiveData<>();
    /** ???????????????????????? */
    private MutableLiveData<List<DailyData>> mObservableDailyIncomeList = new MutableLiveData<>();
    /** ???????????????????????? */
    private MutableLiveData<List<CategoryData>> mObservableCategoryExpenseDataList = new MutableLiveData<>();
    /** ???????????????????????? */
    private MutableLiveData<List<CategoryData>> mObservableCategoryIncomeDataList = new MutableLiveData<>();

    private MutableLiveData<ViewReliedTask<Activity>> mViewReliedTask = new MutableLiveData<>();

    private TallyChartRepository mRepository;

    public TallyChartViewModel(Application application) {
        super(application);
        mRepository = new TallyChartRepository();
    }

    public ObservableField<String> getCurrentDateText() {
        return mCurrentDateText;
    }

    public ObservableBoolean getDisplayDailyChart() {
        return mDisplayDailyChart;
    }

    public ObservableField<String> getExpenseTotalAmountText() {
        return mExpenseTotalAmountText;
    }

    public ObservableField<String> getIncomeTotalAmountText() {
        return mIncomeTotalAmountText;
    }

    public ObservableBoolean getDisplayExpenseChart() {
        return mDisplayExpenseChart;
    }

    LiveData<List<DailyData>> getDailyExpenseList() {
        return mObservableDailyExpenseList;
    }

    LiveData<List<DailyData>> getDailyIncomeList() {
        return mObservableDailyIncomeList;
    }

    LiveData<MonthlyDataList> getMonthlyDataList() {
        return mObservableMonthlyDataList;
    }

    LiveData<List<CategoryData>> getCategoryExpenseDataList() {
        return mObservableCategoryExpenseDataList;
    }

    LiveData<List<CategoryData>> getCategoryIncomeDataList() {
        return mObservableCategoryIncomeDataList;
    }

    LiveData<ViewReliedTask<Activity>> getViewReliedTask() {
        return mViewReliedTask;
    }

    /**
     * ??????????????????
     *
     * @param activity activity
     */
    void onSelectDateClick(Activity activity) {
        Month currentMonth = new Month();
        currentMonth.setYear(mStartDate.get(Calendar.YEAR));
        currentMonth.setMonth(mStartDate.get(Calendar.MONTH) + 1);

        new MonthSelectDialog(activity, mSelectableMonthList, new MonthSelectDialog.DateSelectListener() {
            @Override
            public void onMonthSelect(MonthSelectDialog dialog, Month month) {
                dialog.dismiss();
                Pair<Long, Long> monthDateRange = DateUtils.monthDateRange(month.getYear(), month.getMonth());
                mStartDate.setTimeInMillis(monthDateRange.first);
                mEndDate.setTimeInMillis(monthDateRange.second);

                clearData();
                refreshData();
            }
        }, currentMonth).show();
    }

    /** ????????? ???????????? ?????? */
    void onDailyMarkerViewClick(Activity activity, DailyData dailyData) {
        Pair<Long, Long> dayTimeRange = DateUtils.dayDateRange(
                dailyData.getYear(), dailyData.getMonth(), dailyData.getDayOfMonth());
        RecordQuery query = new RecordQuery.Builder()
                .setType(mDisplayExpenseChart.get() ? RecordQuery.TYPE_EXPENSE : RecordQuery.TYPE_INCOME)
                .setStartTime(dayTimeRange.first)
                .setEndTime(dayTimeRange.second)
                .build();
        RecordsActivity.open(activity, query);
    }

    /** ????????? ???????????? ?????? */
    void onMonthlyMarkerViewClick(Activity activity, MonthlyEntryData monthlyData) {
        Pair<Long, Long> dayTimeRange = DateUtils.monthDateRange(
                monthlyData.getMonth().getYear(), monthlyData.getMonth().getMonth());
        RecordQuery query = new RecordQuery.Builder()
                .setType(mDisplayExpenseChart.get() ? RecordQuery.TYPE_EXPENSE : RecordQuery.TYPE_INCOME)
                .setStartTime(dayTimeRange.first)
                .setEndTime(dayTimeRange.second)
                .build();
        RecordsActivity.open(activity, query);
    }

    /** ??????????????????????????? */
    public String formatCategoryDataAmount(CategoryData data) {
        return "??" + TallyUtils.formatDisplayMoney(data.getAmount());
    }

    /** ????????? ???????????? ?????? */
    public void onSelectAsExpenseChartClick() {
        mDisplayExpenseChart.set(true);
        refreshData();
    }

    /** ????????? ???????????? ?????? */
    public void onSelectAsIncomeChartClick() {
        mDisplayExpenseChart.set(false);
        refreshData();
    }

    /** ?????? ??????????????????????????? */
    public void onSwitchChartModelClick() {
        if (mDisplayDailyChart.get()) {
            // ??????????????? ??????????????????????????????
            onSelectAsYearChartClick();
        } else {
            // ??????????????? ??????????????????????????????
            onSelectAsDailyChartClick();
        }
    }

    /** ???????????? ITEM ?????? */
    public void onCategoryDataItemClick(CategoryData categoryData) {
        mViewReliedTask.setValue(activity -> {
            RecordsActivity.open(activity, new RecordQuery.Builder()
                    .setStartTime(categoryData.getStartDate())
                    .setEndTime(categoryData.getEndDate())
                    .setType(categoryData.getType())
                    .setCategoryUniqueNameArray(new String[]{categoryData.getCategoryUniqueName()})
                    .build());
        });
    }

    /** ????????? ????????? ?????? */
    private void onSelectAsYearChartClick() {
        mDisplayDailyChart.set(false);
        refreshData();
    }

    /** ????????? ????????? ?????? */
    private void onSelectAsDailyChartClick() {
        mDisplayDailyChart.set(true);
        refreshData();
    }

    private void init() {
        mRepository.queryFirstRecordTime(new Callback<Long, IError>() {
            @Override
            public void success(Long firstRecordTime) {
                Month firstMonth = new Month();
                Month currentMonth = new Month();

                Calendar calendar = Calendar.getInstance();
                currentMonth.setYear(calendar.get(Calendar.YEAR));
                currentMonth.setMonth(calendar.get(Calendar.MONTH) + 1);

                calendar.setTimeInMillis(firstRecordTime);
                firstMonth.setYear(calendar.get(Calendar.YEAR));
                firstMonth.setMonth(calendar.get(Calendar.MONTH) + 1);

                for (int y = firstMonth.getYear(); y <= currentMonth.getYear(); y++) {

                    for (int m = 1; m <= 12; m++) {
                        Month month = new Month();
                        month.setYear(y);
                        month.setMonth(m);
                        mSelectableMonthList.add(month);

                        if (y == currentMonth.getYear() && m == currentMonth.getMonth()) {
                            break;
                        }
                    }
                }
            }

            @Override
            public void failure(IError iError) {
                LogUtils.LOGE(TAG, "query first record time error:" + iError.toString());
            }
        });
    }

    private void clearData() {
        mDailyExpenseList.clear();
        mDailyIncomeList.clear();
        mMonthlyExpenseList.clear();
        mMonthlyIncomeList.clear();
        mCategoryDailyExpenseList.clear();
        mCategoryDailyIncomeList.clear();
        mCategoryYearlyExpenseList.clear();
        mCategoryYearlyIncomeList.clear();

        mObservableMonthlyDataList.setValue(null);
        mObservableDailyExpenseList.setValue(null);
        mObservableDailyIncomeList.setValue(null);
        mObservableCategoryExpenseDataList.setValue(null);
        mObservableCategoryIncomeDataList.setValue(null);
    }

    private void refreshData() {
        boolean isDisplayAsDaily = mDisplayDailyChart.get();
        boolean isDisplayExpense = mDisplayExpenseChart.get();

        if (isDisplayAsDaily) {
            // ?????????????????????
            queryDailyData(v -> {
                // ?????????????????????
                boolean hasExpense = ArrayUtils.contains(mDailyExpenseList, item -> item.getAmount() > 0);
                boolean hasIncome = ArrayUtils.contains(mDailyIncomeList, item -> item.getAmount() > 0);
                // ?????????????????????????????????
                if (!hasExpense) {
                    mDailyExpenseList.clear();
                }
                // ?????????????????????????????????
                if (!hasIncome) {
                    mDailyIncomeList.clear();
                }
                if (isDisplayExpense) {
                    mObservableDailyExpenseList.postValue(mDailyExpenseList);
                    mObservableCategoryExpenseDataList.postValue(mCategoryDailyExpenseList);
                } else {
                    mObservableDailyIncomeList.postValue(mDailyIncomeList);
                    mObservableCategoryIncomeDataList.postValue(mCategoryDailyIncomeList);
                }

                // ??????????????????
                displayDailyExpenseAmountTotal();
                displayDailyIncomeAmountTotal();
            });
        } else {
            queryYearlyData(v -> {
                // ????????? ??????????????? ?????????
                boolean hasExpense = ArrayUtils.contains(mMonthlyExpenseList, item -> item.getAmount() > 0);
                boolean hasIncome = ArrayUtils.contains(mMonthlyIncomeList, item -> item.getAmount() > 0);
                // ?????????????????????????????????????????????????????????
                if (!hasExpense && !hasIncome) {
                    mMonthlyExpenseList.clear();
                    mMonthlyIncomeList.clear();
                }

                MonthlyDataList monthlyDataList = new MonthlyDataList();
                monthlyDataList.setExpenseList(mMonthlyExpenseList);
                monthlyDataList.setIncomeList(mMonthlyIncomeList);
                mObservableMonthlyDataList.postValue(monthlyDataList);

                // ????????? ????????????
                if (isDisplayExpense) {
                    mObservableCategoryExpenseDataList.postValue(mCategoryYearlyExpenseList);
                } else {
                    mObservableCategoryIncomeDataList.postValue(mCategoryYearlyIncomeList);
                }

                displayMonthlyExpenseAmountTotal();
                displayMonthlyIncomeAmountTotal();
            });
        }
    }

    /***
     * ?????????????????????
     * @param callback ??????
     */
    private void queryDailyData(SimpleCallback<Void> callback) {

        long startTime = mStartDate.getTimeInMillis();
        long endTime = mEndDate.getTimeInMillis();

        int queryModuleCount = 4;
        AtomicInteger queryCount = new AtomicInteger(0);

        // ?????????????????????
        if (!mDailyExpenseList.isEmpty()) {
            if (queryCount.incrementAndGet() == queryModuleCount) {
                callback.success(null);
            }
        } else {
            mRepository.queryDailyExpense(startTime, endTime, new Callback<List<DailyData>, IError>() {
                @Override
                public void success(List<DailyData> dailyDataList) {
                    dailyDataList = completeEmptyDailyData(startTime, endTime, dailyDataList);
                    mDailyExpenseList.clear();
                    mDailyExpenseList.addAll(dailyDataList);

                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }

                @Override
                public void failure(IError iError) {
                    mDailyExpenseList.clear();
                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }
            });
        }

        // ?????????????????????
        if (!mDailyIncomeList.isEmpty()) {
            if (queryCount.incrementAndGet() == queryModuleCount) {
                callback.success(null);
            }
        } else {
            mRepository.queryDailyInCome(startTime, endTime, new Callback<List<DailyData>, IError>() {
                @Override
                public void success(List<DailyData> dailyDataList) {
                    dailyDataList = completeEmptyDailyData(startTime, endTime, dailyDataList);
                    mDailyIncomeList.clear();
                    mDailyIncomeList.addAll(dailyDataList);
                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }

                @Override
                public void failure(IError iError) {
                    mDailyIncomeList.clear();
                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }
            });
        }

        // ?????????????????????????????????
        if (!mCategoryDailyExpenseList.isEmpty()) {
            if (queryCount.incrementAndGet() == queryModuleCount) {
                callback.success(null);
            }
        } else {
            mRepository.queryCategoryExpense(startTime, endTime, new Callback<List<CategoryData>, IError>() {
                @Override
                public void success(List<CategoryData> categoryData) {
                    mCategoryDailyExpenseList.clear();
                    if (categoryData != null) {
                        mCategoryDailyExpenseList.addAll(categoryData);
                    }
                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }

                @Override
                public void failure(IError iError) {
                    mCategoryDailyExpenseList.clear();
                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }
            });
        }

        // ?????????????????????????????????
        if (!mCategoryDailyIncomeList.isEmpty()) {
            if (queryCount.incrementAndGet() == queryModuleCount) {
                callback.success(null);
            }
        } else {
            mRepository.queryCategoryIncome(startTime, endTime, new Callback<List<CategoryData>, IError>() {
                @Override
                public void success(List<CategoryData> categoryData) {
                    mCategoryDailyIncomeList.clear();
                    if (categoryData != null) {
                        mCategoryDailyIncomeList.addAll(categoryData);
                    }
                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }

                @Override
                public void failure(IError iError) {
                    mCategoryDailyIncomeList.clear();
                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }
            });
        }
    }

    /** ????????????????????? */
    private void queryYearlyData(SimpleCallback<Void> callback) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mStartDate.getTimeInMillis() + (mEndDate.getTimeInMillis() - mStartDate.getTimeInMillis()) / 2);
        Pair<Long, Long> yearDateRange = DateUtils.yearDateRange(calendar.get(Calendar.YEAR));
        long startTime = yearDateRange.first;
        long endTime = yearDateRange.second;

        int queryModuleCount = 4;
        AtomicInteger queryCount = new AtomicInteger(0);

        // ?????????????????????
        if (!mMonthlyExpenseList.isEmpty()) {
            if (queryCount.incrementAndGet() == queryModuleCount) {
                callback.success(null);
            }
        } else {
            mRepository.queryMonthlyExpense(startTime, endTime, new Callback<List<MonthlyData>, IError>() {
                @Override
                public void success(List<MonthlyData> list) {
                    // ???????????????
                    list = completeEmptyMonthlyData(startTime, endTime, list);
                    mMonthlyExpenseList.clear();
                    mMonthlyExpenseList.addAll(list);
                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }

                @Override
                public void failure(IError iError) {
                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }
            });
        }

        // ???????????????????????????
        if (!mMonthlyIncomeList.isEmpty()) {
            if (queryCount.incrementAndGet() == queryModuleCount) {
                callback.success(null);
            }
        } else {
            mRepository.queryMonthlyIncome(startTime, endTime, new Callback<List<MonthlyData>, IError>() {
                @Override
                public void success(List<MonthlyData> list) {
                    // ???????????????
                    list = completeEmptyMonthlyData(startTime, endTime, list);
                    mMonthlyIncomeList.clear();
                    mMonthlyIncomeList.addAll(list);
                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }

                @Override
                public void failure(IError iError) {
                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }
            });
        }

        // ?????????????????????????????????
        if (!mCategoryYearlyExpenseList.isEmpty()) {
            if (queryCount.incrementAndGet() == queryModuleCount) {
                callback.success(null);
            }
        } else {
            mRepository.queryCategoryExpense(startTime, endTime, new Callback<List<CategoryData>, IError>() {
                @Override
                public void success(List<CategoryData> categoryData) {
                    mCategoryYearlyExpenseList.clear();
                    if (categoryData != null) {
                        mCategoryYearlyExpenseList.addAll(categoryData);
                    }
                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }

                @Override
                public void failure(IError iError) {
                    mCategoryYearlyExpenseList.clear();
                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }
            });
        }

        // ?????????????????????????????????
        if (!mCategoryYearlyIncomeList.isEmpty()) {
            if (queryCount.incrementAndGet() == queryModuleCount) {
                callback.success(null);
            }
        } else {
            mRepository.queryCategoryIncome(startTime, endTime, new Callback<List<CategoryData>, IError>() {
                @Override
                public void success(List<CategoryData> categoryData) {
                    mCategoryYearlyIncomeList.clear();
                    if (categoryData != null) {
                        mCategoryYearlyIncomeList.addAll(categoryData);
                    }
                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }

                @Override
                public void failure(IError iError) {
                    mCategoryYearlyIncomeList.clear();
                    if (queryCount.incrementAndGet() == queryModuleCount) {
                        callback.success(null);
                    }
                }
            });
        }
    }

    /** ??????????????????????????? */
    private void displayDailyExpenseAmountTotal() {
        final WrappedLong recordCount = new WrappedLong(0);
        final WrappedObject<Double> total = new WrappedObject<>(0D);
        ArrayUtils.forEach(mDailyExpenseList, (count, index, item) -> {
            total.set(total.get() + item.getAmount());
            recordCount.set(recordCount.get() + item.getCount());
        });

        int year = mStartDate.get(Calendar.YEAR);
        int month = mStartDate.get(Calendar.MONTH) + 1;
        String money = "??" + TallyUtils.formatDisplayMoney(total.get());
        mCurrentDateText.set(ResUtils.getString(getApplication(), R.string.tally_bill_of_month_format, year, month));
        mExpenseTotalAmountText.set(ResUtils.getString(getApplication(), R.string.tally_expense_data_format, recordCount.get(), money));
    }

    /** ??????????????????????????? */
    private void displayDailyIncomeAmountTotal() {
        final WrappedLong recordCount = new WrappedLong(0);
        final WrappedObject<Double> total = new WrappedObject<>(0D);
        ArrayUtils.forEach(mDailyIncomeList, (count, index, item) -> {
            total.set(total.get() + item.getAmount());
            recordCount.set(recordCount.get() + item.getCount());
        });

        int year = mStartDate.get(Calendar.YEAR);
        int month = mStartDate.get(Calendar.MONTH) + 1;
        String money = "??" + TallyUtils.formatDisplayMoney(total.get());
        mCurrentDateText.set(ResUtils.getString(getApplication(), R.string.tally_bill_of_month_format, year, month));
        mIncomeTotalAmountText.set(ResUtils.getString(getApplication(), R.string.tally_income_data_format, recordCount.get(), money));
    }

    /** ??????????????????????????? */
    private void displayMonthlyExpenseAmountTotal() {
        final WrappedLong recordCount = new WrappedLong(0);
        final WrappedObject<Double> total = new WrappedObject<>(0D);
        ArrayUtils.forEach(mMonthlyExpenseList, (count, index, item) -> {
            total.set(total.get() + item.getAmount());
            recordCount.set(recordCount.get() + item.getCount());
        });

        int year = mStartDate.get(Calendar.YEAR);
        String money = "??" + TallyUtils.formatDisplayMoney(total.get());
        mCurrentDateText.set(ResUtils.getString(getApplication(), R.string.tally_bill_of_year_format, year));
        mExpenseTotalAmountText.set(ResUtils.getString(getApplication(), R.string.tally_expense_data_format, recordCount.get(), money));
    }

    /** ??????????????????????????? */
    private void displayMonthlyIncomeAmountTotal() {
        final WrappedLong recordCount = new WrappedLong(0);
        final WrappedObject<Double> total = new WrappedObject<>(0D);
        ArrayUtils.forEach(mMonthlyIncomeList, (count, index, item) -> {
            total.set(total.get() + item.getAmount());
            recordCount.set(recordCount.get() + item.getCount());
        });

        int year = mStartDate.get(Calendar.YEAR);
        String money = "??" + TallyUtils.formatDisplayMoney(total.get());
        mCurrentDateText.set(ResUtils.getString(getApplication(), R.string.tally_bill_of_year_format, year));
        mIncomeTotalAmountText.set(ResUtils.getString(getApplication(), R.string.tally_income_data_format, recordCount.get(), money));
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????
     *
     * @param startDate     ????????????
     * @param endDate       ????????????
     * @param dailyDataList ????????????
     * @return ????????????????????????
     */
    private List<DailyData> completeEmptyDailyData(long startDate, long endDate, List<DailyData> dailyDataList) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(startDate);
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH) + 1;
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(endDate);
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH) + 1;
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);

        // ???????????????????????????????????????????????????????????????????????????
        int totalDayCount = (int) ((endDate - startDate) / TimeUtils.DAY_MILLSECONDS + 2);
        // ?????????????????????????????????????????????
        List<DailyData> result = new ArrayList<>(totalDayCount);

        // ?????? ??????????????????????????????????????????????????????????????????????????????
        // dayCursor ?????????????????????????????????????????????????????????????????????????????????????????????+1
        int dayCursor = 0;
        for (int year = startYear; year <= endYear; year++) {
            int monthStart = startYear == endYear ? startMonth : 1;
            int monthEnd = year == endYear ? endMonth : 12;
            for (int month = monthStart; month <= monthEnd; month++) {
                int monthMaxDayCount = TimeUtils.getDaysTotalOfMonth(year, month);
                for (int day = 1; day <= monthMaxDayCount; day++) {
                    DailyData nextDailyData = dailyDataList != null && dailyDataList.size() > dayCursor ? dailyDataList.get(dayCursor) : null;
                    if (nextDailyData != null
                            && nextDailyData.getYear() == year
                            && nextDailyData.getMonth() == month
                            && nextDailyData.getDayOfMonth() == day) {
                        result.add(nextDailyData);
                        dayCursor++;
                    } else {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month - 1);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        DailyData dailyData = new DailyData();
                        dailyData.setTimeMillis(calendar.getTimeInMillis());
                        dailyData.setYear(year);
                        dailyData.setMonth(month);
                        dailyData.setDayOfMonth(day);
                        dailyData.setAmount(0);
                        result.add(dailyData);
                    }
                }
            }
        }

        return result;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????
     *
     * @param startDate       ????????????
     * @param endDate         ????????????
     * @param monthlyDataList ????????????
     * @return ????????????????????????
     */
    private List<MonthlyData> completeEmptyMonthlyData(long startDate, long endDate, List<MonthlyData> monthlyDataList) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(startDate);
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH) + 1;

        calendar.setTimeInMillis(endDate);
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH) + 1;

        // ?????????????????????????????????????????????
        List<MonthlyData> result = new ArrayList<>();

        // ?????? ??????????????????????????????????????????????????????????????????????????????
        // dayCursor ?????????????????????????????????????????????????????????????????????????????????????????????+1
        int monthCursor = 0;
        for (int year = startYear; year <= endYear; year++) {
            int monthStart = startYear == endYear ? startMonth : 1;
            for (int month = monthStart; month <= 12; month++) {
                MonthlyData nextMonthlyData = monthlyDataList != null && monthlyDataList.size() > monthCursor ? monthlyDataList.get(monthCursor) : null;
                if (nextMonthlyData != null
                        && nextMonthlyData.getMonth() != null
                        && nextMonthlyData.getMonth().getYear() == year
                        && nextMonthlyData.getMonth().getMonth() == month) {
                    result.add(nextMonthlyData);
                    monthCursor++;
                } else {
                    MonthlyData dailyData = new MonthlyData();
                    dailyData.setMonth(new Month(year, month));
                    dailyData.setAmount(0);
                    result.add(dailyData);
                }
            }
        }

        return result;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate(LifecycleOwner owner) {
        Intent intent = ((Activity) owner).getIntent();
        int year = intent.getIntExtra(TallyChartActivity.EXTRA_YEAR, -1);
        int month = intent.getIntExtra(TallyChartActivity.EXTRA_MONTH, -1);

        // ?????????????????????
        mStartDate = Calendar.getInstance();
        mEndDate = Calendar.getInstance();
        // ???????????????????????????
        int currentYear = mStartDate.get(Calendar.YEAR);
        int currentMonth = mStartDate.get(Calendar.MONTH) + 1;
        // ?????????????????????????????????????????????????????????
        if (year > 0 && month > 0) {
            currentYear = year;
            currentMonth = month;
        }
        Pair<Long, Long> currentMonthRange = DateUtils.monthDateRange(currentYear, currentMonth);

        // ??????????????????????????????
        mStartDate.setTimeInMillis(currentMonthRange.first);
        mEndDate.setTimeInMillis(currentMonthRange.second);

        // ???????????????
        init();
        refreshData();
    }
}
