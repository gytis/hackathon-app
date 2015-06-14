package hackathon.app.dao;

import android.util.Log;

import com.google.gson.Gson;

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
import java.util.Date;
import java.util.List;

/**
 * Created by Maxim on 14/06/2015.
 */
public class TicketDao {

    private static final String SERVICE_URL = "http://hackathonserver-gytis.rhcloud.com/tickets";

    public static List<Ticket> getTickets() {

        final HttpClient httpClient = new DefaultHttpClient();
        final HttpGet httpGet = new HttpGet(SERVICE_URL);
        httpGet.setHeader("Accept", "application/json");

        JSONArray jsonArray = null;

        try {
            final HttpResponse httpResponse = httpClient.execute(httpGet);
            final HttpEntity entity = httpResponse.getEntity();
            final String result = EntityUtils.toString(entity);

            jsonArray = new JSONArray(result);
        } catch (Exception e) {
            Log.e("TicketDao", "Response failed");
            Log.e("TicketDao", e.getMessage());
        }

        if (jsonArray == null) {
            return new ArrayList<Ticket>();
        }

        final List<Ticket> tickets = new ArrayList<Ticket>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Ticket ticket = new Ticket();
                JSONObject ticketJSON = jsonArray.getJSONObject(i);
                ticket.setId(ticketJSON.getLong("id"));
                ticket.setUserId(ticketJSON.getLong("user_id"));
                ticket.setEventId(ticketJSON.getLong("event_id"));
                ticket.setDate(new Date(ticketJSON.getLong("date")));
                ticket.setCreatedAt(new Date(ticketJSON.getLong("created_at")));

                tickets.add(ticket);

            } catch (JSONException e) {
                Log.e("TicketDao", "EventJSON parsing failed");
                Log.e("TicketDao", e.getMessage());
            }
        }
        return tickets;
    }

    //TODO
    public static void addTicket(Ticket ticket) {

        final HttpClient httpClient = new DefaultHttpClient();
        final HttpPost httpPost = new HttpPost(SERVICE_URL);
        httpPost.setHeader("Accept", "application/json");

        JSONObject jsonPostObject = new JSONObject();
        try {
            jsonPostObject.put("user_id", ticket.getUserId());
            jsonPostObject.put("event_id", ticket.getEventId());
            jsonPostObject.put("date", ticket.getDate().getTime());

            Log.v("RatingDao", "Creating rating " + jsonPostObject.toString());

            StringEntity stringEntity = new StringEntity(jsonPostObject.toString());
            stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(stringEntity);
            httpClient.execute(httpPost);

            final HttpResponse httpResponse = httpClient.execute(httpPost);
            final HttpEntity entity = httpResponse.getEntity();
            final String result = EntityUtils.toString(entity);

            Log.v("A", result);
        } catch (Exception e) {
            Log.v("TicketDao", "Failed to save new ticket");
        }
    }
}
