package ru.javaproject.studentorder.domain.register;

import java.util.ArrayList;
import java.util.List;

// В классе собраны данные по результатам проверки заявки для каждой заявки в виде списка объектов.
// Каждый элемент списка содержит инфу про 1 персону типа AnswerCityRegisterItem, включая информацию
// о том, кого проверяли, какой результат проверки и об ошибках при запросе, если они есть.
public class AnswerCityRegister {
    private List<AnswerCityRegisterItem> items;

    public void addItem(AnswerCityRegisterItem item){
        if (items == null){
            items = new ArrayList<>(10);
        }
        items.add(item);
    }

    public List<AnswerCityRegisterItem> getItems() {
        return items;
    }
}
