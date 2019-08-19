package com.javamentor.jmp_project.dao;

import com.javamentor.jmp_project.config.DbHelper;

public class HibernateDaoFactory implements DaoFactory {

    @Override
    public UserDao getUserDao() {
        return new UserDaoHibernateImpl(DbHelper.getConfiguration());
    }

}
