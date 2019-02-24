package ru.javaproject.studentorder.dao;

import ru.javaproject.studentorder.domain.StudentOrder;
import ru.javaproject.studentorder.exception.DaoException;

//Интерфейс StudentOrderDao описывает методы для сохранения данных студенческой заявки
public interface StudentOrderDao {
    Long saveStudentOrder (StudentOrder so) throws DaoException;
}
