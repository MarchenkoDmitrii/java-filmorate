package ru.yandex.practicum.filmorate.validate;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Optional;

public class FilmValidate {
    public static Film validate(Film film) {
        if (Optional.ofNullable(film).isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Попытка добавить пустое значение");
        if (film.getName().equals("") || film.getName().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "название не может быть пустым");
        if (film.getDescription().length() > 200)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "максимальная длина описания — 200 символов");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,1)))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "дата релиза — не раньше 28 декабря 1895 года");
        if (film.getDuration() < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "продолжительность фильма должна быть положительной");
        return film;
    }
}
