package com.example.noxqs.crypto.fragments;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.noxqs.crypto.R;
import com.example.noxqs.crypto.utils.ExternalStorageHelper;
import com.example.noxqs.crypto.utils.FileManagementHelper;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.Cipher;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RSAFragment extends Fragment {

    public static final String PUBLIC_KEY = "PUBLICKEY";
    public static final String PRIVATE_KEY = "PRIVATEKEY";
    public static final String ALGORITHM = "RSA";
    public static final String RSA_ENCRYPTED_TEXT_FILENAME = "RSA_encrypted_text.txt";

    @Bind(R.id.et_encrypt)
    EditText etEncrypt;
    @Bind(R.id.button_encrypt)
    Button buttonEncrypt;
    @Bind(R.id.tv_encrypted)
    TextView tvEncrypted;
    @Bind(R.id.tv_decrypted)
    TextView tvDecrypted;

    private static final int KEY_SIZE = 1024;

    private static KeyPair keyPair;


    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    public static RSAFragment newInstance() {
        RSAFragment fragment = new RSAFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rsa, container, false);
        ButterKnife.bind(this, view);

        ExternalStorageHelper.checkExternalMedia();
        generate();
        ExternalStorageHelper.instantiateFile();

        FileManagementHelper.writePublicKeyToFile(keyPair);
        FileManagementHelper.writePrivateKeyToFile(keyPair);

        return view;
    }



    public KeyPair generate() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
            generator.initialize(KEY_SIZE);
            keyPair = generator.generateKeyPair();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().
                    putString(PUBLIC_KEY, keyPair.getPublic().toString()).apply();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().
                    putString(PRIVATE_KEY, keyPair.getPrivate().toString()).apply();
            return keyPair;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnClick(R.id.button_encrypt)
    public void encryptClicked() {
        FileManagementHelper.writeToFile(etEncrypt.getText().toString(), "RSA_plain_text.txt");
        byte[] temporaryByte = encrypt(keyPair.getPublic(), etEncrypt.getText().toString().getBytes());
        tvEncrypted.setText(encodeToBase64(temporaryByte));
        FileManagementHelper.writeToFile(tvEncrypted.getText().toString(), RSA_ENCRYPTED_TEXT_FILENAME);

        byte[] tempByte = decodeFromBase64(FileManagementHelper.readFromFile(RSA_ENCRYPTED_TEXT_FILENAME).getBytes());
        tvDecrypted.setText(decrypt(keyPair.getPrivate(), tempByte));
    }

    private String encodeToBase64(byte[] cipheredText) {
        return Base64.encodeToString(cipheredText, Base64.DEFAULT);
    }

    private byte[] encrypt(PublicKey publicKey, byte[] plainText) {
        byte[] encryptedText = null;
        try {
            final Cipher rsaCipher = Cipher.getInstance(ALGORITHM);
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedText = rsaCipher.doFinal(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedText;
    }


    private String decrypt(PrivateKey privateKey, byte[] cipherText) {
        byte[] decryptedText = null;
        try {
            final Cipher rsaCipher = Cipher.getInstance(ALGORITHM);
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
            decryptedText = rsaCipher.doFinal(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(decryptedText);
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
