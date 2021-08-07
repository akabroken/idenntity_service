/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

/**
 *
 * @author ndegel
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FuseModel {

    private String clientId;
    private String clientName;
    private String clientSecret;
    private String clientSecretClear;
    private String code;

    private int keystoreId;
    private String keyAlias;
    private String keyData;
    private String keyDataUnderParent;
    private String keyCheckDigits;
    private String description;
    private String doExchange;
    private String keyExchangeStatus;

}
