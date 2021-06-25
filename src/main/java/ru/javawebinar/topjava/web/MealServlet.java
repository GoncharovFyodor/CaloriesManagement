package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private ConfigurableApplicationContext springContext;
    private MealRestController mealController;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("getAll");
        request.setAttribute("meals", MealsUtil.createTo(MealsUtil.MEALS, MealsUtil.DEFAULT_CALORIES_PER_DAY));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    public void init() {
        springContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        springContext = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml");
        mealController = springContext.getBean(MealRestController.class);
    }
}
