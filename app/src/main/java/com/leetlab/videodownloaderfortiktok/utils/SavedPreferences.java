package com.leetlab.videodownloaderfortiktok.utils;

import android.util.Log;

import com.leetlab.videodownloaderfortiktok.model.InstaUserModel;
import com.leetlab.videodownloaderfortiktok.model.PrivatAccount;
import com.leetlab.videodownloaderfortiktok.model.UserDetailModel;
import com.leetlab.videodownloaderfortiktok.model.UserMediaModel;
import com.leetlab.videodownloaderfortiktok.model.UserObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SavedPreferences {

    public SavedPreferences() {

    }

    public InstaUserModel parsingInstaUserModel(JSONObject response) {
        InstaUserModel instaUserModel = new InstaUserModel();
        try {
            if (response.has("user")) {
                JSONObject userObject = response.getJSONObject("user");
                if (userObject.has("username"))
                    instaUserModel.setUsername(userObject.getString("username"));
                if (userObject.has("full_name"))
                    instaUserModel.setName(userObject.getString("full_name"));
                if (userObject.has("profile_pic_url"))
                    instaUserModel.setImage_url(userObject.getString("profile_pic_url"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instaUserModel;
    }

    public UserDetailModel parsingUserProfilePic(JSONObject response) {
        UserDetailModel userDetailModel = new UserDetailModel();
        try {
            JSONObject userObject = response.getJSONObject("user");
            if (userObject.has("profile_pic_url_hd") && !userObject.isNull("profile_pic_url_hd")) {

                List<UserMediaModel> userMediaModelList = new ArrayList<>();
                UserMediaModel userMediaModel = new UserMediaModel();

                userMediaModel.setVideo(false);
                userMediaModel.setDisplayUrl(userObject.getString("profile_pic_url_hd"));
                userMediaModel.setMediaUrl(userObject.getString("profile_pic_url_hd"));

                userMediaModelList.add(userMediaModel);

                userDetailModel.setUserMediaModelList(userMediaModelList);
                userDetailModel.setProfilePic(userObject.getString("profile_pic_url_hd"));
            }
            if (userObject.has("username") && !userObject.isNull("username"))
                userDetailModel.setUserName(userObject.getString("username"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userDetailModel;
    }

    public PrivatAccount parsingPrivateVariable(JSONObject response) {
        PrivatAccount privatAccount = new PrivatAccount();
        try {
            if (response.has("shortcode_media") && !response.isNull("shortcode_media")) {
                JSONObject shortcode = response.getJSONObject("shortcode_media");

                if (shortcode.has("owner") && !shortcode.isNull("owner")) {
                    JSONObject ownerObject = shortcode.getJSONObject("owner");

                    if (ownerObject.has("is_private") && !ownerObject.isNull("is_private")) {
                        privatAccount.setPrivate(ownerObject.getBoolean("is_private"));
                    } else {
                        privatAccount.setPrivate(ownerObject.getBoolean("is_private"));
                    }
                } else {
                    Log.d("Private Account", "shortcode_media Not");
                }
            } else {
                Log.d("Private Account", "shortcode_media Not");
            }

        } catch (Exception e) {
            Log.d("Private Account", "Checking Private Account Or Not");
            e.printStackTrace();
        }
        return privatAccount;
    }

    public boolean parsingFollowByUserOrNot(JSONObject response) {
        boolean isFollow = true;
        try {
            if (response.has("shortcode_media") && !response.isNull("shortcode_media")) {
                JSONObject shortcode = response.getJSONObject("shortcode_media");

                if (shortcode.has("owner") && !shortcode.isNull("owner")) {
                    JSONObject ownerObject = shortcode.getJSONObject("owner");

                    if (ownerObject.has("is_private") && !ownerObject.isNull("followed_by_viewer"))
                        isFollow = ownerObject.getBoolean("followed_by_viewer");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isFollow;
    }

    public UserDetailModel parsingMediaFromLink(JSONObject response) {
        Log.d("SavedPref", response.toString());
        UserDetailModel userDetailModel = new UserDetailModel();
        List<UserMediaModel> userMediaModelList = new ArrayList<>();
        try {
            JSONObject shortCode_media_object = response.getJSONObject("shortcode_media");
            String caption = getCaption(shortCode_media_object);
            userDetailModel.setCaption(caption);
            String likes = getLikes(shortCode_media_object);
            userDetailModel.setTotalLikes(likes);
            String comments = getComments(shortCode_media_object);
            userDetailModel.setTotalComments(comments);
            if (shortCode_media_object.has("edge_sidecar_to_children")) {
                JSONObject sideCarObject = shortCode_media_object.
                        getJSONObject("edge_sidecar_to_children");

                JSONArray edgesArray = sideCarObject.getJSONArray("edges");
                for (int i = 0; i < edgesArray.length(); i++) {
                    JSONObject edgeObject = edgesArray.getJSONObject(i);
                    JSONObject nodeObject = edgeObject.getJSONObject("node");
                    if (nodeObject.has("is_video")) {
                        boolean is_inner_video = nodeObject.getBoolean("is_video");
                        if (is_inner_video) {
                            if (nodeObject.has("video_url")) {
                                UserMediaModel mediaModel = new UserMediaModel();
                                mediaModel.setVideo(true);
                                mediaModel.setDisplayUrl(nodeObject.getString(
                                        "display_url"));
                                mediaModel.setMediaUrl(nodeObject.getString(
                                        "video_url"));
                                userMediaModelList.add(mediaModel);
                            }
                        } else {
                            if (nodeObject.has("display_url")) {
                                UserMediaModel mediaModel = new UserMediaModel();
                                mediaModel.setVideo(false);
                                mediaModel.setDisplayUrl(nodeObject.getString(
                                        "display_url"));
                                mediaModel.setMediaUrl(nodeObject.getString(
                                        "display_url"));
                                userMediaModelList.add(mediaModel);
                            }

                        }
                    }
                }
                userDetailModel.setUserMediaModelList(userMediaModelList);
            } else {
                if (shortCode_media_object.has("is_video")) {
                    boolean is_video = shortCode_media_object.getBoolean("is_video");
                    if (is_video) {
                        if (shortCode_media_object.has("video_url")) {
                            UserMediaModel mediaModel = new UserMediaModel();
                            mediaModel.setVideo(true);
                            mediaModel.setDisplayUrl(shortCode_media_object.getString(
                                    "display_url"));
                            mediaModel.setMediaUrl(shortCode_media_object.getString(
                                    "video_url"));
                            userMediaModelList.add(mediaModel);
                        }
                    } else {
                        if (shortCode_media_object.has("display_url")) {
                            UserMediaModel mediaModel = new UserMediaModel();
                            mediaModel.setVideo(false);
                            mediaModel.setDisplayUrl(shortCode_media_object.getString(
                                    "display_url"));
                            mediaModel.setMediaUrl(shortCode_media_object.getString(
                                    "display_url"));
                            userMediaModelList.add(mediaModel);
                        }

                    }

                    userDetailModel.setUserMediaModelList(userMediaModelList);
                }
            }
            Log.d("LIST SIZE ===", userMediaModelList.size() + "");
            if (shortCode_media_object.has("owner") && !shortCode_media_object.isNull("owner")) {
                JSONObject ownerObject = shortCode_media_object.getJSONObject("owner");

                if (ownerObject.has("username") && !ownerObject.isNull("username"))
                    userDetailModel.setUserName(ownerObject.getString("username"));

                if (ownerObject.has("profile_pic_url") && !ownerObject.isNull("profile_pic_url"))
                    userDetailModel.setProfilePic(ownerObject.getString("profile_pic_url"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userDetailModel;
    }

    private String getLikes(JSONObject shortCode_media_object) {
        try {
            if (!shortCode_media_object.has("edge_media_preview_like")) {
                return null;
            }

            JSONObject edgeMediaLike = shortCode_media_object.getJSONObject("edge_media_preview_like");
            if (!edgeMediaLike.has("count")) {
                return null;
            }

            return edgeMediaLike.getString("count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getComments(JSONObject shortCode_media_object) {
        try {
            if (!shortCode_media_object.has("edge_media_preview_comment")) {
                return null;
            }

            JSONObject edgeMediaLike = shortCode_media_object.getJSONObject("edge_media_preview_comment");
            if (!edgeMediaLike.has("count")) {
                return null;
            }

            return edgeMediaLike.getString("count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getCaption(JSONObject shortCode_media_object) {
        JSONObject edgeCaption = null;
        try {
            if (!shortCode_media_object.has("edge_media_to_caption")) {
                return null;
            }

            edgeCaption = shortCode_media_object.getJSONObject("edge_media_to_caption");
            if (!edgeCaption.has("edges")) {
                return null;
            }

            JSONArray captionNode = edgeCaption.getJSONArray("edges");
            if (captionNode.length() != 0) {
                return captionNode.getJSONObject(0).getJSONObject("node").getString("text");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public UserObject parsingUserObject(JSONObject ownerObject) {
        UserObject object = new UserObject();
        try {
            object.setImage(ownerObject.get("profile_pic_url").toString());
            object.setRealName(ownerObject.get("full_name").toString());
            object.setUserName(ownerObject.get("username").toString());
            object.setUserId(ownerObject.get("pk").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }
}
