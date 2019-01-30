package edu.javacourse.studentorder.validator;

import edu.javacourse.studentorder.domain.student.AnswerStudent;
import edu.javacourse.studentorder.domain.StudentOrder;

// Проверка, является ли конкретный заявитель студентом.
public class StudentValidator {
    public AnswerStudent checkStudent(StudentOrder so) {
        System.out.println("Students is checking");
        AnswerStudent ans = new AnswerStudent();
        return ans;
    }
}
