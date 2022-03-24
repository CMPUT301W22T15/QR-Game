package com.example.qrgameteam15;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ViewQRCodeFragment extends DialogFragment {
    TextView attributes;
    QRCode qrCode;

    private OnFragmentInteractionListener listener;

    public ViewQRCodeFragment(QRCode qrcode) {
        this.qrCode = qrcode;
    }

    public interface OnFragmentInteractionListener{
        //
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_qrcode_fragment_layout,null);
        attributes = view.findViewById(R.id.attributes);

        String attributesQR = "hashedID: " + qrCode.getId() + "\n" + "Date: " + qrCode.getDateStr() + "\n" +
                "Score: " + String.valueOf(qrCode.getScore()) + "\n"
                + "hasPhoto: " + String.valueOf(qrCode.getHasPhoto()) + "\n"
                + "hasLocation: " + String.valueOf(qrCode.getHasLocation());
//
//        attributes.setText(attributesQR);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("View More on QR Code")
                .setMessage(attributesQR)
                .setPositiveButton("OK", null).create();
    }
}
