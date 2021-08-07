/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.dao;

import com.interswitch.tims.dto.PostEod;
import com.interswitch.tims.dto.PostInvoice;
import com.interswitch.tims.model.BaseModel;
import java.util.ArrayList;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ndegel
 */
@Repository
public class InvoiceDao extends TimsBaseDao<BaseModel> {

    @Autowired
    @Qualifier("timsDataSource")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
    }

    public BaseModel fetchInvoiceNumber(String msn) throws DataAccessException {
        BaseModel terminal = new BaseModel();
        terminal.setMsn(msn);
        this.psp_Find = new SimpleJdbcCall(jdbcTemplate).withProcedureName("psp_fetch_invoicenumber").returningResultSet("object", BeanPropertyRowMapper.newInstance(BaseModel.class));;
        return find(terminal);
    }

    public ArrayList<BaseModel> fetchItems(String msn) throws DataAccessException {
        BaseModel terminal = new BaseModel();
        terminal.setMsn(msn);
        this.psp_FindAll = new SimpleJdbcCall(jdbcTemplate).withProcedureName("psp_fetch_invoice_items").returningResultSet("object", BeanPropertyRowMapper.newInstance(BaseModel.class));;
        return findAll(terminal);
    }

    public BaseModel insertInvoice(PostInvoice postInvoice) throws DataAccessException {
        BaseModel invoice = new BaseModel();

        this.psp_Run = new SimpleJdbcCall(jdbcTemplate).withProcedureName("psp_insert_invoice").returningResultSet("object", BeanPropertyRowMapper.newInstance(BaseModel.class));;
        return run(invoice);
    }

    public BaseModel insertEod(PostEod postEod) throws DataAccessException {
        BaseModel invoice = new BaseModel();

        this.psp_Run = new SimpleJdbcCall(jdbcTemplate).withProcedureName("psp_insert_eod").returningResultSet("object", BeanPropertyRowMapper.newInstance(BaseModel.class));;
        return run(invoice);
    }

    public BaseModel updateInvoice(BaseModel invoice) throws DataAccessException {
        this.psp_Run = new SimpleJdbcCall(jdbcTemplate).withProcedureName("psp_update_invoice").returningResultSet("object", BeanPropertyRowMapper.newInstance(BaseModel.class));;
        return run(invoice);
    }

    public BaseModel updateEod(BaseModel invoice) throws DataAccessException {
        this.psp_Run = new SimpleJdbcCall(jdbcTemplate).withProcedureName("psp_update_eod").returningResultSet("object", BeanPropertyRowMapper.newInstance(BaseModel.class));;
        return run(invoice);
    }
}
