package org.jeecg.modules.business.vo;

import lombok.Data;

@Data
public class Response<R, E> {
    private R data;
    private E error;
    private int status;
}
