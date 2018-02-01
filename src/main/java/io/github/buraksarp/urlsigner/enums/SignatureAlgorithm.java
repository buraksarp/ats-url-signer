package io.github.buraksarp.urlsigner.enums;

public enum SignatureAlgorithm {

    HMAC_SHA1(1, "HmacSHA1", "HMAC using SHA-1"),
    HMAC_MD5(2, "HmacMD5","HMAC using MD5");

    private final Integer value;
    private final String jcaName;
    private final String description;

    SignatureAlgorithm(Integer value, String jcaName, String description) {
        this.value = value;
        this.jcaName = jcaName;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public String getJcaName() {
        return jcaName;
    }

    public String getDescription() {
        return description;
    }
}
