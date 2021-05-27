package com.yufuchang.accountbook.main.mine.persistence.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;


@Keep
@Entity(tableName = "key_value", indices = {@Index("key")}, primaryKeys = {"key"})
public class KeyValue {

    @NonNull
    @ColumnInfo(name = "key")
    private String key;

    @ColumnInfo(name = "value")
    private String value;

    @NonNull
    public String getKey() {
        return key;
    }

    public KeyValue(@NonNull String key, String value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

