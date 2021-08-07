/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.dao;

import com.interswitch.tims.model.BaseModel;
import java.util.ArrayList;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ndegel
 */
@Repository
public abstract class TimsBaseDao<T extends BaseModel> {

    protected JdbcTemplate jdbcTemplate;
    protected SimpleJdbcCall psp_Insert, psp_Update, psp_Delete, psp_Find, psp_FindAll, psp_Run;

    public abstract void setDataSource(DataSource datasource);

    public T insert(T model) throws DataAccessException {

        SqlParameterSource in = new BeanPropertySqlParameterSource(model);
        Map<String, Object> m = psp_Insert.execute(in);

        String returnValue = String.valueOf(m.get("return_value"));
        model.setCode(returnValue);
        return model;
    }

    public T update(T model) throws DataAccessException {

        SqlParameterSource in = new BeanPropertySqlParameterSource(model);
        Map<String, Object> m = psp_Update.execute(in);

        String returnValue = String.valueOf(m.get("return_value"));
        model.setCode(returnValue);
        return model;
    }

    public T delete(T model) throws DataAccessException {

        SqlParameterSource in = new BeanPropertySqlParameterSource(model);
        Map<String, Object> m = psp_Delete.execute(in);

        String returnValue = String.valueOf(m.get("return_value"));
        model.setCode(returnValue);
        return model;
    }

    public T run(T model) throws DataAccessException {

        SqlParameterSource in = new BeanPropertySqlParameterSource(model);

        Map<String, Object> m = psp_Run.execute(in);
        String returnValue = String.valueOf(m.get("code"));

        if (((ArrayList<T>) m.get("object") != null)) {
            model = ((ArrayList<T>) m.get("object")).get(0);
        }
        model.setCode(returnValue);
        return model;
    }

    public T find(T model) throws DataAccessException {
        SqlParameterSource in = new BeanPropertySqlParameterSource(model);

        Map<String, Object> m = psp_Find.execute(in);

        model = ((ArrayList<T>) m.get("object")).size() > 0 ? ((ArrayList<T>) m.get("object")).get(0) : null;
        return model;
    }

    public ArrayList<T> findAll(T model) throws DataAccessException {
        SqlParameterSource in = new BeanPropertySqlParameterSource(model);

        Map<String, Object> m = psp_FindAll.execute(in);

        if (((ArrayList<T>) m.get("object") != null)) {
            return ((ArrayList<T>) m.get("object"));
        }

        return null;
    }

}
