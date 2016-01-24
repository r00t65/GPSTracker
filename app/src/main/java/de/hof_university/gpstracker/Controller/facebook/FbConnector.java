package de.hof_university.gpstracker.Controller.facebook;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

/**
 * Created by bmairhoermann on 06.01.16.
 */
public class FbConnector {

    public boolean isLoggedIn() {

        // Returns boolean whether user is logged in or not.

        try {
            AccessToken.getCurrentAccessToken().getUserId();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserId() {

        // Returns userID if user is logged in.
        // Returns empty string if user is NOT logged in.

        if (isLoggedIn() == true) {
            return AccessToken.getCurrentAccessToken().getUserId();
        } else {
            return "";
        }
    }

    // ################ Asynch Methods below! ################

    public void getUserInfoJSON() {

        // Place AsynchMethodCall in onComplete() to receive UserInfo

        try {
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            // String.valueOf(response.getJSONObject) gives you following String:  {"name":"Harald Junke","id":"1057563854267320"}
                            // If user is NOT logged in -> response will be null!
                            // Place you AsynchMethodCall below.

                            Log.d("Current User", String.valueOf(response.getJSONObject()));
                        }
                    }
            ).executeAsync();
        } catch (Exception e) {
            Log.d("Facebook_ERROR: ", "getUserInfoJSON ");
        }
    }

    public void getUserFriendlistJSON() {

        // Place AsynchMethodCall in onComplete() to receive UserFriendlist

        try {
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/friends",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            // String.valueOf(response.getJSONObject) gives you following String:  {"data":[
                            //                                                                              {"name":"Angela Merkel","id":"1011252858934239"},
                            //                                                                              {"name":"Hans Wurscht","id":"160084664358218"}
                            //                                                                              ],
                            //                                                                              "paging":{"next":"https:\/\/graph.facebook.com\/v2.5\/1057549224267320\/friends?format=json&access_token=qhr0"},
                            //                                                                              "summary":{"total_count":128}}
                            // If user is NOT logged in -> response will be null!
                            // Place you AsynchMethodCall below.

                            Log.d("User Friendlist", String.valueOf(response.getJSONObject()));
                        }
                    }
            ).executeAsync();
        } catch (Exception e) {
            Log.d("Facebook_ERROR: ", "getUserFriendlistJSON ");
        }
    }
}
