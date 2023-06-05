package com.OnitoAssignment.Task.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ratings {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String tconst;

    private double averageRating;

    private long numVotes;

    public Ratings() {
    }

    public Ratings(String tconst, double averageRating, long numVotes) {
        this.tconst = tconst;
        this.averageRating = averageRating;
        this.numVotes = numVotes;
    }

    public String getTconst() {
        return tconst;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public long getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(long numVotes) {
        this.numVotes = numVotes;
    }

    @Override
    public String toString() {
        return "Ratings{" +
                "tconst='" + tconst + '\'' +
                ", averageRating=" + averageRating +
                ", numVotes=" + numVotes +
                '}';
    }
}
