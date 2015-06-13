package hackathon.app.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import hackathon.app.R;

import java.util.ArrayList;

/**
 * Created by don on 6/13/15.
 */
public class EventListAdapter extends ArrayAdapter<String>{

    private final Context context;
    private final ArrayList<String> values;

    public EventListAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.activity_events, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_events, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        /*ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);*/
        textView.setText(values.get(position));

        // Change icon based on name
        String s = values.get(position);

        System.out.println(s);

        /*if (s.equals("WindowsMobile")) {
            imageView.setImageResource(R.drawable.windowsmobile_logo);
        } else if (s.equals("iOS")) {
            imageView.setImageResource(R.drawable.ios_logo);
        } else if (s.equals("Blackberry")) {
            imageView.setImageResource(R.drawable.blackberry_logo);
        } else {
            imageView.setImageResource(R.drawable.android_logo);
        }*/

        return rowView;
    }

    public void addDataToList(ArrayList<String> newData) {
        for (String value: newData) {
            this.values.add(value);
        }
        this.notifyDataSetChanged();
    }
}
