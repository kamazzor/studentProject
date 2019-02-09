package edu.javacourse.studentorder.validator.register;

// ПОка дубликат RealCityRegister, создан для демонстрации полиморфизма на основе интерфейса
// CityRegisterChecker.
// Класс проверяет в Городском реестре наличие регистрации в Городском реестре у 1 персоны,
// и если она есть, то какого она типа: временная, постоянная или null

import edu.javacourse.studentorder.domain.Adult;
import edu.javacourse.studentorder.domain.Child;
import edu.javacourse.studentorder.domain.register.CityRegisterResponse;
import edu.javacourse.studentorder.domain.Person;
import edu.javacourse.studentorder.exception.CityRegisterException;
import edu.javacourse.studentorder.exception.TransportException;

public class FakeCityRegisterChecker implements CityRegisterChecker{
    //константы для примера
    // их мы сравниваем с серией паспорта, образующейся в виде 1000 (или 2000) + номер заявки
    //симулируем сигнал, что все участники прописаны, всё ок
    private static final String GOOD_1 = "1000";
    private static final String GOOD_2 = "2000";

    //симулируем сигнал, что прописки нет хотя бы у кого-то и ошибок с ГРН нет
    private static final String BAD_1 = "1001";
    private static final String BAD_2 = "2001";

    //симулируем сигнал, что есть ошибка из ГРН
    private static final String ERROR_1 = "1002";
    private static final String ERROR_2 = "2002";

    //симулируем сигнал, что у нас транспортная ошибка
    private static final String ERROR_T_1 = "1003";
    private static final String ERROR_T_2 = "2003";

    public CityRegisterResponse checkPerson(Person person)
            throws CityRegisterException, TransportException {
        // временная переменная для записи результата и его return
        CityRegisterResponse res = new CityRegisterResponse();

        if (person instanceof Adult){
            // создаем временную переменную, приводя person к типу Adult
            Adult t = (Adult) person;
            //получаем номер паспорта
            String ps = t.getPassportSeria();
            // проверяем серию паспорта (только equals, не ==)
            if (ps.equals(GOOD_1) || ps.equals(GOOD_2)){
                res.setExisting(true);
                res.setTemporal(false);
            }
            if (ps.equals(BAD_1) || ps.equals(BAD_2)){
                res.setExisting(false);
            }
            if (ps.equals(ERROR_1) || ps.equals(ERROR_2)){
                CityRegisterException ex = new CityRegisterException("1", "GRN ERROR " + ps);
                throw ex;
            }
            if (ps.equals(ERROR_T_1) || ps.equals(ERROR_T_2)){
                TransportException ex = new TransportException("Transport ERROR " + ps);
                throw ex;
            }
        }

        if (person instanceof Child){
            //временная заглушка в случае ребенка
            res.setExisting(true);
            res.setTemporal(true);
        }
        System.out.println(res);

        return res;
    }
}
