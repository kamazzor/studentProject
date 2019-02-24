package ru.javaproject.studentorder.dao;

import ru.javaproject.studentorder.config.Config;
import ru.javaproject.studentorder.domain.CountryArea;
import ru.javaproject.studentorder.domain.PassportOffice;
import ru.javaproject.studentorder.domain.RegisterOffice;
import ru.javaproject.studentorder.domain.Street;
import ru.javaproject.studentorder.exception.DaoException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

// Класс DictionaryDaoImpl возвращает список улиц на основе введённых пользователем букв (Type a Head)
public class DictionaryDaoImpl implements DictionaryDao {

    //    Текст запроса улиц, частично совпадающих с введенным пользователем параметром pattern,
//    который здесь заменён на ?
    private static final String GET_STREET = "select street_code, street_name FROM jc_street " + "where UPPER(street_name) like UPPER(?)";
    //    Текст запроса паспортных столов по Id региона, в котором они расположены
    private static final String GET_PASSPORT = "select * FROM jc_passport_office " + "where p_office_area_id = ?";
    //    Текст запроса ЗАГСов по Id региона, в котором они расположены
    private static final String GET_REGISTER = "select * FROM jc_register_office " + "where r_office_area_id = ?";
    //    Текст запроса ЗАГСов по Id региона, в котором они расположены
    private static final String GET_AREA = "select * FROM jc_country_struct " + "where area_id like ? and area_id <> ?";

