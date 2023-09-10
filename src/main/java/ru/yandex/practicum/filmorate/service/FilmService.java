package ru.yandex.practicum.filmorate.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DbStorage.FilmDbStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmDbStorage;

    public List<Film> getLikes(int count) {
        return filmDbStorage.getPopularFilms(count);
    }

    public List<Film> addLike(Long idUser, Long idFriend) {
        return filmDbStorage.createLike(idUser,idFriend);
    }

    public List<Film> removeLike(Long idUser, Long idFriend) {
        return filmDbStorage.removeLike(idUser,idFriend);
    }

    public List<Film> getPopularFilms(int count) {
        return filmDbStorage.getPopularFilms(count);
    }

    public Film getFilm(Long id) {
        return filmDbStorage.getFilm(id);
    }

    public List<Film> findAll() {
        return filmDbStorage.findAll();
    }

    public Film create(Film film) {
        return filmDbStorage.create(film);
    }

    public Film update(Film film) {
        return filmDbStorage.update(film);
    }
}
