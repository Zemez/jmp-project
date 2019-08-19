package com.javamentor.jmp_project.dao;

import com.javamentor.jmp_project.config.DbHelper;

public class JdbcDaoFactory implements DaoFactory {

    @Override
    public UserDao getUserDao() {
        return new UserDaoJdbcImpl(DbHelper.getConnection());
    }

}
