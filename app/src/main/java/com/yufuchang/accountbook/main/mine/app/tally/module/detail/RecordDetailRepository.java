package com.yufuchang.accountbook.main.mine.app.tally.module.detail;


import com.yufuchang.accountbook.base.common.Callback;
import com.yufuchang.accountbook.base.common.IError;
import com.yufuchang.accountbook.base.common.NonThrowError;
import com.yufuchang.accountbook.base.common.SimpleCallback;
import com.yufuchang.accountbook.base.error.ErrorCode;
import com.yufuchang.accountbook.main.concurrency.MineExecutors;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.Record;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.sql.TallyDatabase;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.sql.entity.RecordEntity;


class RecordDetailRepository {

    void queryExpense(long expenseId, Callback<Record, IError> callback) {
        MineExecutors.ioExecutor().execute(() -> {
            Record expense = TallyDatabase.getInstance().recordDao().queryById(expenseId);
            if (expense == null) {
                MineExecutors.executeOnUiThread(() -> callback.failure(new NonThrowError(ErrorCode.SQL_ERR, "EMPTY DATA")));
            } else {
                MineExecutors.executeOnUiThread(() -> callback.success(expense));
            }
        });
    }

    void queryIncome(long incomeId, Callback<Record, IError> callback) {
        MineExecutors.ioExecutor().execute(() -> {
            Record income = TallyDatabase.getInstance().recordDao().queryById(incomeId);
            if (income == null) {
                MineExecutors.executeOnUiThread(() -> callback.failure(new NonThrowError(ErrorCode.SQL_ERR, "EMPTY DATA")));
            } else {
                MineExecutors.executeOnUiThread(() -> callback.success(income));
            }
        });
    }

    void deleteExpense(long expenseId, SimpleCallback<Boolean> callback) {
        MineExecutors.ioExecutor().execute(() -> {
            RecordEntity entity = new RecordEntity();
            entity.setId(expenseId);
            TallyDatabase.getInstance().recordDao().delete(entity);
            MineExecutors.executeOnUiThread(() -> callback.success(true));
        });
    }

    void deleteIncome(long incomeId, SimpleCallback<Boolean> callback) {
        MineExecutors.ioExecutor().execute(() -> {
            RecordEntity entity = new RecordEntity();
            entity.setId(incomeId);
            TallyDatabase.getInstance().recordDao().delete(entity);
            MineExecutors.executeOnUiThread(() -> callback.success(true));
        });
    }
}
