package edu.javacourse.studentorder.domain.register;
//Класс служит для хранения ответа из Городского рееста по каждому объекту:
// отдельно для жены, для мужа, для каждого из детей
//класс содержит данные, полученные после проверки по персональным данным 1 персоны,
// зарегистрирован ли он в городском реестре, и если да, то какой тип регистрации.
// Если регистрации нет вообще, то второй выходной параметр = null

public class  CityRegisterCheckerResponse {
    // зарегистрирована ли персона в Городском реестре
    private boolean existing;
    // временная регистрация или постоянная. При отсутствии регистрации вообще = null
    private Boolean temporal;

    public boolean isExisting() {
        return existing;
    }

    public void setExisting(boolean existing) {
        this.existing = existing;
    }

    public Boolean getTemporal() {
        return temporal;
    }

    public void setTemporal(Boolean temporal) {
        this.temporal = temporal;
    }

    @Override
    public String toString() {
        return "CityRegisterCheckerResponse{" +
                "existing=" + existing +
                ", temporal=" + temporal +
                '}';
    }
}
