package com.agan.exam.base;

import com.agan.exam.model.PaperForm1;
import com.agan.exam.model.QuestionType;

import java.util.ArrayList;

public class PaperTemplate {
    // 试卷基础模板1
    public PaperForm1 PaperTemplate01(){
        return new PaperForm1(1,
                6,5,
                2,10,
                0,0,
                2,5,
                2,20);
    };

    public ArrayList<QuestionType> QuestionTypeTemplate01(){
        ArrayList<QuestionType> questionTypes = new ArrayList<>();
        questionTypes.add(new QuestionType(1,"单选题"));
        questionTypes.add(new QuestionType(2,"多选题"));
        questionTypes.add(new QuestionType(3,"判断题"));
        questionTypes.add(new QuestionType(4,"填空题"));
        questionTypes.add(new QuestionType(5,"简答题"));
        return questionTypes;
    }

}
