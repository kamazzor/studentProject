package edu.javacourse.studentorder.domain.register;

import edu.javacourse.studentorder.domain.Person;

//В классе собраны данные по результатам проверки только 1 заявки в виде списка объектов.
//Каждый элемент списка содержит инфу про 1 персону, включая информацию о том, кого проверяли ,какой результат проверки
// и об ошибках при запросе, если они есть.
// статус объекта, что за персона, класс для описания ошибки, содержащций текст и код ошибки, плюс
//индикатор того, связана ли ошибка с плохой связью с ГРН
public class AnswerCityRegisterItem {
    //Перечисляемый список (enum) возможных статусов проверки персоны из ГРН
    public enum CityStatus {
        YES, NO, ERROR;
    }

    //Класс с описанием ошибки: текст и код ошибки. Класс статичный, чтобы
    // к его полям и методам можно было обратиться прямо из класса AnswerCityRegisterItem
    public static class CityError {
        private String code;
        private String text;

        public CityError(String code, String text){
            this.code = code;
            this.text = text;
        }

        public String getCode(){
            return code;
        }

        public String getText(){
            return text;
        }
    }
    private CityStatus status;
    private Person person;
    private CityError error;

    public AnswerCityRegisterItem(CityStatus status, Person person){
        this.status = status;
        this.person = person;
    }

    public AnswerCityRegisterItem(CityStatus status, Person person, CityError error){
        this.status = status;
        this.person = person;
        this.error = error;
    }

    public Person getPerson() {
        return person;
    }

    public CityStatus getStatus() {
        return status;
    }

    public CityError getError() {
        return error;
    }

}
