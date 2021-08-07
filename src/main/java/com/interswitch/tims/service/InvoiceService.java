/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.service;

import com.interswitch.tims.dao.InvoiceDao;
import com.interswitch.tims.dao.TerminalDao;
import com.interswitch.tims.dto.GenInvoiceHash;
import com.interswitch.tims.dto.PostEod;
import com.interswitch.tims.dto.PostInvoice;
import com.interswitch.tims.dto.SystemResponse;
import com.interswitch.tims.model.BaseModel;
import com.interswitch.tims.provider.kra.TimsService;
import com.interswitch.tims.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ndegel
 */
@Service
public class InvoiceService {

    @Autowired
    InvoiceDao invoiceDao;

    @Autowired
    TerminalDao terminalDao;

    @Autowired
    TimsService timsService;

    public SystemResponse getInvoiceNumber(String msn) {
        SystemResponse systemResponse = new SystemResponse();

        BaseModel invoice = invoiceDao.fetchInvoiceNumber(msn);
        systemResponse.setCode(invoice != null ? ResponseUtil.SUCCESS_CODE_0 : ResponseUtil.ENTITY_MISSING_CODE_1);
        if (systemResponse.getCode().equals(ResponseUtil.SUCCESS_CODE_0)) {
            systemResponse.setItem(invoice);
        }
        return systemResponse;
    }

    public SystemResponse genInvoiceHash(GenInvoiceHash genInvoiceHash) throws Exception {
        SystemResponse systemResponse = new SystemResponse();

        BaseModel terminal = terminalDao.fetchTerminal(genInvoiceHash.getMsn());
        systemResponse.setCode(terminal != null ? ResponseUtil.SUCCESS_CODE_0 : ResponseUtil.ENTITY_MISSING_CODE_1);
        if (systemResponse.getCode().equals(ResponseUtil.SUCCESS_CODE_0)) {
            terminal.setLastInvoiceNumber(genInvoiceHash.getInvoiceNumber());
            terminal.setTransactioDate(genInvoiceHash.getTransactionDate());
            systemResponse.setHash(timsService.genInvoiceHash(terminal));
        }
        return systemResponse;
    }

    public SystemResponse postInvoice(PostInvoice postInvoice) throws Exception {
        SystemResponse systemResponse = new SystemResponse();

        BaseModel invoice = invoiceDao.insertInvoice(postInvoice);
        systemResponse.setCode(invoice.getCode());
        systemResponse.setItem(invoice);
        return systemResponse;
    }

    public SystemResponse postEod(PostEod postEod) throws Exception {
        SystemResponse systemResponse = new SystemResponse();

        BaseModel invoice = invoiceDao.insertEod(postEod);
        systemResponse.setCode(invoice.getCode());
        if (systemResponse.getCode().equals(ResponseUtil.SUCCESS_CODE_0)) {
            systemResponse.setItem(invoice);
        }
        return systemResponse;
    }
}
