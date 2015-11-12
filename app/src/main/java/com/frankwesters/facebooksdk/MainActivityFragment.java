package com.frankwesters.facebooksdk;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private View view;
    private static final int LOGIN_REQUEST_CODE = 1;
    private static final String FRIEND_INFO = "name";
    private static final String FRIENDS_LIMIT = "400";


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        //Check if logged in
        if (AccessToken.getCurrentAccessToken() == null) {
            System.out.println("check");
            startActivityForResult(new Intent(this.getActivity(), LoginActivity.class), LOGIN_REQUEST_CODE);
        } else {
            renderLayout();
        }

        return view;
    }

    void renderLayout() {

        final ListView listView = (ListView) view.findViewById(R.id.listView_friends);
        Bundle parameters = new Bundle(2);
        parameters.putString("fields",FRIEND_INFO);
        parameters.putString("limit",FRIENDS_LIMIT);

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/taggable_friends",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONArray responseArray = (JSONArray) response.getJSONObject().get("data");

                            ArrayList<String> friendList = new ArrayList<>();
                            for (int i = 0; i < responseArray.length(); i++) {
                                friendList.add(responseArray.getJSONObject(i).getString(FRIEND_INFO));
                            }

                            ArrayAdapter<String> friendsAdapter = new ArrayAdapter<>(
                                    MainActivityFragment.this.getActivity(),
                                    R.layout.list_item_friends,
                                    R.id.list_item_friends_textview,
                                    friendList
                            );
                            listView.setAdapter(friendsAdapter);

                        } catch (JSONException exception){
                            exception.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){
            case Activity.RESULT_OK:
                renderLayout();
                break;
            default:
                getActivity().finish();
        }
    }

}
