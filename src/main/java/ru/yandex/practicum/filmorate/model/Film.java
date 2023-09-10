package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;
import java.util.*;

@Data
public class Film {

    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private TreeSet<Genre> genres;
    private Mpa mpa;

}
