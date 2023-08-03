package ru.yandex.practicum.filmorate.storage;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {

    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    List<Film> getPopularFilms(int count);

    List<Film> createLike(Long idFilm, Long idUser);

    List<Film> removeLike(Long idFilm, Long idUser);

    Film getFilm(Long id);
}
