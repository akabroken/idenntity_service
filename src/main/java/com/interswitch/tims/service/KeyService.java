/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.service;

import com.interswitch.tims.dto.SystemResponse;
import com.interswitch.tims.model.BaseModel;
import com.interswitch.tims.model.FuseModel;
import com.interswitch.tims.util.*;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ndegel
 */
@Service
public class KeyService {

    @Autowired
    FuseService fuseService;

    @Autowired
    RedisService redisService;

    private final static Logger logger = LoggerFactory.getLogger(RedisService.class);

    public SystemResponse fetchMlePublicKey() throws Exception {
        SystemResponse systemResponse = new SystemResponse();
        systemResponse.setCode(ResponseUtil.SUCCESS_CODE_0);
        BaseModel item = new BaseModel();

        RSAPublicKey pubkey = getMlePubKey(ConstantUtil.IDENTITY_MLE_RSA_PUBLIC_KEY);
        item.setKey(new String(Base64.encode(pubkey.getEncoded())));
        systemResponse.setItem(item);
        return systemResponse;
    }

    private void cacheKey(BaseModel key) {
        redisService.set(key.getKeyID(), key.getKey(), Duration.ofMinutes(5));
    }

    public String getSessionKey(String keyId) {
        return (String) redisService.get(keyId);
    }

    public RSAPublicKey getVisaPublicKey(String instid) throws Exception {
        String key = instid + "_RSA_PUBLIC_KEY";
        String keyString = "MIIDyzCCArOgAwIBAgIIVgYupEeqdlowDQYJKoZIhvcNAQELBQAwMTEOMAwGA1UEAwwFVkRQQ0ExEjAQBgNVBAoMCVZEUFZJU0FDQTELMAkGA1UEBhMCVVMwHhcNMjEwNzIxMDc0NzE3WhcNMjMwNzIxMDc0NzE3WjCBmDEbMBkGCSqGSIb3DQEJARYMdmRwQHZpc2EuY29tMS0wKwYDVQQDDCQyMGJhMDUyZi1hZTMyLTQ3ZDYtYTQ3MS02ZDRlZTJlYTZhOTkxDDAKBgNVBAsMA1ZEUDENMAsGA1UECgwEVklTQTETMBEGA1UEBwwKRm9zdGVyQ2l0eTELMAkGA1UECAwCQ0ExCzAJBgNVBAYTAlVTMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4BPo8BE4IdVd0NY26Q39wzFmYtR6BjtNSvcRFCoAxeHV0qZl7kiPp68XS6j59gFGS/ujdAs76Fx+XcxrRkWWUzi0rKy3Y4sga3IWMRM6JYCiPfZhrT6msH7Y+jnqtWY7ZEjcufy4IgtxLeKvXmZki/YVTJ/SAT6gG+gwt9hOkZnU4hKse2q4mMWY/ZXVZEgrHpv7XBgWMe3OHU16qZtrz/ArfxK6TzQRL/lH5K3ywwhM516SY7ItecQQgJpw2wOwRxf5xhubYWnEW85DnUsHbOzwP86/d5gQL7HmEnbkOBLZR8qnX2UBwTL9uz8Lb0jn/C2QpTqxJPE6AkdClAAQiQIDAQABo38wfTAMBgNVHRMBAf8EAjAAMB8GA1UdIwQYMBaAFK/dbragS5x5uRYIYuYjMRCnguuhMB0GA1UdJQQWMBQGCCsGAQUFBwMCBggrBgEFBQcDBDAdBgNVHQ4EFgQUHiX9aee5Pm3xYnLKvMR9Ybsem8UwDgYDVR0PAQH/BAQDAgXgMA0GCSqGSIb3DQEBCwUAA4IBAQClXqhURySTSby7rOIqjbXywGcnJnjPBMJHJ2QeBwm4zUzFUcwFz+aHGXkSmh3thZbQRCe43UJdQoHHoh7PR00c9Gto0aAWgsoqHyvn01+qPgMUyfi2MTJItoCHEg90gYHmJFwhrFe1swaRzMZob3oOByzF0230j1nyYxj+be6br/C7V9Sp5kRULEoYJwvqTWZ8Yg+vRx93zVqvg5LIqCBsj6x+bl32miTDf6W5/1EywYB+m4je7vBQ3frqWS16SlDKRP0F1qYPuNytJp8c8v5PDKkoSAvoa+PBaY8TOjvQJU3+7hyDuZ73bgoApsHS+mLURaP2oam/e03J8ES1l/vg";

        return (RSAPublicKey) redisService.getSet(key, CryptoUtil.rsaPubFromCertString(keyString));
    }

