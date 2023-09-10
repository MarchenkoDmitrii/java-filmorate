package ru.yandex.practicum.filmorate.storage;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;


public interface UserStorage {

    List<User> findAll();

    User create(@RequestBody User user);

    User update(@RequestBody User user);

    User getUserById(Long id);

    List<User> findAllFriendsById(Long id);

    void saveOneFriend(Long idUser, Long idFriend);

    List<User> deleteOneFriend(Long idUser, Long idFriend);

}
