package com.spoctexter.UserAccountLayer;


import com.spoctexter.UserProfileLayer.UserProfile;
import com.spoctexter.UserProfileLayer.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserRepository userProfileRepository;

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository,
                              UserRepository userProfileRepository) {
        this.userAccountRepository = userAccountRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public void addNewUserAccount(UserAccount userAccount) {
        Optional<UserAccount> accountOptional = userAccountRepository
                .findUserAccountByUserName(userAccount.getUserName());

        if (accountOptional.isPresent()) {
            throw new IllegalStateException("That username is already taken");
        }

        else {
            userAccountRepository.save(userAccount);
        }

    }

    public UserAccount getUserAccountByUserName(String userName) {
        Optional <UserAccount> userAccountOptional = userAccountRepository.findUserAccountByUserName(userName);

        if (userAccountOptional.isPresent()) {
            return userAccountOptional.get();
        }
        else{
            throw new IllegalStateException("Username not associated with an account.");
        }

    }

    public Boolean validatePassword(String userName, String password) {
        System.out.println(getUserAccountByUserName(userName).getUserPassword());
        System.out.println(password);

        if (password.equals(getUserAccountByUserName(userName).getUserPassword())) {
            System.out.println("true, they match");
            return true;
        }
        else {
            System.out.println("false, they don't match");
            return false;

        }
    }

}
