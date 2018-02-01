package io.github.buraksarp.urlsigner.enums;

public enum SigningPart {

    FQDN_AND_ALL_DIRECTORY("1", "Use the FQDN and all directory parts for signature verification"),
    FQDN_AND_FIRST_DIRECTORY("110", "Use the FQDN and first directory for signature verification, but ignore the remainder of the path"),
    ONLY_ALL_DIRECTORY("01", "Ignore the FQDN, but verify using all directory parts"),
    ONLY_FIRST_TWO_DIRECTORY("0110", "Ignore the FQDN, and use only the first two directory parts, skipping the remainder, for signatures");

    private final String value;
    private final String effect;

    SigningPart(String value, String effect) {
        this.value = value;
        this.effect = effect;
    }

    public String getValue() {
        return value;
    }

    public String getEffect() {
        return effect;
    }

}
