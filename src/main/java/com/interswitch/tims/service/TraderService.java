/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.service;

import com.interswitch.tims.dao.TraderDao;
import com.interswitch.tims.dto.SystemResponse;
import com.interswitch.tims.model.BaseModel;
import com.interswitch.tims.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ndegel
 */
@Service
public class TraderService {

    @Autowired
    TraderDao traderDao;

    public SystemResponse getTrader(String pin) {
        SystemResponse systemResponse = new SystemResponse();
        
        BaseModel trader = traderDao.fetchTrader(pin);
        systemResponse.setCode(trader != null ? ResponseUtil.SUCCESS_CODE_0 : ResponseUtil.ENTITY_MISSING_CODE_1);
        if (systemResponse.getCode().equals(ResponseUtil.SUCCESS_CODE_0)) {
            systemResponse.setItem(trader);
        }

        return systemResponse;
    }
}
