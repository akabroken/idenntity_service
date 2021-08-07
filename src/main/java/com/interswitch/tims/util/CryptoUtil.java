package com.interswitch.tims.util;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptoUtil {

    Logger logger = LoggerFactory.getLogger(getClass());

    public static PrivateKey rsaPrivFromME(String modulus, String privateExponent) throws Exception {
        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
        KeyFactory keyFactory = KeyFactory.getInstance(ConstantUtil.RSA);
        return (PrivateKey) keyFactory.generatePrivate(privateKeySpec);
    }

    public static PublicKey rsaPubFromME(String modulus, String privateExponent) throws Exception {
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
        KeyFactory keyFactory = KeyFactory.getInstance(ConstantUtil.RSA);
        return (PublicKey) keyFactory.generatePublic(publicKeySpec);
    }

    public static PublicKey rsaPubFromString(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Security.addProvider(new BouncyCastleProvider());
        byte[] byteKey = Base64.decode(key.getBytes());
        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
        KeyFactory keyfactory = KeyFactory.getInstance("RSA");

        return keyfactory.generatePublic(X509publicKey);

    }

    public static PublicKey rsaPubFromCertString(String key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        byte[] byteKey = Base64.decode(key.getBytes());
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(byteKey));

        return cert.getPublicKey();

    }

    public static PrivateKey rsaPrivFromCertString(String key) throws Exception {

        byte[] byteKey = Base64.decode(key.getBytes());
        ASN1Sequence primitive = (ASN1Sequence) ASN1Sequence.fromByteArray(byteKey);
        Enumeration<?> e = primitive.getObjects();
        BigInteger v = ((ASN1Integer) e.nextElement()).getValue();
        int version = v.intValue();
        if (version != 0 && version != 1) {
            throw new IllegalArgumentException("wrong version for RSA private key");
        }
        BigInteger modulus = ((ASN1Integer) e.nextElement()).getValue();
        ((ASN1Integer) e.nextElement()).getValue();
        BigInteger privateExponent = ((ASN1Integer) e.nextElement()).getValue();
        ((ASN1Integer) e.nextElement()).getValue();
        ((ASN1Integer) e.nextElement()).getValue();
        ((ASN1Integer) e.nextElement()).getValue();
        ((ASN1Integer) e.nextElement()).getValue();
        ((ASN1Integer) e.nextElement()).getValue();
        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(modulus, privateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(ConstantUtil.RSA);
        return (PrivateKey) keyFactory.generatePrivate(privateKeySpec);

    }

    public static byte[] encryptRSA(RSAPublicKey publicKey, byte[] clearText,String alogrithm) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Cipher encryptCipher = Cipher.getInstance(alogrithm);
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return encryptCipher.doFinal(clearText);
    }

    public static String encryptRSA(PublicKey publicKey, String clearText) throws Exception {

        Security.addProvider(new BouncyCastleProvider());
        Cipher encryptCipher = Cipher.getInstance(ConstantUtil.RSA_ECD_PKCS1PADDING,new BouncyCastleProvider());
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] authDataBytes = encryptCipher.doFinal(clearText.getBytes(StandardCharsets.UTF_8));

        return new String(Base64.encode(authDataBytes)).trim();
    }

    public static String decryptRSA(PrivateKey privateKey, String encData) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Cipher encryptCipher = Cipher.getInstance(ConstantUtil.RSA_ECD_PKCS1PADDING, new BouncyCastleProvider());
        encryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] encDataBytes = encryptCipher.doFinal(Base64.decode(encData));
        return new String(encDataBytes).trim();
    }

    public static PublicKey getPublicKey(String modulus, String publicExponent) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        RSAPublicKeySpec publicKeyspec = new RSAPublicKeySpec(new BigInteger(modulus, 16), new BigInteger(publicExponent, 16));
        KeyFactory factory = KeyFactory.getInstance(ConstantUtil.RSA);
        PublicKey publicKey = factory.generatePublic(publicKeyspec);
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String modulus, String privateExponent) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        RSAPrivateKeySpec privateKeyspec = new RSAPrivateKeySpec(new BigInteger(modulus, 16), new BigInteger(privateExponent, 16));
        KeyFactory factory = KeyFactory.getInstance(ConstantUtil.RSA);
        PrivateKey privateKey = factory.generatePrivate(privateKeyspec);
        return privateKey;
    }

    public static byte[] getHmacSha(String data, String key, String algorithm) throws Exception {
        //HmacSHA256, HmacSHA1
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(signingKey);
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] encrypt3DES(byte[] clearText) throws Exception {
        return encrypt_3DES(clearText, ConstantUtil.DES_ECD_PKCS5PADDING, ConstantUtil._3DESKEY);
    }

    public static byte[] encrypt3DES(byte[] clearText, String algorithm) throws Exception {
        return encrypt_3DES(clearText, algorithm, ConstantUtil._3DESKEY);
    }

    public static byte[] encrypt3DES(byte[] clearText, String algorithm, String key) throws Exception {
        return encrypt_3DES(clearText, algorithm, key);
    }

    private static byte[] encrypt_3DES(byte[] clearText, String algorithm, String key) throws Exception {
        return des3Srv(algorithm, Cipher.ENCRYPT_MODE, clearText, key);
    }

    public static byte[] decrypt3DES(byte[] encryptedText) throws Exception {
        return decrypt_3DES(encryptedText, ConstantUtil.DES_ECD_PKCS5PADDING, ConstantUtil._3DESKEY);
    }

    public static byte[] decrypt3DES(byte[] encryptedText, String algorithm) throws Exception {
        return decrypt_3DES(encryptedText, algorithm, ConstantUtil._3DESKEY);
    }

    public static byte[] decrypt3DES(byte[] encryptedText, String algorithm, String key) throws Exception {
        return decrypt_3DES(encryptedText, algorithm, key);
    }

    private static byte[] decrypt_3DES(byte[] encryptedText, String algorithm, String key) throws Exception {
        return des3Srv(algorithm, Cipher.DECRYPT_MODE, encryptedText, key);
    }

    private static byte[] des3Srv(String algorithm, int mode, byte[] text, String key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        Cipher cipher = Cipher.getInstance(algorithm, ConstantUtil.BC);
        SecretKey seckey = new SecretKeySpec(Hex.decode(key), ConstantUtil.DESEDE);
        cipher.init(mode, seckey);

        return cipher.doFinal(text);
    }

}
