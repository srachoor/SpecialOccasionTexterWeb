package com.spoctexter;

import com.spoctexter.friends.Friend;
import com.spoctexter.friends.FriendRepository;
import com.spoctexter.occasions.Occasion;
import com.spoctexter.occasions.OccasionRepository;
import com.spoctexter.twilio.SchedulerController;
import com.spoctexter.userAccount.UserAccount;
import com.spoctexter.userAccount.UserAccountRepository;
import com.spoctexter.userProfile.UserProfile;
import com.spoctexter.userProfile.UserRepository;
import com.github.javafaker.Faker;
import com.twilio.rest.supersim.v1.Command;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Random;
import java.util.UUID;

@EnableEncryptableProperties
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}