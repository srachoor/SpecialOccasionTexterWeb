package com.spoctexter;

import com.spoctexter.Friends.Friend;
import com.spoctexter.Friends.FriendRepository;
import com.spoctexter.UserAccountLayer.UserAccount;
import com.spoctexter.UserAccountLayer.UserAccountRepository;
import com.spoctexter.UserProfileLayer.User_profile;
import com.spoctexter.UserProfileLayer.User_repository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(User_repository userRepository, UserAccountRepository accountRepository, FriendRepository friendRepository) {
        return args -> {
            Faker faker = new Faker();

            Random rand = new Random();
            Integer num1, num2, num3;

            for (int i = 0; i < 50; i++) {
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                String email = (firstName +"."+lastName+"@spoctexter.com");
                UUID id = UUID.randomUUID();
                num1 = rand.nextInt (900) + 100;
                num2 = rand.nextInt (643) + 100;
                num3 = rand.nextInt (9000) + 1000;
                String phoneNumber = num1.toString() + num2.toString() + num3.toString();

                String username = firstName.substring(0,1) + lastName;
                String password = "randompassword";

                User_profile newUser = new User_profile(
                        id,
                        firstName,
                        lastName,
                        email,
                        phoneNumber,
                        OffsetDateTime.now()
                );

//                userRepository.save(newUser);

                UserAccount newAccount = new UserAccount(
                        username,
                        password,
                        OffsetDateTime.now()
                        //newUser
                );
    
                System.out.println(newAccount);
//                accountRepository.save(newAccount);
                newAccount.setUserProfile(newUser);
//                accountRepository.save(newAccount);
//                userRepository.save(newUser);

                System.out.println(newAccount);

                for (int x = 0; x < 3; x++) {
                    firstName = faker.name().firstName();
                    lastName = faker.name().lastName();
                    id = UUID.randomUUID();
                    num1 = rand.nextInt (900) + 100;
                    num2 = rand.nextInt (643) + 100;
                    num3 = rand.nextInt (9000) + 1000;
                    phoneNumber = num1.toString() + num2.toString() + num3.toString();
//
                    Friend newFriend = new Friend (
                            id,
                            firstName,
                            lastName,
                            phoneNumber,
                            OffsetDateTime.now()
                    );

                    System.out.println(newFriend);
//                    friendRepository.save(newFriend);
                    newFriend.setUserAccount(newAccount);
//                    newAccount.addFriend(newFriend);
//                    System.out.println(newFriend);
                    friendRepository.save(newFriend);
//                    userRepository.save(newUser);
//                    accountRepository.save(newAccount);
                }


//                accountRepository.save(newAccount);

            }




        };
    }

}