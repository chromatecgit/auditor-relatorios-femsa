package exceptions;

public class WarningException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	public WarningException() {
		super();
	}
	
	public WarningException(final String message) {
		super(message);
		
	}
	
	public WarningException(final Throwable cause) {
		super(cause);
	}
	
	public WarningException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
