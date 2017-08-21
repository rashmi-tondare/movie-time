package com.assignment.movietime.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.assignment.movietime.GlideApp;
import com.assignment.movietime.R;
import com.assignment.movietime.model.ChatMessage;
import com.bumptech.glide.Glide;

/**
 * Created by Rashmi on 20/08/17.
 */

public class ChatListItemViewModel extends BaseObservable {

    private ChatMessage chatMessage;

    public ChatListItemViewModel(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    @Bindable
    public String getImageUrl() {
        return chatMessage.getSenderPhotoUrl();
    }

    @BindingAdapter({"profileImage"})
    public static void loadImage(ImageView imgView, String url) {
        GlideApp.with(imgView.getContext())
                .load(url)
                .placeholder(R.drawable.ic_account)
                .into(imgView);
    }

    @Bindable
    public String getMessage() {
        return chatMessage.getMessage();
    }

    @Bindable
    public String getSenderName() {
        return chatMessage.getSenderName();
    }
}
