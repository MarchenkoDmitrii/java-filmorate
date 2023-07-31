package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage{
    protected int id = 0;
    private Map<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public List<User> findAll() {
        if (users.entrySet().size() == 0)
            throw new ValidationException("Нет пользователей", HttpStatus.NOT_FOUND);
        return users.values().stream().collect(Collectors.toList());
    }

    @Override
    public User create(User user) {
        if (user == null)
            throw new ValidationException("Попытка добавить пустое значение", HttpStatus.BAD_REQUEST);
        if ((user.getLogin().contains(" ")) || (user.getLogin().equals(null)))
            throw new ValidationException("логин не может быть пустым и содержать пробелы", HttpStatus.BAD_REQUEST);
        if ((!user.getEmail().contains("@")) || (user.getEmail().equals(null)))
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @", HttpStatus.BAD_REQUEST);
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("дата рождения не может быть в будущем.", HttpStatus.BAD_REQUEST);
        if (Optional.ofNullable(user.getName()).isEmpty())
            user.setName(user.getLogin());
        user.setId(++id);
        this.id = user.getId();
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Нет такого пользователя", HttpStatus.NOT_FOUND);
        }
        return user;
    }
}