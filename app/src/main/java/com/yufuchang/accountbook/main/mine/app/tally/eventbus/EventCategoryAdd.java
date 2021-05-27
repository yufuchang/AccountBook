package com.yufuchang.accountbook.main.mine.app.tally.eventbus;


import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.CategoryModel;



public class EventCategoryAdd {

    private CategoryModel category;

    public EventCategoryAdd(CategoryModel category) {
        this.category = category;
    }

    public CategoryModel getCategory() {
        return category;
    }
}
