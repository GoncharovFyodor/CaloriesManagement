package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.Collection;

import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.Role.ADMIN;

class InMemoryAdminRestControllerTest {
    private static final Logger log = LoggerFactory.getLogger(InMemoryAdminRestControllerTest.class);

    private static ConfigurableApplicationContext appCtx;
    private static AdminRestController controller;

    @BeforeAll
    static void beforeClass() {
        appCtx = new ClassPathXmlApplicationContext("spring/inmemory.xml");
        log.info("\n{}\n", Arrays.toString(appCtx.getBeanDefinitionNames()));
        controller = appCtx.getBean(AdminRestController.class);
    }

    @AfterAll
    static void afterClass() {
//        May cause during JUnit "Cache is not alive (STATUS_SHUTDOWN)" as JUnit share Spring context for speed
//        http://stackoverflow.com/questions/16281802/ehcache-shutdown-causing-an-exception-while-running-test-suite
//        appCtx.close();
    }

    @BeforeEach
    void setup() {
        // re-initialize
        InMemoryUserRepository repository = appCtx.getBean(InMemoryUserRepository.class);
        repository.init();
    }

    @Test
    void delete() {
        controller.delete(USER_ID);
        Assertions.assertNull(controller.getAll().get(USER_ID));
    }

    @Test
    void deleteNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }
}