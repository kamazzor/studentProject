package edu.javacourse.studentorder.validator.register;

// Класс проверяет в Городском реестре наличие регистрации в Городском реестре у 1 персоны,
// и если она есть, то какого она типа: временная, постоянная или null

import edu.javacourse.studentorder.domain.register.CityRegisterResponse;
import edu.javacourse.studentorder.domain.Person;
import edu.javacourse.studentorder.exception.CityRegisterException;

public class RealCityRegisterChecker implements CityRegisterChecker {
    public CityRegisterResponse checkPerson(Person person) throws CityRegisterException {


        return null;
    }
}
