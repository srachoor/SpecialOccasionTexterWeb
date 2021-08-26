package com.spoctexter.UserProfileLayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface User_repository extends JpaRepository<User_profile, UUID> {

    @Query(value = "SELECT * FROM user_profile WHERE email = ?1", nativeQuery = true)  // native query example -- PSQL
    Optional<User_profile> findUserProfileByEmail(String email);

    @Query("SELECT s FROM User_profile s WHERE s.phoneNumber = ?1") // JPQL -- Java Persistence Query Language example
    Optional<User_profile> findUserProfileByPhoneNumber(String phone_number);

}
