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

    private final DbGenreStorage dbGenreStorage;

    public List<Genre> getGenres() {
        return dbGenreStorage.findAll();
    }

    public Optional<Genre> getGenre(Integer id) {
        return dbGenreStorage.getById(id);
    }
}