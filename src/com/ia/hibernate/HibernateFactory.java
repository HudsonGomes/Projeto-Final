/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ia.hibernate;

/**
 *
 * @author mg
 */
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

import com.ia.DAO.AbstractDao;


public class HibernateFactory {


    /**
     * Fábrica de sessões.
     */
    private static SessionFactory sessionFactory;

    /**
     * Fábrica de sessões.
     */
    private static SessionFactory sessionFactoryCsw;

    /**
     * Constrói uma SessionFactory, se ela ainda for nula.
     * @return A fábrica de sessão do hibernate.
     * @throws HibernateException Caso ocorra erro na configuração e construção
     * da fábrica de sessões.
     */
    public static SessionFactory buildIfNeeded() throws HibernateException {
        if (sessionFactory != null) {
            return sessionFactory;
        }
        try {
            return configureSessionFactory();
        } catch (HibernateException e) {
            throw e;
        }
    }

    /**
     * Constrói uma SessionFactory, se ela ainda for nula.
     * @return A fábrica de sessão do hibernate.
     * @throws HibernateException Caso ocorra erro na configuração e construção
     * da fábrica de sessões.
     */
    public static SessionFactory buildIfNeeded(String banco) throws HibernateException {
        SessionFactory sf = getSessionFactory(banco);
        if (sf != null) {
            return sf;
        }
        try {
            return configureSessionFactory(banco);
        } catch (HibernateException e) {
            throw e;
        }
    }

    private static SessionFactory getSessionFactory(String banco) {
        if (AbstractDao.CSW.equals(banco)) {
            return sessionFactoryCsw;
        }
        return sessionFactory;
    }

    /**
     * Carrega as configurações nas anotações e constrói a fábrica de sessão.
     * @return A fábrica de sessão.
     * @throws HibernateException Caso ocorra erro na configuração e construção
     * da fábrica de sessões.
     */
    private static SessionFactory configureSessionFactory()
            throws HibernateException {
        AnnotationConfiguration configuration = new AnnotationConfiguration();
        configuration.configure("hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
        return sessionFactory;
    }

    /**
     * Carrega as configurações nas anotações e constrói a fábrica de sessão.
     * @return A fábrica de sessão.
     * @throws HibernateException Caso ocorra erro na configuração e construção
     * da fábrica de sessões.
     */
    private static SessionFactory configureSessionFactory(String banco)
            throws HibernateException {
        AnnotationConfiguration configuration = new AnnotationConfiguration();
        if (AbstractDao.CSW.equals(banco)) {
            configuration.configure("hibernate.sqlserver.cfg.xml");
            sessionFactoryCsw = configuration.buildSessionFactory();
            return sessionFactoryCsw;
        } else {
            sessionFactory = configuration.buildSessionFactory();
            return sessionFactory;
        }
    }

    /**
     * Abre uma nova sessão. Caso a fábrica ainda não tenha sido iniciada
     * é feita uma chamada ao método {@link HibernateFactory#buildIfNeeded()}.
     * @return Uma nova sessão.
     * @throws HibernateException Caso ocorra uma exceção na construção da
     * fábrica de sessões ou na abertura da nova sessão.
     */
    public static Session openSession(String banco) throws HibernateException {
        buildIfNeeded(banco);
        if (AbstractDao.CSW.equals(banco)) {
            return sessionFactoryCsw.openSession();
        }
        return sessionFactory.openSession();
    }

    /**
     * Fecha a fábrica de sessões, caso o fechamento gere exceção é feito um
     * log de ERROR.
     */
    public static void closeFactory(String banco) {
        if (getSessionFactory(banco) != null) {
            try {
                getSessionFactory(banco).close();
            } catch (HibernateException ignored) {
                System.out.println("Couldn't close SessionFactory");
            }
        }
    }

    /**
     * Fecha a sessão, caso a sessão esteja fechada acrescenta no log um WARN.
     * @param session Uma sessão hibernate.
     */
    public static void close(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (HibernateException ignored) {
                System.out.println("Couldn't close Session");
            }
        }
    }

    /**
     * Executa o rollback da transação.
     * @param tx A transação.
     */
    public static void rollback(Transaction tx) {
        try {
            if (tx != null) {
                tx.rollback();
            }
        } catch (HibernateException ignored) {
            System.out.println("Couldn't rollback Transaction");
        }
    }
}
