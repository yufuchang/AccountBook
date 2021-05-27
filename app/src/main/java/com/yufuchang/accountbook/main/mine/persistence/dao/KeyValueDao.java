package com.yufuchang.accountbook.main.mine.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.yufuchang.accountbook.main.mine.persistence.entity.KeyValue;



@Dao
public interface KeyValueDao {

    /**
     * 插入 K-V 键值对
     *
     * @param keyValues 键值对
     * @return id array
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(KeyValue... keyValues);

    /**
     * 插入 K-V 键值对
     *
     * @param keyValue 键值对
     * @return id
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(KeyValue keyValue);

    /**
     * 查询 Key 对应的 Value
     *
     * @param key 键
     * @return 查询结果
     */
    @Query("select * from key_value where `key` = :key limit 1")
    KeyValue query(String key);

    /**
     * 更新键值对
     *
     * @param keyValue 键值对
     */
    @Update
    void update(KeyValue keyValue);

    /**
     * 删除键值对
     *
     * @param key 键
     */
    @Query("delete from key_value where `key` = :key")
    void delete(String key);
}
