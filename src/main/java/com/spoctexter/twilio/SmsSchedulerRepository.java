package com.spoctexter.twilio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsSchedulerRepository extends JpaRepository<SmsScheduler, Long> {
}
