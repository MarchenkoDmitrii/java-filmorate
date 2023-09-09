package ru.yandex.practicum.filmorate.storage.DbStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.mapper.MpaMapper;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class DbMpaStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public List<Mpa> findAll() {
        String sqlQuery = "select * from MPA";

        List<Mpa> mpa = jdbcTemplate.query(sqlQuery, new MpaMapper());
        log.info("Получены все рейтинги");

        return mpa;
    }

    @Override
    @Transactional
    public Optional<Mpa> getById(Integer id) {

        String sqlQuery = "select count(*) from MPA where RATING_ID = ?";
        if (jdbcTemplate.queryForObject(sqlQuery, Integer.class, id) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого рейтинга");
        }
        Mpa mpa = jdbcTemplate.queryForObject(
                "select * from MPA where RATING_ID = ?",
                new MpaMapper(),
                id
        );
        return Optional.ofNullable(mpa);
    }
}