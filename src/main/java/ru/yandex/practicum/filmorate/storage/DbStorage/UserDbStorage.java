package ru.yandex.practicum.filmorate.storage.DbStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;
import ru.yandex.practicum.filmorate.validate.UserValidate;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        List<User> users;

        String sqlQuery = "SELECT * FROM USERS u ";
        users = jdbcTemplate.query(sqlQuery, new UserMapper());
        return users;
    }

    @Override
    public User create(User user) {

        UserValidate.validate(user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertQuery = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) " +
                "VALUES " +
                "(?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setString(4, user.getBirthday().toString());
            return ps;
        }, keyHolder);
        int userId = keyHolder.getKey().intValue();
        user.setId(userId);
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE USER_ID = ?";
        if (jdbcTemplate.queryForObject(sql, Integer.class, user.getId()) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого юзера");
        }
        UserValidate.validate(user);
        String sqlQuery = "UPDATE USERS SET USER_NAME = ?, EMAIL = ?, LOGIN = ?, BIRTHDAY = ? WHERE USER_ID = ?";

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(sqlQuery);
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getLogin());
                ps.setString(4, user.getBirthday().toString());
                ps.setLong(5, user.getId());
                return ps;
            });
        return user;
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE USER_ID = ?";
        if (jdbcTemplate.queryForObject(sql, Integer.class, id) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого юзера");
        }
        String sqlQuery = "SELECT * FROM USERS u " +
                "WHERE u.USER_ID = ?";

        User user = jdbcTemplate.queryForObject(
                sqlQuery,
                new UserMapper(),
                id
        );
        return user;
    }

    @Override
    public List<User> findAllFriendsById(Long id) {
        List<User> users;

        String sqlQuery = "SELECT *" +
                "FROM USERS " +
                "JOIN FRIEND_USER fu on USERS.USER_ID = fu.FRIEND_ID " +
                "WHERE fu.USER_ID = ?" +
                "ORDER by USER_ID";
        users = jdbcTemplate.query(sqlQuery,
                new Object[]{id},
                new UserMapper());
        return users;
    }

    @Override
    public void saveOneFriend(Long idUser, Long idFriend) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE USER_ID = ?";
        if (jdbcTemplate.queryForObject(sql, Integer.class, idUser) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого юзера");
        }
        if (jdbcTemplate.queryForObject(sql, Integer.class, idFriend) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого юзера");
        }
        boolean common = (jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FRIEND_USER WHERE USER_ID = " + idFriend
                + " AND " + "FRIEND_ID = " + idUser, Integer.class) != 0);
        jdbcTemplate.update("DELETE FROM FRIEND_USER WHERE USER_ID = ? AND FRIEND_ID = ?", idUser, idFriend);
        String insertQuery = "INSERT INTO FRIEND_USER (USER_ID, FRIEND_ID, COMMON_FRIEND) VALUES (?, ?, ?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(insertQuery);
                ps.setLong(1, idUser);
                ps.setLong(2, idFriend);
                ps.setBoolean(3,common);
                return ps;
            });
    }

    @Override
    public List<User> deleteOneFriend(Long idUser, Long idFriend) {

        String sqlQuery = "DELETE FROM FRIEND_USER WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, idUser, idFriend);
        return null;
    }
}
