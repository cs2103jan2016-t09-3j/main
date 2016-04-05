package tnote.log;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class TNoteLogger {
	
	private static Logger logger;
	private static FileHandler fileTxt;
	private static SimpleFormatter formatterTxt;
	
	
	public static void setUp() throws Exception {
		fileTxt = new FileHandler("TNoteLog.log", true);
		formatterTxt = new SimpleFormatter();
		logger = Logger.getGlobal();
		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);
	}
}
