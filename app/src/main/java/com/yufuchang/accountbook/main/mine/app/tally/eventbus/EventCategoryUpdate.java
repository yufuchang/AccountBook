package com.yufuchang.accountbook.main.mine.app.tally.eventbus;


import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.CategoryModel;



public class EventCategoryUpdate {

    private CategoryModel category;

    public EventCategoryUpdate(CategoryModel category) {
        this.category = category;
    }

    public CategoryModel getCategory() {
        return category;
    }
}
