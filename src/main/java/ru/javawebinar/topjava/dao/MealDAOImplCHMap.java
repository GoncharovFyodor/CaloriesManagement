package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MealDAOImplCHMap implements MealDAO {
    private static final ConcurrentHashMap<Integer, Meal> mealsDB = new ConcurrentHashMap<>();
    private static final AtomicInteger counter = new AtomicInteger(0);

    static {
        mealsDB.put(counter.addAndGet(1), new Meal(counter.get(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 1000));
        mealsDB.put(counter.addAndGet(1), new Meal(counter.get(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 600));
        mealsDB.put(counter.addAndGet(1), new Meal(counter.get(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        mealsDB.put(counter.addAndGet(1), new Meal(counter.get(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 900));
        mealsDB.put(counter.addAndGet(1), new Meal(counter.get(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 600));
        mealsDB.put(counter.addAndGet(1), new Meal(counter.get(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 600));
    }

    @Override
    public List<Meal> getAll() {
        List<Meal> result = mealsDB.values().stream().collect(Collectors.toList());
        return result;
    }

    @Override
    public void add(Meal meal) {
        int id = counter.addAndGet(1);
        meal.setId(id);
        mealsDB.put(id, meal);
    }

    @Override
    public void delete(int id) {
        mealsDB.remove(id);
    }

    @Override
    public void update(Meal meal) {
        mealsDB.put(meal.getId(), meal);
    }

    @Override
    public Meal getById(int id) {
        return mealsDB.get(id);
    }
}
