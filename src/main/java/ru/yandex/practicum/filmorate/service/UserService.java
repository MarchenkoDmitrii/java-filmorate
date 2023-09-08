package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbStorage.UserDbStorage;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserDbStorage userStorage;

    public List<User> getFriends(Long id) {
        return userStorage.findAllFriendsById(id);
    }

    public void addFriend(Long idUser, Long idFriend) {
        userStorage.saveOneFriend(idUser, idFriend);
    }

    public List<User> removeFriend(Long idUser, Long idFriend) {
        return userStorage.deleteOneFriend(idUser, idFriend);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {

        List<User> friendsList = userStorage.findAllFriendsById(id);
        List<User> otherFriendsList = userStorage.findAllFriendsById(otherId);
        friendsList.retainAll(otherFriendsList);

       return new ArrayList<>(friendsList);
    }

    public User getUser(Long id) {
        return userStorage.getUserById(id);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }
}
