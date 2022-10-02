package com.leetlab.videodownloaderfortiktok.ui.fragment;

import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.leetlab.videodownloaderfortiktok.R;
import com.leetlab.videodownloaderfortiktok.model.InstaContent;
import com.leetlab.videodownloaderfortiktok.model.InstaUserModel;
import com.leetlab.videodownloaderfortiktok.model.PostModel;
import com.leetlab.videodownloaderfortiktok.model.PrivatAccount;
import com.leetlab.videodownloaderfortiktok.model.UserDetailModel;
import com.leetlab.videodownloaderfortiktok.model.UserMediaModel;
import com.leetlab.videodownloaderfortiktok.utils.InternetConectionn;
import com.leetlab.videodownloaderfortiktok.utils.MySingleton;
import com.leetlab.videodownloaderfortiktok.utils.SavedPreferences;
import com.leetlab.videodownloaderfortiktok.utils.SharedPrefManager;
import com.leetlab.videodownloaderfortiktok.utils.UIUtil;
import com.leetlab.utils.dialog.ProgressDialog;
import com.leetlab.utils.dialog.ProgressType;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.CLIPBOARD_SERVICE;

public class PhotoVideosFragment extends Fragment {

    @BindView(R.id.et_url)
    EditText etUrl;

    private ClipboardManager clipboard;
    private ProgressDialog dialog;
    private UserMediaModel userMediaModel;
    private UserDetailModel userDetailModel;
    private SavedPreferences savedPreferences;
    private InstaUserModel instaUserModel;
    private int mStatusCode = 500;

    public PhotoVideosFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_videos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        userMediaModel = new UserMediaModel();
        savedPreferences = new SavedPreferences();

        if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
            instaUserModel = SharedPrefManager.getInstance(getContext()).getUser();
        }

        clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        dialog = new ProgressDialog(getActivity(), ProgressType.HORIZONTAL)
                .setMessage("Resolving url...")
                .setRadius(8);

    }

    @OnClick({R.id.pasteTxt, R.id.downloadBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pasteTxt:
                if (clipboard != null) {
                    if (clipboard.getPrimaryClip().getItemCount() > 0) {
                        String url = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
                        etUrl.setText(url);
                    }
                }
                break;
            case R.id.downloadBtn:
                if (!InternetConectionn.CheckInternetConnection(getContext())) {
                    UIUtil.popError(getActivity());
                    return;
                }


                check_url();

                break;
        }
    }

    private void check_url() {
        String obj = etUrl.getText().toString();
        if (obj.length() <= 0) {
            etUrl.setError("Enter url.");
            return;
        }
        if (!obj.contains("www")) {
            obj = "https://www.instagram.com/" + obj.substring(obj.lastIndexOf("com/") + 4);
        }

        if (!obj.startsWith("https://www.instagram.com/") || TextUtils.isEmpty(obj)) {
            Toast.makeText(getContext(), "Invalid URL", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(obj.substring(0, obj.indexOf("?")));
            stringBuilder.append("?__a=1");
            String final_url = stringBuilder.toString();
            checkingPrivateDataOrNot(final_url);

        } catch (Exception unused) {
            Toast.makeText(getContext(), "Invalid URL", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkingPrivateDataOrNot(String url) {
        dialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (mStatusCode == 200) {
                        JSONObject graphQlObject = response.getJSONObject("graphql");
                        if (response.has("logging_page_id")) {
                            gettingProfilePic(graphQlObject);
                        } else {
                            checkingPrivateAccountOrNot(graphQlObject);
                        }

                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            if (mStatusCode == 404) {
                Toast.makeText(getContext(), "Media is Private", Toast.LENGTH_SHORT).show();
                if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
                    Toast.makeText(getContext(), "Invalid Url", Toast.LENGTH_SHORT).show();
                } else {
                    UIUtil.showDialog(getActivity());
                }
            } else if (mStatusCode == 500) {
                Toast.makeText(getContext(), "Internet connectivity not good", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("User-Agent", "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+");
                    headers.put("Cookie", "ds_user_id=" + instaUserModel.getDs_user_id() + ";sessionid=" + instaUserModel.getSessionid());
                    Log.d("Cookies ===", "ds_user_id=" + instaUserModel.getDs_user_id() + ";sessionid=" + instaUserModel.getSessionid());
                    return headers;
                } else
                    return super.getHeaders();
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                try {
                    mStatusCode = volleyError.networkResponse.statusCode;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return super.parseNetworkError(volleyError);
            }

        };

        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void gettingProfilePic(JSONObject graphQlObject) {
        userDetailModel = savedPreferences.parsingUserProfilePic(graphQlObject);
        settingProfileData(userDetailModel);
    }

    private void checkingPrivateAccountOrNot(JSONObject graphQlObject) {
        PrivatAccount privatAccount = savedPreferences.parsingPrivateVariable(graphQlObject);
        if (privatAccount.isPrivate()) {
            checkingFollowedOrNot(graphQlObject);
        } else {
            gettingMediaFromLink(graphQlObject);
        }
    }

    private void checkingFollowedOrNot(JSONObject graphQlObject) {
        boolean isFollowed = savedPreferences.parsingFollowByUserOrNot(graphQlObject);
        if (isFollowed) {
            Toast.makeText(getContext(), "User Follow Account", Toast.LENGTH_SHORT).show();
            gettingMediaFromLink(graphQlObject);
        } else
            Toast.makeText(getContext(), "User Not Follow Account", Toast.LENGTH_SHORT).show();
    }

    private void gettingMediaFromLink(JSONObject graphQlObject) {
        userDetailModel = savedPreferences.parsingMediaFromLink(graphQlObject);
        settingProfileData(userDetailModel);
    }

    private void settingProfileData(UserDetailModel userDetailModel) {
        PostModel post = new PostModel();
        post.fullName = userDetailModel.getUserName();
        post.profileUrl = userDetailModel.getProfilePic();
        post.totalComments = "32223";
        post.totalLikes = "322323";
        post.caption = "This is Caption";
        post.username = userDetailModel.getUserName();
        List<InstaContent> contents = new ArrayList<>();
        for (UserMediaModel media : userDetailModel.getUserMediaModelList()) {
            contents.add(new InstaContent(media.getDisplayUrl(), media.isVideo(), media.getMediaUrl()));
        }
        post.instaContent = contents;
        DownloadDialog.newInstance(post).show(getFragmentManager(), "download");
    }


}
