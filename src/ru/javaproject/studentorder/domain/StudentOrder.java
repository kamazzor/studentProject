package ru.javaproject.studentorder.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Итоговый класс студенческой заявки, использующий созданные классы Adult, Child
public class StudentOrder {
    private long studentOrderId;                    // номер студенческой заявки
    private LocalDateTime studentOrderDate;         // дата и время подачи студенческой заявки
    private StudentOrderStatus studentOrderStatus;  // статус студенческой заявки enum-типа
    private String marriageCertificateId;           // номер сертификата о браке
    private LocalDate marriageDate;                 // дата заключения брака
    private RegisterOffice marriageOffice;          // название Отдела ЗАГСА
    private Adult husband;                          // жених
    private Adult wife;                             // невеста
    private List<Child> children;                   // ребенок
    public long getStudentOrderId() {
        return studentOrderId;
    }

    public void setStudentOrderId(long studentOrderId) {
        this.studentOrderId = studentOrderId;
    }

    public LocalDateTime getStudentOrderDate() {
        return studentOrderDate;
    }

    public void setStudentOrderDate(LocalDateTime studentOrderDate) {
        this.studentOrderDate = studentOrderDate;
    }

    public StudentOrderStatus getStudentOrderStatus() {
        return studentOrderStatus;
    }

    public void setStudentOrderStatus(StudentOrderStatus studentOrderStatus) {
        this.studentOrderStatus = studentOrderStatus;
    }

    public String getMarriageCertificateId() {
        return marriageCertificateId;
    }

    public void setMarriageCertificateId(String marriageCertificateId) {
        this.marriageCertificateId = marriageCertificateId;
    }

    public LocalDate getMarriageDate() {
        return marriageDate;
    }

    public void setMarriageDate(LocalDate marriageDate) {
        this.marriageDate = marriageDate;
    }

    public RegisterOffice getMarriageOffice() {
        return marriageOffice;
    }

    public void setMarriageOffice(RegisterOffice marriageOffice) {
        this.marriageOffice = marriageOffice;
    }

    public Adult getHusband() {
        return husband;
    }

    public void setHusband(Adult husband) {
        this.husband = husband;
    }

    public Adult getWife() {
        return wife;
    }

    public void setWife(Adult wife) {
        this.wife = wife;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void addChild(Child child){
        //Проверяем, пустой ли список
        if (children == null){
            children = new ArrayList<>(5);
        }
        this.children.add(child);
    }
}

