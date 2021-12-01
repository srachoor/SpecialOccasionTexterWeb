package com.spoctexter.occasions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OccasionRepository extends JpaRepository<Occasion, Long> {

    //This is a PSQL query, not a Hibernate query
    @Query(value = "SELECT * FROM occasion WHERE EXTRACT(month FROM \"occasion_date\") = ?1 AND EXTRACT(day FROM \"occasion_date\") = ?2)", nativeQuery = true)
    List<Occasion> findOccasionsByDate(int month, int day);

}
