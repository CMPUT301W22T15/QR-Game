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

public class CustomList2 extends ArrayAdapter<Player> {
    private ArrayList<Player> playersWithSameQRCode;
    private Context context;

    public CustomList2(Context context, ArrayList<Player> playersWithSameQRCode) {
        super(context, 0, playersWithSameQRCode);
        this.context = context;
        this.playersWithSameQRCode = playersWithSameQRCode;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content2, parent, false);
        }

        Player player = playersWithSameQRCode.get(position);

        TextView playerName = view.findViewById(R.id.player_text);

        playerName.setText(player.getUsername());

        return view;
    }
}
