package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.*;

@Controller
public class MealRestController {

    @Autowired
    private MealService service;

    public Meal save(Meal meal) throws NotFoundException {
        return service.save(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) throws NotFoundException {
        service.delete(id, SecurityUtil.authUserId());
    }

    public MealTo get(int id) throws NotFoundException {
        Meal meal = service.get(id, SecurityUtil.authUserId());

        return MealsUtil.createTo(service.getAll(SecurityUtil.authUserId()), meal);
    }

    public void update(Meal meal) throws NotFoundException {
        service.update(meal, SecurityUtil.authUserId());
    }

    public List<MealTo> getAll() {
        return createTo(service.getAll(SecurityUtil.authUserId()), DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getFilteredByTime(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        return filteredByCycles(service.getAll(SecurityUtil.authUserId()),
                LocalDateTime.of(startDate, startTime).toLocalTime(),
                LocalDateTime.of(endDate, endTime).toLocalTime(), DEFAULT_CALORIES_PER_DAY);
    }
}