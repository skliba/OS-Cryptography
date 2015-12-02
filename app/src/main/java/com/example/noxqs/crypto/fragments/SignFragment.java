package com.example.noxqs.crypto.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.noxqs.crypto.R;
import com.example.noxqs.crypto.utils.FileManagementHelper;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by noxqs on 28.11.15..
 */
public class SignFragment extends Fragment {

    public static final String SIGNATURE_DSA = "SHA/DSA";
    public static final String ALGORITHM_DSA = "DSA";
    public static final int KEY_SIZE = 1024;

    @Bind(R.id.message_to_be_signed)
    TextView messageToBeSigned;
    @Bind(R.id.signature)
    TextView signature;
    @Bind(R.id.correct)
    TextView correct;

    KeyPair pair;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign, container, false);
        ButterKnife.bind(this, view);

        //not extracting string resource cause its irrelevant
        messageToBeSigned.setText("This is a message that needs to be signed");
        return view;
    }

    @OnClick(R.id.start_signature)
    public void signatureClicked() {
        try {
            generateKeys();
            signature.setText(sign(messageToBeSigned.getText().toString().getBytes()) + "");
            Log.e("Provjera1", (signature.getText().toString().getBytes()) + "");
            if (verify(signature.getText().toString().getBytes())) {
                correct.setText("Correct");
                Log.e("TU", "U CORRECT SAM");
            } else {
                correct.setText("Incorrect");
                Log.e("TU", "U INCORRECT SAM");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO keyevi se moraju citati iz filea

    private void generateKeys() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("SHA256withRSA");
        Log.e("TU", "U CORRECT SAM");
        generator.initialize(KEY_SIZE);
        pair = generator.generateKeyPair();
        FileManagementHelper.writePrivateKeyToFile(pair);
        FileManagementHelper.writePublicKeyToFile(pair);
    }

    private byte[] sign(byte[] bytes) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(pair.getPrivate());
        signature.update(bytes);
        return signature.sign();

    }

    private boolean verify(byte[] bytes) throws Exception {
        Log.e("BYTE", bytes.toString());
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(pair.getPublic());
        signature.update(bytes);
        Log.e("TU", bytes.toString());
        return signature.verify(bytes);
    }


    private String encodeToBase64(byte[] cipheredText) {
        return Base64.encodeToString(cipheredText, Base64.DEFAULT);
    }

    private byte[] decodeFromBase64(byte[] cipheredText) {
        return Base64.decode(cipheredText, Base64.DEFAULT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
