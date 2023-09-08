package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre implements Comparable<Genre> {

    private int id;
    private String name;

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(int id) {
        this.id = id;
    }

    public Genre() {
    }

    @Override
    public int compareTo(Genre o) {
        int result = this.id - o.getId();
        if (result == 0) {
            return 0;
        }
        if (result < 0) {
            return -1;
        }
        return 1;
    }
}
