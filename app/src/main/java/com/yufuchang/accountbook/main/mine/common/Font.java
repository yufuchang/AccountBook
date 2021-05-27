package com.yufuchang.accountbook.main.mine.common;



public enum Font {
    // Quicksand bold
    QUICKSAND_BOLD("Quicksand-Bold.ttf"),
    QUICKSAND_LIGHT("Quicksand-Light.ttf"),
    QUICKSAND_MEDIUM("Quicksand-Medium.ttf"),
    QUICKSAND_REGULAR("Quicksand-Regular.ttf");

    private final String name;

    Font(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
