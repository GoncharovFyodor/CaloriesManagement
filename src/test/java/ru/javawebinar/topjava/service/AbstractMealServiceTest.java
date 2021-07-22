package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Assert;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.swing.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertThrows;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.meal1;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public abstract class AbstractMealServiceTest extends AbstractDbTest{
    private static final Logger log = LoggerFactory.getLogger("result");
    private long start;
    private double lasted;
    private String resultTest;
    private static final StringBuilder results = new StringBuilder();

    static {
        SLF4JBridgeHandler.install();
    }

    @Rule
    public final Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms\n");
        }
    };

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Autowired
    private MealService service;

    @AfterClass
    public static void printResult() {
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
    }

    @Test
    public void delete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), meal7, meal6, meal5, meal4, meal3, meal2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        service.delete(1, USER_ID);
    }

    @Test
    public void deleteNotFoundCheckRule() {
        thrown.expect(NotFoundException.class);
        service.delete(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotOwn() throws Exception {
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        //assertMatch(newMeal, created);
        assertMatch(service.getAll(USER_ID), newMeal, meal7, meal6, meal5, meal4, meal3, meal2, meal1);
    }

    @Test
    public void get() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        actual.setUser(null);
        MATCHER.assertMatch(actual, adminMeal1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotOwn() throws Exception {
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        service.update(meal1, ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception {
        assertMatch(service.getAll(USER_ID), meals);
    }

    @Test
    public void getBetween() throws Exception {
        assertMatch(service.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID), meal4, meal3, meal2, meal1);
    }

    @Test
    public void updateNotOwn() {
        //assertThrows(NotFoundException.class, () -> service.update(meal1, ADMIN_ID));
        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.update(getUpdated(), ADMIN_ID));
        Assert.assertEquals("Not found entity with id=" + MEAL1_ID, exception.getMessage());
        MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), meal1);
    }
}