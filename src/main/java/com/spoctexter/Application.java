package com.spoctexter;

import com.spoctexter.Friends.FriendRepository;
import com.spoctexter.UserAccountLayer.UserAccount;
import com.spoctexter.UserAccountLayer.UserAccountRepository;
import com.spoctexter.UserProfileLayer.UserProfile;
import com.spoctexter.UserProfileLayer.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.OffsetDateTime;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            UserRepository userRepository,
            UserAccountRepository accountRepository,
            FriendRepository friendRepository) {

        return args -> {
            Faker faker = new Faker();
            Random rand = new Random();
            Integer num1, num2, num3;
            num1 = rand.nextInt (900) + 100;
            num2 = rand.nextInt (643) + 100;
            num3 = rand.nextInt (9000) + 1000;

            UUID id = UUID.randomUUID();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = firstName + "." + lastName + "@spoctexter.com";
            String phoneNumber = num1.toString() + num2.toString() + num3.toString();
            String username = firstName.substring(0,1) + lastName;

            UserProfile user = new UserProfile(
                    id,
                    firstName,
                    lastName,
                    email,
                    phoneNumber,
                    OffsetDateTime.now()
            );

//            userRepository.save(user);

            UserAccount userAccount =  new UserAccount(
                    username,
                    "randompassword",
                    OffsetDateTime.now(),
                    user
            );

            accountRepository.save(userAccount);

        };
    }

}