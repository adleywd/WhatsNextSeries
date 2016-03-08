package Utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;

import br.com.adley.myseriesproject.R;

/**
 * Created by Adley on 07/03/2016.
 */
/*public class HomePageAdapter extends BaseAdapter {
    private Activity activity;
    private HashMap<String, String> map;

    public HomePageAdapter(Activity activity, HashMap<String, String> map) {
        this.activity = activity;
        this.map = map;
    }

    public int getCount() {
        return map.size();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.home_page_list_items,
                    null);
        }

        // Recommended to use a list as the dataset passed in the constructor.
        // Otherwise not sure how you going to map a position to an index in the dataset.
        String key =  // get a key from the HashMap above
        String value = map.get(key);

        TextView keyView = convertView.findViewById(R.id.item_key);
        keyView.setText(key);

        TextView valueView = convertView.findViewById(R.id.item_value);
        valueView .setText(value);

        return convertView;
    }
}
*/