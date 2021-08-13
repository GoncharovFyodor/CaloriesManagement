package ru.javawebinar.topjava.web;

import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.meal1;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.UserTestData.*;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void getUsers() throws Exception {
        perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users",
                        new AssertionMatcher<List<User>>() {
                            @Override
                            public void assertion(List<User> actual) throws AssertionError {
                                MATCHER.assertMatch(actual, admin, user);
                            }
                        }
                ));
    }

    @Test
    public void getMeals() throws Exception {
        List<MealTo> mealTos = MealsUtil.getFilteredTos(meals, MealsUtil.DEFAULT_CALORIES_PER_DAY,
                meal1.getTime(), meal1.getTime());

        MealTo mealTo = !mealTos.isEmpty() ? mealTos.get(0) : null;

        if (mealTo == null) throw new Exception("mealWithExceed == null");

        perform(get("/meals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(6)))
                .andExpect(model().attribute("meals", hasItem(
                        allOf(
                                hasProperty("id", is(mealTo.getId())),
                                hasProperty("dateTime", is(mealTo.getDateTime())),
                                hasProperty("description", is(mealTo.getDescription())),
                                hasProperty("calories", is(mealTo.getCalories())),
                                hasProperty("exceed", is(mealTo.isExcess()))
                        ))));
    }

}