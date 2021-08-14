package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class MealRestControllerTest extends AbstractControllerTest {
    private static String REST_URL = MealRestController.REST_URL + "/";

    @Autowired
    protected MealService mealService;

    @Test
    public void testGet() throws Exception {
        perform(get(REST_URL + meal1.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MATCHER.contentJson(meal1));
    }

    @Test
    public void testDelete() throws Exception {
        perform(delete(REST_URL + meal1.getId()))
                .andDo(print())
                .andExpect(status().isOk());
        List<Meal> list = new ArrayList<>(meals);
        list.remove(meal1);
        MATCHER.assertMatch(list, mealService.getAll(USER_ID));
    }

    @Test
    public void testGetAll() throws Exception {
        perform(get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MATCHER_TO.contentJson(MealsUtil.getTos(meals,
                        MealsUtil.DEFAULT_CALORIES_PER_DAY)));
    }

    @Test
    public void testCreate() throws Exception {
        Meal expected = new Meal(LocalDateTime.now(), "testCreate", MealsUtil.DEFAULT_CALORIES_PER_DAY);

        ResultActions action = perform(post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Meal returned = MATCHER.readFromJson(action);
        expected.setId(returned.getId());

        MATCHER.assertMatch(expected, returned);
        MATCHER.assertMatch(Arrays.asList(expected, meal6, meal5, meal4, meal3, meal2, meal1),
                mealService.getAll(USER_ID));
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = new Meal(meal1.getId(),meal1.getDateTime(),meal1.getDescription(),meal1.getCalories());
        updated.setDescription("Updated description");

        perform(put(REST_URL + meal1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isOk());
        MATCHER.assertMatch(updated, mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void testGetBetween() throws Exception {
        perform(get(REST_URL + "between?startDate=" + meal1.getDate() +
                        "&startTime=" + meal1.getTime() +
                        "&endDate=" + meal1.getDate() +
                        "&endTime=" + meal1.getTime()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MATCHER_TO.contentJson(MealsUtil
                        .getTos(Collections.singleton(meal1), MealsUtil.DEFAULT_CALORIES_PER_DAY)));
    }

}