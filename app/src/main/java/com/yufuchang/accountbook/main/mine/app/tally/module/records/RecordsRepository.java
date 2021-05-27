package com.yufuchang.accountbook.main.mine.app.tally.module.records;


import com.yufuchang.accountbook.base.common.Callback;
import com.yufuchang.accountbook.base.common.IError;
import com.yufuchang.accountbook.base.common.NonThrowError;
import com.yufuchang.accountbook.base.error.ErrorCode;
import com.yufuchang.accountbook.main.concurrency.MineExecutors;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.Record;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.sql.TallyDatabase;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.sql.dao.RecordDao;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.sql.entity.RecordEntity;

import java.util.List;



class RecordsRepository {

    /**
     * 查询记录
     *
     * @param page     页号
     * @param pageSize 每页数量
     * @param query    查询条件
     * @param callback 回调
     */
    void queryRecords(int page, int pageSize, RecordQuery query, Callback<List<Record>, IError> callback) {
        MineExecutors.ioExecutor().execute(() -> {

            int type = query.getType();
            long startTime = query.getStartTime();
            long endTime = query.getEndTime();
            long limit = pageSize;
            long offset = ((page - 1) * pageSize);
            String[] categoryArray = query.getCategoryUniqueNameArray();

            List<Record> recordList;
            RecordDao recordDao = TallyDatabase.getInstance().recordDao();

            if (type == RecordQuery.TYPE_ALL) {
                recordList = categoryArray != null ?
                        recordDao.queryAll(startTime, endTime, limit, offset, categoryArray) :
                        recordDao.queryAll(startTime, endTime, limit, offset);
            } else {
                recordList = categoryArray != null ?
                        recordDao.query(type, startTime, endTime, limit, offset, categoryArray) :
                        recordDao.query(type, startTime, endTime, limit, offset);
            }
            callback.success(recordList);
        });
    }

    /** 删除消费记录 */
    public void deleteRecord(long recordId, Callback<Void, IError> callback) {
        MineExecutors.ioExecutor().execute(() -> {
            try {
                RecordEntity entity = new RecordEntity();
                entity.setId(recordId);
                TallyDatabase.getInstance().recordDao().delete(entity);
                MineExecutors.executeOnUiThread(() -> callback.success(null));
            } catch (Exception e) {
                MineExecutors.executeOnUiThread(() -> callback.failure(new NonThrowError(ErrorCode.SQL_ERR, "SQL ERR")));
            }
        });
    }
}
