package com.yufuchang.accountbook.main.framework;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

public interface UpdatableView<M, Q extends QueryEnum, UA extends UserActionEnum, E> {

    void displayData(M model, Q query);

    void displayErrorMessage(Q query, E error);

    void displayUserActionResult(M model, Bundle args, UA userAction, boolean success, E error);

    Uri getDataUri(Q query);

    Context getContext();

    void addListener(UserActionListener<UA> listener);

    interface UserActionListener<UA extends UserActionEnum> {

        void onUserAction(UA action, @Nullable Bundle args);
    }
}
