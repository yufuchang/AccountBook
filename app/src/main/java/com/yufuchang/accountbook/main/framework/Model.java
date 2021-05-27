package com.yufuchang.accountbook.main.framework;

import android.os.Bundle;
import android.support.annotation.Nullable;



public interface Model<Q extends QueryEnum, UA extends UserActionEnum,M extends Model, E> {
    Q[] getQueries();

    UA[] getUserActions();

    void deliverUserAction(UA action,
                           @Nullable Bundle args,
                           UserActionCallback<M, UA, E> callback);

    void requestData(Q query, DataQueryCallback<M, Q, E> callback);

    void cleanUp();

    interface DataQueryCallback<M extends Model, Q extends QueryEnum, E> {
        void onModelUpdated(M model, Q query);

        void onError(Q query, E error);
    }

    interface UserActionCallback<M extends Model, UA extends UserActionEnum, E> {
        void onModelUpdated(M model, UA userAction);

        void onError(UA userAction, E error);
    }
}
