package com.leetlab.videodownloaderfortiktok.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.model.InstaUserModel;
import com.leetlab.videodownloaderfortiktok.model.StoryModel;
import com.leetlab.videodownloaderfortiktok.model.UserObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InstaStoriesLoader {

    private static InstaUserModel currentUserModel;
    private static StoriesLoadListener loadListener;

    public static void init(Context context, UserObject userObject, InstaUserModel instaUserModel, StoriesLoadListener listener) {
        currentUserModel = instaUserModel;
        loadListener = listener;
        if (userObject != null) {
            String url = String.format("https://i.instagram.com/api/v1/feed/user/%s/reel_media/", userObject.getUserId());
            new MyAsyncClass(context, MyAsyncClass.GET, url, fetchStoriesFeedInterface).execute();
        }
    }

    static MyAsyncClass.AsyncInterface fetchStoriesFeedInterface = new MyAsyncClass.AsyncInterface() {
        private ArrayList<StoryModel> stories;
        private ProgressDialog mProgressDialog;

        @Override
        public void onPreExecute() {
            loadListener.onStart();
        }

        @Override
        public void onSuccess() {
            if (stories.size() == 0) {
                loadListener.onError("No Stories Found");
            } else {
                loadListener.onSuccessful(stories);
            }

        }

        @Override
        public void onError(String error) {
            loadListener.onError("No Story Found.");

        }

        @Override
        public void parseJson(String s) {
            stories = new ArrayList<>();
            List<StoryModel> imageList = new ArrayList();
            List<StoryModel> videoList = new ArrayList();
            List<StoryModel> videoThumbs = new ArrayList();
            HashMap<String, String> vids = new HashMap<>();
            try {
                vids.clear();
                JSONArray array = new JSONObject(s).getJSONArray("items");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject itemObj = array.getJSONObject(i);
                    JSONArray video = itemObj.optJSONArray("video_versions");
                    if (video != null) {
                        StoryModel model = new StoryModel();
                        model.setFilePath(video.getJSONObject(0).getString("url"));
                        model.setFileName(null);
                        model.setType(1);
                        model.setSaved(false);
                        videoList.add(model);
                        JSONArray imageArray = itemObj.getJSONObject("image_versions2").getJSONArray("candidates");
                        StoryModel storyModel = new StoryModel();
                        model.setFilePath(video.getJSONObject(0).getString("url"));
                        storyModel.setFileName(null);
                        storyModel.setType(1);
                        storyModel.setSaved(false);
                        videoThumbs.add(storyModel);
                        vids.put(imageArray.getJSONObject(imageArray.length() - 1).getString("url"), video.getJSONObject(0).getString("url"));
                    } else {
                        String url = itemObj.getJSONObject("image_versions2")
                                .getJSONArray("candidates").getJSONObject(0)
                                .getString("url");
//                        if (!url.endsWith(".jpg")) {
//                            url = url.split(".jpg")[0] + ".jpg";
//                        }
                        StoryModel imageStories = new StoryModel();
                        imageStories.setFilePath(url);
                        imageStories.setFileName(null);
                        imageStories.setSaved(false);
                        imageStories.setType(0);
                        imageList.add(imageStories);
                    }
                }
                stories.addAll(imageList);
                stories.addAll(videoList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public HashMap<String, String> setHeaders() {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("User-Agent", "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+");
            headers.put("Cookie", "ds_user_id=" + currentUserModel.getDs_user_id() + ";sessionid=" + currentUserModel.getSessionid());
            return headers;
        }

        @Override
        public RequestBody setParams() {
            return null;
        }
    };

    public static class MyAsyncClass extends AsyncTask<Void, Void, Integer> {
        public static final int GET = 11011;
        public static final int POST = 11111;
        private final Context mContext;
        private final int requestType;
        private final String requestUrl;
        private final AsyncInterface listener;
        private RequestBody requestBody;
        private HashMap<String, String> requestHeaders;
        private int result;
        private String error_msg = "";

        public MyAsyncClass(Context mContext, int requestType, String requestUrl, AsyncInterface listener) {
            this.mContext = mContext;
            this.requestType = requestType;
            this.requestUrl = requestUrl;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (listener != null) {
                listener.onPreExecute();
            }
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            if ((requestUrl != null && requestType != -1) &&
                    (requestUrl.contains("http") || requestUrl.contains("https"))) {
                if (listener != null) {
                    requestBody = listener.setParams();
                }

                if (listener != null) {
                    requestHeaders = listener.setHeaders();
                }

                Request.Builder request = new Request.Builder();
                request.url(requestUrl);
                if (requestType == POST) {
                    request.post(requestBody);
                } else {
                    request.get();
                }

                if (requestHeaders != null) {
                    for (String key : requestHeaders.keySet()) {
                        String value = requestHeaders.get(key);
                        if (value != null)
                            request.header(key, value);
                    }
                }
                Request request1 = request.build();
                OkHttpClient client = new OkHttpClient();
                try {
                    Response response = client.newCall(request1).execute();
                    if (response != null && response.networkResponse() != null &&
                            response.body() != null) {
                        int status = response.networkResponse().code();
                        if (status == 200 || status == 401) {
                            String json_string = response.body().string();
                            if (json_string != null && !json_string.equalsIgnoreCase("")) {
                                if (listener != null) {
                                    listener.parseJson(json_string);
                                }
                                result = 1;
                            } else {
                                result = 0;
                                error_msg = "JSON null";
                            }
                        } else {
                            result = 0;
                            error_msg = mContext.getString(R.string.str_error_no_internet);
                        }
                        return -1;
                    }
                    result = 0;
                    error_msg = mContext.getString(R.string.str_error_no_internet);
                } catch (Exception e) {
                    result = 0;
                    e.printStackTrace();
                    error_msg = mContext.getString(R.string.str_error_internal_server_error);
                }
            }

            return -1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (mContext != null) {
                if (listener != null) {
                    if (result == 1) {
                        listener.onSuccess();
                    } else if (result == 0) {
                        listener.onError(error_msg);
                    } else {
                        error_msg = mContext.getString(R.string.str_error_no_internet);
                        listener.onError(error_msg);
                    }
                }
            }
        }

        public interface AsyncInterface {
            public void onPreExecute();

            public void onSuccess();

            public void onError(String error);

            public void parseJson(String s);

            public HashMap<String, String> setHeaders();

            public RequestBody setParams();

        }
    }

    public interface StoriesLoadListener {
        void onStart();

        void onSuccessful(ArrayList<StoryModel> stories);

        void onError(String message);
    }
}
