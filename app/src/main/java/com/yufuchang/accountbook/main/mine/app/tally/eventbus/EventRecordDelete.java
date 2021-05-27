package com.yufuchang.accountbook.main.mine.app.tally.eventbus;


import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.Record;


public class EventRecordDelete {
    private final Record mRecord;

    public EventRecordDelete(Record expense) {
        mRecord = expense;
    }

    public Record getRecord() {
        return mRecord;
    }
}
