package com.yufuchang.accountbook.main.mine.app.tally.eventbus;


import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.Record;

public class EventRecordAdd {
    private final Record mRecord;

    public EventRecordAdd(Record record) {
        mRecord = record;
    }

    public Record getRecord() {
        return mRecord;
    }
}
