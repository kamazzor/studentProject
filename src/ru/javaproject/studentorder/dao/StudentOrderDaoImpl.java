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
                    "h_apartment, h_university_id, h_student_number, w_sur_name, w_given_name, " +
                    "w_patronymic, w_date_of_birth, w_passport_seria, w_passport_number, w_passport_date, " +
                    "w_passport_office_id, w_post_index, w_street_code, w_building, w_extension, " +
                    "w_apartment, w_university_id, w_student_number, certificate_id, register_office_id, " +
                    "marriage_date)" +
            "VALUES (?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?);";

    private static final String INSERT_CHILD =
            "INSERT INTO jc_student_child(" + 
                    "student_order_id, c_sur_name, c_given_name, c_patronymic, " +
                    "c_date_of_birth, c_certificate_number, c_certificate_date, c_register_office_id, " +
                    "c_post_index, c_street_code, c_building, c_extension, c_apartment)" +
            "VALUES (?, ?, ?, ?, " +
                    "?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?);";

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
        //Возвращаем сгенерированный Id переданной студенческой заявки
        // из столбца student_order_id таблицы jc_student_order
        Long result = -1L;

        //       Создаем экземпляр соединения с БД внутри try...catch
//       Создаем экземпляр выражения, с помощью которого и будем делать запросы к базе данных
//       Если соединение не создастся, ты даже не зайдёшь внуть try...catch,
//       если создастся, то после выполнение блока соединение автоматически закроется,
//       вызвав автоматически метод close()
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_ORDER, new String[] {"student_order_id"})) {

            //Транзакция добавления данных студенческой заявки в БД
            //Включаем механизм транзакции, точнее выключаем выполнение команд по-отдельности
            con.setAutoCommit(false);
            try {
                //Задаём параметры sql-скрипта
                //Header
                stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));   //дата и время подачи заявки
                stmt.setInt(2, StudentOrderStatus.START.ordinal());             //стартовый статус

                //Муж и жена, где start - стартовое значение индекса параметра sql-запроса
                setParamsForAdult(stmt, 3, so.getHusband());
                setParamsForAdult(stmt, 18, so.getWife());

                //Данные брака
                stmt.setString(33, so.getMarriageCertificateId());              //ID сертификата брака
                stmt.setLong(34, so.getMarriageOffice().getOfficeId());         //ID ЗАГСа брака
                stmt.setDate(35, Date.valueOf(so.getMarriageDate()));           //Дата брака

                //Подтверждаем, что модификацию данных закончили, возвращая кол-во модифицированнхы записей (не параметров)
                stmt.executeUpdate();

                //Получаем список (последних?) автоматически сгенерированных ключей последних измененных данных
                // из нужного списка колонок. В нашем случае нас интересует одна колонка student_order_id
                ResultSet gkRs = stmt.getGeneratedKeys();
                if (gkRs.next()) {
                    result = gkRs.getLong(1);
                }
                //Закрываем полученный список (необязательно, т.к. он закроется вместе с con,
                // который находится в начальных скобках try(здесь)...catch
                gkRs.close();
                //Вставляем данные детей в БД, передав в том числе result - сгенерированный id студенческой заявки
                saveChildren(con, so, result);

                con.commit();
            }catch(SQLException ex) {
                con.rollback();
                //Бросаем ошибку, чтобы перейти в catch верхнего try...catch
                throw ex;
            }


        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        // TODO: 2/23/2019 end this function
        return result;

    }

    //Сохраняем в БД в таблицу jc_student_order детей из студенческой заявки,
    // передав в т.ч. сгенерированный ID студенческой заявки
    private void saveChildren(Connection con, StudentOrder so, Long soId) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(INSERT_CHILD)) {
            for (Child child : so.getChildren()) {
                stmt.setLong(1, soId);
                setParamsForChild(stmt, child);
                //добавляем группу команд цикла в партию Batch для накопления команд
                //и будущего их исполнения
                stmt.addBatch();
            }
            //исполняем всю партию команда Batch сразу
            stmt.executeBatch();
        }
    }

    private void setParamsForChild(PreparedStatement stmt, Child child) throws SQLException {
        setParamsForPerson(stmt, 2, child);
        stmt.setString(6, child.getCertificateNumber());
        stmt.setDate(7, Date.valueOf(child.getIssueDate()));
        stmt.setLong(8, child.getIssueDepartment().getOfficeId());
        setParamsForAddress(stmt, 9, child);
    }

    private void setParamsForAdult(PreparedStatement stmt, int start, Adult adult) throws SQLException {
        setParamsForPerson(stmt, start, adult);
        stmt.setString(start+4, adult.getPassportSeria());                  // серия паспорта
        stmt.setString(start+5, adult.getPassportNumber());                 // номер паспорта
        stmt.setDate(start+6, Date.valueOf(adult.getIssueDate()));          //срок действия паспорта
        stmt.setLong(start+7, adult.getIssueDepartment().getOfficeId());   //ID отдела, выдавшего паспорт
        setParamsForAddress(stmt, start+8, adult);
        stmt.setLong(start+13, adult.getUniversity().getUniversityId());    //ID университета у взрослого
        stmt.setString(start+14, adult.getStudentId());                     //Номер студенческого у взрослого
    }

    private void setParamsForPerson(PreparedStatement stmt, int start, Person person) throws SQLException {
        stmt.setString(start, person.getSurName());                                         //фамилия
        stmt.setString(start + 1, person.getGivenName());                      //имя
        stmt.setString(start + 2, person.getPatronymic());                     //отчество
        stmt.setDate(start + 3, Date.valueOf(person.getDateOfBirth()));        // дата рождения
    }

    private void setParamsForAddress(PreparedStatement stmt, int start, Person person) throws SQLException {
        Address p_address = person.getAddress();
        stmt.setString(start, p_address.getPostCode());                                     //индекс
        stmt.setLong(start+1, p_address.getStreet().getStreetCode());        //ID улицы в адресе регистрации
        stmt.setString(start+2, p_address.getBuilding());                    //Здание в адресе регистрации
        stmt.setString(start+3, p_address.getExtention());                   //Корпус в адресе регистрации
        stmt.setString(start+4, p_address.getApartment());                   //Квартира в адресе регистрации
    }

}
