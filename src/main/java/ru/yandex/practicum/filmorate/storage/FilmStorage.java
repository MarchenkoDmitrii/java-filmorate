package ru.yandex.practicum.filmorate.storage;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {
    List<Film> findAll();
    Film create(@RequestBody Film film);
    Film update(@RequestBody Film film);

}
