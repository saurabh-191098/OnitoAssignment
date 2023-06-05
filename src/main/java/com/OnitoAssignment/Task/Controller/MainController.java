package com.OnitoAssignment.Task.Controller;

import com.OnitoAssignment.Task.Entity.Movies;
import com.OnitoAssignment.Task.Service.CSVHelper;
import com.OnitoAssignment.Task.Service.RequestHandler;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class MainController {

    @Autowired
    private CSVHelper csvHelper;

    @Autowired
    private RequestHandler requestHandler;

    @RequestMapping("/")
    public ResponseEntity<String> uploadCSVFilesIntoDB() {
        try {
            csvHelper.uploadCsvDataIntoDb("D:\\Intellij_Workspace\\Task\\src\\main\\resources\\CSVFiles\\ratings.csv");
            csvHelper.uploadCsvDataIntoDb("D:\\Intellij_Workspace\\Task\\src\\main\\resources\\CSVFiles\\movies.csv");
            return ResponseEntity.ok("Uploaded CSV files data into Db...");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/api/v1/longest-duration-movies")
    public ResponseEntity<List> getTopTenMovies(){
        return ResponseEntity.ok(this.requestHandler.getTopTenMovies().toList());
    }

    @PostMapping("/api/v1/new-movie")
    public ResponseEntity<String> addNewMovie(@RequestBody Movies movies){
        if (!Objects.isNull(movies)) {
            this.requestHandler.addNewMovie(movies);
            return ResponseEntity.ok("Success...");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/api/v1/top-rated-movies")
    public ResponseEntity<List> getTopRatedMovies(){
        return ResponseEntity.ok(this.requestHandler.getTopRatedMovies().toList());
    }

    @GetMapping("/api/v1/genre-movies-with-subtotals")
    public ResponseEntity<List> getGenreMoviesWithSubtotals() {
        return ResponseEntity.ok(this.requestHandler.getGenreMoviesWithSubtotals().toList());
    }

    @PostMapping("/api/v1/update-runtime-minutes")
    public ResponseEntity<String> updateRuntimeMinutesOfAllRecords(){
        return ResponseEntity.ok(this.requestHandler.updateRuntimeMinutesOfAllRecords());
    }

}
