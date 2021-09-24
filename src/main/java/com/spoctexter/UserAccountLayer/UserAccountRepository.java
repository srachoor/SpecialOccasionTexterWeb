package com.spoctexter.UserAccountLayer;

import com.spoctexter.UserProfileLayer.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    @Query(value = "SELECT * FROM user_account WHERE user_name = ?1", nativeQuery = true)  // native query example -- PSQL
    Optional<UserAccount> findUserAccountByUserName(String userName);

}
