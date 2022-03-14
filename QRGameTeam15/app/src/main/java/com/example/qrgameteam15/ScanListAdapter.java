package com.example.qrgameteam15;

import android.content.Context;
import android.util.Log;
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
    private ArrayList<QRCode> qrCodes;

    /**
     * Constructor for the ScanListAdapter
     * @param context
     * Represents the context of the application
     * @param resource
     * Suggests the layout to be used
     * @param qrCodes
     * Suggests the items that will be contained in the list
     */
    public ScanListAdapter(Context context, int resource, ArrayList<QRCode> qrCodes) {
        super(context, resource, qrCodes);
        this.myContext = context;
        this.myResource = resource;
        this.qrCodes = qrCodes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(myContext).inflate(myResource, parent,false);
        }

        QRCode qrcode = qrCodes.get(position);
        TextView date = view.findViewById(R.id.scan_date);
        TextView score = view.findViewById(R.id.scan_score);
        TextView hasLocation = view.findViewById(R.id.has_location);
        TextView hasPhoto = view.findViewById(R.id.has_photo);
        String qrcode1 = qrcode.getDateStr();

        date.setText(qrcode.getDateStr());
        score.setText("Score: " + String.valueOf(qrcode.getScore()));
        hasLocation.setText("hasLocation: " + String.valueOf(qrcode.getHasLocation()));
        hasPhoto.setText("hasPhoto: " + String.valueOf(qrcode.getHasPhoto()));

        return view;
    }
}
