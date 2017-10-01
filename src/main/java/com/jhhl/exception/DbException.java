package com.jhhl.exception;


public class DbException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DbException() {

    }

    public DbException(String info) {
        super(info);
    }

    public DbException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public DbException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
