package ru.javaproject.studentorder.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//Класс для работы с внешними свойствами базы данных jc_student
public class Config {

    //Запишем как константы названия свойств в dao.properties
    public static final String DB_URL = "db.url";
    public static final String DB_LOGIN = "db.login";
    public static final String DB_PASSWORD = "db.password";

    //Хранилище для считанных из dao.properties значений
    private static Properties properties = new Properties();

    //получаем значение свойства по его названию
    public synchronized static String getProperty(String name){
        if(properties.isEmpty()){
//            Создаем входной поток данных из внешнего файла как поток байтов
            try (InputStream is = Config.class.getClassLoader()
                    .getResourceAsStream("dao.properties")){

//               Специальный метод, который из потока байтов построчно выбирает
//               все пары свойство/значение
                properties.load(is);

            } catch (IOException e) {
                e.printStackTrace();
//                Т.к. свойства базы данных не загрузились, то останавливем программу,
//                т.к. дальнейшая работа программы без этих свойств бессмысленна
                throw new RuntimeException(e);
            }
        }
        return properties.getProperty(name);
    }
}
