package com.yufuchang.accountbook.main.mine.app.tally.module.records;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.AlertDialog;
import android.view.View;


import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.base.common.Callback;
import com.yufuchang.accountbook.base.common.IError;
import com.yufuchang.accountbook.base.utils.CommonUtils;
import com.yufuchang.accountbook.base.utils.UIUtils;
import com.yufuchang.accountbook.main.framework.BaseViewModel;
import com.yufuchang.accountbook.main.mine.app.tally.eventbus.EventRecordDelete;
import com.yufuchang.accountbook.main.mine.app.tally.module.chart.TallyChartActivity;
import com.yufuchang.accountbook.main.mine.app.tally.module.detail.RecordDetailActivity;
import com.yufuchang.accountbook.main.mine.app.tally.module.edit.RecordEditActivity;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.Record;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;


public class RecordItemViewModel extends BaseViewModel {

    private DecimalFormat mMoneyFormat = new DecimalFormat("0.00");

    private RecordsRepository mRepository;

    public RecordItemViewModel(Application application) {
        super(application);
        mRepository = new RecordsRepository();
    }

    /** 格式化金额 */
    public String formatMoney(Record record) {
        if (record == null) {
            return "--";
        }
        return "¥" + mMoneyFormat.format(record.getAmount());
    }

    /**
     * 记录 ITEM 点击
     *
     * @param view     {@link View}
     * @param activity {@link Activity}
     * @param record   记录 ITEM
     */
    public void onItemClick(View view, Activity activity, Record record) {
        if (CommonUtils.isViewFastDoubleClick(view) || record == null) {
            return;
        }
        if (record.getType() == Record.TYPE_EXPENSE) {
            RecordDetailActivity.openExpenseDetail(activity, record.getId());
        }
        if (record.getType() == Record.TYPE_INCOME) {
            RecordDetailActivity.openIncomeDetail(activity, record.getId());
        }
    }

    /**
     * 记录 ITEM 长按
     *
     * @param view     {@link View}
     * @param activity {@link Activity}
     * @param record   记录 ITEM
     */
    public boolean onItemLongClick(View view, Activity activity, final Record record) {
        new AlertDialog.Builder(activity).setItems(R.array.recordItemLongClickOption, (dialog, which) -> {
            switch (which) {

                // 修改
                case 0:
                    if (record.getType() == Record.TYPE_EXPENSE) {
                        RecordEditActivity.openAsUpdateExpense(activity, record.getId());
                    }
                    if (record.getType() == Record.TYPE_INCOME) {
                        RecordEditActivity.openAsUpdateIncome(activity, record.getId());
                    }
                    break;

                // 删除
                case 1:
                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.dialog_title_delete_confirm)
                            .setNegativeButton(R.string.dialog_btn_cancel, null)
                            .setPositiveButton(R.string.dialog_btn_delete, (dialogInterface, w) -> {
                                // 删除
                                mRepository.deleteRecord(record.getId(), new Callback<Void, IError>() {
                                    @Override
                                    public void success(Void aVoid) {
                                        EventBus.getDefault().post(new EventRecordDelete(record));
                                    }

                                    @Override
                                    public void failure(IError iError) {
                                        UIUtils.showToastShort(getApplication(), iError.msg());
                                    }
                                });
                            }).show();

                    break;
                default:
                    break;
            }
        }).show();
        return true;
    }

    /** 列表日期标题点击 */
    public void onItemDateTitleClick(Activity activity, RecordsDateTitle date) {
        if (date == null) {
            return;
        }
        // 打开对应月份的图表分析页
        TallyChartActivity.open(activity, date.getYear(), date.getMonth());
    }
}
