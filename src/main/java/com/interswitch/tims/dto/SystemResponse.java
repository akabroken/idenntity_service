/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.interswitch.tims.model.BaseModel;

/**
 *
 * @author ndegel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemResponse extends BaseModel {

    private BaseModel item;

    public BaseModel getItem() {
        return item;
    }

    public void setItem(BaseModel item) {
        this.item = item;
    }

}
