package com.spoctexter.userProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserProfile, UUID> {

    @Query(value = "SELECT * FROM user_profile WHERE email = ?1", nativeQuery = true)  // native query example -- PSQL
    Optional<UserProfile> findUserProfileByEmail(String email);

    @Query("SELECT s FROM UserProfile s WHERE s.phoneNumber = ?1") // JPQL -- Java Persistence Query Language example
    Optional<UserProfile> findUserProfileByPhoneNumber(String phone_number);

}
