package ru.javawebinar.topjava.repository.inmemory;

import javafx.collections.transformation.SortedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.DateTimeUtil.isBetween;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = getLogger(InMemoryMealRepository.class);

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
        }

        if (meal.getUserId() == userId) {
            repository.put(meal.getId(), meal);
            return meal;
        }

        return null;
    }

    private Meal save(Meal meal) {
        return this.save(meal, 1);
    }

    @PostConstruct
    public void postConstruct() {
        log.info("+++ PostConstruct");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("+++ PreDestroy");
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        Meal meal = repository.get(id);
        if (meal == null || meal.getUserId() != userId) return false;

        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        Meal meal = repository.get(id);

        if (meal == null || meal.getUserId() != userId) return null;

        return meal;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll {}");
        List<Meal> meals = new ArrayList<>();

        for (Meal meal : repository.values()) {
            if (meal.getUserId() == userId) meals.add(meal);
        }

        if (meals.size() <= 0 || meals.isEmpty()) return null;

        Collections.sort(meals, (o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()));

        return meals;
    }

    @Override
    public Collection<Meal> getFilteredByTime(LocalTime startTime, LocalTime endTime, int userId) {
        log.info("getFilteredByTime");
        List<Meal> mealList = new ArrayList<>();

        for (Meal meal : repository.values()) {
            if (isBetween(meal.getTime(), startTime, endTime)) {
                if (meal.getUserId() == userId) mealList.add(meal);
            }
        }

        if (mealList.size() <= 0 || mealList.isEmpty()) return null;

        Collections.sort(mealList, (o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()));

        return mealList;
    }
}
