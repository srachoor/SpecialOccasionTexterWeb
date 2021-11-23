package com.spoctexter.userProfile;

import com.spoctexter.exception.NamingConflictException;
import com.spoctexter.exception.NotFoundException;
import com.spoctexter.inputvalidation.InputValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
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

    @Transactional
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

    @Transactional
    public void deleteUserProfileByEmail(String email) {
        userRepository.deleteById(inputValidator.checkEmail(email,this.userRepository).getId());
    }

    @Transactional
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

        UserProfile currentUser = inputValidator.checkEmail(email,userRepository);
        currentUser.setFirstName(newFirstName);
        userRepository.save(currentUser);

    }

    @Transactional
    public void updateUserProfileLastName(String email, String newLastName) {

        UserProfile currentUser = inputValidator.checkEmail(email,userRepository);
        currentUser.setLastName(newLastName);
        userRepository.save(currentUser);

    }

    public void updateUserProfile(String newEmail, String newPhoneNumber, String newFirstName, String newLastName, UUID userProfileId) {

        UserProfile userProfile = this.getUserProfileByID(userProfileId);

        if(newEmail != "") {
            this.updateUserProfileEmail(userProfile.getEmail(), newEmail);
        }
        if(newPhoneNumber != "") {
            this.updateUserProfilePhoneNumber(userProfile.getPhoneNumber(),newPhoneNumber);
        }
        if(newFirstName != "") {
            this.updateUserProfileFirstName(userProfile.getEmail(), newFirstName);
        }
        if(newLastName != "") {
            this.updateUserProfileLastName(userProfile.getEmail(),newLastName);
        }
    }
}