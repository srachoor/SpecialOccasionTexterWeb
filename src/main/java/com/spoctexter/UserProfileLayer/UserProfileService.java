package com.spoctexter.UserProfileLayer;

import com.spoctexter.exception.BadInputException;
import com.spoctexter.exception.NamingConflictException;
import com.spoctexter.exception.NotFoundException;
import com.spoctexter.inputvalidation.InputValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserProfileService {

    private final UserRepository userRepository;

    @Autowired
    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final InputValidation inputValidator = new InputValidation();

    public List<UserProfile> getUserProfiles() { return userRepository.findAll(); }

    public void addNewUserProfile(UserProfile userProfile) {
        UserProfile conflictUserEmail = inputValidator.checkEmailConflict(userProfile.getEmail(),userRepository);
        UserProfile conflictUserPhone = inputValidator.checkPhoneConflict(userProfile.getPhoneNumber(),userRepository);

        if (conflictUserPhone == null && conflictUserEmail == null) {
            userRepository.save(userProfile);
        }
        else if (conflictUserPhone != null && conflictUserEmail == null){
            throw new NamingConflictException("Phone number " + userProfile.getPhoneNumber() + " is already registered to another user.");
        }
        else if (conflictUserPhone == null && conflictUserEmail != null){
            throw new NamingConflictException("Email " + userProfile.getEmail() + " is already registered to another user.");
        }
        else {
            throw new NamingConflictException("Email " + userProfile.getEmail() + " and phone number " + userProfile.getPhoneNumber() + " are both already registered to another user");
        }
    }

    public UserProfile getUserProfileByID(UUID id) {

        return this.userRepository
                .findById(id)
                .orElseThrow(
                        () -> {
                            NotFoundException notFoundException = new NotFoundException(
                                    "User with id " + id + " not found");
                            return notFoundException;
                        }
                );
    }

    public UserProfile getUserProfileByEmail(String email) {
        return inputValidator.checkEmail(email, this.userRepository);
    }

    public UserProfile getUserProfileByPhoneNumber(String phoneNumber) {
        return inputValidator.checkPhone(phoneNumber, this.userRepository);
    }

    public void deleteUserProfileByID(UUID id) {
        Optional<UserProfile> userOptional = userRepository
                .findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
        }
        throw new NotFoundException(("User with " + id + " does not exist."));
    }

    public void deleteUserProfileByEmail(String email) {
        userRepository.deleteById(inputValidator.checkEmail(email,this.userRepository).getId());
    }

    public void deleteUserProfileByPhoneNumber(String phoneNumber) {
        userRepository.deleteById(inputValidator.checkPhone(phoneNumber,this.userRepository).getId());
    }

    @Transactional
    public void updateUserProfileEmail(String email, String newEmail) {

        UserProfile currentUser = inputValidator.checkEmail(email, userRepository);

        UserProfile conflictUser = inputValidator.checkEmailConflict(newEmail, userRepository);
        if (conflictUser != null) {
            throw new NamingConflictException("Sorry, your new email is already taken.");
        }
        else {
            currentUser.setEmail(newEmail);
            userRepository.save(currentUser);
        }
    }

    @Transactional
    public void updateUserProfilePhoneNumber(String phoneNumber, String newPhoneNumber) {

        UserProfile currentUser = inputValidator.checkPhone(phoneNumber, userRepository);

        UserProfile conflictUser = inputValidator.checkPhoneConflict(newPhoneNumber, userRepository);
        if (conflictUser != null) {
            throw new NamingConflictException("Sorry, your new phone number is already taken.");
        }
        else {
            currentUser.setPhoneNumber(newPhoneNumber);
            userRepository.save(currentUser);
        }
    }

    @Transactional
    public void updateUserProfileFirstName(String email, String newFirstName) {
        if (!inputValidator.isValidEmail(email)) {
            throw new IllegalStateException("Please enter a valid email");
        }
        Optional<UserProfile> currentUserOptional = userRepository
                .findUserProfileByEmail(email);
        if (!currentUserOptional.isPresent()) {
            throw new IllegalStateException("Sorry, we could not find your account based on the email.");
        }
        else {
            currentUserOptional.get().setFirstName(newFirstName);
            userRepository.save(currentUserOptional.get());
        }
    }

    @Transactional
    public void updateUserProfileLastName(String email, String newLastName) {
        if (!inputValidator.isValidEmail(email)) {
            throw new IllegalStateException("Please enter a valid email");
        }
        Optional<UserProfile> currentUserOptional = userRepository
                .findUserProfileByEmail(email);
        if (!currentUserOptional.isPresent()) {
            throw new IllegalStateException("Sorry, we could not find your account based on the email.");
        }
        else {
            currentUserOptional.get().setLastName(newLastName);
            userRepository.save(currentUserOptional.get());
        }
    }
}
