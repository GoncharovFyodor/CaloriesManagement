package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MealDAOListImpl implements MealDAO {
    private static List<Meal> meals;

    static {
        meals = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 520),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 900),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 600),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 400)
        );
    }

    @Override
    public List<Meal> getAllMeals() {
        return meals;
    }
}