/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.dao;

import com.interswitch.tims.model.BaseModel;
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
public class TraderDao extends TimsBaseDao<BaseModel> {
    
    @Autowired
    @Qualifier("timsDataSource")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
    }
    
    public BaseModel fetchTrader(String pin) throws DataAccessException {
        BaseModel trader = new BaseModel();
        trader.setPin(pin);
        this.psp_Find = new SimpleJdbcCall(jdbcTemplate).withProcedureName("psp_fetch_trader_bypin").returningResultSet("object", BeanPropertyRowMapper.newInstance(BaseModel.class));;
        return find(trader);
    }
    
}
