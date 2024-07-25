package com.exam.utility;

import com.github.pagehelper.PageInfo;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @version 3.5.0
 * @description: The type Page info helper.
 * Copyright (C), 2020-2024
 * @date 2021/12/25 9:45
 */
public class PageInfoHelper {

    /**
     * Copy map page info.
     *
     * @param <T>    the type parameter
     * @param <J>    the type parameter
     * @param source the source
     * @param mapper the mapper
     * @return the page info
     */
    public static <T, J> PageInfo<J> copyMap(PageInfo<T> source, Function<? super T, ? extends J> mapper) {
        PageInfo<J> newPage = new PageInfo<>();
        newPage.setPageNum(source.getPageNum());
        newPage.setPageSize(source.getPageSize());
        newPage.setSize(source.getSize());
        newPage.setStartRow(source.getStartRow());
        newPage.setEndRow(source.getEndRow());
        newPage.setTotal(source.getTotal());
        newPage.setPages(source.getPages());
        newPage.setList(source.getList().stream().map(mapper).collect(Collectors.toList()));
        newPage.setPrePage(source.getPrePage());
        newPage.setNextPage(source.getNextPage());
        newPage.setIsFirstPage(source.isIsFirstPage());
        newPage.setIsLastPage(source.isIsLastPage());
        newPage.setHasPreviousPage(source.isHasPreviousPage());
        newPage.setHasNextPage(source.isHasNextPage());
        newPage.setNavigatePages(source.getNavigatePages());
        newPage.setNavigatepageNums(source.getNavigatepageNums());
        newPage.setNavigateFirstPage(source.getNavigateFirstPage());
        newPage.setNavigateLastPage(source.getNavigateLastPage());
        return newPage;
    }
}
