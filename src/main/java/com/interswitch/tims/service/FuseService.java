/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.service;

import com.interswitch.tims.dao.FuseDao;
import com.interswitch.tims.model.FuseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ndegel
 */
@Service
public class FuseService {

    @Autowired
    FuseDao fuseDao;

    public FuseModel fetchClient(String clientID) {
        return fuseDao.fetchClient(clientID);
    }

    public FuseModel fetchKeystore(String name) {
        return fuseDao.fetchKeystore(name);
    }

}
