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
        Optional<UserProfile> userOptional = userRepository
                .findUserProfileByEmail(userProfile.getEmail());

        if (userOptional.isPresent()) {
            throw new NamingConflictException("Email is already registered to another user.");
        }

        else if (!userOptional.isPresent()) {
            userOptional = userRepository
                    .findUserProfileByPhoneNumber(userProfile
                            .getPhoneNumber());
        }

        if (userOptional.isPresent()) {
            throw new NamingConflictException("Phone number is already registered to another user.");
        }

        if (!inputValidator.isValidPhone(userProfile.getPhoneNumber())){
            throw new BadInputException("Please enter a valid phone number.");
        }

        if (!inputValidator.isValidEmail(userProfile.getEmail())) {
            throw new BadInputException("Please enter a valid email address.");
        }

        userRepository.save(userProfile);
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
        if (!inputValidator.isValidEmail(newEmail) || !inputValidator.isValidEmail(email)) {
            throw new BadInputException(email + " is not a valid email.");
        }
        Optional<UserProfile> currentUserOptional = userRepository
                .findUserProfileByEmail(email);
        if (!currentUserOptional.isPresent()) {
            throw new NotFoundException("Sorry, we could not find your email.");
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
        if (!inputValidator.isValidPhone(phoneNumber) || !inputValidator.isValidPhone(newPhoneNumber)) {
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
