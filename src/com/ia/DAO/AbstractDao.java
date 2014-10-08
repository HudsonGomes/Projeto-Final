/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ia.DAO;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author mg
 */
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.ia.hibernate.HibernateFactory;

import com.ia.DAO.AbstractDao;
import com.ia.hibernate.HibernateUtil;
import org.hibernate.Transaction;

/**
 * @author mg
 */
public abstract class AbstractDao<T> {

    protected Session session;
    protected Transaction tx;
    private String banco;

    public static final String CSW = "csw";

    /**
     * @throws DataAccessLayerException
     */
    public AbstractDao() throws DaoException {
        HibernateFactory.buildIfNeeded();
    }
    
    /**
     * @throws DaoException
     */
    public AbstractDao(String banco) throws DaoException {
        this.banco = banco;
        HibernateFactory.buildIfNeeded(banco);
    }


    /**
     * @param obj
     * @throws DaoException
     */
    public void saveOrUpdate(T obj) throws DaoException {
        try {
            startOperation();
            session.saveOrUpdate(obj);
            tx.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            HibernateFactory.close(session);
        }
    }

    /**
     * @param obj
     * @throws DaoException
     */
    protected void delete(T obj) throws DaoException {
        try {
            startOperation();
            session.delete(obj);
            tx.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            HibernateFactory.close(session);
        }
    }

    /**
     * @param clazz
     * @param id
     * @throws DaoException
     */
    protected void delete(Class<T> clazz, Integer id) throws DaoException {
        Object obj = null;
        try {
            startOperation();
            obj = session.load(clazz, id);
            session.delete(obj);
            tx.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            HibernateFactory.close(session);
        }
    }

    /**
     * @param clazz
     * @param id
     * @return
     * @throws DaoException
     */
    protected Object find(Class<T> clazz, Integer id) throws DaoException {
        Object obj = null;
        try {
            startOperation();
            obj = session.load(clazz, id);
            tx.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            //HibernateFactory.close(session);
            //if (fachada != null) {
            //    fachada.addSessaoHibernate(session);
            //}
        }
        return obj;
    }

    /**
     * @param clazz
     * @return
     * @throws DaoException
     */
    @SuppressWarnings("unchecked")
    protected List<T> findAll(Class<T> clazz) throws DaoException {
        List<T> objects = null;
        try {
            startOperation();
            Query query = session.createQuery("from " + clazz.getName());
            objects = query.list();
            tx.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            HibernateFactory.close(session);
        }
        return objects;
    }

    /**
     * @throws HibernateException
     */
    protected void startOperation() throws HibernateException,DaoException {
        session = HibernateFactory.openSession(banco);
        tx = (Transaction) session.beginTransaction();
    }

    /**
     * @param e
     * @throws DaoException
     */
    protected void handleException(HibernateException e) throws DaoException {
        HibernateFactory.rollback(tx);
        throw new DaoException(e);
    }
}
