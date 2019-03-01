package ru.javaproject.studentorder.dao;

import org.postgresql.core.SqlCommand;
import ru.javaproject.studentorder.config.Config;
import ru.javaproject.studentorder.domain.*;
import ru.javaproject.studentorder.exception.DaoException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//Этот класс реализует интерфейс StudentOrderDao,
//описывающий методы для сохранения данных студенческой заявки в базу данных
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

    // Вставляем данные о детях из студенческой заявки, кроме student_child_id - он SERIAL
    private static final String INSERT_CHILD =
            "INSERT INTO jc_student_child(" + 
                    "student_order_id, c_sur_name, c_given_name, c_patronymic, " +
                    "c_date_of_birth, c_certificate_number, c_certificate_date, c_register_office_id, " +
                    "c_post_index, c_street_code, c_building, c_extension, c_apartment)" +
            "VALUES (?, ?, ?, ?, " +
                    "?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?);";

//    Получаем из БД студенческие заявки со параметризованным статусом каждой из них (пока статус = START, т.е. 0,
//    сортируя заявки по дате подачи заявки. В таблицу присоединены данные о ID и названии
//    паспортных столов мужа и жены
    private static final String SELECT_ORDERS =
            "SELECT  so.*, ro.r_office_area_id, ro.r_office_name, " +
                    "po_h.p_office_area_id as h_p_office_area_id, po_h.p_office_name as h_p_office_area_name, " +
                    "po_w.p_office_area_id as w_p_office_area_id, po_w.p_office_name as w_p_office_area_name " +
                    "FROM jc_student_order so " +
                    "INNER JOIN jc_register_office ro ON ro.r_office_id = so.register_office_id " +
                    "INNER JOIN jc_passport_office po_h ON po_h.p_office_id = so.h_passport_office_id " +
                    "INNER JOIN jc_passport_office po_w ON po_w.p_office_id = so.w_passport_office_id " +
                    "WHERE student_order_status = ? " +
                    "ORDER BY student_order_date";

    // Получаем из БД детей ID Загсов и их названий . Детей выбираем только для ID студенческих хаявок, которые
    // были получены из БД в getStudentOrders();
    // TODO: 3/1/2019 Add parameters to query
    private static final String SELECT_CHILD =
            "SELECT soc.*, ro.r_office_area_id, ro.r_office_name " +
                    "FROM jc_student_child soc " +
                    "INNER JOIN jc_register_office ro ON ro.r_office_id = soc.c_register_office_id " +
                    "WHERE soc.student_order_id IN ";

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
    //Методы для задания параметров
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

    //Реализация метода получения данных студенческих заявок из БД со статусом 0 (START)
    @Override
    public List<StudentOrder> getStudentOrders() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_ORDERS)) {

            stmt.setInt(1, StudentOrderStatus.START.ordinal());
            //Получаем список студенческих заявок
            ResultSet rs = stmt.executeQuery();
            //Создаем список ID студенческих заявок для вставки этих ID в SQL-запрос списка детей из этих заявок
//            List<Long> ids = new LinkedList<>();

            while (rs.next()){
                //Формируем студенческую заявку на основе
                // полученных данных текущей заявки из БД
                StudentOrder so = new StudentOrder();
                //Запускаем методы для заполнения экземпляра студенческой заявки
                // на основе полученных данных текущей заявки из БД
                fillStudentOrder(rs, so);                       //Заполняем Header
                fillMarriage(rs, so);                           //Заполняем данные о браке

                Adult husband = fillAdult(rs, "h_");    //Заполняем экземпляр мужа
                Adult wife = fillAdult(rs, "w_");       //Заполняем экземпляр жены
                so.setHusband(husband);
                so.setWife(wife);

                //добавляем заполненную из БД студенческую заявку в возвращаемый ResultSet
                result.add(so);
//                ids.add(so.getStudentOrderId());
            }
//            Ищем в БД детей из тех студенческих заявок, которые мы получили из БД
            findChildren(con, result);

            //Строим окончание SQL-запроса SELECT_CHILD - WHERE soc.student_order_id IN (id1, id2, id7, ...)
            //добавляя в скобки id полученных из БД студенческих заявок.
//            StringBuilder sb = new StringBuilder("(");
//            for (Long id : ids) {
//                sb.append((sb.length() > 1 ? "," : "") + String.valueOf(id));   //добавляем запятую после каждого id
//            }
//            sb.append(")");

            rs.close();

        }catch(SQLException ex){
            throw new DaoException(ex);
        }

        return result;
    }

    //Метод для поиска детей из тех студенческих заявок, которые мы получили из БД
    private void findChildren(Connection con, List<StudentOrder> result) throws SQLException{
        //Строим окончание SQL-запроса SELECT_CHILD - WHERE soc.student_order_id IN (id1, id2, id7, ...), а именно:
        //Делаем из студенческих заявок конвеер (поток), из каждой заявки вынимаем только её ID,
        // затем склеиваем их через delimiter ",", добавляя скобки по бокам.
        String cl = "(" + result.stream().map(so -> String.valueOf(so.getStudentOrderId()))
                .collect(Collectors.joining(",")) + ")";

        //Создаем Map для быстрого поиска объекта студенческого заявления по его id (не в БД).
        Map<Long, StudentOrder> maps = result.stream().collect(Collectors
                .toMap(so -> so.getStudentOrderId(), so -> so));

        //Получаем детей для тех студенческих заявок, которые мы получили после запроса SELECT_ORDERS,
        //после добавляем соответствующих детей в каждую студенческую заявку
        try(PreparedStatement stmt = con.prepareStatement(SELECT_CHILD + cl)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Child ch = fillChild(rs);
                StudentOrder so = maps.get(rs.getLong("student_order_id"));
                so.addChild(ch);
            }
        }
    }

    //Метод заполняет данные в adult и возвращает его.

    private Adult fillAdult(ResultSet rs, String prefix) throws SQLException {

        Adult adult = new Adult();
        adult.setSurName(rs.getString(prefix + "sur_name"));
        adult.setGivenName(rs.getString(prefix + "given_name"));
        adult.setPatronymic(rs.getString(prefix + "patronymic"));
        adult.setDateOfBirth(rs.getDate(prefix + "date_of_birth").toLocalDate());
        adult.setPassportSeria(rs.getString(prefix + "passport_seria"));
        adult.setPassportNumber(rs.getString(prefix + "passport_number"));
        adult.setIssueDate(rs.getDate(prefix + "passport_date").toLocalDate());

        //Данные о паспортном столе взрослого
        Long poId = rs.getLong(prefix + "passport_office_id");
        String poArea = rs.getString(prefix + "p_office_area_id");
        String poName = rs.getString(prefix + "p_office_area_name");
        PassportOffice po = new PassportOffice(poId, poArea, poName);
        adult.setIssueDepartment(po);

        Address address = new Address();
        address.setPostCode(rs.getString(prefix + "post_index"));
        // TODO: 2/25/2019 get 2nd & 3rd parameters of temporary Street - from DB
        Street street = new Street(rs.getLong(prefix + "street_code"), "");
        address.setStreet(street);
        address.setBuilding(rs.getString(prefix + "building"));
        address.setExtention(rs.getString(prefix + "extension"));
        address.setApartment(rs.getString(prefix + "apartment"));
        adult.setAddress(address);

        University uni = new University(rs.getLong(prefix + "university_id"), "");
        adult.setUniversity(uni);
        adult.setStudentId(rs.getString(prefix + "student_number"));


        return adult;
    }
    //Метод для заполнения экземпляра студенческой заявки на основе
    //полученных данных текущей заявки из БД

    private void fillStudentOrder(ResultSet rs, StudentOrder so) throws SQLException {
        //Заполняем Header
        so.setStudentOrderId(rs.getLong("student_order_id"));
        so.setStudentOrderDate(rs.getTimestamp("student_order_date").toLocalDateTime());

        so.setStudentOrderStatus(StudentOrderStatus.fromValue(rs.getInt("student_order_status")));

        //Заполняем данные о браке

    }
    private void fillMarriage(ResultSet rs, StudentOrder so) throws SQLException {
        so.setMarriageCertificateId(rs.getString("certificate_id"));        //ID сертификата о браке
        so.setMarriageDate(rs.getDate("marriage_date").toLocalDate());      //Дата брака

        Long roId = rs.getLong("register_office_id");                       //ID ЗАГСа брака
        String areaId = rs.getString("r_office_area_id");
        String areaName = rs.getString("r_office_name");
        RegisterOffice ro = new RegisterOffice(roId, areaId, areaName);
        so.setMarriageOffice(ro);

        // TODO: 2/25/2019 set another fields of StudentOrder from DB

    }

    private Child fillChild(ResultSet rs) throws SQLException {
        String surName = rs.getString("c_sur_name");
        String givenName = rs.getString("c_given_name");
        String patronymic = rs.getString("c_patronymic");
        LocalDate dateOfBirth = rs.getDate("c_date_of_birth").toLocalDate();

        Child child = new Child(surName, givenName, patronymic, dateOfBirth);

        child.setCertificateNumber(rs.getString("c_certificate_number"));
        child.setIssueDate(rs.getDate("c_certificate_date").toLocalDate());

        Long roId = rs.getLong("c_register_office_id");
        String roArea = rs.getString("r_office_area_id");
        String roName = rs.getString("r_office_name");
        RegisterOffice ro = new RegisterOffice(roId, roArea, roName);
        child.setIssueDepartment(ro);

        Address address = new Address();
        address.setPostCode(rs.getString( "c_post_index"));
        // TODO: 3/1/2019 get 2nd & 3rd parameters of temporary Street - from DB
        Street street = new Street(rs.getLong("c_street_code"), "");
        address.setStreet(street);
        address.setBuilding(rs.getString("c_building"));
        address.setExtention(rs.getString("c_extension"));
        address.setApartment(rs.getString("c_apartment"));
        child.setAddress(address);

        return child;
    }

}
