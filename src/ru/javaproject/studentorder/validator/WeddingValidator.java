package ru.javaproject.studentorder.validator;

import ru.javaproject.studentorder.domain.wedding.AnswerWedding;
import ru.javaproject.studentorder.domain.StudentOrder;

public class WeddingValidator {
    public AnswerWedding checkWedding(StudentOrder so) {
        System.out.println("Wedding is running");
        AnswerWedding ans = new AnswerWedding();
        return ans;
    }
}
