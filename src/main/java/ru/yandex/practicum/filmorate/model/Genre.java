package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Genre implements Comparable<Genre> {

    private int id;
    private String name;

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
