package org.jeecg.loader.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 路由配置VO
 * @author lsq
 * @Date 2023/10/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredicatesVo {
    private String  name;
    private List<String> args;
}