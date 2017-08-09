package exceptions;

public class HaltException extends Exception {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	public HaltException() {
		super();
	}
	
	public HaltException(final String message) {
		super(message);
		
	}
	
	public HaltException(final Throwable cause) {
		super(cause);
	}
	
	public HaltException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
