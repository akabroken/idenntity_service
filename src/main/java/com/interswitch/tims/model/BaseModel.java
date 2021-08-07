/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.model;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import lombok.Data;

/**
 *
 * @author ndegel
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseModel implements Serializable {

    private String code;
    private String message;
    private ArrayList<BaseModel> errors;

    private String msn;
    private String lastInvoiceNumber;
    private String nextInvoiceNumber;
    private String encData;

    private String key;
    private String keyID;
    private transient String rsaPubKey;
    private String type;
    private String msnType;

    private String hash;
    private String pin;
    private String transactioDate;

    private String name;
    private String location;
    private String coordinates;
    private String businessType;

    private Double discount;
    private String invoiceType;
    private String invoiceCategory;
    private Double totalInvoiceAmount;
    private Double totalTaxableAmount;
    private String exemptionNumber;
    private String QRCode;

    private String hSCode;
    private String category;
    private String hSDesc;
    private Integer quantity;
    
    private Double unitPrice;
    private Double itemAmount;
    private String taxRate;
    private Double taxAmount;
}
