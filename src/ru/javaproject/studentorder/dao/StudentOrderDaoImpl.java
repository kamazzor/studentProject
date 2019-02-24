package ru.javaproject.studentorder.dao;

import ru.javaproject.studentorder.config.Config;
import ru.javaproject.studentorder.domain.*;
import ru.javaproject.studentorder.exception.DaoException;

import java.sql.*;
import java.time.LocalDateTime;

//Этот класс реализует интерфейс StudentOrderDao,
//описывающий методы для сохранения данных студенческой заявки
public class StudentOrderDaoImpl implements StudentOrderDao {

    // Вставляем все данные студенческой заявки, кроме student_order_id, который генерируется автоматически
    public static final String INSERT_ORDER =
            "INSERT INTO jc_student_order(" +
                    "student_order_date, student_order_status, h_sur_name, h_given_name, " +
                    "h_patronymic, h_date_of_birth, h_passport_seria, h_passport_number, h_passport_date, " +
                    "h_passport_office_id, h_post_index, h_street_code, h_building, h_extension, " +
                    "h_apartment, w_sur_name, w_given_name, w_patronymic, w_date_of_birth, " +
                    "w_passport_seria, w_passport_number, w_passport_date, w_passport_office_id, w_post_index, " +
                    "w_street_code, w_building, w_extension, w_apartment, certificate_id, " +
                    "register_office_id, marriage_date)" +
            "VALUES (?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?);";

    // TODO: 2/23/2019 refactoring - make one method
    //    Соединяюсь с БД, указывая её конкретную принадлежность к СУБД PostgreSQL
    private Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(
                Config.getProperty(Config.DB_URL),
                Config.getProperty(Config.DB_LOGIN),
                Config.getProperty(Config.DB_PASSWORD));
        return con;
    }

    //Метод сохраняет студенческую заявку в БД, возвращая на данном этапе (46) id последней записанной в БД заявки
    @Override
    public Long saveStudentOrder(StudentOrder so) throws DaoException {
        Long result = -1L;

        //       Создаем экземпляр соединения с БД внутри try...catch
//       Создаем экземпляр выражения, с помощью которого и будем делать запросы к базе данных
//       Если соединение не создастся, ты даже не зайдёшь внуть try...catch,
//       если создастся, то после выполнение блока соединение автоматически закроется,
//       вызвав автоматически метод close()
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_ORDER, new String[] {"student_order_id"})) {

            //Задаём параметры sql-скрипта
            //Header
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));   //дата и время подачи заявки
            stmt.setInt(2, StudentOrderStatus.START.ordinal());             //стартовый статус

            //Муж
            Adult husband = so.getHusband();
            stmt.setString(3, husband.getSurName());                        //фамилию 
            stmt.setString(4, husband.getGivenName());                      //имя 
            stmt.setString(5, husband.getPatronymic());                     //отчество 
            stmt.setDate(6, Date.valueOf(husband.getDateOfBirth()));        // дата рождения 
            stmt.setString(7, husband.getPassportSeria());                  // серия паспорта 
            stmt.setString(8, husband.getPassportNumber());                 // номер паспорта
            stmt.setDate(9, Date.valueOf(husband.getIssueDate()));          //срок действия паспорта
            stmt.setLong(10, husband.getIssueDepartment().getOfficeId());   //ID отдела, выдавшего паспорт
            Address h_address = husband.getAddress();
            stmt.setString(11, h_address.getPostCode());                    //индекс
            stmt.setLong(12, h_address.getStreet().getStreetCode());        //ID улицы в адресе регистрации
            stmt.setString(13, h_address.getBuilding());                    //Здание в адресе регистрации
            stmt.setString(14, h_address.getExtention());                   //Корпус в адресе регистрации
            stmt.setString(15, h_address.getApartment());                   //Квартира в адресе регистрации

            //Жена
            Adult wife = so.getWife();
            stmt.setString(16, wife.getSurName());                        //фамилия
            stmt.setString(17, wife.getGivenName());                      //имя
            stmt.setString(18, wife.getPatronymic());                     //отчество 
            stmt.setDate(19, Date.valueOf(wife.getDateOfBirth()));        // дата рождения 
            stmt.setString(20, wife.getPassportSeria());                  // серия паспорта 
            stmt.setString(21, wife.getPassportNumber());                 // номер паспорта 
            stmt.setDate(22, Date.valueOf(wife.getIssueDate()));          //срок действия паспорта
            stmt.setLong(23, wife.getIssueDepartment().getOfficeId());   //ID отдела, выдавшего паспорт
            Address w_address = wife.getAddress();
            stmt.setString(24, w_address.getPostCode());                    //индекс
            stmt.setLong(25, w_address.getStreet().getStreetCode());        //ID улицы в адресе регистрации
            stmt.setString(26, w_address.getBuilding());                    //Здание в адресе регистрации
            stmt.setString(27, w_address.getExtention());                   //Корпус в адресе регистрации
            stmt.setString(28, w_address.getApartment());                   //Квартира в адресе регистрации

            //Данные брака
            stmt.setString(29, so.getMarriageCertificateId());              //ID сертификата брака
            stmt.setLong(30, so.getMarriageOffice().getOfficeId());         //ID ЗАГСа брака
            stmt.setDate(31, Date.valueOf(so.getMarriageDate()));           //Дата брака

            //Подтверждаем, что модификацию данных закончили, возвращая кол-во модифицированнхы записей (не параметров)
            stmt.executeUpdate();

            //Получаем список автоматически сгенерированных ключей последних измененных данных
            // из нужного списка колонок. В нашем случае нас интересует одна колонка student_order_id
            ResultSet gkRs = stmt.getGeneratedKeys();
            if (gkRs.next()){
                result = gkRs.getLong(1);
            }



        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        // TODO: 2/23/2019 end this function
        return result;

    }

}
