package ru.javaproject.studentorder.validator;

import ru.javaproject.studentorder.domain.Child;
import ru.javaproject.studentorder.domain.Person;
import ru.javaproject.studentorder.domain.StudentOrder;
import ru.javaproject.studentorder.domain.register.AnswerCityRegister;
import ru.javaproject.studentorder.domain.register.AnswerCityRegisterItem;
import ru.javaproject.studentorder.domain.register.CityRegisterResponse;
import ru.javaproject.studentorder.exception.CityRegisterException;
import ru.javaproject.studentorder.exception.TransportException;
import ru.javaproject.studentorder.validator.register.CityRegisterChecker;
import ru.javaproject.studentorder.validator.register.FakeCityRegisterChecker;

// Класс проверяет заявку на все критерии для мужа, жены и ребенка
public class CityRegisterValidator {

    public String hostName; //Адрес Городского реестра
    String login;           //логин для входа в Городской реестр
    String password;        //пароль для входа в Городской реестр

    // Код ошибки отсутствия доступа к ГРН (internal code)
    private static final String IN_CODE = "NO_GRN";

    private CityRegisterChecker personChecker;

    public CityRegisterValidator() {
        // пока заглушка обращения в Городской реестр
        personChecker = new FakeCityRegisterChecker();
    }

    // Проверяем регистрацию для всех членов заявки и возвращаем список объектов,
    // каждый из которых содержит ответы из ГРН + описание ошибкок, если есть
    public AnswerCityRegister checkCityRegister(StudentOrder so) {
        AnswerCityRegister ans = new AnswerCityRegister();

        // Получаем данные ответа на мужа из Городского реестра и добавляем их
        // в виде объекта в массив ответов по конкретной заявке
        ans.addItem(checkPerson(so.getHusband()));
        // Аналогично для жены
        ans.addItem(checkPerson(so.getWife()));
        // Аналогично для каждого ребенка из списка детей заявителя
        for (Child child : so.getChildren()){
            ans.addItem(checkPerson(child));
        }

        return ans;
    }

    // Проверяем персону, отлавливая ошибки при работе с ГРН
    private AnswerCityRegisterItem checkPerson(Person person){
        // Создаем экземпляры статуса ответа и ошибки из ГРН
        AnswerCityRegisterItem.CityStatus status = null;
        AnswerCityRegisterItem.CityError error = null;
        try {
            CityRegisterResponse tmp = personChecker.checkPerson(person);
            // Определяем статус запроса в зависимости от параметра заявки existing
            // в ГРН с помошью тетрарного оператора,
            status = tmp.isExisting() ?
                    AnswerCityRegisterItem.CityStatus.YES :
                    AnswerCityRegisterItem.CityStatus.NO;
        } catch (CityRegisterException e) {
            e.printStackTrace();
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(e.getCode(), e.getMessage());
        } catch (TransportException e) {
            e.printStackTrace();
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(IN_CODE, e.getMessage());
            //обрабатываем непредусмотренные ошибки в конце
        } catch (Exception e){
            e.printStackTrace();
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(IN_CODE, e.getMessage());
        }

        AnswerCityRegisterItem ans = new AnswerCityRegisterItem(status, person, error);

        // Временный ответ-заглушка
        return ans;
    }
}
