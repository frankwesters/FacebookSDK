package com.frankwesters.facebooksdk;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    /**
     * The Facebook callback manager
     */
    private CallbackManager callbackManager;

    private static final String[] FB_PERMISSIONS = {"user_friends"};

    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        callbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(FB_PERMISSIONS);
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getActivity().setResult(getActivity().RESULT_OK);
                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                getActivity().finish();
            }

            @Override
            public void onCancel() {
                getActivity().setResult(getActivity().RESULT_CANCELED);
                getActivity().finish();
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onException");
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
