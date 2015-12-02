package com.example.noxqs.crypto.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.noxqs.crypto.R;

import java.security.Security;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by noxqs on 28.11.15..
 */
public class AESFragment extends Fragment {

    public static final String ALGORITHM = "AES";
    public static final String SECRETKEY = "SECRETKEY";

    @Bind(R.id.et_encrypt)
    EditText etEncrypt;
    @Bind(R.id.button_encrypt)
    Button buttonEncrypt;
    @Bind(R.id.tv_encrypted)
    TextView tvEncrypted;
    @Bind(R.id.tv_decrypted)
    TextView tvDecrypted;

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aes, container, false);


        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
