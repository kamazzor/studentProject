package ru.javaproject.studentorder;

import ru.javaproject.studentorder.domain.AnswerChildren;
import ru.javaproject.studentorder.domain.StudentOrder;
import ru.javaproject.studentorder.domain.register.AnswerCityRegister;
import ru.javaproject.studentorder.domain.student.AnswerStudent;
import ru.javaproject.studentorder.domain.wedding.AnswerWedding;
import ru.javaproject.studentorder.mail.MailSender;
import ru.javaproject.studentorder.validator.ChildrenValidator;
import ru.javaproject.studentorder.validator.CityRegisterValidator;
import ru.javaproject.studentorder.validator.StudentValidator;
import ru.javaproject.studentorder.validator.WeddingValidator;

import java.util.LinkedList;
import java.util.List;

// Класс, проверающий заявку по всем критериям студенческой заявки.
// В классе созданы экземпляры проверяльщиков по каждому критерию заявки в отдельности
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

        // print all readed orders
        for (StudentOrder so : soList) {
            System.out.println();
            checkOneOrder(so);
        }
    }

    //read Student orders
    public List<StudentOrder> readStudentOrders() {
        List<StudentOrder> soList = new LinkedList<>();

        for (int c = 0; c < 5; c++) {
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
        return cityRegisterVal.checkCityRegister(so);
    }

    // check Wedding status of orderer
    public AnswerWedding checkWedding(StudentOrder so) {
        return weddingVal.checkWedding(so);
    }


    // check if orderer have childrens
    public AnswerChildren checkChildren(StudentOrder so) {
        return childrenVal.checkChildren(so);
    }

    // check if orderer is student
    public AnswerStudent checkStudent(StudentOrder so) {
        return studentVal.checkStudent(so);
    }


    // email result to orderer
    public void sendMail(StudentOrder so) {
        mailSender.sendMail(so);
    }
}
