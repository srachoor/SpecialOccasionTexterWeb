package com.spoctexter.friends;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendRepository extends JpaRepository<Friend, UUID> {

//    How do I do the below query in the native JPQL query language?
//    @Query("Select s FROM Friend s WHERE s.getUserAccount.getId = ?1 AND s.friendPhoneNumber = ?2")
//    Optional<Friend> findFriendByUserAccountIdAndPhoneNumber(UUID userAccountID, String friendPhoneNumber);

    @Query(value = "SELECT * FROM friend WHERE user_profile_id = ?1 AND friend_phone_number = ?2", nativeQuery = true)
    Optional<Friend> findFriendByUserAccountIdAndPhoneNumber(UUID userAccountID, String friendPhoneNumber);

}
