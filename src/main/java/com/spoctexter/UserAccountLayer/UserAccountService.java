package com.spoctexter.UserAccountLayer;


import com.spoctexter.UserProfileLayer.UserProfile;
import com.spoctexter.UserProfileLayer.UserRepository;
import com.spoctexter.exception.BadInputException;
import com.spoctexter.exception.NamingConflictException;
import com.spoctexter.exception.NotFoundException;
import com.spoctexter.inputvalidation.InputValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserRepository userProfileRepository;
    private final InputValidation inputValidator = new InputValidation();

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository,
                              UserRepository userProfileRepository) {
        this.userAccountRepository = userAccountRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public void addNewUserAccount(UserAccount userAccount) {

        UserAccount conflictUserAccount = inputValidator.checkUserNameConflict(userAccount.getUserName(),userAccountRepository);

        if(conflictUserAccount == null) {
            userAccountRepository.save(userAccount);
        }
        else {
            throw new NamingConflictException("Username " + userAccount.getUserName() + " is already taken by another account.");
        }
    }

    public UserAccount getUserAccountByUserName(String userName) {

        return this.userAccountRepository
                .findUserAccountByUserName(userName)
                .orElseThrow(
                        () -> {
                            com.spoctexter.exception.NotFoundException notFoundException = new NotFoundException(
                                    "Username " + userName + " not found");
                            return notFoundException;
                        }
                );

    }

    public Boolean validatePassword(String userName, String password) {

        if (password.equals(getUserAccountByUserName(userName).getUserPassword())) {
            return true;
        }
        else{
            throw new BadInputException("Password is not correct.");
        }
    }

    public void updateUserName(String userName, String newUserName) {
        Optional<UserAccount> userAccountOptional = userAccountRepository.findUserAccountByUserName(userName);

        if(userAccountOptional.isPresent()) {
            userAccountOptional.get().setUserName(newUserName);
            userAccountRepository.save(userAccountOptional.get());
        } else {
            throw new NotFoundException("Username not associated with an account.");
        }

    }

    public void updatePassword(String userName, String newPassword) {
        Optional<UserAccount> userAccountOptional = userAccountRepository.findUserAccountByUserName(userName);

        if (userAccountOptional.isPresent()) {
            userAccountOptional.get().setUserPassword(newPassword);
            userAccountRepository.save(userAccountOptional.get());
        } else {
            throw new NotFoundException("Username not associated with an account.");
        }
    }
}
