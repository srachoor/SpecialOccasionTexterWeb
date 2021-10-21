package com.spoctexter.UserAccountLayer;

import com.spoctexter.UserProfileLayer.UserProfile;

@Deprecated
public class RequestWrapperAccProf {

    UserProfile userProfile;
    UserAccount userAccount;

    public RequestWrapperAccProf(UserProfile userProfile, UserAccount userAccount) {
        this.userProfile = userProfile;
        this.userAccount = userAccount;
    }

    public RequestWrapperAccProf() {
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
}
