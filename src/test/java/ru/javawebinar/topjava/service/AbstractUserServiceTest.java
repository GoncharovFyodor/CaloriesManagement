package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public class AbstractUserServiceTest extends AbstractDbTest {
    @Autowired
    private UserService service;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setUp() throws Exception {
        cacheManager.getCache("users").clear();
    }

    @Test
    public void testCreate() throws Exception {
        User newUser = new User(null, "New", "new@gmail.com", "newPass", Role.USER);
        User created = service.create(newUser);
        newUser.setId(created.getId());
        MATCHER.assertMatch(Arrays.asList(ADMIN, newUser, USER), service.getAll());
    }

    @Test(expected = DataAccessException.class)
    public void testDuplicateMailCreate() throws Exception {
        service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER));
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(USER_ID);
        MATCHER.assertMatch(Collections.singletonList(ADMIN), service.getAll());
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundDelete() throws Exception {
        service.delete(1);
    }

    @Test
    public void testGet() throws Exception {
        User user = service.get(USER_ID);
        MATCHER.assertMatch(USER, user);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        service.get(1);
    }

    @Test
    public void testGetByEmail() throws Exception {
        User user = service.getByEmail("user@yandex.ru");
        MATCHER.assertMatch(USER, user);
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<User> all = service.getAll();
        MATCHER.assertMatch(Arrays.asList(ADMIN, USER), all);
    }

    @Test
    public void testUpdate() throws Exception {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        service.update(updated);
        MATCHER.assertMatch(updated, service.get(USER_ID));
    }
}
