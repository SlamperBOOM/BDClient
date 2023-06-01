package org.slamperboom.model.processing;

import org.slamperboom.model.processing.predefinedQueries.*;

import java.util.ArrayList;
import java.util.List;

public class PredefinedQueryFactory {

    private PredefinedQueryFactory(){}

    public static PredefinedQuery getPredefinedQuery(int queryID){
        switch (queryID){
            case 1: return new WorkersQuery();
            case 2: return new BrigadeWorkersQuery();
            case 3: return new PilotsQuery();
            case 4: return new PlanesQuery();
            case 5: return new PlanesServiceQuery();
            case 6: return new FlightsQuery();
            case 7: return new CancelledFlightsQuery();
            case 8: return new PostponedFlightsQuery();
            case 9: return new AnotherFlightsQuery();
            case 10: return new CategoryFlightsQuery();
            case 11: return new PassengersQuery();
            case 12: return new FreeSeatsQuery();
            case 13: return new RefundTicketsQuery();
            default: return null;
        }
    }

    //Добавить необходимые условия в лист для каждого из запросов
    public static List<List<ParameterForPredefinedCondition>> getConditionsLabels(){
        List<List<ParameterForPredefinedCondition>> listOfConditions = new ArrayList<>();

        //first
        List<ParameterForPredefinedCondition> first = new ArrayList<>();
        first.add(new ParameterForPredefinedCondition("Работники или главы",
                "Работники", "Главы"));
        first.add(new ParameterForPredefinedCondition("Параметр",
                "Пол", "Возраст", "Зарплата", "Дети", "Трудоустроен"));
        first.add(new ParameterForPredefinedCondition("Условие"));
        first.add(new ParameterForPredefinedCondition("Департамент"));
        first.add(new ParameterForPredefinedCondition("Подсчет", "Да", "Нет"));
        listOfConditions.add(first);

        //second
        List<ParameterForPredefinedCondition> second = new ArrayList<>();
        second.add(new ParameterForPredefinedCondition("Параметр принадлежности",
                "Brigade", "Department", "Flight"));
        second.add(new ParameterForPredefinedCondition("Условие"));
        second.add(new ParameterForPredefinedCondition("Параметр работника",
                "Salary", "Age"));
        second.add(new ParameterForPredefinedCondition("Условие"));
        second.add(new ParameterForPredefinedCondition("Подсчет", "Да", "Нет"));
        listOfConditions.add(second);

        //third
        List<ParameterForPredefinedCondition> third = new ArrayList<>();
        third.add(new ParameterForPredefinedCondition("Параметр",
                "Проходил медосмотр в", "Не проходил медосмотр в", "Возраст", "Пол", "Зарплата"));
        third.add(new ParameterForPredefinedCondition("Условие"));
        third.add(new ParameterForPredefinedCondition("Подсчет", "Да", "Нет"));
        listOfConditions.add(third);

        //fourth
        List<ParameterForPredefinedCondition> fourth = new ArrayList<>();
        fourth.add(new ParameterForPredefinedCondition("Параметр",
                "Список всех самолетов", "Нахождение в указанное время", "Обслуживаемые рейсы"));
        fourth.add(new ParameterForPredefinedCondition("Условие"));
        fourth.add(new ParameterForPredefinedCondition("Подсчет", "Да", "Нет"));
        listOfConditions.add(fourth);

        //fifth
        List<ParameterForPredefinedCondition> fifth = new ArrayList<>();
        fifth.add(new ParameterForPredefinedCondition("Параметр",
                "Период", "Дата ремонта", "Количество ремонтов", "Количество рейсов до ремонта"));
        fifth.add(new ParameterForPredefinedCondition("Условие"));
        fifth.add(new ParameterForPredefinedCondition("Подсчет", "Да", "Нет"));
        listOfConditions.add(fifth);

        //sixth
        List<ParameterForPredefinedCondition> sixth = new ArrayList<>();
        sixth.add(new ParameterForPredefinedCondition("Параметр",
                "Маршрут", "Цена", "Длительность", "Все параметры"));
        sixth.add(new ParameterForPredefinedCondition("Условие"));
        sixth.add(new ParameterForPredefinedCondition("Подсчет", "Да", "Нет"));
        listOfConditions.add(sixth);

        //seventh
        List<ParameterForPredefinedCondition> seventh = new ArrayList<>();
        seventh.add(new ParameterForPredefinedCondition("Параметр",
                "Все отмененные", "Отмененные по маршруту", "По количеству невостребованных", "По % невостребованных"));
        seventh.add(new ParameterForPredefinedCondition("Условие"));
        seventh.add(new ParameterForPredefinedCondition("Подсчет", "Да", "Нет"));
        listOfConditions.add(seventh);

        //eighth
        List<ParameterForPredefinedCondition> eighth = new ArrayList<>();
        eighth.add(new ParameterForPredefinedCondition("Параметр",
                "Все задержанные", "По причине", "По маршруту", "Количество сданных билетов"));
        eighth.add(new ParameterForPredefinedCondition("Условие"));
        eighth.add(new ParameterForPredefinedCondition("Подсчет", "Да", "Нет"));
        listOfConditions.add(eighth);

        //ninth
        List<ParameterForPredefinedCondition> ninth = new ArrayList<>();
        ninth.add(new ParameterForPredefinedCondition("Параметр",
                "Модель самолета", "Среднее кол-во билетов на маршрут", "По времени вылета"));
        ninth.add(new ParameterForPredefinedCondition("Условие"));
        ninth.add(new ParameterForPredefinedCondition("Подсчет", "Да", "Нет"));
        listOfConditions.add(ninth);

        //tenth
        List<ParameterForPredefinedCondition> tenth = new ArrayList<>();
        tenth.add(new ParameterForPredefinedCondition("Параметр",
                "Тип рейса", "Город прибытия"));
        tenth.add(new ParameterForPredefinedCondition("Условие"));
        tenth.add(new ParameterForPredefinedCondition("Подсчет", "Да", "Нет"));
        listOfConditions.add(tenth);

        //eleventh
        List<ParameterForPredefinedCondition> eleventh = new ArrayList<>();
        eleventh.add(new ParameterForPredefinedCondition("Параметр",
                "Рейс", "Дата вылета", "Дата вылета за границу", "Сдача багажа", "Пол", "Возраст"));
        eleventh.add(new ParameterForPredefinedCondition("Условие"));
        eleventh.add(new ParameterForPredefinedCondition("Подсчет", "Да", "Нет"));
        listOfConditions.add(eleventh);

        //twelfth
        List<ParameterForPredefinedCondition> twelfth = new ArrayList<>();
        twelfth.add(new ParameterForPredefinedCondition("Параметр",
                "Рейс", "Дата", "Маршрут", "Цена", "Дата вылета"));
        twelfth.add(new ParameterForPredefinedCondition("Условие"));
        twelfth.add(new ParameterForPredefinedCondition("Подсчет", "Да", "Нет"));
        listOfConditions.add(twelfth);

        //thirteenth
        List<ParameterForPredefinedCondition> thirteenth = new ArrayList<>();
        thirteenth.add(new ParameterForPredefinedCondition("Параметр",
                "Рейс", "Дата", "Маршрут", "Цена", "Возраст", "Пол"));
        thirteenth.add(new ParameterForPredefinedCondition("Условие"));
        listOfConditions.add(thirteenth);
        return listOfConditions;
    }

    public static List<String> getTitles(){
        List<String> listOfTitles = new ArrayList<>();
        listOfTitles.add("Работники аэропорта");
        listOfTitles.add("Работники бригад");
        listOfTitles.add("Пилоты");
        listOfTitles.add("Самолеты");
        listOfTitles.add("Обслуживание самолетов");
        listOfTitles.add("Рейсы");
        listOfTitles.add("Отмененные рейсы");
        listOfTitles.add("Задержанные рейсы");
        listOfTitles.add("Рейсы по самолету?");
        listOfTitles.add("Рейсы по категориям?");
        listOfTitles.add("Пассажиры");
        listOfTitles.add("Свободные места");
        listOfTitles.add("Сданные билеты");
        return listOfTitles;
    }
}
