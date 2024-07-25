package com.exam.utility;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

/**
 * @version 3.5.0
 * @description: The type Model mapper single.
 * Copyright (C), 2020-2024
 * @date 2021/12/25 9:45
 */
public class ModelMapperSingle {
    /**
     * The constant modelMapper.
     */
    protected final static ModelMapper modelMapper = new ModelMapper();
    private final static ModelMapperSingle modelMapperSingle = new ModelMapperSingle();

    static {
        modelMapper.getConfiguration().setFullTypeMatchingRequired(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    /**
     * Instance model mapper.
     *
     * @return the model mapper
     */
    public static ModelMapper Instance() {
        return modelMapperSingle.modelMapper;
    }
}
