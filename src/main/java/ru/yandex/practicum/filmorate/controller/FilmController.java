package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import java.util.*;


@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final Long id = 0L;
    private final FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(filmService.findAll());
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Получена сущность Film "+ film.getName());
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Обновлена сущность Film "+ film.getName());
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id,
                        @PathVariable Long userId) {
        log.info("Поставлен лайк фильму " + getFilm(id).getName() + "от пользователя c ID" + userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Удален лайк фильму " + getFilm(id).getName() + "от пользователя c ID" + userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopulars(@RequestParam(defaultValue = "10", required = false) int count) {
        return new ArrayList<>(filmService.getPopularFilms(count));
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        return filmService.getFilm(id);
    }
}
