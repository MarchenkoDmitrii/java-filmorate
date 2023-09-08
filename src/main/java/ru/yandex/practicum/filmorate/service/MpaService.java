package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.DbStorage.DbMpaStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {

    private final DbMpaStorage mpaStorage;

    public List<Mpa> getMpa() {
        return mpaStorage.findAll();
    }

    public Optional<Mpa> getMpa(Integer id) {
        return mpaStorage.getById(id);
    }
}