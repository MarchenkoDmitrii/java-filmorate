package ru.yandex.practicum.filmorate.storage;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserStorage {
    List<User> findAll();
    User create(@RequestBody User user);
    User update(@RequestBody User user);

}
