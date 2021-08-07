/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.service;

import com.interswitch.tims.dao.TerminalDao;
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
public class TerminalService {

    @Autowired
    TerminalDao terminalDao;

    public SystemResponse getDetails(String msn) {
        SystemResponse systemResponse = new SystemResponse();

        BaseModel terminal = terminalDao.fetchTerminal(msn);
        systemResponse.setCode(terminal != null ? ResponseUtil.SUCCESS_CODE_0 : ResponseUtil.ENTITY_MISSING_CODE_1);
        if (systemResponse.getCode().equals(ResponseUtil.SUCCESS_CODE_0)) {
            systemResponse.setItem(terminal);
        }
        return systemResponse;
    }
}
