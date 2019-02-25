package ru.javaproject.studentorder.dao;

import ru.javaproject.studentorder.domain.StudentOrder;
import ru.javaproject.studentorder.exception.DaoException;

import java.util.List;

//Интерфейс StudentOrderDao описывает методы для сохранения данных студенческой заявки
public interface StudentOrderDao {
    //Метод сохранения данных студенческой заявки в БД
    Long saveStudentOrder (StudentOrder so) throws DaoException;

    //Метод получения данных студенческих заявок из БД со статусом 0 (START)
    List<StudentOrder> getStudentOrders() throws DaoException;
}
