package com.leetlab.videodownloaderfortiktok.utils.volley;

import android.content.Context;

import com.leetlab.videodownloaderfortiktok.model.InstaContent;
import com.leetlab.videodownloaderfortiktok.model.InstaDpModel;
import com.leetlab.videodownloaderfortiktok.model.PostModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestUtil {
    private Context context;

    private RequestUtil(Context context) {
        this.context = context;
    }

    public static RequestUtil init(Context context) {
        return new RequestUtil(context);
    }

    public void fetchPost(String url, PhotoVideoListener listener) {
        PostModel post = new PostModel();
        VolleyUtil.init(context).get(url, (isSuccessFul, response) -> {
            if (!isSuccessFul) {
                listener.onResponse(isSuccessFul, null);
                return;
            }
            JSONObject graphql = response.getJSONObject("graphql");
            JSONObject shortcodeMedia = graphql.getJSONObject("shortcode_media");
            JSONObject owner = shortcodeMedia.getJSONObject("owner");
            JSONObject edgeMediaComment = shortcodeMedia.getJSONObject("edge_media_preview_comment");
            JSONObject edgeMediaLike = shortcodeMedia.getJSONObject("edge_media_preview_like");
            JSONObject edgeCaption = shortcodeMedia.getJSONObject("edge_media_to_caption");
            JSONArray captionNode = edgeCaption.getJSONArray("edges");
            List<InstaContent> contents = new ArrayList<>();
            if (shortcodeMedia.has("edge_sidecar_to_children")) {
                JSONArray edgeChildern = shortcodeMedia.getJSONObject("edge_sidecar_to_children").getJSONArray("edges");
                for (int i = 0; i < edgeChildern.length(); i++) {
                    JSONObject nodeChild = edgeChildern.getJSONObject(i).getJSONObject("node");
                    boolean isVideo = nodeChild.getBoolean("is_video");
                    String contentUrl = null;
                    String contentThumbnail = null;
                    if (isVideo) {
                        contentUrl = nodeChild.getString("video_url");
                        contentThumbnail = shortcodeMedia.getString("display_url");
                    } else {
                        contentUrl = nodeChild.getString("display_url");
                    }
                    contents.add(new InstaContent(contentUrl, isVideo, contentThumbnail));
                }
            } else {
                String singleUrl = null;
                String singleVideoThumbnail = null;
                boolean isVideo = shortcodeMedia.getBoolean("is_video");
                if (isVideo) {
                    singleUrl = shortcodeMedia.getString("video_url");
                    singleVideoThumbnail = shortcodeMedia.getString("display_url");
                } else {
                    singleUrl = shortcodeMedia.getString("display_url");
                }
                contents.add(new InstaContent(singleUrl, isVideo, singleVideoThumbnail));
            }

            post.profileUrl = owner.getString("profile_pic_url");
            post.fullName = owner.getString("full_name");
            post.username = owner.getString("username");
            post.totalComments = edgeMediaComment.getString("count");
            post.totalLikes = edgeMediaLike.getString("count");
            post.caption = null;
            if (captionNode.length() != 0) {
                post.caption = captionNode.getJSONObject(0).getJSONObject("node").getString("text");
            }
            post.instaContent = contents;
            listener.onResponse(isSuccessFul, post);
        });
    }

    public void fetchInstaDP(String url, InstaDpListener listener) {
        InstaDpModel DpInfo = new InstaDpModel();
        VolleyUtil.init(context).get(url, (isSuccessFul, response) -> {
            if (!isSuccessFul) {
                listener.onResponse(isSuccessFul, null);
                return;
            }
            try {
                JSONObject jsonArray = response.getJSONObject("graphql");
                JSONObject jsonArray1 = jsonArray.getJSONObject("user");
                DpInfo.profileUrl = jsonArray1.getString("profile_pic_url_hd");
                DpInfo.username = jsonArray1.getString("username");
                DpInfo.caption = jsonArray1.getString("biography");
                DpInfo.fullName = jsonArray1.getString("full_name");
                listener.onResponse(true, DpInfo);
            } catch (Exception e) {
                listener.onResponse(isSuccessFul, null);
            }

        });
    }

    public interface PhotoVideoListener {
        void onResponse(boolean isSuccessFull, PostModel post);
    }

    public interface InstaDpListener {
        void onResponse(boolean isSuccessFull, InstaDpModel post);
    }
}