    // TODO: 2/23/2019 refactoring - make one method
    //    Соединяюсь с БД, указывая её конкретную принадлежность к СУБД PostgreSQL
    private Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(
                Config.getProperty(Config.DB_URL),
                Config.getProperty(Config.DB_LOGIN),
                Config.getProperty(Config.DB_PASSWORD));
        return con;
    }

    //    Функция для поиска улиц по шаблону, введенному пользователем в web-форме
    public List<Street> findStreets(String pattern) throws DaoException {
        List<Street> result = new LinkedList<>();


//       Создаем экземпляр соединения с БД внутри try...catch
//       Создаем экземпляр выражения, с помощью которого и будем делать запросы к базе данных
//       Если соединение не создастся, ты даже не зайдёшь внуть try...catch,
//       если создастся, то после выполнение блока соединение автоматически закроется,
//       вызвав автоматически метод close()
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(GET_STREET)) {

//           (Необязательно) регистрируем класс-драйвер PostgreSQL на этапе его загрузки Java-машиной
//           Class.forName("org.postgresql.Driver");

//            заносим параметры sql-запроса в stmt учитывая, чтобы pattern может располагаться
//            в любой части строки
            stmt.setString(1, "%" + pattern + "%");

//           Делаем запрос в базу данных
            ResultSet rs = stmt.executeQuery();

//           Добавляем все полученные после запроса sql улицы в LinkedList
            while (rs.next()) {
                Street str = new Street(rs.getLong("street_code"), rs.getString("street_name"));
                result.add(str);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return result;
    }

    //    Функция для поиска паспортных столов по Id региона, в котором они находятся
    @Override
    public List<PassportOffice> findPassportOffices(String areaId) throws DaoException {
        List<PassportOffice> result = new LinkedList<>();


//       Создаем экземпляр соединения с БД внутри try...catch
//       Создаем экземпляр выражения, с помощью которого и будем делать запросы к базе данных
//       Если соединение не создастся, ты даже не зайдёшь внуть try...catch,
//       если создастся, то после выполнение блока соединение автоматически закроется,
//       вызвав автоматически метод close()
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(GET_PASSPORT)) {

//           (Необязательно) регистрируем класс-драйвер PostgreSQL на этапе его загрузки Java-машиной
//           Class.forName("org.postgresql.Driver");

//            заносим параметры sql-запроса в stmt учитывая, чтобы pattern может располагаться
//            в любой части строки
            stmt.setString(1, areaId);

//           Делаем запрос в базу данных
            ResultSet rs = stmt.executeQuery();

//           Добавляем все полученные после запроса sql улицы в LinkedList
            while (rs.next()) {
                PassportOffice str = new PassportOffice(rs.getLong("p_office_id"), rs.getString("p_office_area_id"), rs.getString("p_office_name"));
                result.add(str);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return result;

    }

    //    Функция для поиска ЗАГСов по Id региона, в котором они находятся
    @Override
    public List<RegisterOffice> findRegisterOffices(String areaId) throws DaoException {
        List<RegisterOffice> result = new LinkedList<>();


//       Создаем экземпляр соединения с БД внутри try...catch
//       Создаем экземпляр выражения, с помощью которого и будем делать запросы к базе данных
//       Если соединение не создастся, ты даже не зайдёшь внуть try...catch,
//       если создастся, то после выполнение блока соединение автоматически закроется,
//       вызвав автоматически метод close()
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(GET_REGISTER)) {

//           (Необязательно) регистрируем класс-драйвер PostgreSQL на этапе его загрузки Java-машиной
//           Class.forName("org.postgresql.Driver");

//            заносим параметры sql-запроса в stmt учитывая, чтобы pattern может располагаться
//            в любой части строки
            stmt.setString(1, areaId);

//           Делаем запрос в базу данных
            ResultSet rs = stmt.executeQuery();

//           Добавляем все полученные после запроса sql улицы в LinkedList
            while (rs.next()) {
                RegisterOffice str = new RegisterOffice(rs.getLong("r_office_id"), rs.getString("r_office_area_id"), rs.getString("r_office_name"));
                result.add(str);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return result;

    }

    //    Функция для поиска нужных регионов по маске area_id из базы jc_country_struct
    @Override
    public List<CountryArea> findAreas(String areaId) throws DaoException {
        List<CountryArea> result = new LinkedList<>();

//       Создаем экземпляр соединения с БД внутри try...catch
//       Создаем экземпляр выражения, с помощью которого будем делать запросы к базе данных
//       Если соединение не создастся, ты даже не зайдёшь внуть try...catch,
//       если создастся, то после выполнение блока соединение автоматически закроется,
//       вызвав автоматически метод close()
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(GET_AREA)) {

//           (Необязательно) регистрируем класс-драйвер PostgreSQL на этапе его загрузки Java-машиной
//           Class.forName("org.postgresql.Driver");

//            заносим параметры sql-запроса в stmt
//            Первый параметр строим с помощью спецфункции, которая в зависимости от переданного
//            areaId возвращает маску для поиска региона
//            Второй параметр - это сам areaId, т.е. при поиске исключаем сам источник поиска регионов
            String param1 = buildParam(areaId);
            String param2 = areaId;
            stmt.setString(1, param1);
            stmt.setString(2, param2);

//           Делаем запрос в базу данных
            ResultSet rs = stmt.executeQuery();

//           Добавляем все полученные после запроса sql улицы в LinkedList
            while (rs.next()) {
                CountryArea str = new CountryArea(rs.getString("area_id"), rs.getString("area_name"));
                result.add(str);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return result;


    }

    //    Спецфункция, ищущая подрегионы в регионе по введенной маске этого региона
    private String buildParam(String areaId) throws SQLException {
        //Проверяем, существует ли areaId либо он пустой (очищаем от пробелов)
        if (areaId == null || areaId.trim().isEmpty()) {
            return "__0000000000";
        } else if (areaId.endsWith("0000000000")) {
            return areaId.substring(0, 2) + "___0000000";
        } else if (areaId.endsWith("0000000")) {
            return areaId.substring(0, 5) + "___0000";
        } else if (areaId.endsWith("0000")) {
            return areaId.substring(0, 8) + "____";
        } else {
            throw new SQLException("Invalid parameter 'areaId': " + areaId);
        }
    }
}