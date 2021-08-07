/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.service;

import com.interswitch.tims.dto.SystemResponse;
import com.interswitch.tims.util.ResponseUtil;
import org.springframework.stereotype.Service;

/**
 *
 * @author ndegel
 */
@Service
public class ApplicationService {

    public SystemResponse getStatus() {
        SystemResponse systemResponse = new SystemResponse();
        systemResponse.setCode(ResponseUtil.SUCCESS_CODE_0);

        return systemResponse;
    }
}
