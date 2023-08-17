package org.jeecg.loader.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredicatesVo {
    private String  name;
    private List<String> args;
}
