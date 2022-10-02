package com.leetlab.videodownloaderfortiktok.utils;

import com.leetlab.videodownloaderfortiktok.model.InstaUserModel;
import com.leetlab.videodownloaderfortiktok.model.UserObject;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class AccountDB {
    private final String SHARED_PREF_NAME = "userDetails";
    private final String KEY_NAME = "keyName";
    private final String KEY_USERNAME = "keyUserName";
    private final String KEY_PHOTO = "keyPhoto";
    private final String KEY_COOKIE = "keyCookie";
    private final String KEY_CRF_TOKEN = "keyToken";
    private final String KEY_SESSION_ID = "keySessionId";
    private final String KEY_URL_GEN = "keyUrlGen";
    private final String KEY_DS_USER_ID = "keyDsUserId";
    private final String KEY_MCD = "keyMcd";
    private final String KEY_MID = "keyMid";

    private static final String ACCOUNTS_BOOK = "accountsBook";
    private static final String CURRENT_ACCOUNT = "currentAccount";
    private static final String FAV_ACCOUNT_BOOK = "favAccountsBook";

    public static void addAccount(InstaUserModel user) {
        Paper.book(ACCOUNTS_BOOK).write(user.getUsername(), user);
    }

    public static List<String> getAllAccounts() {
        return Paper.book(ACCOUNTS_BOOK).getAllKeys();
    }

    public static InstaUserModel getAccount(String username) {
        return Paper.book(ACCOUNTS_BOOK).read(username);
    }

    public static boolean isLoggedIn() {
        return getAllAccounts().size() > 0;
    }

    public static List<InstaUserModel> getAccounts() {
        List<InstaUserModel> accounts = new ArrayList<>();
        for (String key : getAllAccounts()) {
            InstaUserModel account = Paper.book(ACCOUNTS_BOOK).read(key);
            accounts.add(account);
        }

        return accounts;
    }

    public static InstaUserModel getCurrentAccount() {
        return Paper.book().read(CURRENT_ACCOUNT);
    }

    public static void setCurrentAccount(InstaUserModel account) {
        Paper.book().write(CURRENT_ACCOUNT, account);
    }

    public static void updateFav(UserObject userObject) {
        if (Paper.book(FAV_ACCOUNT_BOOK).contains(userObject.getUserName())) {
            Paper.book(FAV_ACCOUNT_BOOK).delete(userObject.getUserName());
        } else {
            Paper.book(FAV_ACCOUNT_BOOK).write(userObject.getUserName(), userObject);
        }
    }

    public static List<UserObject> getFavourites() {
        List<UserObject> favList = new ArrayList<>();
        for (String key : Paper.book(FAV_ACCOUNT_BOOK).getAllKeys()) {
            favList.add(Paper.book(FAV_ACCOUNT_BOOK).read(key));
        }

        return favList;
    }

    public static boolean isFav(String username) {
        return Paper.book(FAV_ACCOUNT_BOOK).contains(username);
    }

}
