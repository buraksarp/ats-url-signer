package io.github.buraksarp.urlsigner;

import io.github.buraksarp.urlsigner.enums.SigningPart;
import io.github.buraksarp.urlsigner.enums.SignatureAlgorithm;

import java.util.concurrent.TimeUnit;

public interface SignedUrlBuilder {

    SignedUrlBuilder setContentUrl(String contentUrl);

    SignedUrlBuilder setTimeToLive(int timeToLive, TimeUnit timeUnit);

    SignedUrlBuilder setClientIp(String clientIp);

    SignedUrlBuilder setParts(SigningPart signingPart);

    SignedUrlBuilder signWith(SignatureAlgorithm signatureAlgorithm, String secretKey, int keyIndex);

    String compact();

}

