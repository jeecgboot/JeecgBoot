package com.exam.domain.question;



import lombok.Data;

import java.util.List;

@Data
public class QuestionObject {

    private String titleContent;

    private String analyze;

    private List<QuestionItemObject> questionItemObjects;

    private String correct;
}
