
package com.assignment.movietime.view.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assignment.movietime.R;
import com.assignment.movietime.constants.AppConstants;
import com.assignment.movietime.databinding.ListItemChatBinding;
import com.assignment.movietime.model.ChatMessage;
import com.assignment.movietime.view.activities.BaseActivity;
import com.assignment.movietime.viewmodel.ChatListItemViewModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

/**
 * Created by Rashmi on 20/08/17.
 */

public class ChatAdapter extends FirebaseRecyclerAdapter<ChatMessage, ChatAdapter.ChatViewHolder> {

    private static final String TAG = ChatAdapter.class.getSimpleName();

    private Context context;
    private DatabaseReference mFirebaseDbReference;

    public ChatAdapter(Context context, DatabaseReference firebaseDbReference) {
        super(ChatMessage.class, R.layout.list_item_chat, ChatViewHolder.class, firebaseDbReference.child(AppConstants.MESSAGES_CHILD));
        this.mFirebaseDbReference = firebaseDbReference;
        this.context = context;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemChatBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.list_item_chat,
                parent,
                false);
        return new ChatViewHolder(binding);
    }

    @Override
    protected void populateViewHolder(ChatViewHolder viewHolder, ChatMessage model, int position) {
        if (BaseActivity.currentActivity != null) {
            BaseActivity.currentActivity.dismissLoader();
        }
        Log.d(TAG, "populateViewHolder: " + model.getMessage() + " by " + model.getSenderName());
        viewHolder.binding.setViewModel(new ChatListItemViewModel(model));
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        private ListItemChatBinding binding;

        public ChatViewHolder(ListItemChatBinding binding) {
            super(binding.lnrChat);
            this.binding = binding;
        }
    }
}
