
package com.assignment.movietime.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.assignment.movietime.R;
import com.assignment.movietime.constants.AppConstants;
import com.assignment.movietime.model.ChatMessage;
import com.assignment.movietime.utils.SnackbarUtils;
import com.assignment.movietime.view.activities.BaseActivity;
import com.assignment.movietime.view.listeners.NotifyUIListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rashmi on 20/08/17.
 */

public class ChatViewModel extends BaseObservable {
    private static final String TAG = ChatViewModel.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDbReference;

    private Activity mActivity;
    public ObservableField<String> message;
    public ObservableBoolean sendEnabled;
    private TextWatcher textWatcher;

    @Nullable
    private NotifyUIListener notifyUIListener;

    public ChatViewModel() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDbReference = FirebaseDatabase.getInstance().getReference();

        message = new ObservableField<>("");
        sendEnabled = new ObservableBoolean(false);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                sendEnabled.set(!TextUtils.isEmpty(editable.toString()) && mFirebaseUser != null);
            }
        };
    }

    public void addNotifyUIListener(NotifyUIListener notifyUIListener) {
        this.notifyUIListener = notifyUIListener;
    }

    public void removeNotifyUIListener() {
        this.notifyUIListener = null;
    }

    public void startAuth() {
        this.mActivity = BaseActivity.currentActivity;

        if (mFirebaseUser == null) {
            signIn();
        }
        else {
            Log.d(TAG, "ChatViewModel: " + mFirebaseUser.getDisplayName());
            if (notifyUIListener != null) {
                notifyUIListener.onAuthSuccess();
            }
        }
    }

    private void signIn() {
        Log.d(TAG, "signIn: ");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mActivity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        BaseActivity.currentActivity.startActivityForResult(signInIntent, AppConstants.REQUEST_CODE_SIGN_IN);
    }

    public void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == AppConstants.REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else {
                // Google Sign-In failed
                Log.e(TAG, "Google Sign-In failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            SnackbarUtils.displaySnackbar(mActivity.getString(R.string.snackbar_auth_failed));
                        }
                        else {
                            mFirebaseUser = mFirebaseAuth.getCurrentUser();
                            SnackbarUtils.displaySnackbar(mActivity.getString(
                                    R.string.snacbar_auth_complete,
                                    mFirebaseUser.getDisplayName()));

                            if (notifyUIListener != null) {
                                notifyUIListener.onAuthSuccess();
                            }
                        }
                    }
                });
    }

    @Bindable
    public TextWatcher getTextWatcher() {
        return textWatcher;
    }

    public void onSendClicked(View view) {
        ChatMessage chatMessage = new ChatMessage(message.get(),
                mFirebaseUser.getDisplayName(),
                mFirebaseUser.getPhotoUrl() == null ? "" : mFirebaseUser.getPhotoUrl().toString());
        mFirebaseDbReference.child(AppConstants.MESSAGES_CHILD).push().setValue(chatMessage);

        Map<String, String> notification = new HashMap<>();
        notification.put(AppConstants.NOTIFICATION_TITLE, mFirebaseUser.getDisplayName());
        notification.put(AppConstants.NOTIFICATION_BODY, message.get());
        mFirebaseDbReference.child(AppConstants.NOTIFICATIONS_CHILD).push().setValue(notification);

        message.set("");
        message.notifyChange();
    }
}
