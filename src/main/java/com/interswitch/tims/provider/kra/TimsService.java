/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.provider.kra;

import com.interswitch.tims.model.BaseModel;
import com.interswitch.tims.model.ProviderData;
import com.interswitch.tims.service.HttpService;
import com.interswitch.tims.util.ConstantUtil;
import com.interswitch.tims.util.CryptoUtil;
import com.interswitch.tims.util.SystemUtil;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author ndegel
 */
@Service
public class TimsService {

    @Value("${tims.host}")
    private String host;

    @Autowired
    HttpService httpService;

    private final static Logger logger = LoggerFactory.getLogger(TimsService.class);

    public BaseModel postInvoice(BaseModel invoice, ArrayList<BaseModel> items) throws Exception {

        String resource = TimsConstantUtil.INVOICE_RS = "MSN=" + invoice.getMsn() + "&MTYP=" + invoice.getMsnType();

        TimsModel batchHeader = new TimsModel();
        batchHeader.setDateOfTransaction(resource);
        batchHeader.setDateOfTransaction(invoice.getTransactioDate());
        batchHeader.setNumberOfLastInvoiceSent(invoice.getLastInvoiceNumber());
        batchHeader.setPINOfSupplier(invoice.getPin());
        batchHeader.setMiddlewareSerialNumber(invoice.getMsn());
        batchHeader.setNumberOfInvoiceRecords("1");

        ArrayList<TimsModel> invoiceM = new ArrayList<>();

        TimsModel invoiceDet = new TimsModel();
        batchHeader.setDateOfTransaction(resource);
        batchHeader.setDateOfTransaction(invoice.getTransactioDate());
        batchHeader.setNumberOfLastInvoiceSent(invoice.getLastInvoiceNumber());
        batchHeader.setPINOfSupplier(invoice.getPin());
        batchHeader.setMiddlewareSerialNumber(invoice.getMsn());
        batchHeader.setNumberOfInvoiceRecords("1");
        invoiceM.add(invoiceDet);

        TimsModel batchDetails = new TimsModel();
        batchDetails.setINVOICE(invoiceM);

        TimsModel request = new TimsModel();
        request.setHASH(genInvoiceHash(invoice));
        request.setBATCHHEADER(batchHeader);
        request.setBATCHDETAILS(batchDetails);

        TimsModel timsModel = new TimsModel();
        timsModel.setREQUEST(request);

        TimsModel response = postTims(ConstantUtil.HTTP_POST, resource, SystemUtil.marshallJson(timsModel));

        return invoice;
    }

    public BaseModel postEod(BaseModel eod) throws Exception {

        return eod;

    }

    public String genInvoiceHash(BaseModel invoice) throws InvalidKeySpecException, Exception {
        String signature = invoice.getMsnType() + invoice.getTransactioDate() + invoice.getLastInvoiceNumber() + invoice.getPin() + invoice.getMsn();

        logger.info(signature);
        RSAPublicKey pubKey = (RSAPublicKey) CryptoUtil.rsaPubFromString(invoice.getRsaPubKey());

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashSignature = md.digest(signature.getBytes(StandardCharsets.UTF_8));
        String sigHex = new String(Hex.encode(hashSignature));
    
        return new String(Hex.encode(CryptoUtil.encryptRSA(pubKey, sigHex.getBytes(StandardCharsets.UTF_8), ConstantUtil.RSA_ECB_OAEPWithSHA_256AndMGF1Padding)));
    }

    private TimsModel postTims(String httpMethod, String resource, String payload) throws Exception {
        String url = this.host + resource;
        ProviderData providerData = new ProviderData();
        providerData.setHttpMethod(httpMethod);
        providerData.setUrl(url);
        providerData.setIs2waySSL(false);
        providerData.setRequest(payload);

        addHeaders(providerData);
        httpService.send(providerData);

        TimsModel timsResponse = (TimsModel) SystemUtil.unmarshallJson(providerData.getResponse(), TimsModel.class);

        return timsResponse;
    }

    private void addHeaders(ProviderData providerData) throws Exception {

        Header[] headers = {
            new BasicHeader("Content-type", "application/json")};

        providerData.setOutheaders(headers);
    }
}
