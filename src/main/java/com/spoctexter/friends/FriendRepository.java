package com.spoctexter.friends;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FriendRepository extends JpaRepository<Friend, UUID> {
}
