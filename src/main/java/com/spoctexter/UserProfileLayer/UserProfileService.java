package com.spoctexter.UserProfileLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.*;

@Service
class UserProfileService {

    private final UserRepository userRepository;

    @Autowired
    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserProfile> getUserProfiles() { return userRepository.findAll(); }

    public void addNewUserProfile(UserProfile userProfile) {
        Optional<UserProfile> userOptional = userRepository
                .findUserProfileByEmail(userProfile.getEmail());

        if (userOptional.isPresent()) {
            throw new IllegalStateException("Email already taken");
        }

        else if (!userOptional.isPresent()) {
            userOptional = userRepository.findUserProfileByPhoneNumber(userProfile.getPhoneNumber());
        }

        if (userOptional.isPresent()) {
            throw new IllegalStateException("Phone number already taken");
        }

        if (isValidPhone(userProfile.getPhoneNumber()) == false){
            throw new IllegalStateException("Please enter a valid phone number");
        }

        if (isValidEmail(userProfile.getEmail()) == false) {
            throw new IllegalStateException("Please enter a valid email address");
        }

        userRepository.save(userProfile);
    }

    public Optional <UserProfile> getUserProfileByID(UUID id) {
        return this.userRepository.findById(id);
    }

    public Optional <UserProfile> getUserProfileByEmail(String email) {
        Optional<UserProfile> userOptional = userRepository
                .findUserProfileByEmail(email);
        if (userOptional.isPresent()) {
            return this.userRepository.findUserProfileByEmail(email);
        }
        throw new IllegalStateException(("Email does not exist"));
    }

    public Optional <UserProfile> getUserProfileByPhoneNumber(String phoneNumber) {
        Optional<UserProfile> userOptional = userRepository
                .findUserProfileByPhoneNumber(phoneNumber);
        if (userOptional.isPresent()) {
            return this.userRepository.findUserProfileByPhoneNumber(phoneNumber);
        }
        throw new IllegalStateException(("Phone number does not exist"));
    }

    public void deleteUserProfileByID(UUID id) {
        Optional<UserProfile> userOptional = userRepository
                .findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
        }
        throw new IllegalStateException(("User does not exist"));
    }

    public void deleteUserProfileByEmail(String email) {
        Optional<UserProfile> userOptional = userRepository
                .findUserProfileByEmail(email);
        if (userOptional.isPresent()) {
            userRepository.deleteById(userOptional.get().getId());
        }
        else {
            throw new IllegalStateException(("Email does not exist or is not valid."));
        }
    }

    public void deleteUserProfileByPhoneNumber(String phoneNumber) {
        Optional<UserProfile> userOptional = userRepository
                .findUserProfileByPhoneNumber(phoneNumber);
        if (userOptional.isPresent()) {
            userRepository.deleteById(userOptional.get().getId());
        }
        else {
            throw new IllegalStateException(("Phone number does not exist or is not valid."));
        }
    }

    @Transactional
    public void updateUserProfileEmail(String email, String newEmail) {
        if (isValidEmail(newEmail) == false || isValidEmail(email) == false) {
            throw new IllegalStateException("Please enter a valid email address.");
        }
        Optional<UserProfile> currentUserOptional = userRepository
                .findUserProfileByEmail(email);
        if (!currentUserOptional.isPresent()) {
            throw new IllegalStateException("Sorry, we could not find your email.");
        }
        else {
            Optional <UserProfile> conflictUser = userRepository
                    .findUserProfileByEmail(newEmail);
            if (conflictUser.isPresent()) {
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
        if (isValidPhone(phoneNumber) == false || isValidPhone(newPhoneNumber) == false) {
            throw new IllegalStateException("Please enter a valid phone number.");
        }
        Optional<UserProfile> currentUserOptional = userRepository
                .findUserProfileByPhoneNumber(phoneNumber);
        if (!currentUserOptional.isPresent()) {
            throw new IllegalStateException("Sorry, we could not find your account based on the phone number you entered.");
        }
        else {
            Optional <UserProfile> conflictUser = userRepository
                    .findUserProfileByPhoneNumber(newPhoneNumber);
            if (conflictUser.isPresent()) {
                throw new IllegalStateException("Sorry, your new phone number is already taken.");
            }
            else {
                currentUserOptional.get().setPhoneNumber(newPhoneNumber);
                userRepository.save(currentUserOptional.get());
            }
        }
    }

    @Transactional
    public void updateUserProfileFirstName(String email, String newFirstName) {
        if (isValidEmail(email) == false) {
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
        if (isValidEmail(email) == false) {
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

    public boolean isValidPhone(String phoneNum) {
        if (phoneNum.length() == 10) {
            try {
                Long.parseLong(phoneNum);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public boolean isValidEmail(String emailStr) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return emailStr.matches(regex);
    }

}
