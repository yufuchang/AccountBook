package com.yufuchang.accountbook.main.framework;



public interface Presenter<Q extends QueryEnum, UA extends UserActionEnum> {

    void loadInitialQueries();
}
