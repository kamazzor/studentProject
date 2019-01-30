package edu.javacourse.studentorder.exception;

//Класс с данными ошибки, присланной из ГРН
public class CityRegisterException extends Exception {

    //код ошибки из ГРН
    private String code;

    public CityRegisterException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CityRegisterException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
