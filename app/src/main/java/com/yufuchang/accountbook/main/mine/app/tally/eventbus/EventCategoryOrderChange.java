package com.yufuchang.accountbook.main.mine.app.tally.eventbus;



public class EventCategoryOrderChange {

    private final int categoryType;

    /**
     * 构造函数
     *
     * @param categoryType 分类类型
     */
    public EventCategoryOrderChange(int categoryType) {
        this.categoryType = categoryType;
    }

    public int getCategoryType() {
        return categoryType;
    }
}
