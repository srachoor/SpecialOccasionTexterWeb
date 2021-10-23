package com.spoctexter.inputvalidation;

import com.spoctexter.UserAccountLayer.UserAccount;
import com.spoctexter.UserAccountLayer.UserAccountRepository;
import com.spoctexter.UserProfileLayer.UserProfile;
import com.spoctexter.UserProfileLayer.UserRepository;
import com.spoctexter.exception.BadInputException;
import com.spoctexter.exception.NotFoundException;
import com.spoctexter.friends.Friend;
import com.spoctexter.friends.FriendRepository;

import java.util.UUID;

public class InputValidation {

    public InputValidation() {
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

    public UserProfile checkPhone(String phoneNum, UserRepository userRepository) {
        if(!this.isValidPhone(phoneNum)) {
            throw new BadInputException(phoneNum + " is not a valid phone number.");
        }
        return userRepository
                .findUserProfileByPhoneNumber(phoneNum)
                .orElseThrow(
                        () -> {
                            NotFoundException notFoundException = new NotFoundException(
                                    "User with phone number " + phoneNum + " is not found.");
                            return notFoundException;
                        }
                );
    }

    public UserProfile checkEmail(String email, UserRepository userRepository) {
        if (!this.isValidEmail(email)) {
            throw new BadInputException(email + " is not a valid email.");
        }
        return userRepository
                .findUserProfileByEmail(email)
                .orElseThrow(
                        () -> {
                            NotFoundException notFoundException = new NotFoundException(
                                    "User with email " + email + " not found");
                            return notFoundException;
                        }
                );
    }

    public Friend checkFriend(UUID friendID, FriendRepository friendRepository) {
        return friendRepository
                .findById(friendID)
                .orElseThrow(
                        () -> {
                            NotFoundException notFoundException = new NotFoundException(
                                    "Friend was not found in your account");
                            return notFoundException;
                        }
                );
    }

    public UserAccount checkUserName(String userName, UserAccountRepository userAccountRepository) {
        return userAccountRepository
                .findUserAccountByUserName(userName)
                .orElseThrow(
                        () -> {
                            NotFoundException notFoundException = new NotFoundException(
                                    "User with username " + userName + " was not found.");
                            return notFoundException;
                        }
                );
    }

    public UserProfile checkEmailConflict(String newEmail, UserRepository userRepository) {
        if(!this.isValidEmail(newEmail)) {
            throw new BadInputException(newEmail + " is not a valid email.");
        }
        return userRepository
                .findUserProfileByEmail(newEmail)
                .orElse(null);
    }

    public UserProfile checkPhoneConflict(String phoneNum, UserRepository userRepository) {
        if(!this.isValidPhone(phoneNum)) {
            throw new BadInputException(phoneNum + " is not a valid phone number.");
        }
        return userRepository
                .findUserProfileByPhoneNumber(phoneNum)
                .orElse(null);
    }

    public UserAccount checkUserNameConflict(String userName, UserAccountRepository userAccountRepository) {
        return userAccountRepository
                .findUserAccountByUserName(userName)
                .orElse(null);
    }

    public Friend checkFriendConflict(UserAccount userAccount, Friend friend, FriendRepository friendRepository) {
        return friendRepository
                .findFriendByUserAccountIdAndPhoneNumber(userAccount.getId(), friend.getFriendPhoneNumber())
                .orElse(null);
    }

}
