package com.OnitoAssignment.Task.Service;

import com.OnitoAssignment.Task.Entity.Movies;
import com.OnitoAssignment.Task.Entity.Ratings;
import com.OnitoAssignment.Task.Repository.MoviesDao;
import com.OnitoAssignment.Task.Repository.RatingsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;

@Service
public class CSVHelper {

    @Autowired
    private MoviesDao moviesDao;

    @Autowired
    private RatingsDao ratingsDao;

    private final String VALID_FILE_TYPE = ".csv";
    private final String MOVIES_FILE = "movies";
    private final String RATINGS_FILE = "ratings";

    static String line = "";
    static String separator = ",";

    public CSVHelper() {
    }

    public void uploadCsvDataIntoDb(String file) throws Exception {
        if (!file.endsWith(VALID_FILE_TYPE)) {
            throw new RuntimeException("Invalid File...");
        }
            BufferedReader br = new BufferedReader(new FileReader(file));
            int l = 0;
            while ((line= br.readLine())!=null) {
                if (l==0) {
                    l++;
                    continue;
                }
                if (file.contains(MOVIES_FILE)) {
                    String[] moviesData = line.split(separator);
                    Movies movies = new Movies(moviesData[0], moviesData[1], moviesData[2],
                            Integer.parseInt(moviesData[3]), moviesData[4]);
                    moviesDao.save(movies);
                }
                if (file.contains(RATINGS_FILE)) {
                    String[] ratingsData = line.split(separator);
                    Ratings ratings = new Ratings(ratingsData[0], Double.parseDouble(ratingsData[1]),
                            Long.parseLong(ratingsData[2]));
                    ratingsDao.save(ratings);
                }
            }
        }
}
