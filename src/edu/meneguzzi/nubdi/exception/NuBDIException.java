/**
 * 
 */
package edu.meneguzzi.nubdi.exception;

/**
 * @author meneguzzi
 *
 */
public class NuBDIException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public NuBDIException(String msg) {
		super(msg);
	}
	
	public NuBDIException(String msg, Throwable e) {
		super(msg,e);
	}

}
