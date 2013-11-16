package ru.trolsoft.utils;

/**
 * 
 * Version 1.00
 * 
 * @author trol
 *
 */
public class InvalidArgumentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param message
	 */
	public InvalidArgumentException(String message) {
		super(message);
	}

}
