package hackathon.app.dao;

import android.os.Bundle;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class FacebookDao {

    public void getUserInfo(GraphRequest.GraphJSONObjectCallback callback) {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken == null || accessToken.isExpired()) {
            throw new RuntimeException("Invalid access token");
        }

        GraphRequest request = GraphRequest.newMeRequest(accessToken, callback);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
