package com.example.noxqs.crypto.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.noxqs.crypto.R;
import com.example.noxqs.crypto.utils.ExternalStorageHelper;
import com.example.noxqs.crypto.utils.FileManagementHelper;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by noxqs on 28.11.15..
 */
public class AESFragment extends Fragment {

    public static final String AES_ENCRYPTED_TEXT_FILENAME = "AES_encrypted_text.txt";
    public static final String AES = "AES";

    @Bind(R.id.et_encrypt)
    EditText etEncrypt;
    @Bind(R.id.tv_encrypted)
    TextView tvEncrypted;
    @Bind(R.id.tv_decrypted)
    TextView tvDecrypted;
    static {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    @Bind(R.id.et_key)
    EditText etKey;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aes, container, false);

        ExternalStorageHelper.checkExternalMedia();
        ExternalStorageHelper.instantiateFile();

        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.button_encrypt)
    public void encryptClicked() {
        byte[] key = null;
        try {
            FileManagementHelper.writeToFile(etKey.getText().toString() , "tajni_kljuc.txt");
            byte[] keyStart = etKey.getText().toString().getBytes();
            KeyGenerator kgen = KeyGenerator.getInstance(AES);
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");

            sr.setSeed(keyStart);
            kgen.init(128, sr);

            SecretKey skey = kgen.generateKey();
            key = skey.getEncoded();
            byte[] encrypted = encrypt(key, etEncrypt.getText().toString().getBytes());

            FileManagementHelper.writeToFile(encodeToBase64(encrypted), AES_ENCRYPTED_TEXT_FILENAME);
            tvEncrypted.setText(FileManagementHelper.readFromFile(AES_ENCRYPTED_TEXT_FILENAME));

            byte[] decrypted = decrypt(key, FileManagementHelper.readFromFile(AES_ENCRYPTED_TEXT_FILENAME).getBytes());
            String decryptedString = new String(decrypted);
            tvDecrypted.setText(decryptedString);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String encodeToBase64(byte[] cipheredText) {
        return Base64.encodeToString(cipheredText, Base64.DEFAULT);
    }

    private byte[] decodeFromBase64(byte[] cipheredText) {
        return Base64.decode(cipheredText, Base64.DEFAULT);
    }

    private byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(clear);
    }

    private byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decoded = decodeFromBase64(encrypted);
        return cipher.doFinal(decoded);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
