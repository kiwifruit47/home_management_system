package org.cscb525;

import org.cscb525.config.SessionFactoryUtil;

public class Main {
    public static void main(String[] args) {
        SessionFactoryUtil.getSessionFactory().openSession();

    }
}