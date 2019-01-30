package edu.javacourse.studentorder;

import edu.javacourse.studentorder.domain.*;
import edu.javacourse.studentorder.domain.register.AnswerCityRegister;
import edu.javacourse.studentorder.domain.student.AnswerStudent;
import edu.javacourse.studentorder.domain.wedding.AnswerWedding;
import edu.javacourse.studentorder.mail.MailSender;
import edu.javacourse.studentorder.validator.ChildrenValidator;
import edu.javacourse.studentorder.validator.CityRegisterValidator;
import edu.javacourse.studentorder.validator.StudentValidator;
import edu.javacourse.studentorder.validator.WeddingValidator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// Класс, проверающий заявку по всем критериям студенческой заявки.

public class StudentOrderValidator {
    private CityRegisterValidator cityRegisterVal;
    private WeddingValidator weddingVal;
    private ChildrenValidator childrenVal;
    private StudentValidator studentVal;
    private MailSender mailSender;


    public StudentOrderValidator() {
        cityRegisterVal = new CityRegisterValidator();
        weddingVal = new WeddingValidator();
        childrenVal = new ChildrenValidator();
        studentVal = new StudentValidator();
        mailSender = new MailSender();
    }

    public static void main(String[] args) {
        StudentOrderValidator sov = new StudentOrderValidator();
        sov.checkAll();
    }


    // check all conditions
    public void checkAll() {
        List<StudentOrder> soList = readStudentOrders();

        // print all readed Orders using "foreach" cycle
        for (StudentOrder so : soList) {
            System.out.println();
            checkOneOrder(so);
        }
    }

    //read Student orders
    public List<StudentOrder> readStudentOrders() {
        List<StudentOrder> soList = new LinkedList<>();

        for (int c = 0; c < soList.size(); c++) {
            soList.add(SaveStudentOrder.buildStudentOrder(c));
        }

        return soList;
    }

    public void checkOneOrder(StudentOrder so) {
        AnswerCityRegister cityAnswer = checkCityRegister(so);
        //временно закомментили для проверки
//        AnswerWedding weddingAnswer = checkWedding(so);
//        AnswerChildren childrenAnswer = checkChildren(so);
//        AnswerStudent studentAnswer = checkStudent(so);

//        sendMail(so);
    }

    //check City Register of orderer
    public AnswerCityRegister checkCityRegister(StudentOrder so) {
//        CityRegisterValidator crv = new CityRegisterValidator();
//        return crv.checkCityRegister(so);
        return cityRegisterVal.checkCityRegister(so);
    }

    // check Wedding status of orderer
    public AnswerWedding checkWedding(StudentOrder so) {
//        WeddingValidator wd = new WeddingValidator();
//        return wd.checkWedding(so);
        return weddingVal.checkWedding(so);
    }


    // check if orderer have childrens
    public AnswerChildren checkChildren(StudentOrder so) {
//        ChildrenValidator cv = new ChildrenValidator();
//        return cv.checkChildren(so);
        return childrenVal.checkChildren(so);
    }

    // check if orderer is student
    public AnswerStudent checkStudent(StudentOrder so) {
        // another form of checkStudent request
//        return new StudentValidator().checkStudent(so);
        return studentVal.checkStudent(so);
    }


    // email result to orderer
    public void sendMail(StudentOrder so) {
//        new MailSender().sendMail(so);
        mailSender.sendMail(so);
    }
}
