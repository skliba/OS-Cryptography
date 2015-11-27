package com.example.noxqs.crypto;

import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.noxqs.crypto.utils.ExternalStorageHelper;
import com.example.noxqs.crypto.utils.FileManagementHelper;
import com.example.noxqs.crypto.view.BaseView;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.Cipher;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    public static final String PUBLIC_KEY = "PUBLICKEY";
    public static final String PRIVATE_KEY = "PRIVATEKEY";
    public static final String ALGORITHM = "RSA";
    @Bind(R.id.et_encrypt)
    EditText etEncrypt;
    @Bind(R.id.tv_encrypted)
    TextView tvEncrypted;
    @Bind(R.id.tv_decrypted)
    TextView tvDecrypted;

    private static final int KEY_SIZE = 1024;

    private static KeyPair keyPair;


    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BaseView baseView = this;

        ExternalStorageHelper.checkExternalMedia();
        generate();
        instantiateFile();

        FileManagementHelper.writePublicKeyToFile(keyPair);
        FileManagementHelper.writePrivateKeyToFile(keyPair);
    }

    private void instantiateFile() {
        File baseWriteLocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/CryptoFiles/");
        if (!baseWriteLocation.exists()) {
            baseWriteLocation.mkdirs();
        }
    }

    public KeyPair generate() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
            generator.initialize(KEY_SIZE);
            keyPair = generator.generateKeyPair();
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(PUBLIC_KEY, keyPair.getPublic().toString());
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(PRIVATE_KEY, keyPair.getPrivate().toString());
            return keyPair;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnClick(R.id.button_encrypt)
    public void encryptClicked() {
        FileManagementHelper.writePLainTextToFile(etEncrypt.getText().toString());
        byte[] something = encrypt(keyPair.getPublic(), etEncrypt.getText().toString().getBytes());
        String kaoString = encodeToBase64(keyPair.getPublic(), something);
        tvEncrypted.setText("" + kaoString);

        byte [] something1 = decodeFromBase64(keyPair.getPrivate(), tvEncrypted.getText().toString().getBytes());
        String kaoNes = decrypt(keyPair.getPrivate(), something1);
        tvDecrypted.setText("" + kaoNes);
        FileManagementHelper.writeEncryptedTextToFile(tvEncrypted.getText().toString());
    }

    private String encodeToBase64(PublicKey publicKey, byte[] cipheredText) {
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

    private byte[] decodeFromBase64(PrivateKey privateKey, byte[] cipheredText) {
        Log.e("FGFASFSAF", cipheredText.toString());
        return Base64.decode(cipheredText, Base64.DEFAULT);
    }

}

