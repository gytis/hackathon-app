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
import java.util.Date;
import java.util.List;

/**
 * Created by Maxim on 14/06/2015.
 */
public class TicketDao {

    private static final String SERVICE_URL = "http://hackathonserver-gytis.rhcloud.com";

    public static List<Ticket> getTickets() {

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
            Log.e("TicketDao", "Response failed");
            Log.e("TicketDao", e.getMessage());
        }

        if (jsonObject == null) {
            return new ArrayList<Ticket>();
        }

        JSONArray jsonArray = null;

        // Parse data
        try {
            jsonArray = jsonObject.getJSONObject("result").getJSONArray("records");
        } catch (JSONException e) {
            Log.e("TicketDao", "Failed Parsing");
            Log.e("TicketDao", e.getMessage());
        }

        if (jsonArray == null) {
            return new ArrayList<Ticket>();
        }

        //[{"id":0,"user_id":0,"event_id":0,"date":"12569537329","created_at":"12569537329"},{"id":1,"user_id":1,"event_id":1,"date":"12569537329","created_at":"12569537329"}]

        final List<Ticket> tickets = new ArrayList<Ticket>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Ticket ticket = new Ticket();
                JSONObject ticketJSON = jsonArray.getJSONObject(i);
                ticket.setId(ticketJSON.getLong("id"));
                ticket.setUserId(ticketJSON.getLong("user_id"));
                ticket.setEventId(ticketJSON.getLong("event_id"));
                ticket.setDate(new Date(ticketJSON.getLong("date") * 1000));
                ticket.setCreatedAt(new Date(ticketJSON.getLong("created_at") * 1000));

                tickets.add(ticket);

            } catch (JSONException e) {
                Log.e("TicketDao", "EventJSON parsing failed");
                Log.e("TicketDao", e.getMessage());
            }
        }
        return tickets;
    }
}
