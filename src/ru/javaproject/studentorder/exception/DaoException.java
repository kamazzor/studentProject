package ru.javaproject.studentorder.exception;

//Класс DaoException обрабатывает ошибки при обращении к словарб адресов, (?) имён
public class DaoException extends Exception {
    public DaoException(){
    }

    public DaoException(String message){
        super(message);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }

    public DaoException(String message, Throwable cause){
        super(message, cause);
    }
}
