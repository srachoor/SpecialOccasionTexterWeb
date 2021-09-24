package com.spoctexter.UserAccountLayer;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
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

}
