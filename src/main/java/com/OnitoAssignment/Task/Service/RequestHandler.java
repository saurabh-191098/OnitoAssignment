package com.OnitoAssignment.Task.Service;

import com.OnitoAssignment.Task.Entity.Movies;
import com.OnitoAssignment.Task.Entity.Ratings;
import com.OnitoAssignment.Task.Repository.MoviesDao;
import com.OnitoAssignment.Task.Repository.RatingsDao;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class RequestHandler {

    @Autowired
    private MoviesDao moviesDao;

    @Autowired
    private RatingsDao ratingsDao;

    private static final double AVERAGE_RATING_LIMIT = 6.0;
    private static final String DOCUMENTARY = "documentary";
    private static final String ANIMATION = "animation";

    public JSONArray getTopTenMovies() {
        JSONArray jsonArray = new JSONArray();
        Map<String, Double> map = new HashMap<>();
        for (Movies m : moviesDao.findAll()) {
            map.put(m.getTconst(), (double) m.getRuntimeMinutes());
        }
        List<Map.Entry<String, Double>> entryList = sortBYValue(map);
        int l = 1;
        for (int i = entryList.size()-1; i>=0; i--) {
            if (l>10) {
                break;
            }
            JSONObject jsonObject = new JSONObject();
            String id = entryList.get(i).getKey();
            Optional<Movies> m = moviesDao.findById(id);
            jsonObject.put("tconst", id);
            jsonObject.put("primaryTitle", m.get().getPrimaryTitle());
            jsonObject.put("runtimeMinutes", m.get().getRuntimeMinutes());
            jsonObject.put("genres", m.get().getGenres());
            jsonArray.put(jsonObject);
            l++;
        }
        System.out.println(jsonArray);
        return jsonArray;
    }

    public void addNewMovie(Movies movies) {
        moviesDao.save(movies);
    }

    public JSONArray getTopRatedMovies() {
        JSONArray jsonArray = new JSONArray();
        Map<String, Double> map = new HashMap<>();
        for (Ratings r : ratingsDao.findAll()) {
            if (r.getAverageRating()>AVERAGE_RATING_LIMIT) {
                map.put(r.getTconst(), r.getAverageRating());
            }
        }
        List<Map.Entry<String, Double>> entryList = sortBYValue(map);
        for (Map.Entry<String, Double> e : entryList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tconst", e.getKey());
            Optional<Movies> ob = moviesDao.findById(e.getKey());
            jsonObject.put("primaryTitle", ob.isPresent()?ob.get().getPrimaryTitle():"");
            jsonObject.put("genres", ob.isPresent()?ob.get().getGenres():"");
            jsonObject.put("averageRating", e.getValue());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray getGenreMoviesWithSubtotals() {
        JSONArray parentJsonArray = new JSONArray();
        JSONObject parentJsonObject = new JSONObject();
        List<String> genresList = new ArrayList<>();
        List<Long> totalList = new ArrayList<>();
        for (String s : moviesDao.getAllGenres()) {
            genresList.add(s);
        }
        for (int i=0; i< genresList.size(); i++) {
            long total =0;
            JSONArray childJsonArray = new JSONArray();
            for (Movies m: moviesDao.findAll()) {
                JSONObject childJsonObject = new JSONObject();
                String id = m.getTconst();
                if (!m.getGenres().toLowerCase().equals(genresList.get(i).toLowerCase())) {
                    continue;
                } else {
                    childJsonObject.put("genres", m.getGenres());
                    childJsonObject.put("primaryTitle", m.getPrimaryTitle());
                    Optional<Ratings> r = ratingsDao.findById(id);
                    long votes = r.isPresent()?r.get().getNumVotes():0;
                    childJsonObject.put("numVotes", votes);
                    childJsonArray.put(childJsonObject);
                    total+=votes;
                }
            }
            totalList.add(total);
            parentJsonObject.put("listOf"+genresList.get(i),childJsonArray);
            parentJsonObject.put("totalOf"+genresList.get(i), totalList.get(i));
            if (parentJsonArray.toString().contains("listOf"+genresList.get(i)) ||
                    parentJsonArray.toString().contains("totalOf"+genresList.get(i))) {
                continue;
            } else {
                parentJsonArray.put(parentJsonObject);
            }
        }
        return parentJsonArray;
    }

    @Transactional
    public String updateRuntimeMinutesOfAllRecords() {
        for (Movies m : moviesDao.findAll()) {
            int min = 0;
            if (m.getGenres().toLowerCase().strip().equals(DOCUMENTARY.toLowerCase())) {
                min = m.getRuntimeMinutes() + 15;
                moviesDao.updateInRuntimeMinutes(min, m.getGenres());
            } else if (m.getGenres().toLowerCase().strip().equals(ANIMATION.toLowerCase())) {
                min = m.getRuntimeMinutes() + 30;
                moviesDao.updateInRuntimeMinutes(min, m.getGenres());
            } else {
                min = m.getRuntimeMinutes() + 45;
                moviesDao.updateInRuntimeMinutes(min, m.getGenres());
            }
        }
        return "Success";
    }

    private List<Map.Entry<String, Double>> sortBYValue(Map<String, Double> map) {
        List<Map.Entry<String, Double>> ls = new ArrayList<>(map.entrySet());
        Collections.sort(ls, (o1, o2) -> {
            return (o1.getValue().compareTo(o2.getValue()));
        });
        return ls;
    }
}
