package hackathon.app.facebook;

import android.os.Bundle;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;

/**
 * Created by don on 6/14/15.
 */
public class FacebookService {

    public void getUserInfo(GraphRequest.GraphJSONObjectCallback callback) {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken == null || accessToken.isExpired()) {
            throw new RuntimeException("Invalid access token");
        }

        GraphRequest request = GraphRequest.newMeRequest(accessToken, callback);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
