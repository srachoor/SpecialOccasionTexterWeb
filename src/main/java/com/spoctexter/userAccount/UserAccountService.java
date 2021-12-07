package com.spoctexter.userAccount;


import com.spoctexter.userProfile.UserRepository;
import com.spoctexter.exception.BadInputException;
import com.spoctexter.exception.NamingConflictException;
import com.spoctexter.inputvalidation.InputValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UserAccountService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;
    private final UserRepository userProfileRepository;
    private final InputValidation inputValidator = new InputValidation();
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository
                .findUserAccountByUserName(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("Username %s not found", username))
                );

        return new MyUserDetails(userAccount);
    }

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository,
                              UserRepository userProfileRepository,
                              PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addNewUserAccount(UserAccount userAccount) {

        UserAccount conflictUserAccount = inputValidator.checkUserNameConflict(userAccount.getUsername(),userAccountRepository);

        if(conflictUserAccount == null) {
            userAccountRepository.save(userAccount);
        }
        else {
            throw new NamingConflictException("Username " + userAccount.getUsername() + " is already taken by another account.");
        }
    }

    public UserAccount getUserAccountByUserName(String userName) {
        return inputValidator.checkUserName(userName, userAccountRepository);
    }

    public Boolean validatePassword(String userName, String password) {

        if (passwordEncoder.matches(password,getUserAccountByUserName(userName).getPassword())) {
            return true;
        }
        else{
            throw new BadInputException("Password is not correct.");
        }
    }

    @Transactional
    public void updateUserName(String userName, String newUserName) {
        UserAccount userAccount = getUserAccountByUserName(userName);
        userAccount.setUsername(newUserName);
        userAccountRepository.save(userAccount);
    }

    @Transactional
    public void updatePassword(String userName, String newPassword) {
        UserAccount userAccount = getUserAccountByUserName(userName);
        userAccount.setPassword(newPassword);
        userAccountRepository.save(userAccount);

    }
}
