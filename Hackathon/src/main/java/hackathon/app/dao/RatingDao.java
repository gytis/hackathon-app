package hackathon.app.dao;

import android.content.Context;
import android.util.Log;
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
public class RatingDao {

    private static final String SERVICE_URL = "http://hackathonserver-gytis.rhcloud.com/ratings";

    private final Context context;

    public RatingDao(final Context context) {
        this.context = context;
    }

    public List<Rating> getRatings() {
        final HttpClient httpClient = new DefaultHttpClient();
        final HttpGet httpGet = new HttpGet(SERVICE_URL);
        httpGet.setHeader("Accept", "application/json");

        final JSONArray array;

        try {
            final HttpResponse httpResponse = httpClient.execute(httpGet);
            final HttpEntity entity = httpResponse.getEntity();
            final String result = EntityUtils.toString(entity);
            array = new JSONArray(result);
        } catch (Exception e) {
            Log.e("RatingsDao", "Failed to retrieve ratings");
            return new ArrayList<Rating>();
        }

        final List<Rating> ratings = new ArrayList<Rating>();

        for (int i = 0; i < array.length(); i++) {
            try {
                final JSONObject object = array.getJSONObject(i);
                ratings.add(new Rating(object.getLong("id"), object.getLong("event_id"), object.getLong("user_id"), object.getInt("value")));
            } catch (JSONException e) {
                Log.e("RatingDao", "Failed to create ratings array");
                return new ArrayList<Rating>();
            }
        }

        return ratings;
    }

    public void createRating(final long eventId, final int value) {
        final long userId = new CurrentUserHolder(context).getCurrentUserId();
        final HttpClient httpClient = new DefaultHttpClient();
        final HttpPost httpPost = new HttpPost(SERVICE_URL);

        JSONObject jsonPostObject = new JSONObject();
        try {
            jsonPostObject.put("user_id", userId);
            jsonPostObject.put("event_id", eventId);
            jsonPostObject.put("value", value);

            Log.v("RatingDao", "Creating rating " + jsonPostObject.toString());

            StringEntity stringEntity = new StringEntity(jsonPostObject.toString());
            stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(stringEntity);
            httpClient.execute(httpPost);
        } catch (Exception e) {
            Log.v("UserDao", "Failed to save new user");
        }
    }

}
