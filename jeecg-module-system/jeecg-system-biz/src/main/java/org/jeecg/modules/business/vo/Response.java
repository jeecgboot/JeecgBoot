package org.jeecg.modules.business.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<R, E> {
    private R data;
    private E error;
    private int status;

    public Response(E error) {
        this.error = error;
    }
}
