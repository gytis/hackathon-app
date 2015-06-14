package hackathon.app.dao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.facebook.AccessToken;
import hackathon.app.CurrentUserHolder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public final class UserDao {

    private static final String SERVICE_URL = "http://hackathonserver-gytis.rhcloud.com/users";

    private final Context context;

    public UserDao(final Context context) {
        this.context = context;
    }

    public List<User> getUsers() {
        final HttpClient httpClient = new DefaultHttpClient();
        final HttpGet httpGet = new HttpGet(SERVICE_URL);
        httpGet.setHeader("Accept", "application/json");

        JSONArray array = null;

        try {
            final HttpResponse httpResponse = httpClient.execute(httpGet);
            final HttpEntity entity = httpResponse.getEntity();
            final String result = EntityUtils.toString(entity);
            array = new JSONArray(result);
        } catch (Exception e) {
            Log.e("UserDao", e.getMessage());
        }

        if (array == null) {
            return new ArrayList<User>();
        }

        final List<User> users = new ArrayList<User>();

        for (int i = 0; i < array.length(); i++) {
            try {
                final JSONObject object = array.getJSONObject(i);
                users.add(new User(object.getInt("id"), object.getString("facebook_id"), object.getString("name"), object.getBoolean("registered"),
                        object.getString("photo")));
            } catch (JSONException e) {
                Log.e("UserDao", e.getMessage());
            }
        }
        return users;
    }

    private boolean isUserRegistered(String facebookId) {
        final HttpClient httpClient = new DefaultHttpClient();
        final HttpGet httpGet = new HttpGet(SERVICE_URL + "/fb/" + facebookId);
        httpGet.setHeader("Accept", "application/json");

        JSONObject jsonObject = null;

        try {
            final HttpResponse httpResponse = httpClient.execute(httpGet);
            final HttpEntity entity = httpResponse.getEntity();
            final String result = EntityUtils.toString(entity);
            jsonObject = new JSONObject(result);
        } catch (Exception e) {
            return false;
        }

        try {
            if (jsonObject != null && jsonObject.getBoolean("registered")) {
                new CurrentUserHolder(context).setCurrentUserId(jsonObject.getLong("id"));
                return true;
            }
        } catch (JSONException e) {
            Log.e("UserDao", e.getMessage());
        }
        return false;
    }

    public void registerUser(final String facebookId, final String name) {
        // check if user is registered

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (isUserRegistered(facebookId)) {
                    return null;
                }
                final HttpClient httpClient = new DefaultHttpClient();
                final HttpPost httpPost = new HttpPost(SERVICE_URL);

                JSONObject jsonPostObject = new JSONObject();
                try {
                    jsonPostObject.put("name", name);
                    jsonPostObject.put("facebook_id", facebookId);
                    jsonPostObject.put("registered", true);
                    jsonPostObject.put("photo", "https://graph.facebook.com/" + facebookId + "/picture");

                    StringEntity stringEntity = new StringEntity(jsonPostObject.toString());
                    stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                    httpPost.setEntity(stringEntity);
                    final HttpResponse httpResponse = httpClient.execute(httpPost);
                    final String response = EntityUtils.toString(httpResponse.getEntity());
                    final JSONObject responseObject = new JSONObject(response);
                    new CurrentUserHolder(context).setCurrentUserId(responseObject.getLong("id"));
                    final JSONArray facebookFriends = getFacebookFriends();
                    saveFriends(responseObject.getLong("id"), facebookFriends);
                } catch (Exception e) {
                    Log.v("UserDao", "Failed to save new user");
                }

                return null;
            }
        }.execute();

    }

    private JSONArray getFacebookFriends() {
        final HttpClient httpClient = new DefaultHttpClient();
        final HttpGet httpGet = new HttpGet("https://graph.facebook.com/me/friends?access_token=" + AccessToken.getCurrentAccessToken().getToken());
        httpGet.setHeader("Accept", "application/json");

        final JSONObject jsonObject;

        try {
            final HttpResponse httpResponse = httpClient.execute(httpGet);
            final HttpEntity entity = httpResponse.getEntity();
            final String result = EntityUtils.toString(entity);
            jsonObject = new JSONObject(result);
        } catch (Exception e) {
            Log.e("UserDao", "Failed to get facebook friends");
            return new JSONArray();
        }

        try {
            return jsonObject.getJSONArray("data");
        } catch (JSONException e) {
            Log.e("UserDao", "Failed to get facebook friends");
            return new JSONArray();
        }
    }

    private void saveFriends(final long userId, final JSONArray friends) {
        final HttpClient httpClient = new DefaultHttpClient();
        final HttpPost httpPost = new HttpPost(SERVICE_URL + "/" + userId + "/friends");

        try {
            StringEntity stringEntity = new StringEntity(friends.toString());
            stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(stringEntity);
            httpClient.execute(httpPost);
        } catch (Exception e) {
            Log.e("UserDao", "Failed to save friends");
        }

    }

}