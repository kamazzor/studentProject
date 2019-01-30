package edu.javacourse.studentorder.validator;

import edu.javacourse.studentorder.domain.Child;
import edu.javacourse.studentorder.domain.register.AnswerCityRegister;
import edu.javacourse.studentorder.domain.register.CityRegisterCheckerResponse;
import edu.javacourse.studentorder.domain.StudentOrder;
import edu.javacourse.studentorder.exception.CityRegisterException;
import edu.javacourse.studentorder.validator.register.CityRegisterChecker;
import edu.javacourse.studentorder.validator.register.FakeCityRegisterChecker;

import java.util.ArrayList;
import java.util.Iterator;
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

        try {
            //получаем данные ответа на мужа из Городского реестра
            CityRegisterCheckerResponse hans = personChecker.checkPerson(so.getHusband());
            // получаем данные ответа на жену из Городского реестра
            CityRegisterCheckerResponse wans = personChecker.checkPerson(so.getWife());
            //получаем данные ответа на каждого ребенка из списка детей заявителя из Городского реестра
            List<Child> children = so.getChildren();
            //1-й способ с помощью fori
//            for(int i = 0; i < so.getChildren().size(); i++) {
//                CityRegisterCheckerResponse cans =
//                        personChecker.checkPerson(children.get(i));
//            }

            //2-й способ с помощью Iterator
//            for(Iterator<Child> it = children.iterator(); it.hasNext(); ) {
//                Child child = it.next();
//                CityRegisterCheckerResponse cans = personChecker.checkPerson(child);
//            }

            //3-й способ с помощью foreach
            for (Child child : children){
                CityRegisterCheckerResponse cans = personChecker.checkPerson(child);
            }

        } catch (CityRegisterException ex) {
            ex.printStackTrace();
        }


        AnswerCityRegister ans = new AnswerCityRegister();
        //временное значение ответа
        ans.success = false;
        return ans;
    }
}
