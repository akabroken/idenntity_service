/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.api.v1;

import com.interswitch.tims.dto.SystemResponse;
import com.interswitch.tims.model.SystemExchange;
import com.interswitch.tims.service.*;
import com.interswitch.tims.util.*;
import javax.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author ndegel
 */
@RestController
@RequestMapping("/api/v1/service/")
public class ServiceApi {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @Autowired
    ApplicationService applicationService;

    @RequestMapping(value = "status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SystemResponse getStatus() {
        SystemExchange systemExchange = (SystemExchange) request.getAttribute(ConstantUtil.SYSTEM_EXCHANGE);
        return SystemUtil.setResponse(request, response, systemExchange, applicationService.getStatus());
    }
}
