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


//    @Bean
//    public ScheduledAnnotationBeanPostProcessor getScheduledAnnotationBeanPostProcessor() {
//        return new ScheduledAnnotationBeanPostProcessor();
//    }

//    @Bean
//    CommandLineRunner commandLineRunner(
//            UserRepository userRepository,
//            UserAccountRepository accountRepository,
//            FriendRepository friendRepository,
//            OccasionRepository occasionRepository) {
//
//        return args -> {
//            Faker faker = new Faker();
//            Random rand = new Random();
//            Integer num1, num2, num3;
//            num1 = rand.nextInt (900) + 100;
//            num2 = rand.nextInt (643) + 100;
//            num3 = rand.nextInt (9000) + 1000;
//
//            UUID id = UUID.randomUUID();
//            String firstName = faker.name().firstName();
//            String lastName = faker.name().lastName();
//            String email = firstName + "." + lastName + "@spoctexter.com";
//            String phoneNumber = num1.toString() + num2.toString() + num3.toString();
//            String username = firstName.substring(0,1) + lastName;
//
//            UserProfile user = new UserProfile(
//                    id,
//                    firstName,
//                    lastName,
//                    email,
//                    phoneNumber,
//                    OffsetDateTime.now()
//            );
//
//            UserAccount userAccount =  new UserAccount(
//                    username,
//                    "randompassword",
//                    OffsetDateTime.now(),
//                    user
//            );
//
//            accountRepository.save(userAccount);
//
//            Friend Sai = new Friend (UUID.randomUUID(),"Sai","Rachoor","8564269229", LocalDate.now());
//            userAccount.addFriend(Sai);
//
//            friendRepository.save(Sai);
//
//            Occasion saiBirthday = new Occasion("Sai's Birthday", LocalDate.now().minusYears(32));
//            Occasion saiAnniversary = new Occasion("Sai's Anniversary", LocalDate.now());
//
//            Sai.addOccasion(saiBirthday);
//            Sai.addOccasion(saiAnniversary);
//
//            occasionRepository.save(saiBirthday);
//            occasionRepository.save(saiAnniversary);
//
////            accountRepository.save(userAccount);
//
//        };
//    }

}