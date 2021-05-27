package com.yufuchang.accountbook.main.mine.app.tally.module.edit.category;



import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.base.common.IError;
import com.yufuchang.accountbook.base.common.NonThrowError;
import com.yufuchang.accountbook.base.common.Result;
import com.yufuchang.accountbook.base.common.SimpleCallback;
import com.yufuchang.accountbook.base.error.ErrorCode;
import com.yufuchang.accountbook.base.utils.ArrayUtils;
import com.yufuchang.accountbook.base.utils.CommonUtils;
import com.yufuchang.accountbook.base.utils.ResUtils;
import com.yufuchang.accountbook.main.concurrency.MineExecutors;
import com.yufuchang.accountbook.main.mine.WorkerApp;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.CategoryModel;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.sql.TallyDatabase;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.sql.dao.CategoryDao;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.sql.entity.CategoryEntity;

import java.util.List;



class CategoryRepository {

    /**
     * 查询分类
     *
     * @param categoryId 分类 ID
     * @param callback   回调
     */
    void queryCategoryById(long categoryId, SimpleCallback<CategoryModel> callback) {
        MineExecutors.ioExecutor().execute(() -> {
            CategoryModel categoryModel = TallyDatabase.getInstance().categoryDao().queryById(categoryId);
            callback.success(categoryModel);
        });
    }

    /**
     * 读取所有支出分类
     *
     * @param callback 回调
     */
    void loadAllExpenseCategory(SimpleCallback<List<CategoryModel>> callback) {
        MineExecutors.ioExecutor().execute(() -> {
            List<CategoryModel> categoryList = TallyDatabase.getInstance().categoryDao().allExpenseCategory();
            callback.success(categoryList);
        });
    }

    /**
     * 读取所有支出分类
     *
     * @param callback 回调
     */
    void loadAllIncomeCategory(SimpleCallback<List<CategoryModel>> callback) {
        MineExecutors.ioExecutor().execute(() -> {
            List<CategoryModel> categoryList = TallyDatabase.getInstance().categoryDao().allIncomeCategory();
            callback.success(categoryList);
        });
    }

    /**
     * 修改分类名称
     *
     * @param categoryId 分类 ID
     * @param icon       分类图标
     * @param name       分类名称
     * @param callback   回调
     */
    void updateCategory(long categoryId, String icon, String name, SimpleCallback<CategoryModel> callback) {
        MineExecutors.ioExecutor().execute(() -> {
            TallyDatabase.getInstance().categoryDao().update(categoryId, icon, name);
            CategoryModel categoryModel = TallyDatabase.getInstance().categoryDao().queryById(categoryId);
            callback.success(categoryModel);
        });
    }

    /**
     * 更新分类排序
     *
     * @param categoryList 分类列表
     * @param callback     回调
     */
    void updateCategoryOrder(List<CategoryModel> categoryList, SimpleCallback<Void> callback) {
        MineExecutors.ioExecutor().execute(() -> {
            CategoryDao categoryDao = TallyDatabase.getInstance().categoryDao();
            ArrayUtils.forEach(categoryList, (count, index, item) -> {
                categoryDao.updateOrder(item.getId(), item.getOrder());
            });
            callback.success(null);
        });
    }

    /**
     * 添加分类
     *
     * @param type       分类类型 {@link CategoryModel#TYPE_EXPENSE} {@link CategoryModel#TYPE_INCOME}
     * @param uniqueName 分类标识
     * @param iconName   分类图标
     * @param name       分类名称
     * @param callback   回调
     */
    void addCategory(int type,
                     String uniqueName,
                     String iconName,
                     String name,
                     SimpleCallback<Result<Void, IError>> callback) {
        MineExecutors.ioExecutor().execute(() -> {
            CategoryDao categoryDao = TallyDatabase.getInstance().categoryDao();

            List<CategoryModel> categoryList = categoryDao.allCategory();
            boolean alreadyContains = ArrayUtils.contains(categoryList, item -> {
                return CommonUtils.isEqual(item.getUniqueName(), uniqueName);
            });
            // 已经存在，不重复添加
            if (alreadyContains) {
                callback.success(new Result<>(null, new NonThrowError(ErrorCode.ILLEGAL_ARGS,
                        ResUtils.getString(WorkerApp.getAppContext(), R.string.err_category_already_exist))));
                return;
            }

            CategoryEntity entity = new CategoryEntity();
            entity.setType(type);
            entity.setUniqueName(uniqueName);
            entity.setIcon(iconName);
            entity.setName(name);
            TallyDatabase.getInstance().categoryDao().insert(entity);

            callback.success(new Result<>(null, null));
        });
    }
}
