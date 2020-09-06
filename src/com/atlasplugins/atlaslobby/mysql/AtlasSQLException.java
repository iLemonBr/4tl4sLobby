package com.atlasplugins.atlaslobby.mysql;

public class AtlasSQLException extends RuntimeException {

	private static final long serialVersionUID = -1746878370430655863L;

	public AtlasSQLException() {
		super();
	}

	public AtlasSQLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AtlasSQLException(String message, Throwable cause) {
		super(message, cause);
	}

	public AtlasSQLException(String message) {
		super(message);
	}

	public AtlasSQLException(Throwable cause) {
		super(cause);
	}

}
