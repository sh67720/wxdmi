package com.wxdmi.service;

import com.wxdmi.dao.AdminMapper;
import com.wxdmi.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by vincent on 16/3/20.
 */
@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;
    public Admin login(String username, String password){
        return adminMapper.login(username, password);
    }
}
