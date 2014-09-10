/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ia.DAO;

/**
 *
 * @author mg
 */
/**
 * Represents Exceptions thrown by the Data Access Layer.
 * @author Taciano Morais Silva
 */
public class DaoException extends Exception {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 7691613091025100703L;

    /**
     *
     */
    public DaoException() {
    }

    /**
     * @param message
     */
    public DaoException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public DaoException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}

