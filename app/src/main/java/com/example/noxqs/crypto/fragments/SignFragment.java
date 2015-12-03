package com.example.noxqs.crypto.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.noxqs.crypto.R;
import com.example.noxqs.crypto.utils.ExternalStorageHelper;
import com.example.noxqs.crypto.utils.FileManagementHelper;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by noxqs on 28.11.15..
 */
public class SignFragment extends Fragment {

    public static final String ALGORITHM = "SHA1withRSA";
    public static final String MESSAGE = "This is a message that needs to be signed";
    public static final String PLAIN_TEXT_FILE_NAME = "plain_text_to_be_signed.txt";
    public static final String SIGNED_DATA_FILENAME = "signed_data.txt";
    public static final String RSA = "RSA";

    @Bind(R.id.message_to_be_signed)
    TextView messageToBeSigned;
    @Bind(R.id.signature)
    TextView signature;
    @Bind(R.id.correct)
    TextView correct;

    KeyPair pair;
    private byte[] signedData;
    private Signature signatureObj;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign, container, false);
        ButterKnife.bind(this, view);

        ExternalStorageHelper.checkExternalMedia();
        ExternalStorageHelper.instantiateFile();
        try {
            generateKeys();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //not extracting string resource cause its irrelevant
        messageToBeSigned.setText(MESSAGE);
        FileManagementHelper.writeToFile(messageToBeSigned.getText().toString(), PLAIN_TEXT_FILE_NAME);

        return view;
    }

    @OnClick(R.id.start_signature)
    public void signatureClicked() {
        try {
            signedData = sign(FileManagementHelper.readFromFile(PLAIN_TEXT_FILE_NAME).getBytes());
            FileManagementHelper.writeToFile(signedData.toString(), SIGNED_DATA_FILENAME);
            signature.setText(encodeToBase64(signedData));


            //TODO uncomment this if its needed to show that code is valid
            //signedData = (signedData.toString() + "sdgfdgad").getBytes();

            if (verify(signedData)) {
                correct.setText("Correct");
            } else {
                correct.setText("Incorrect");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateKeys() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
        pair = generator.generateKeyPair();
        FileManagementHelper.writePrivateKeyToFile(pair);
        FileManagementHelper.writePublicKeyToFile(pair);
    }

    private byte[] sign(byte[] bytes) throws Exception {
        signatureObj = Signature.getInstance(ALGORITHM);
        signatureObj.initSign(pair.getPrivate());
        signatureObj.update(bytes);
        return signatureObj.sign();
    }

    private boolean verify(byte[] bytes) throws Exception {
        signatureObj.initVerify(pair.getPublic());
        signatureObj.update(FileManagementHelper.readFromFile(PLAIN_TEXT_FILE_NAME).getBytes());
        return signatureObj.verify(signedData);
    }

    //ne radi pretvorba, baca exception
    private PrivateKey convertStringToPrivateKey() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException{
        String pkey = FileManagementHelper.readFromFile("privatni_kljuc.txt");
        byte[] keyBytes = decodeFromBase64(pkey.getBytes("utf-8"));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePrivate(keySpec);
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
