package ru.javaproject.studentorder.dao;

import ru.javaproject.studentorder.domain.CountryArea;
import ru.javaproject.studentorder.domain.PassportOffice;
import ru.javaproject.studentorder.domain.RegisterOffice;
import ru.javaproject.studentorder.domain.Street;
import ru.javaproject.studentorder.exception.DaoException;

import java.util.List;

//Интерфейс DictionaryDaoImpl описывает функции поиска необходимых данных в БД
public interface DictionaryDao {

//    Метод для поиска строк в базе jc_street по введенному пользователем паттерну
    List<Street> findStreets(String pattern) throws DaoException;

//    Метод для поиска паспортных столов в базе jc_passport_office
//    по введенному пользователем Id региона, в котором он находится
    List<PassportOffice> findPassportOffices(String areaId) throws DaoException;

//    Метод для поиска ЗАГСов в базе jc_passport_office
//    по введенному пользователем Id региона, в котором он находится
    List<RegisterOffice> findRegisterOffices(String areaId) throws DaoException;

    //    Метод для поиска подрегионов в регионе в базе jc_country_struct
//    по введенному пользователем Id региона
    List<CountryArea> findAreas(String areaId) throws DaoException;
}
