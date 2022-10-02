package com.leetlab.videodownloaderfortiktok.model;

import java.util.List;

public class UserDetailModel {

    String userName;
    String profilePic;
    String caption;
    String totalLikes = "0";
    String totalComments = "0";
    List<UserMediaModel> userMediaModelList;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(String totalLikes) {
        this.totalLikes = totalLikes;
    }

    public String getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(String totalComments) {
        this.totalComments = totalComments;
    }

    public List<UserMediaModel> getUserMediaModelList() {
        return userMediaModelList;
    }

    public void setUserMediaModelList(List<UserMediaModel> userMediaModelList) {
        this.userMediaModelList = userMediaModelList;
    }
}