/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.dao;

import com.interswitch.tims.model.FuseModel;
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
public class FuseDao extends FuseBaseDao<FuseModel> {

    @Autowired
    @Qualifier("fuseDataSource")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
    }
    
    public FuseModel fetchClient(String clientid) throws DataAccessException {
        FuseModel client = new FuseModel();
        client.setClientId(clientid);
        this.psp_Find = new SimpleJdbcCall(jdbcTemplate).withProcedureName("psp_retrieve_client_by_client_id").returningResultSet("object", BeanPropertyRowMapper.newInstance(FuseModel.class));;
        return find(client);
    }

    public FuseModel fetchKeystore(String keyid) throws DataAccessException {
        FuseModel keystore = new FuseModel();
        keystore.setKeyAlias(keyid);
        this.psp_Find = new SimpleJdbcCall(jdbcTemplate).withProcedureName("psp_retrieve_key_by_key_alias").returningResultSet("object", BeanPropertyRowMapper.newInstance(FuseModel.class));;
        return find(keystore);
    }

}
