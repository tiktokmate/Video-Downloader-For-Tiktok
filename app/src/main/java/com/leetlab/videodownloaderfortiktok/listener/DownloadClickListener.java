package com.leetlab.videodownloaderfortiktok.listener;

import com.leetlab.videodownloaderfortiktok.model.InstaContent;

public interface DownloadClickListener {
    void onClick(InstaContent content, int position);

    void onLongClickListener(InstaContent content, int position);
}
