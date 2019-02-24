package ru.javaproject.studentorder.validator;

import ru.javaproject.studentorder.domain.AnswerChildren;
import ru.javaproject.studentorder.domain.StudentOrder;

public class ChildrenValidator {
    public AnswerChildren checkChildren(StudentOrder so) {
        System.out.println("Children check is running");
        AnswerChildren ans = new AnswerChildren();
        return ans;
    }
}
