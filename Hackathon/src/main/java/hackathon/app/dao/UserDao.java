package hackathon.app.dao;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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

    private static final String SERVICE_URL = "http://socialncl-nclhackathon.rhcloud.com/users";

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
                users.add(new User(object.getInt("id"), object.getString("facebook_id"), object.getBoolean("registered"),
                        object.getString("facebook_avatar")));
            } catch (JSONException e) {
                Log.e("UserDao", e.getMessage());
            }
        }

        return users;
    }
}