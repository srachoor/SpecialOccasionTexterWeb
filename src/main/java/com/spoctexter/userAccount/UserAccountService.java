package com.spoctexter.userAccount;


import com.spoctexter.userProfile.UserRepository;
import com.spoctexter.exception.BadInputException;
import com.spoctexter.exception.NamingConflictException;
import com.spoctexter.inputvalidation.InputValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



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
        return inputValidator.checkUserName(userName, userAccountRepository);
    }

    public Boolean validatePassword(String userName, String password) {

        if (password.equals(getUserAccountByUserName(userName).getUserPassword())) {
            return true;
        }
        else{
            throw new BadInputException("Password is not correct.");
        }
    }

    @Transactional
    public void updateUserName(String userName, String newUserName) {
        UserAccount userAccount = getUserAccountByUserName(userName);
        userAccount.setUserName(newUserName);
        userAccountRepository.save(userAccount);
    }

    @Transactional
    public void updatePassword(String userName, String newPassword) {
        UserAccount userAccount = getUserAccountByUserName(userName);
        userAccount.setUserPassword(newPassword);
        userAccountRepository.save(userAccount);

    }
}
