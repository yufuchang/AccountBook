package com.yufuchang.accountbook.main.mine.app.tally.ui.dialog;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.v4.app.DialogFragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.base.utils.ResUtils;
import com.yufuchang.accountbook.main.framework.BaseViewModel;
import com.yufuchang.accountbook.main.framework.ViewReliedTask;
import com.yufuchang.accountbook.main.mine.app.tally.common.router.TallyRouter;

import java.util.ArrayList;
import java.util.List;



public class MenuDialogViewModel extends BaseViewModel {

    /** 菜单列表 */
    private MutableLiveData<List<MenuDialogItem>> mMenuList = new MutableLiveData<>();
    private MutableLiveData<ViewReliedTask<DialogFragment>> mViewReliedTask = new MutableLiveData<>();

    public MenuDialogViewModel(Application application) {
        super(application);
        init();
    }

    LiveData<List<MenuDialogItem>> getMenuList() {
        return mMenuList;
    }

    LiveData<ViewReliedTask<DialogFragment>> getViewReliedTask() {
        return mViewReliedTask;
    }

    /** 菜单点击 */
    public void onMenuClick(MenuDialogItem item) {
        if (item == null) {
            return;
        }
        mViewReliedTask.setValue(dialog -> {
            if (dialog.getActivity() == null) {
                return;
            }
            dialog.dismiss();
            ARouter.getInstance().build(item.getPath()).navigation(dialog.getActivity());
        });
    }

    /** 关闭点击 */
    public void onCloseClick() {
        mViewReliedTask.setValue(DialogFragment::dismiss);
    }

    private void init() {
        List<MenuDialogItem> menuList = new ArrayList<>();
        // 关于
        menuList.add(new MenuDialogItem(
                ResUtils.getString(getApplication(), R.string.menu_tall_about),
                TallyRouter.ABOUT,
                ResUtils.getDrawable(getApplication(), R.drawable.ic_about)));
//        // 备份文件
//        menuList.add(new MenuDialogItem(
//                ResUtils.getString(getApplication(), R.string.tally_toolbar_title_backup_file),
//                TallyRouter.BACKUP_FILE,
//                ResUtils.getDrawable(getApplication(), R.drawable.ic_backup_file)));
        // 账单记录
        menuList.add(new MenuDialogItem(
                ResUtils.getString(getApplication(), R.string.menu_tally_records),
                TallyRouter.RECORDS,
                ResUtils.getDrawable(getApplication(), R.drawable.ic_list)));
        // 图表
        menuList.add(new MenuDialogItem(
                ResUtils.getString(getApplication(), R.string.menu_tally_chart),
                TallyRouter.CHART,
                ResUtils.getDrawable(getApplication(), R.drawable.ic_chart)));
        mMenuList.setValue(menuList);
    }
}
