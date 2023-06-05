package com.OnitoAssignment.Task.Repository;

import com.OnitoAssignment.Task.Entity.Movies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoviesDao extends JpaRepository<Movies, String> {

    @Modifying
    @Query("update Movies m set m.runtimeMinutes = :minutes where m.genres = :genres")
    void updateInRuntimeMinutes(@Param("minutes") int minutes, @Param("genres") String genres);

    @Query("select m.genres from Movies m group by m.genres")
    List<String> getAllGenres();

}
