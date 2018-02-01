package io.github.buraksarp.urlsigner.crypto;

import io.github.buraksarp.urlsigner.exception.SignatureException;
import io.github.buraksarp.urlsigner.enums.SignatureAlgorithm;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class MacSigner implements Signer {

    private final SignatureAlgorithm signatureAlgorithm;
    private final String secretKey;

    public MacSigner (SignatureAlgorithm signatureAlgorithm, String secretKey) {
        this.signatureAlgorithm = signatureAlgorithm;
        this.secretKey = secretKey;
    }

    @Override
    public byte[] sign(byte[] data) {
        Mac mac = getMacInstance();
        return mac.doFinal(data);
    }

    private Mac getMacInstance() {

        Mac mac;
        try {
            mac = Mac.getInstance(signatureAlgorithm.getJcaName());
            Key key = new SecretKeySpec(secretKey.getBytes(), signatureAlgorithm.getJcaName());
            mac.init(key);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new SignatureException(e.getMessage());
        }

        return mac;
    }

}
