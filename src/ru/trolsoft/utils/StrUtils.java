package ru.trolsoft.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Version 1.00
 * 
 * @author trol
 *
 */
public class StrUtils {
	
	/**
	 * Parse 
	 * @param s
	 * @return nul if string value is invalid, else parsed value
	 */
	public static Integer str2int(String s) {
		try {
			if ( s.startsWith("0x") || s.startsWith("0X") ) {
				return Integer.parseInt(s.substring(2), 16);
			}
			return Integer.parseInt(s);
		} catch (Exception e) {
			return null;
		}
	}

	
	/**
	 * 
	 * @param args
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @throws InvalidArgumentException
	 */
	public static Map<String, String> parseArgs(String[] args, int fromIndex, int toIndex) throws InvalidArgumentException {
		Map<String, String> result = new HashMap<String, String>();
		for ( int i = fromIndex; i <= toIndex; i++ ) {
			String arg = args[i];
			int indx = arg.indexOf('='); 
			if ( indx <= 0 ) {
				throw new InvalidArgumentException("invalid argument: " + arg);
			}
			String name = arg.substring(0, indx);
			String value = arg.substring(indx+1);
			result.put(name, value);
		}
		return result;
	}
	
	
	/**
	 * 
	 * @param args
	 * @param fromIndex
	 * @return
	 * @throws InvalidArgumentException
	 */
	public static Map<String, String> parseArgsFrom(String[] args, int fromIndex) throws InvalidArgumentException {
		return parseArgs(args, fromIndex, args.length-1);
	}

	/**
	 * 
	 * @param args
	 * @param toIndex
	 * @return
	 * @throws InvalidArgumentException
	 */
	public static Map<String, String> parseArgsTo(String[] args, int toIndex) throws InvalidArgumentException {
		return parseArgs(args, 0, toIndex);
	}
	
	
	/**
	 * 
	 * @param args
	 * @return
	 * @throws InvalidArgumentException
	 */
	public static Map<String, String> parseArgs(String[] args) throws InvalidArgumentException {
		return parseArgs(args, 0, args.length-1);
	}
	
	/**
	 * 
	 * @param args
	 * @param validKeys
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @throws InvalidArgumentException
	 */
	public static Map<String, String> parseArgs(String[] args, String[] validKeys, int fromIndex, int toIndex) throws InvalidArgumentException {
		Map<String, String> result = parseArgs(args, fromIndex, toIndex);
		for ( String key : result.keySet() ) {
			boolean isValid = false;
			for ( String s : validKeys ) {
				if ( s.equals(key) ) {
					isValid = true;
					break;
				}				
			}
			if ( !isValid ) {
				throw new InvalidArgumentException("invalid option: " + key);
			}
		}
		return result;
	}
	
	
	/**
	 * 
	 * @param args
	 * @param validKeys
	 * @param fromIndex
	 * @return
	 * @throws InvalidArgumentException
	 */
	public static Map<String, String> parseArgsFrom(String[] args, String[] validKeys, int fromIndex) throws InvalidArgumentException {
		return parseArgs(args, validKeys, fromIndex, args.length-1);
	}

	/**
	 * 
	 * @param args
	 * @param validKeys
	 * @param toIndex
	 * @return
	 * @throws InvalidArgumentException
	 */
	public static Map<String, String> parseArgsTo(String[] args, String[] validKeys, int toIndex) throws InvalidArgumentException {
		return parseArgs(args, validKeys, 0, toIndex);
	}
	
	
	/**
	 * 
	 * @param args
	 * @param validKeys
	 * @return
	 * @throws InvalidArgumentException
	 */
	public static Map<String, String> parseArgs(String[] args, String[] validKeys) throws InvalidArgumentException {
		return parseArgs(args, validKeys, 0, args.length-1);
	}


	/**
	 * 
	 * @param args
	 * @param keyName
	 * @param defaultValue
	 * @return
	 * @throws InvalidArgumentException
	 */
	public static int getIntArgValue(Map<String, String> args, String keyName, int defaultValue) throws InvalidArgumentException {
		if ( !args.containsKey(keyName) ) {
			return defaultValue;
		}
		Integer val = str2int(args.get(keyName));
		if ( val == null ) {
			throw new InvalidArgumentException("invalid integer argument: " + keyName+"="+args.get(keyName));
		}
		return val;
	}

}
