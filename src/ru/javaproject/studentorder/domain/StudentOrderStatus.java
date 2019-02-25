package ru.javaproject.studentorder.domain;

//Enum-класс со статусами студенческой заявки.
//По умолчанию эти статусы индексируются от 0 до кол-ва переменных через шаг 1
public enum StudentOrderStatus {
    START, CHECKED;

    //Преобразуем статус заявки из цифрового вида в реальное значение из этого enum-класса
    public static StudentOrderStatus fromValue(int value){
        for (StudentOrderStatus sos : StudentOrderStatus.values()) {
            if (sos.ordinal() == value) {
                return sos;
            }
        }
        throw new RuntimeException("Unknown value: " + value);
    }
}
