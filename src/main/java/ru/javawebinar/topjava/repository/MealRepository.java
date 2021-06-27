package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface MealRepository {
    Meal save(Meal Meal, int userId);

    // false if not found
    boolean delete(int id, int userId);

    // null if not found
    Meal get(int id, int userId);

    // null if not found
    List<Meal> getAll(int userId);

    List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId);
}
