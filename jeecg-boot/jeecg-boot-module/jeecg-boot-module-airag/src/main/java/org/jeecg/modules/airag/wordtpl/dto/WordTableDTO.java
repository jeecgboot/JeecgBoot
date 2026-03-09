package org.jeecg.modules.airag.wordtpl.dto;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class WordTableDTO {

    private String value = "";

    private String type = "table";

    private List<WordTableRowDTO> trList;

    private int width;

    private int height;

    private List<JSONObject> colgroup = new ArrayList<>();
}
