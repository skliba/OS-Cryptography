package com.example.noxqs.crypto.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.noxqs.crypto.R;
import com.example.noxqs.crypto.utils.ExternalStorageHelper;
import com.example.noxqs.crypto.utils.FileManagementHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by noxqs on 03.12.15..
 */
public class HashFragment extends Fragment {

    @Bind(R.id.et_encrypt)
    EditText etEncrypt;
    @Bind(R.id.tv_encrypted)
    TextView tvEncrypted;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hash, container, false);

        ExternalStorageHelper.checkExternalMedia();
        ExternalStorageHelper.instantiateFile();

        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.button_hash)
    public void hashClicked(){

        FileManagementHelper.writeToFile(etEncrypt.getText().toString(), "plain_text_that_needs_to_be_hashed.txt");

        String hash = hashSafe(FileManagementHelper.readFromFile("plain_text_that_needs_to_be_hashed.txt"), "SHA512");
        FileManagementHelper.writeToFile(hash, "hash.txt");
        tvEncrypted.setText(FileManagementHelper.readFromFile("hash.txt"));
    }

    public static String hash(String text, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(text.getBytes());
        byte byteData[] = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte aByteData : byteData) {
            sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static String hashSafe(String text, String algorithm) {
        try {
            return hash(text, algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
