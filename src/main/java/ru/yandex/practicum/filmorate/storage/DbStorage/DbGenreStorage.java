package ru.yandex.practicum.filmorate.storage.DbStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;


import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class DbGenreStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public List<Genre> findAll() {

        String sqlQuery = "select * from GENRES";

        List<Genre> genres = jdbcTemplate.query(sqlQuery, new GenreMapper());

        log.info("Получены все жанры");

        return genres;
    }

    @Override
    @Transactional
    public Optional<Genre> getById(Integer id) {

        String sql = "SELECT COUNT(*) FROM GENRES WHERE GENRE_ID = ?";
        if (jdbcTemplate.queryForObject(sql, Integer.class, id) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого жанра");
        }

        Genre genre = jdbcTemplate.queryForObject(
                "select * from GENRES where GENRE_ID = ?",
                new GenreMapper(),
                id
        );
        return Optional.ofNullable(genre);
    }
}