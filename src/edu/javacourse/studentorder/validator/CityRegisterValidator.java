package edu.javacourse.studentorder.validator;

import edu.javacourse.studentorder.domain.Child;
import edu.javacourse.studentorder.domain.Person;
import edu.javacourse.studentorder.domain.register.AnswerCityRegister;
import edu.javacourse.studentorder.domain.register.AnswerCityRegisterItem;
import edu.javacourse.studentorder.domain.register.CityRegisterResponse;
import edu.javacourse.studentorder.domain.StudentOrder;
import edu.javacourse.studentorder.exception.CityRegisterException;
import edu.javacourse.studentorder.validator.register.CityRegisterChecker;
import edu.javacourse.studentorder.validator.register.FakeCityRegisterChecker;

import java.util.List;

//проверяет заявку на все критерии для мужа, жены и ребенка
public class CityRegisterValidator {

    public String hostName; //Адрес Городского реестра
    String login;           //логин для входа в Городской реестр
    String password;        //пароль для входа в Городской реестр

    private CityRegisterChecker personChecker;

    public CityRegisterValidator() {
        // пока заглушка обращения в Городской реестр
        personChecker = new FakeCityRegisterChecker();
    }

    public AnswerCityRegister checkCityRegister(StudentOrder so) {
        AnswerCityRegister ans = new AnswerCityRegister();

        //получаем данные ответа на мужа из Городского реестра и добавляем их
        // в виде объекта в массив ответов по конкретной заявке
        ans.addItem(checkPerson(so.getHusband()));
        // получаем данные ответа на жену из Городского реестра
        ans.addItem(checkPerson(so.getWife()));
        //получаем данные ответа на каждого ребенка из списка детей заявителя из Городского реестра
        for (Child child : so.getChildren()){
            ans.addItem(checkPerson(child));
        }

        return ans;
    }

    private AnswerCityRegisterItem checkPerson(Person person){
        try {
            CityRegisterResponse cans = personChecker.checkPerson(person);
        } catch (CityRegisterException e) {
            e.printStackTrace();
        }
        //временный ответ-заглушка
        return null;
    }
}
