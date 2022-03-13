package com.example.qrgameteam15;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class acts as an adapter for MyScans list
 * It adds the Date and Score to the list
 * Also suggests where the QRCodes has an associated Photo and/or Location
 */
public class ScanListAdapter extends ArrayAdapter<QRCode> {
    private Context myContext;
    int myResource;

    /**
     * Constructor for the ScanListAdapter
     * @param context
     * Represents the context of the application
     * @param resource
     * Suggests the layout to be used
     * @param objects
     * Suggests the items that will be contained in the list
     */
    public ScanListAdapter(Context context, int resource, ArrayList<QRCode> objects) {
        super(context, resource, objects);
        this.myContext = context;
        myResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String scanDate = QRCode.getDateStr();
        System.out.print("position");
        int scanScore = QRCode.getScore();
        boolean scanHasLocation = QRCode.getHasLocation();
        boolean scanHasPhoto = QRCode.getHasPhoto();

        LayoutInflater inflater = LayoutInflater.from(myContext);
        View view = convertView;
        view = inflater.inflate(myResource, parent, false);

        TextView date = view.findViewById(R.id.scan_date);
        TextView score = view.findViewById(R.id.scan_score);
        TextView hasLocation = view.findViewById(R.id.has_location);
        TextView hasPhoto = view.findViewById(R.id.has_photo);

        date.setText(scanDate);
        score.setText("Score: " + String.valueOf(scanScore));
        hasLocation.setText("Has Location: " + String.valueOf(scanHasLocation));
        hasPhoto.setText("Has Photo: " + String.valueOf(scanHasPhoto));

        return view;
    }
}
