package org.cscb525;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.CompanyDao;
import org.cscb525.dto.CreateCompanyDto;

public class Main {
    public static void main(String[] args) {
        SessionFactoryUtil.getSessionFactory().openSession();

    }
}