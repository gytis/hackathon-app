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
 * Created by don on 6/13/15.
 */
public final class EventDao {

    private static final String SERVICE_URL = "http://ckan.escapp.net/api/action/datastore_search?resource_id=9940951b-b38a-46c1-99ab-41de70c5edb0";

    public List<Event> getEvents() {

        final HttpClient httpClient = new DefaultHttpClient();
        final HttpGet httpGet = new HttpGet(SERVICE_URL);
        httpGet.setHeader("Accept", "application/json");

        JSONObject jsonObject = null;

        try {
            final HttpResponse httpResponse = httpClient.execute(httpGet);
            final HttpEntity entity = httpResponse.getEntity();
            final String result = EntityUtils.toString(entity);

            jsonObject = new JSONObject(result);
        } catch (Exception e) {
            Log.e("EventDao", "Response failed");
            Log.e("EventDao", e.getMessage());
        }

        if (jsonObject == null) {
            return new ArrayList<Event>();
        }

        JSONArray jsonArray = null;

        // Parse data
        try {
            jsonArray = jsonObject.getJSONObject("result").getJSONArray("records");
        } catch (JSONException e) {
            Log.e("EventDao", "Failed Parsing");
            Log.e("EventDao", e.getMessage());
        }

        if (jsonArray == null) {
            return new ArrayList<Event>();
        }

        final List<Event> eventList = new ArrayList<Event>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject eventJSON = jsonArray.getJSONObject(i);
                Long id = eventJSON.getLong("_id");
                String name = eventJSON.getString("Title");
                String description = eventJSON.getString("Description");
                String postcode = eventJSON.getString("Postcode");
                String houseName = eventJSON.getString("House name / number");
                String street = eventJSON.getString("Street");
                String town = eventJSON.getString("Town / City");
                Boolean disabledAccess = false;
                if (eventJSON.getString("Disability and Additional Needs").equals("Yes")) {
                    disabledAccess = true;
                }
                String email = eventJSON.getString("Email");
                String info = eventJSON.getString("Session information");

                Event event = new Event(id, name, description, postcode, houseName, street, town, disabledAccess, email, info);
                eventList.add(event);

            } catch (JSONException e) {
                Log.e("EventDao", "EventJSON parsing failed");
                Log.e("EventDao", e.getMessage());
            }
        }
        return eventList;

    }

}