    public RSAPrivateKey getVisaPrivateKey(String instid) throws Exception {
        String key = "VISA_IDENTITY_RSA_PRIVATE_KEY";
        String keyString = "MIIEowIBAAKCAQEAw/d5On8yf0w7SMCaNEhFXd4wHpTk++j0sC6yhYt7X7f7oADfzLrQIAQ5yvV6801GO/HA7eOQH5MhC23xpSF0jMc2DN46YVK+GLsElPGpexq7ITDngHIMaN8FnSDHOT97CDMkl1s6f2Diqi4Fv6yz4bV896M2fyuuw7ByW6w5bTC3I0EEzieSDhAFLnVvvHLTNCElh8Nzl56JK5nKTjjQf6KYFiMPdw61kETNw16WfSzDcD0JMQ6FdWaLE028P3e8S33BCV15ZY8uHipOiTqMuRhZWms+u3H/igUXuMR4h7TAwtWx950PzmOhdgzUAtOYlXgcb76rF2aVK9alojmlFwIDAQABAoIBAEOPU+oI5B3i6CVi0UbpC5EbArfoWTFH1OPQlZdYyQXs5gD5jC6G0S3YhxwjmWm74FTWOc9JvuxDywantsFZUVcfbtDs5G+dGRBpmWDIF8frJUyCEa3Eo+abzNGo9UYVTpM7HohX2UkE2+AT0cONBM7xxM2TJTyekLfCYwqby4iKwcQiDXL+TGDK2vMsuJwzVNJ1Xa+BzoME3yiF45Aa8pXitrZbnO+DBhkMMvBx7Xo9hL0dgQzQow9yDQg8+9Zad4FugrJz/bwnntl7Iq+4RsySJl0kuDpcp4clH5r7911fbI+y7aTvCQTZuy9glWEuggiAcz6JYOmK8Em6G/e8NLECgYEAyr7VcGwo0v3HyZyXrHljjUHRcxr0omtuVaKZHv7+RDQkNHA4IxF5lgw0ysmt1WaW/E7FIwpOwRlrBCinoGejMxQ1Rbq3c8D59z1rb9xY5TF3yRRZCD3ZVN+utnZMQSJupEvOllkq2gx0e/o7nxZfV6Ra7S6AY7xWpUINtZZOgTMCgYEA93DRWri+a1G8SDEKP51LxCW6CvlPpNx5aqc+NxT2N7cM2fiagglaacZXnsL6LSf0ZgtZPKrAlZBLPhqZX1t8i56A1nvTl0hiDrAMKgklSpC0Fi9Uevtx5SfH22xaDQEr++N3Q5gjgmATcuF0MqkEP/S7+Oci9LlD5oYpxWaJlI0CgYA8YxqvoYeHUI9xPXLvrDg6mqAH6L90oQr79rIkyU5yghVGiSYvS8UrK/eNbwSXyzatb8jH5woN+cQmOPW0kSnKzPoRCswZbiEFwwV9C+RuX42OFsFt8v4IiZdAQXv06oyJ4wplPanFyG/pfV7Ew6UiNZHfkzoqeq5UCpiKvXokgQKBgQCF4eEk+Tju0HbTiNVGvlE0fEVBksvpI/H+JyeenhTq6Hxno//VxEHLYaofGNhMfoXagOFz1eAGDODpZXL/CX1F/ikv5CgEfwGveMncJL6srccDCfDn6g0iNHgK3YGL78pgrB7nz/XGoT1kt2Ar2Q+Xm0/pyQY869E1dfo7+NMQMQKBgELP5htsSWJLfLL+jGr0hQvBx5wL+8VjrpQxokXM0q2u01Jii5iUjr54y94ZUKpreLPB0QZk2W6RCYurp3FXlb82lnDNiVmSewefSJmNvxLVwWZ5X1JpQxPns7TkS4YdxZVw+1SSQgogsYGWsRQCOkD1ZGCBV+KBhG6LwktHZEqf";

        return (RSAPrivateKey) redisService.getSet(key, CryptoUtil.rsaPrivFromCertString(keyString));
    }

