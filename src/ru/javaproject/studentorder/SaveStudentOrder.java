package ru.javaproject.studentorder;

import ru.javaproject.studentorder.dao.DictionaryDaoImpl;
import ru.javaproject.studentorder.dao.StudentOrderDao;
import ru.javaproject.studentorder.dao.StudentOrderDaoImpl;
import ru.javaproject.studentorder.domain.*;
import ru.javaproject.studentorder.exception.DaoException;

import java.time.LocalDate;
import java.util.List;

//Начальный класс программы
public class SaveStudentOrder {
    public static void main(String[] args) throws DaoException {

//        // Ищем улицы в таблице jc_street по паттерну в любом месте названия
//        List<Street> d = new DictionaryDaoImpl().findStreets("про");
//        for (Street s : d) {
//            System.out.println(s.getStreetName());
//        }
//
////        Ищем паспортные столы в таблице jc_passport_office по введенному Id региона,
////        где этот паспортный стол находится
//        List<PassportOffice> po = new DictionaryDaoImpl().findPassportOffices("010020000000");
//        for (PassportOffice p : po) {
//            System.out.println(p.getOfficeName());
//        }
//
////        Ищем ЗАГСы в таблице jc_register_office по введенному Id региона,
////        где этот паспортный стол находится
//        List<RegisterOffice> ro = new DictionaryDaoImpl().findRegisterOffices("010010000000");
//        for (RegisterOffice r : ro) {
//            System.out.println(r.getOfficeName());
//        }
//
////        Ищем подрегионы в таблице jc_country_struct по маске региона areaId,
////        в котором нужно найти подрегионы находится
//        List<CountryArea> ca1 = new DictionaryDaoImpl().findAreas("020000000000");
//        for (CountryArea c : ca1) {
//            System.out.println(c.getAreaId() + " : " + c.getAreaName());
//        }
//        Тестовое заполнение студенческо заявки не из веб-формы
//        StudentOrder s = buildStudentOrder(10);
//        Создаем экземпляр объекта для работы с БД
        StudentOrderDao dao = new StudentOrderDaoImpl();
//        Сохраняем экземпляр студенческой заявки в БД
//        Long id = dao.saveStudentOrder(s);
//        System.out.println(id+"\n");
//
        List<StudentOrder> soList = dao.getStudentOrders();
        for (StudentOrder so : soList) {
            System.out.println(so.getStudentOrderId());
        }


    }

    //Временная (старая) реализация сохранения студенческой заявки
    private static long saveStudentOrder(StudentOrder studentOrder) {
        long answer;
        answer = 199;
        System.out.println("saveStudentOrder");
        return answer;
    }

    //Временная реализация студенческой заявки, передавая в неё id этой заявки
    public static StudentOrder buildStudentOrder(long id) {
        StudentOrder so = new StudentOrder();
        so.setStudentOrderId(id);
        so.setMarriageCertificateId("" + (123456000 + id));
        so.setMarriageDate(LocalDate.of(2016, 7, 4));
        //Тестовый экземпляр ЗАГСа из заявки для проверки системы
        RegisterOffice ro = new RegisterOffice(1L, "", "");
        so.setMarriageOffice(ro);

        //Создал пример улицы с кодом 1 и именем First Street)
        Street street = new Street(1, "FirstStreet");
        Address address = new Address("195000", street, "12", "", "142");

        // Муж
        Adult husband = new Adult("Петров", "Виктор",
                "Сергеевич", LocalDate.of(1997, 8, 24));
        husband.setPassportSeria("" + (1000 + id));
        husband.setPassportNumber("" + (100000 + id));
        husband.setIssueDate(LocalDate.of(2017, 9, 15));
        //Создал тестовый экземпляр Паспортного стола мужа из заявки для проверки системы
        PassportOffice po1 = new PassportOffice(1L, "", "");
        husband.setIssueDepartment(po1);
        husband.setStudentId("" + (100000 + id));
        husband.setAddress(address);
        husband.setUniversity(new University(2L, ""));
        husband.setStudentId("HH12345");

        // Жена
        Adult wife = new Adult("Петрова", "Вероника",
                "Алекссевна", LocalDate.of(1998, 3, 12));
        wife.setPassportSeria("" + (2000 + id));
        wife.setPassportNumber("" + (200000 + id));
        wife.setIssueDate(LocalDate.of(2018, 4, 5));

//      Создал тестовый экземпляр Паспортного стола жены из заявки для проверки системы
        PassportOffice po2 = new PassportOffice(2L, "", "");
        wife.setIssueDepartment(po2);
        wife.setStudentId("" + (200000 + id));
        wife.setAddress(address);
        wife.setUniversity(new University(1L, ""));
        wife.setStudentId("WW12345");

        // Ребенок
        Child child1 = new Child("Петрова", "Ирина",
                "Викторовна", LocalDate.of(2018, 6, 29));
        child1.setCertificateNumber("" + (300000 + id));
        child1.setIssueDate(LocalDate.of(2018, 6, 11));
        //Создал тестовый экземпляр Паспортного стола ребенка1 из заявки для проверки системы
        RegisterOffice ro2 = new RegisterOffice(2L, "", "");
        child1.setIssueDepartment(ro2);
        child1.setAddress(address);

        // Ребенок
        Child child2 = new Child("Петрова", "Евгений",
                "Викторович", LocalDate.of(2018, 6, 29));
        child2.setCertificateNumber("" + (400000 + id));
        child2.setIssueDate(LocalDate.of(2018, 7, 19));

        //Создал тестовый экземпляр Паспортного стола ребенка2 из заявки для проверки системы
        RegisterOffice ro3 = new RegisterOffice(3L, "", "");
        child2.setIssueDepartment(ro3);
        child2.setAddress(address);

        so.setHusband(husband);
        so.setWife(wife);
        so.addChild(child1);
        so.addChild(child2);

        return so;
    }
}
