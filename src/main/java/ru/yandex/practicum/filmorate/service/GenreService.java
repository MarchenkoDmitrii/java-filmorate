package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.DbStorage.DbGenreStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    private final DbGenreStorage filmDbStorage;

    public List<Genre> getGenres() {
        return filmDbStorage.findAll();
    }

    public Optional<Genre> getGenre(Integer id) {
        return filmDbStorage.getById(id);
    }
}