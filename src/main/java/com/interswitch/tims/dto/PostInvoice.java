/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.interswitch.tims.model.BaseModel;
import java.util.ArrayList;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author ndegel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PostInvoice {

    @NotNull(message = "msn cannot be null.")
    private String msn;

    @NotNull(message = "transaction date be null.")
    private String transactionDate;

    @NotNull(message = "invoice number be null.")
    private String invoiceNumber;

    @NotNull(message = "invoice date be null.")
    private String invoiceDate;

    @NotNull(message = "buyer pin date be null.")
    private String buyerPin;

    private Double discount;
    private String invoiceType;
    private String invoiceCategory;
    private Double totalInvoiceAmount;
    private Double totalTaxableAmount;
    private String exemptionNumber;
    private String QRCode;

    private ArrayList<BaseModel> items;

}
