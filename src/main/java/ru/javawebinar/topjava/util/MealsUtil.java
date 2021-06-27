package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final Integer DEFAULT_CALORIES_PER_DAY = 2000;
    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    public static void main(String[] args) {

        //List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        //mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(MEALS,
                LocalDateTime.of(LocalDate.of(2021,5,1),LocalTime.of(7,0)),
                LocalDateTime.of(LocalDate.of(2021,5,1),LocalTime.of(12, 0)),
                2000));
    }

    public static List<MealTo> filteredByCycles(List<Meal> meals, LocalDateTime startTime, LocalDateTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDay = new HashMap<>();
        for (Meal meal : meals) {
            caloriesByDay.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }
        List<MealTo> result = new ArrayList<>();
        for (Meal meal : meals) {
            if (DateTimeUtil.isBetween(meal.getDateTime(), startTime, endTime)) {
                result.add(new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesByDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return result;
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalDateTime startTime, LocalDateTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDay = meals.stream()
                .collect(Collectors.toMap(meal -> meal.getDateTime().toLocalDate(),
                        Meal::getCalories,
                        Integer::sum
                ));
        return meals.stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDateTime(), startTime, endTime))
                .map(meal -> new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesByDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<MealTo> createTo(List<Meal> meals, int caloriesPerDay) {
        return filteredByCycles(meals, LocalDateTime.MIN, LocalDateTime.MAX, caloriesPerDay);
    }

    public static MealTo createTo(Meal meal, boolean exceeded) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceeded);
    }

    public static MealTo createTo(List<Meal> meals, Meal meal) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));

        return createTo(meal, caloriesSumByDate.get(meal.getDate()) > DEFAULT_CALORIES_PER_DAY);
    }
}
