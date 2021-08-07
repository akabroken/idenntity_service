/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author ndegel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GenInvoiceHash {

    @NotNull(message = "msn cannot be null.")
    private String msn;

    @NotNull(message = "transaction date be null.")
    private String transactionDate;

    private String invoiceNumber;

}
