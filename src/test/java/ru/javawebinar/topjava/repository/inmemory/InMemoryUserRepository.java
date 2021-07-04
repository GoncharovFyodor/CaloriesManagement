package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    static final int USER_ID = 1;
    static final int ADMIN_ID = 2;

    public void init() {
        User user = new User(UserTestData.USER_ID, "user", "", "", Role.USER);
        User admin = new User(UserTestData.ADMIN_ID, "admin", "", "", Role.ADMIN);
        repository.clear();
        repository.put(UserTestData.USER_ID, user);
        repository.put(UserTestData.ADMIN_ID, admin);
        counter.getAndSet(UserTestData.ADMIN_ID + 1);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
        }
        repository.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        List<User> users = (List<User>) repository.values();
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                int compare = o1.getName().compareTo(o2.getName());
                if (compare == 0) return o1.getEmail().compareTo(o2.getEmail());
                return compare;
            }
        });
        return users;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        Objects.requireNonNull(email, "email must not be null");
        for (User user : repository.values()) {
            if (email.equals(user.getEmail())) return user;
        }
        return null;
    }
}
