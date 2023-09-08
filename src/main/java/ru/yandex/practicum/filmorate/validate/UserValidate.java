package ru.yandex.practicum.filmorate.validate;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidate {
    public static User validate(User user) {
        if (user == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Попытка добавить пустое значение");
        if ((user.getLogin().contains(" ")) || (user.getLogin().equals("asd")))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "название не может быть пустым");
        if ((!user.getEmail().contains("@")) || (user.getEmail().equals(null)))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "электронная почта не может быть пустой и должна содержать символ @");
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "дата рождения не может быть в будущем.");
        if (user.getName().isEmpty() || user.getName().equals(""))
            user.setName(user.getLogin());
        return user;
    }
}
