package com.yufuchang.accountbook.main.mine.persistence.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.yufuchang.accountbook.main.mine.WorkerApp;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.sql.TallyDatabase;
import com.yufuchang.accountbook.main.mine.persistence.dao.KeyValueDao;
import com.yufuchang.accountbook.main.mine.persistence.entity.KeyValue;


@Database(entities = {KeyValue.class}, version = 60, exportSchema = false)
public abstract class MineDatabase extends RoomDatabase {
    /** sqlite db name */
    private static final String DATABASE_NAME = "sql_mine";

    /** db version of app version 0.6.0 */
    private static final int VERSION_0_6_0 = 60;

    private static MineDatabase sInstance = null;

    /**
     * key-value 数据表操作
     *
     * @return key-value 数据表操作
     */
    public abstract KeyValueDao keyValueDao();

    public static MineDatabase getInstance() {
        if (sInstance == null) {
            synchronized (TallyDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(
                            WorkerApp.getAppContext(),
                            MineDatabase.class, DATABASE_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return sInstance;
    }
}
