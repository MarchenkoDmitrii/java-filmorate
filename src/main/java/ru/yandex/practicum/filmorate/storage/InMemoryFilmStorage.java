package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validate.FilmValidate;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Long id = 0L;
    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, List<Long>> likes = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        FilmValidate.validate(film);
        film.setId(++id);
        this.id = film.getId();
        films.put(film.getId(), film);
        likes.put(film.getId(), new ArrayList<>());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"нет такого пользователя");
        }
        return film;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        if (likes.values().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"нет лайков");
        Map<Long,List<Long>> sortedLikes = likes.entrySet().stream()
                .sorted(Comparator.comparingLong(entry -> entry.getValue().size()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, TreeMap::new));
        return  sortedLikes.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .limit(count)
                .map(films::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> createLike(Long idFilm, Long idUser) {
        likes.get(idFilm).add(idUser);
        return likes.get(idFilm).stream()
                .map(films::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> removeLike(Long idFilm, Long idUser) {
        if (!films.containsKey(idFilm) || !likes.get(idFilm).contains(idUser))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Такого фильма или юзера нет");
        likes.get(idFilm).remove(idUser);
        return likes.get(idFilm).stream()
                .map(films::get)
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilm(Long id) {
        if (!films.containsKey(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Такого фильма нет");
        return films.get(id);
    }
}
