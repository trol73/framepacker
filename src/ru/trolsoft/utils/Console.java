package ru.trolsoft.utils;

import org.fusesource.jansi.AnsiConsole;

/**
 * 
 * Version 1.01
 * 
 * @author trol
 *
 */
public class Console {
	
	public enum Color {
		Black(30),
		Red(31),
		Green(32),
		Yellow(33),
		Blue(34),
		Magenta(35),
		Cyan(36),
		White(37);
		
		private final int value;
		
		Color(int value) {
			this.value = value;
		}
	}
	
	private static boolean inited = false;
	
	/**
	 * 
	 */
	public Console() {
		if ( !inited ) {
			inited = true;
			AnsiConsole.systemInstall();
		}
	}
	
	
	/**
	 * 
	 * @param code
	 */
	private static void esc(int code) {
		System.out.print("\33[" + code + "m");
	}
	
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public Console out(String s) {
		System.out.print(s);
		return this;
	}
	
	
	/**
	 * 
	 * @param color
	 * @param bright
	 * @return
	 */
	public Console color(Color color, boolean bright) {
		esc(color.value);
		esc(bright ? 1 : 22);
		return this;
	}
	
	/**
	 * 
	 * @param color
	 * @return
	 */
	public Console color(Color color) {
		esc(color.value);
		return this;
	}
	
	/**
	 * 
	 * @param color
	 * @return
	 */
	public Console bckg(Color color) {
		esc(color.value + 10);
		return this;
	}
	
	/**
	 * 
	 * @return
	 */
	public Console reset() {
		esc(0);
		esc(39);
		esc(49);
		return this;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Console nl() {
		System.out.println();
		return this;
	}
	
	
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public Console error(String msg) {
		color(Color.Red, true).out(msg).reset().nl();
		return this;
	}
	
	/**
	 * 
	 * @param msg
	 * @param arg
	 * @return
	 */
	public Console error(String msg, String arg) {
		color(Color.Red, true).out(msg).color(Color.Cyan, true).out(arg).reset().nl();
		return this;
	}
	
	/**
	 * 
	 * @param msg
	 * @param arg
	 * @return
	 */
	public Console warning(String msg, String arg) {
		color(Color.Green, true).out(msg).color(Color.Cyan, true).out(arg).reset().nl();
		return this;
	}
	
	/**
	 * 
	 * @param msg
	 * @param arg
	 * @return
	 */
	public Console error(String msg, int arg) {
		return error(msg, String.valueOf(arg));
	}

	/**
	 * 
	 * @param msg
	 * @param val
	 * @return
	 */
	public Console info(String msg, String val) {
		color(Color.White, false).out(msg).color(Color.White, true).out(val).reset();
		return this;
	}
	
	/**
	 * 
	 * @param msg
	 * @param val
	 * @return
	 */
	public Console info(String msg, int val) {
		return info(msg, String.valueOf(val));
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public Console info(String msg) {
		return info(msg, "");
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/*
	public static final String RESET = "\033[0m";
	public static final String BRIGHT = "\033[1m";
	public static final String NOBRIGHT = "\033[22m";

	public static final String C_BLACK = "\033[30m";
	public static final String C_RED = "\033[31m";
	public static final String C_GREEN = "\033[32m";
	public static final String C_YELLOW = "\033[33m";
	public static final String C_BLUE = "\033[34m";
	public static final String C_MAGENTA = "\033[35m";
	public static final String C_CYAN = "\033[36m";
	public static final String C_WHITE = "\033[37m";

	public static final String C_RESET = "\033[39m";
	

	public static final String B_BLACK = "\033[40m";
	public static final String B_RED = "\033[41m";
	public static final String B_GREEN = "\033[42m";
	public static final String B_YELLOW = "\033[43m";
	public static final String B_BLUE = "\033[44m";
	public static final String B_MAGENTA = "\033[45m";
	public static final String B_CYAN = "\033[46m";
	public static final String B_WHITE = "\033[47m";
	public static final String B_RESET = "\033[49m";
*/
}
