package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validate.UserValidate;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    protected Long id = 0L;
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Map<Long, Boolean>> friendList = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public List<User> findAll() {
        if (users.entrySet().size() == 0)
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"нет пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        UserValidate.validate(user);
        user.setId(++id);
        this.id = user.getId();
        users.put(user.getId(), user);
        friendList.put(user.getId(), new HashMap<>());
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
        return friendList.get(id).keySet().stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public void saveOneFriend(Long idUser, Long idFriend) {
        if (!(users.containsKey(idUser) && users.containsKey(idFriend)))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет таких пользователей");
         friendList.get(idUser).put(idFriend, false);
         if (friendList.get(idFriend).containsKey(idUser)) {
             friendList.get(idUser).put(idFriend, true);
         } else {
             friendList.get(idUser).put(idFriend, false);
         }
    }

    @Override
    public List<User> deleteOneFriend(Long idUser, Long idFriend) {
        if (!users.containsKey(idUser) || !users.containsKey(idFriend))
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Нет таких пользователей");
         friendList.get(idUser).remove(idFriend);
         friendList.get(idFriend).put(idUser, false);
        return friendList.get(idUser).keySet().stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

}
