package com.OnitoAssignment.Task.Repository;

import com.OnitoAssignment.Task.Entity.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingsDao extends JpaRepository<Ratings, String> {

}
