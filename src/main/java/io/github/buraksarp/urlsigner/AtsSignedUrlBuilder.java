package io.github.buraksarp.urlsigner;

import io.github.buraksarp.urlsigner.enums.SigningPart;
import org.joda.time.DateTime;
import io.github.buraksarp.urlsigner.crypto.MacSigner;
import io.github.buraksarp.urlsigner.enums.SignatureAlgorithm;
import io.github.buraksarp.urlsigner.util.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AtsSignedUrlBuilder implements SignedUrlBuilder {

    private String contentUrl;
    private int timeToLive;
    private TimeUnit timeUnit;
    private String clientIp;
    private int keyIndex;
    private SigningPart signingPart;
    private SignatureAlgorithm signatureAlgorithm;
    private String secretKey;

    public static final String X_LATE = "0123456789abcdef";

    public SignedUrlBuilder setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
        return this;
    }

    public SignedUrlBuilder setTimeToLive(int timeToLive, TimeUnit timeUnit) {
        this.timeToLive = timeToLive;
        this.timeUnit = timeUnit;
        return this;
    }

    public SignedUrlBuilder setClientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public SignedUrlBuilder setParts(SigningPart signingPart) {
        this.signingPart = signingPart;
        return this;
    }

    public SignedUrlBuilder signWith(SignatureAlgorithm signatureAlgorithm, String secretKey, int keyIndex) {
        this.signatureAlgorithm = signatureAlgorithm;
        this.secretKey = secretKey;
        this.keyIndex = keyIndex;
        return this;
    }

    public String compact() {

        Assert.notNull(signingPart, "signing part must NOT be null");
        Assert.notNullorEmpty(secretKey, "secret key must NOT be null or empty");
        Assert.notNull(timeUnit, "Time to live must be set");
        Assert.notNull(signatureAlgorithm, "Sign with must be set");

        StringBuilder sb = new StringBuilder();
        sb.append(this.getUrlPart(contentUrl, signingPart)).append("?");
        sb.append(Assert.hasText(clientIp) ? "C=" + clientIp + "&" : "");
        sb.append("E=").append(this.getExpireTimeSeconds()).append("&");
        sb.append("A=").append(this.signatureAlgorithm.getValue()).append("&");
        sb.append("K=").append(this.keyIndex).append("&");
        sb.append("P=").append(this.signingPart.getValue()).append("&");
        sb.append("S=");

        MacSigner macSigner = new MacSigner(this.signatureAlgorithm, this.secretKey);
        byte[] digest = macSigner.sign(sb.toString().getBytes());

        String token = this.hexlate(digest);
        sb.append(token);

        return sb.toString();
    }

    private String hexlate(byte[] bytes) {

        char[] chars = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; i++) {
            int val = bytes[i];
            if (val < 0) {
                val += 256;
            }
            chars[(2 * i)] = X_LATE.charAt(val / 16);
            chars[(2 * i + 1)] = X_LATE.charAt(val % 16);
        }

        return new String(chars);
    }

    private long getExpireTimeSeconds() {

        DateTime dateTime = new DateTime();

        switch (timeUnit) {
            case DAYS:
                dateTime = dateTime.plusDays(timeToLive);
                break;
            case HOURS:
                dateTime = dateTime.plusHours(timeToLive);
                break;
            case MINUTES:
                dateTime = dateTime.plusMinutes(timeToLive);
                break;
            case SECONDS:
                dateTime = dateTime.plusSeconds(timeToLive);
                break;
            case MILLISECONDS:
                dateTime = dateTime.plusMillis(timeToLive);
                break;
            default:
                throw new IllegalStateException(timeUnit.name() + " is NOT supported, try [DAYS, HOURS, MINUTES, SECONDS, MILLISECONDS]");
        }

        //seconds since Jan 01 1970
        return TimeUnit.MILLISECONDS.toSeconds(dateTime.getMillis());
    }

    private String getUrlPart(String contentUrl, SigningPart signingPart) {

        URL url;
        try {
            url = new URL(contentUrl);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Malformed URL");
        }

        String urlSigningPart = null;

        switch (signingPart) {
            case FQDN_AND_ALL_DIRECTORY:
                urlSigningPart = url.getHost() + url.getPath();
                break;
            case ONLY_ALL_DIRECTORY:
                urlSigningPart = url.getPath().substring(1);
                break;
            case FQDN_AND_FIRST_DIRECTORY:
                urlSigningPart = url.getHost() + getDirectory(url.getPath(), 1);
                break;
            case ONLY_FIRST_TWO_DIRECTORY:
                urlSigningPart = getDirectory(url.getPath(), 2).substring(1);
        }

        return urlSigningPart;
    }

    private String getDirectory(String path, int directoryCount) {

        char[] chars = path.toCharArray();
        String directory = null;

        int counter = 1;
        for(int i = 1; i < chars.length; i++) {
            if(chars[i] == '/') {
                if (counter == directoryCount) {
                    directory = path.substring(0, i);
                    break;
                } else {
                    counter++;
                }
            }
        }

        return directory == null ? path : directory;
    }

}
