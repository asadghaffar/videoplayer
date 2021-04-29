package com.fcodex.videoplayer.Modal;

import android.net.Uri;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Modal {

    private long id;
    private Uri data;
    private String title, duration;

    public Modal(long id, Uri data, String title, String duration) {
        this.id = id;
        this.data = data;
        this.title = title;
        this.duration = duration;
    }
}
