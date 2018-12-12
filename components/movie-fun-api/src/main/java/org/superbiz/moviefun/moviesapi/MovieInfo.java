package org.superbiz.moviefun.moviesapi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class MovieInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String director;
    private String title;
    private int year;
    private String genre;
    private int rating;

    public MovieInfo() {
    }
    public MovieInfo(String director, String title, int year) {
        this.director = director;
        this.title = title;
        this.year = year;
    }

    public MovieInfo(String director, String title, String genre,int year,  int rating) {
        this.director = director;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieInfo movieInfo = (MovieInfo) o;
        return id == movieInfo.id &&
                year == movieInfo.year &&
                rating == movieInfo.rating &&
                Objects.equals(director, movieInfo.director) &&
                Objects.equals(title, movieInfo.title) &&
                Objects.equals(genre, movieInfo.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, director, title, year, genre, rating);
    }


    public long getId() {
        return id;
    }

    public String getDirector() {
        return director;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public int getRating() {
        return rating;
    }


}
