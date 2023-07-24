package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class FilmController {
    private int id = 0;
    Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping("/films")
    public List<Film> findAll() {
        if (films.equals(null))
            throw new ValidationException("Нет фильмов", HttpStatus.NO_CONTENT);
        return films.values().stream().collect(Collectors.toList());
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        if (film.getName().equals("") || film.getName().isEmpty())
            throw new ValidationException("название не может быть пустым", HttpStatus.NOT_FOUND);
        if (film.getDescription().length() > 200)
            throw new ValidationException("максимальная длина описания — 200 символов", HttpStatus.BAD_REQUEST);
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,1)))
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года", HttpStatus.BAD_REQUEST);
        if (film.getDuration() < 0)
            throw new ValidationException("продолжительность фильма должна быть положительной", HttpStatus.BAD_REQUEST);
        film.setId(++id);
        this.id = film.getId();
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Нет такого пользователя", HttpStatus.NOT_FOUND);
        }
        return film;
    }
}
