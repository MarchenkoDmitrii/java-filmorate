package ru.yandex.practicum.filmorate.storage.DbStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.validate.FilmValidate;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;


@Repository
@Slf4j
@RequiredArgsConstructor
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public List<Film> findAll() {
        List<Film> films;

        String sqlQuery = "SELECT * FROM FILMS f " +
                "JOIN MPA m ON m.RATING_ID = f.MPA ";

        films = jdbcTemplate.query(sqlQuery, new FilmMapper());

        if (films != null && !films.isEmpty()) {
            for (Film film : films) {
                    List<Genre> genres;
                    sqlQuery = "SELECT g.* FROM GENRES_FILMS gf\n" +
                            "JOIN GENRES g ON g.GENRE_ID = gf.GENRE_ID\n" +
                            "WHERE gf.FILM_ID = ?";
                if (Optional.ofNullable(film.getGenres()) == null){}
                genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
                        Genre genre = new Genre(
                                rs.getInt("GENRE_ID"),
                                rs.getString("GENRE_NAME")
                        );

                        return genre;
                    }, film.getId());
                    film.setGenres(new TreeSet<>(genres));
            }
        }
        return films;
    }
    @Override
    @Transactional
    public Film create(Film film) {
        FilmValidate.validate(film);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String insertQuery = "INSERT INTO FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, MPA) " +
                "VALUES " +
                "(?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setString(3, film.getReleaseDate().toString());
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());

            return ps;
        }, keyHolder);

        Integer filmId = keyHolder.getKey().intValue();

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                for (Genre genre : film.getGenres()) {
                    String sql = "INSERT INTO GENRES_FILMS (GENRE_ID, FILM_ID) VALUES (?, ?)";
                    jdbcTemplate.update(sql, genre.getId(), filmId);
                }
            }
        film.setId( (long) filmId);
        return film;
    }

    @Override
    @Transactional
    public Film update(Film film) {
        String sqlInsert = "SELECT COUNT(*) FROM FILMS WHERE FILM_ID = ?";
        if (jdbcTemplate.queryForObject(sqlInsert,Integer.class,film.getId()) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого фильма");
        }

        FilmValidate.validate(film);

        String sqlQuery = "UPDATE FILMS SET FILM_NAME = ?, FILM_DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?,  MPA = ? WHERE FILM_ID = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setString(3, film.getReleaseDate().toString());
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            ps.setLong(6, film.getId());
            return ps;
        });

        String sqlQueryDeleteGenres = "delete from GENRES_FILMS " +
                "where FILM_ID = ?";
        jdbcTemplate.update(sqlQueryDeleteGenres, film.getId());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                String sql = "INSERT INTO GENRES_FILMS (GENRE_ID, FILM_ID) VALUES (?, ?)";
                System.out.println(sql + " " + genre.getId() + " " + film.getId());
                jdbcTemplate.update(sql, genre.getId(), film.getId());
            }
        }
        return film;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        List<Film> films;
        String sqlQuery = "SELECT *, COUNT(LF.USER_ID) AS LIKE_COUNT " +
                "FROM FILMS f " +
                "LEFT JOIN LIKES_FILMS LF ON LF.FILM_ID = f.FILM_ID " +
                "LEFT JOIN MPA m ON m.RATING_ID = f.MPA " +
                "GROUP BY f.FILM_ID " +
                "ORDER BY LIKE_COUNT DESC " +
                "LIMIT ?";
        System.out.println(sqlQuery);
        films = jdbcTemplate.query(sqlQuery, new FilmMapper(), count);

        for (Film film : films) {
            List<Genre> genres;
            sqlQuery = "SELECT g.* FROM GENRES_FILMS gf\n" +
                    "JOIN GENRES g ON g.GENRE_ID = gf.GENRE_ID\n" +
                    "WHERE gf.FILM_ID = ?";

            genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
                Genre genre = new Genre(
                        rs.getInt("GENRE_ID"),
                        rs.getString("GENRE_NAME")
                );

                return genre;
            }, film.getId());
            film.setGenres(new TreeSet<>(genres));
        }

        log.info("Получен список популярных фильмов с отбором по жанру и году с ограничением по количеству = {}.",
                count);

        return films;
    }

    @Override
    public List<Film> createLike(Long idFilm, Long idUser) {
        String sql = "SELECT COUNT(*) FROM FILMS WHERE FILM_ID = ?";
        if (jdbcTemplate.queryForObject(sql,Integer.class, idFilm) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого фильма");
        }
        String sql1 = "SELECT COUNT(*) FROM USERS WHERE USER_ID = ?";
        if (jdbcTemplate.queryForObject(sql1, Integer.class, idUser) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого ");
        }

        String sqlQuery = "INSERT INTO LIKES_FILMS (FILM_ID, USER_ID) VALUES(?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sqlQuery);
            ps.setLong(1, idFilm);
            ps.setLong(2, idUser);
            return ps;
        });
        ArrayList<Film> result = new ArrayList<>();
        result.add(getFilm(idFilm));
        result.add(getFilm(idUser));
        return result;

    }
    @Override
    public List<Film> removeLike(Long idFilm, Long idUser) {
        String sql = "SELECT COUNT(*) FROM LIKES_FILMS WHERE FILM_ID = ? AND USER_ID = ?";
        if (jdbcTemplate.queryForObject(sql,Integer.class,idFilm ,idUser) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого фильма");
        }

        String sqlQuery = "DELETE FROM LIKES_FILMS WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, idFilm, idUser);
        return null;
    }

    @Override
    public Film getFilm(Long id) {
        List<Genre> genres;
        String sql = "SELECT COUNT(*) FROM FILMS WHERE FILM_ID = ?";
        if (jdbcTemplate.queryForObject(sql, Integer.class, id) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет такого фильма");
        }

        String sqlQuery = "SELECT * FROM FILMS f " +
                "JOIN MPA m ON m.RATING_ID = f.MPA " +
                "WHERE f.FILM_ID = ?";

        Film film = jdbcTemplate.queryForObject(
                sqlQuery,
                new FilmMapper(),
                id
        );

        sqlQuery = "SELECT g.* FROM GENRES_FILMS gf\n" +
                "JOIN GENRES g ON g.GENRE_ID = gf.GENRE_ID\n" +
                "WHERE gf.FILM_ID = ? " +
                "ORDER BY GENRE_ID ASC";

        genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Genre genre = new Genre(
                    rs.getInt("GENRE_ID"),
                    rs.getString("GENRE_NAME")
            );

            return genre;
        }, id);

        film.setGenres(new TreeSet<>(genres));
        log.info("Получен фильм с ID = {}.", id);

        return film;
    }
}
