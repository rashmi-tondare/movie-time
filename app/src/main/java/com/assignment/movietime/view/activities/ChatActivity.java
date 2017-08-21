
package com.assignment.movietime.view.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.assignment.movietime.R;
import com.assignment.movietime.databinding.ActivityChatBinding;
import com.assignment.movietime.view.adapters.ChatAdapter;
import com.assignment.movietime.view.listeners.NotifyUIListener;
import com.assignment.movietime.viewmodel.ChatViewModel;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends BaseActivity implements NotifyUIListener {

    private ChatViewModel chatViewModel;
    private ActivityChatBinding chatBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        chatViewModel = new ChatViewModel();
        chatBinding.setViewModel(chatViewModel);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        BaseActivity.currentActivity = this;
        chatViewModel.addNotifyUIListener(this);
        chatViewModel.startAuth();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        chatViewModel.handleOnActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAuthSuccess() {
        showLoader();
        final RecyclerView recyclerChats = chatBinding.recyclerChats;
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerChats.setLayoutManager(linearLayoutManager);

        final ChatAdapter chatAdapter = new ChatAdapter(this, FirebaseDatabase.getInstance().getReference());
        recyclerChats.setAdapter(chatAdapter);
        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messageCount = chatAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                    (positionStart >= (messageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    recyclerChats.scrollToPosition(positionStart);
                }
            }
        });

        recyclerChats.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                recyclerChats.scrollToPosition(chatAdapter.getItemCount() - 1);
            }
        });
    }
}
