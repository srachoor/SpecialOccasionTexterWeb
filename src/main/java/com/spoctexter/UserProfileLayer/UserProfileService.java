package com.spoctexter.UserProfileLayer;

import com.spoctexter.UserProfileLayer.User_profile;
import com.spoctexter.UserProfileLayer.User_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
class UserProfileService {

    private final User_repository userRepository;

    @Autowired
    public UserProfileService(User_repository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User_profile> getUserProfiles() { return userRepository.findAll(); }

    public void addNewUserProfile(User_profile userProfile) {
        Optional<User_profile> userOptional = userRepository
                .findUserProfileByEmail(userProfile.getEmail());

        if(userOptional.isPresent()) {
            throw new IllegalStateException("Email Taken");
        }
        userRepository.save(userProfile);
    }

    public void deleteUserProfileByID(UUID id) {
        Optional<User_profile> userOptional = userRepository
                .findById(id);
        if(userOptional.isPresent()) {
            userRepository.deleteById(id);
        }
        throw new IllegalStateException(("User Does not Exist"));
    }

    public void deleteUserProfileByEmail(String email) {
        Optional<User_profile> userOptional = userRepository
                .findUserProfileByEmail(email);
        if(userOptional.isPresent()) {
            userRepository.deleteById(userOptional.get().getId());
        }
        throw new IllegalStateException(("Email does not Exist"));
    }

    public void deleteUserProfileByPhoneNumber(String phoneNumber) {
        Optional<User_profile> userOptional = userRepository
                .findUserProfileByPhoneNumber(phoneNumber);
        if(userOptional.isPresent()) {
            userRepository.deleteById(userOptional.get().getId());
        }
        throw new IllegalStateException(("Phone Number does not Exist"));
    }

    @Transactional
    public void updateUserProfileEmail(String email, String newEmail) {
        if(newEmail == null) { throw new IllegalStateException("Please enter a valid email address."); }
        Optional<User_profile> currentUserOptional = userRepository
                .findUserProfileByEmail(email);
        if(!currentUserOptional.isPresent()) {
            throw new IllegalStateException("Sorry, we could not find your email.");
        }
        else {
            Optional <User_profile> conflictUser = userRepository
                    .findUserProfileByEmail(newEmail);
            if(conflictUser.isPresent()) {
               throw new IllegalStateException("Sorry, your new email is already taken.");
            }
            else {
                currentUserOptional.get().setEmail(newEmail);
                userRepository.save(currentUserOptional.get());
            }
        }
    }

    @Transactional
    public void updateUserProfilePhoneNumber(String phoneNumber, String newPhoneNumber) {
        if(newPhoneNumber == null || newPhoneNumber.length() < 10) { throw new IllegalStateException("Please enter a valid phoneNumber."); }
        Optional<User_profile> currentUserOptional = userRepository
                .findUserProfileByPhoneNumber(phoneNumber);
        if(!currentUserOptional.isPresent()) {
            throw new IllegalStateException("Sorry, we could not find your account based on the phone number you entered.");
        }
        else {
            Optional <User_profile> conflictUser = userRepository
                    .findUserProfileByPhoneNumber(newPhoneNumber);
            if(conflictUser.isPresent()) {
                throw new IllegalStateException("Sorry, your new phone number is already taken.");
            }
            else {
                currentUserOptional.get().setPhoneNumber(newPhoneNumber);
                userRepository.save(currentUserOptional.get());
            }
        }
    }
}
