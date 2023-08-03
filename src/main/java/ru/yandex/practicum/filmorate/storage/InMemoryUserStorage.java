package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage{
    protected Long id = 0L;
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> friendList = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public List<User> findAll() {
        if (users.entrySet().size() == 0)
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"нет пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        if (user == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Попытка добавить пустое значение");
        if ((user.getLogin().contains(" ")) || (user.getLogin().equals("asd")))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "название не может быть пустым");
        if ((!user.getEmail().contains("@")) || (user.getEmail().equals(null)))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "электронная почта не может быть пустой и должна содержать символ @");
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "дата рождения не может быть в будущем.");
        if (user.getName().isEmpty() ) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        this.id = user.getId();
        users.put(user.getId(), user);
        friendList.put(user.getId(), new HashSet<>());
        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Нет такого пользователя");
        }
        return user;
    }

    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id)) throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Нет такого пользователя");
        return users.get(id);
    }

    @Override
    public List<User> findAllFriendsById(Long id) {
        if (!friendList.containsKey(id)) throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Нет такого пользователя");
        return friendList.get(id).stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> saveOneFriend(Long idUser, Long idFriend) {
        if (!(users.containsKey(idUser) && users.containsKey(idFriend)))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет таких пользователей");
         friendList.get(idUser).add(idFriend);
         friendList.get(idFriend).add(idUser);
        return friendList.get(idUser).stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> deleteOneFriend(Long idUser, Long idFriend) {
        if (!users.containsKey(idUser) || !users.containsKey(idFriend))
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Нет таких пользователей");
         friendList.get(idUser).remove(idFriend);
         friendList.get(idFriend).remove(idUser);
        return friendList.get(idUser).stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public User getUser(Long id) {
        return users.get(id);
    }
}
