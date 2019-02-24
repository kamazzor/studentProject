package ru.javaproject.studentorder.validator;

import ru.javaproject.studentorder.domain.student.AnswerStudent;
import ru.javaproject.studentorder.domain.StudentOrder;

// Проверка, является ли конкретный заявитель студентом.
public class StudentValidator {
    public AnswerStudent checkStudent(StudentOrder so) {
        System.out.println("Students is checking");
        AnswerStudent ans = new AnswerStudent();
        return ans;
    }
}
