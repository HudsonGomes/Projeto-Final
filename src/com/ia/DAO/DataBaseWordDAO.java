package com.ia.DAO;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.ia.models.DataBaseWord;
import com.ia.DAO.AbstractDao;
import com.ia.DAO.DaoException;

public class DataBaseWordDAO extends AbstractDao<DataBaseWord>{


    public DataBaseWordDAO() throws DaoException {
        
    }
    
    public void saveOrUpdate(DataBaseWord obj) throws DaoException {
        super.saveOrUpdate(obj);
    }

    /**
    * @param obj
    * @throws DaoException
    */
    public void delete(DataBaseWord obj) throws DaoException {
        super.delete(obj);
    }

    /**
    * @param clazz
    * @param id
    * @throws DaoException
    */
    public void delete(Class<DataBaseWord> clazz, Integer id) throws DaoException {
        super.delete(clazz, id);
    }

    /**
    * @param clazz
    * @param id
    * @return
    * @throws DaoException
    */
    public Object find(Class<DataBaseWord> clazz, Integer id) throws DaoException {
        return super.find(clazz, id);
    }

    /**
    * @param clazz
    * @return
    * @throws DaoException
    */
    @SuppressWarnings("unchecked")
    public List<DataBaseWord> findAll(Class<DataBaseWord> clazz) throws DaoException {
        return super.findAll(clazz);
    }
    
    @SuppressWarnings("unchecked")
	public DataBaseWord getSameString(String string) throws Exception{
		
		DataBaseWord obj = null;
        try {
        	startOperation();
        	String queryStr = "select c from " 
    			+ DataBaseWord.class.getName() + " c where c.nome = :string ";
    		
    		
    		Query query = session.createQuery(queryStr);
    		
    		query.setString("string", string);
    		obj = (DataBaseWord)query.list().get(0);
    		tx.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
         
        }
        return obj;
	}
}

