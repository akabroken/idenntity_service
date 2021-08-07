/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.api.v1;

import com.interswitch.tims.dto.GenInvoiceHash;
import com.interswitch.tims.dto.PostEod;
import com.interswitch.tims.dto.PostInvoice;
import com.interswitch.tims.dto.SystemResponse;
import com.interswitch.tims.model.SystemExchange;
import com.interswitch.tims.service.*;
import com.interswitch.tims.util.*;
import javax.servlet.http.*;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author ndegel
 */
@RestController
@RequestMapping("/api/v1/invoices/")
public class InvoiceApi {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @Autowired
    InvoiceService invoiceService;

    @RequestMapping(value = "invoice/number", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SystemResponse getInvoiceNumber(@RequestParam String msn) {
        SystemExchange systemExchange = (SystemExchange) request.getAttribute(ConstantUtil.SYSTEM_EXCHANGE);
        return SystemUtil.setResponse(request, response, systemExchange, invoiceService.getInvoiceNumber(msn));
    }

    @RequestMapping(value = "invoice/hash", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public SystemResponse genInvoiceHash(@RequestBody @Valid GenInvoiceHash genInvoiceHash) throws Exception {
        SystemExchange systemExchange = (SystemExchange) request.getAttribute(ConstantUtil.SYSTEM_EXCHANGE);
        return SystemUtil.setResponse(request, response, systemExchange, invoiceService.genInvoiceHash(genInvoiceHash));
    }

    @RequestMapping(value = "invoice", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public SystemResponse postInvoice(@RequestBody @Valid PostInvoice postInvoice) throws Exception {
        SystemExchange systemExchange = (SystemExchange) request.getAttribute(ConstantUtil.SYSTEM_EXCHANGE);
        return SystemUtil.setResponse(request, response, systemExchange, invoiceService.postInvoice(postInvoice));
    }

    @RequestMapping(value = "eod", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public SystemResponse postEod(@RequestBody @Valid PostEod postEod) throws Exception {
        SystemExchange systemExchange = (SystemExchange) request.getAttribute(ConstantUtil.SYSTEM_EXCHANGE);
        return SystemUtil.setResponse(request, response, systemExchange, invoiceService.postEod(postEod));
    }
}
