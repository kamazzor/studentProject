package ru.javaproject.studentorder.validator.register;

// Класс проверяет в Городском реестре наличие регистрации в Городском реестре у 1 персоны,
// и если она есть, то какого она типа: временная, постоянная или null

import ru.javaproject.studentorder.domain.register.CityRegisterResponse;
import ru.javaproject.studentorder.domain.Person;
import ru.javaproject.studentorder.exception.CityRegisterException;
import ru.javaproject.studentorder.exception.TransportException;

public class RealCityRegisterChecker implements CityRegisterChecker {
    public CityRegisterResponse checkPerson(Person person)
            throws CityRegisterException, TransportException {


        return null;
    }
}