    public RSAPrivateKey getMlePrivateKey(String instid) throws Exception {
        String key = "IDENTITY_MLE_RSA_PRIVATE_KEY";
        String keyString = "MIIEowIBAAKCAQEAw/d5On8yf0w7SMCaNEhFXd4wHpTk++j0sC6yhYt7X7f7oADfzLrQIAQ5yvV6801GO/HA7eOQH5MhC23xpSF0jMc2DN46YVK+GLsElPGpexq7ITDngHIMaN8FnSDHOT97CDMkl1s6f2Diqi4Fv6yz4bV896M2fyuuw7ByW6w5bTC3I0EEzieSDhAFLnVvvHLTNCElh8Nzl56JK5nKTjjQf6KYFiMPdw61kETNw16WfSzDcD0JMQ6FdWaLE028P3e8S33BCV15ZY8uHipOiTqMuRhZWms+u3H/igUXuMR4h7TAwtWx950PzmOhdgzUAtOYlXgcb76rF2aVK9alojmlFwIDAQABAoIBAEOPU+oI5B3i6CVi0UbpC5EbArfoWTFH1OPQlZdYyQXs5gD5jC6G0S3YhxwjmWm74FTWOc9JvuxDywantsFZUVcfbtDs5G+dGRBpmWDIF8frJUyCEa3Eo+abzNGo9UYVTpM7HohX2UkE2+AT0cONBM7xxM2TJTyekLfCYwqby4iKwcQiDXL+TGDK2vMsuJwzVNJ1Xa+BzoME3yiF45Aa8pXitrZbnO+DBhkMMvBx7Xo9hL0dgQzQow9yDQg8+9Zad4FugrJz/bwnntl7Iq+4RsySJl0kuDpcp4clH5r7911fbI+y7aTvCQTZuy9glWEuggiAcz6JYOmK8Em6G/e8NLECgYEAyr7VcGwo0v3HyZyXrHljjUHRcxr0omtuVaKZHv7+RDQkNHA4IxF5lgw0ysmt1WaW/E7FIwpOwRlrBCinoGejMxQ1Rbq3c8D59z1rb9xY5TF3yRRZCD3ZVN+utnZMQSJupEvOllkq2gx0e/o7nxZfV6Ra7S6AY7xWpUINtZZOgTMCgYEA93DRWri+a1G8SDEKP51LxCW6CvlPpNx5aqc+NxT2N7cM2fiagglaacZXnsL6LSf0ZgtZPKrAlZBLPhqZX1t8i56A1nvTl0hiDrAMKgklSpC0Fi9Uevtx5SfH22xaDQEr++N3Q5gjgmATcuF0MqkEP/S7+Oci9LlD5oYpxWaJlI0CgYA8YxqvoYeHUI9xPXLvrDg6mqAH6L90oQr79rIkyU5yghVGiSYvS8UrK/eNbwSXyzatb8jH5woN+cQmOPW0kSnKzPoRCswZbiEFwwV9C+RuX42OFsFt8v4IiZdAQXv06oyJ4wplPanFyG/pfV7Ew6UiNZHfkzoqeq5UCpiKvXokgQKBgQCF4eEk+Tju0HbTiNVGvlE0fEVBksvpI/H+JyeenhTq6Hxno//VxEHLYaofGNhMfoXagOFz1eAGDODpZXL/CX1F/ikv5CgEfwGveMncJL6srccDCfDn6g0iNHgK3YGL78pgrB7nz/XGoT1kt2Ar2Q+Xm0/pyQY869E1dfo7+NMQMQKBgELP5htsSWJLfLL+jGr0hQvBx5wL+8VjrpQxokXM0q2u01Jii5iUjr54y94ZUKpreLPB0QZk2W6RCYurp3FXlb82lnDNiVmSewefSJmNvxLVwWZ5X1JpQxPns7TkS4YdxZVw+1SSQgogsYGWsRQCOkD1ZGCBV+KBhG6LwktHZEqf";

        return (RSAPrivateKey) redisService.getSet(key, CryptoUtil.rsaPrivFromCertString(keyString));
    }

    public RSAPrivateKey getMlePrivateKey() throws Exception {

        FuseModel keystore = fuseService.fetchKeystore(ConstantUtil.IDENTITY_MLE_RSA_PRIVATE_KEY);
        return (RSAPrivateKey) CryptoUtil.rsaPrivFromME(keystore.getKeyDataUnderParent(), keystore.getKeyData());
    }

    public RSAPublicKey getMlePubKey(String keyid) throws Exception {
        Object pubKeyObj = redisService.get(keyid);
        if (pubKeyObj == null) {
            FuseModel keystore = fuseService.fetchKeystore(keyid);
            pubKeyObj = (RSAPublicKey) CryptoUtil.rsaPubFromME(keystore.getKeyDataUnderParent(), keystore.getKeyData());
            redisService.set(keyid, pubKeyObj);
        }
        return (RSAPublicKey) pubKeyObj;
    }

}
