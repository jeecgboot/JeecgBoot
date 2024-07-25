package com.exam.viewmodel;

import com.exam.utility.ModelMapperSingle;
import org.modelmapper.ModelMapper;

/**
 * @version 3.5.0
 * @description: The type Base vm.
 * Copyright (C), 2020-2024
 * @date 2021/12/25 9:45
 */
public class BaseVM {
    /**
     * The constant modelMapper.
     */
    protected static ModelMapper modelMapper = ModelMapperSingle.Instance();


    /**
     * Gets model mapper.
     *
     * @return the model mapper
     */
    public static ModelMapper getModelMapper() {
        return modelMapper;
    }

    /**
     * Sets model mapper.
     *
     * @param modelMapper the model mapper
     */
    public static void setModelMapper(ModelMapper modelMapper) {
        BaseVM.modelMapper = modelMapper;
    }
}
