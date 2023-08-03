package ru.yandex.practicum.filmorate.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public List<Film> getLikes(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public List<Film> addLike(Long idUser, Long idFriend) {
        return filmStorage.createLike(idUser,idFriend);
    }

    public List<Film> removeLike(Long idUser, Long idFriend) {
        return filmStorage.removeLike(idUser,idFriend);
    }

    public List<Film> getPopularFilms(int count){
        return filmStorage.getPopularFilms(count);
    }

    public Film getFilm(Long id){
        return filmStorage.getFilm(id);
    }

    public List<Film> findAll(){
        return filmStorage.findAll();
    }

    public Film create(Film film){
        return filmStorage.create(film);
    }

    public Film update(Film film){
        return filmStorage.update(film);
    }
}
