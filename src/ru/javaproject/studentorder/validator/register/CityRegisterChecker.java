package ru.javaproject.studentorder.validator.register;

import ru.javaproject.studentorder.domain.register.CityRegisterResponse;
import ru.javaproject.studentorder.domain.Person;
import ru.javaproject.studentorder.exception.CityRegisterException;
import ru.javaproject.studentorder.exception.TransportException;

//Интерфейс проверки в Городском реестре наличия регистрации в Городском реестре у 1 персоны,
// и если она есть, то какого она типа: временная, постоянная или null

public interface CityRegisterChecker {
    CityRegisterResponse checkPerson(Person person)
            throws CityRegisterException, TransportException;
}
