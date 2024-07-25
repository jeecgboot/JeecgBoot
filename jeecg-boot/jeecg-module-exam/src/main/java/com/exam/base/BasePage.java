package com.exam.base;

/**
 * @version 3.3.0
 * @description: The type Base page.
 * Copyright (C), 2020-2024
 * @date 2021/5/25 10:45
 */
public class BasePage {

    private Integer pageIndex;

    private Integer pageSize;

    /**
     * Gets page index.
     *
     * @return the page index
     */
    public Integer getPageIndex() {
        return pageIndex;
    }

    /**
     * Sets page index.
     *
     * @param pageIndex the page index
     */
    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * Gets page size.
     *
     * @return the page size
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * Sets page size.
     *
     * @param pageSize the page size
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
