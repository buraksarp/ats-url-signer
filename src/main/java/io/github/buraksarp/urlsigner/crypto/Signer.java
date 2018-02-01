package io.github.buraksarp.urlsigner.crypto;

public interface Signer {

    byte[] sign(byte[] data);

}
