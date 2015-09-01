package de.konqi.fitapi.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by konqi on 23.08.2015.
 */
public class HashBuilder {
    private static final Logger logger = LoggerFactory.getLogger(HashBuilder.class);

    public static HashBuilder MD5 = new HashBuilder("MD5");
    public static HashBuilder SHA256 = new HashBuilder("SHA-256");

    private MessageDigest md;

    private HashBuilder(String algorithm) {
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Unable to initialize message digester.", e);
            md = null;
        }
    }

    public DigestOutput digest(String string) {
        return new DigestOutput(md.digest(string.getBytes()));
    }

    public DigestOutput digest(byte[] bytes) {
        return new DigestOutput(md.digest(bytes));
    }

    public static class DigestOutput {
        byte[] output;

        private DigestOutput(byte[] bytes) {
            this.output = bytes;
        }

        public byte[] asBytes() {
            return output;
        }

        public String asHex() {
            return bytesToHex(output);
        }
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
