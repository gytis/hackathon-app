package hackathon.app.dao;

import android.os.AsyncTask;
import android.util.Log;
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

    public UserDao() {

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
                    Log.v("UserDao", httpResponse.getEntity().toString());
                } catch (Exception e) {
                    Log.v("UserDao", e.getMessage());
                }

                return null;
            }
        }.execute();

    }

}