//@@author A0124131B
package tnote.util.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class configures the global logger for TNote to use. It disables the
 * console handler and attaches a file handler
 * 
 * @author A0124131B
 *
 */
public class TNoteLogger {

	private static final String TNOTE_LOG_FILE = "TNoteLog.log";
	private static Logger logger;
	private static FileHandler fileTxt;
	private static SimpleFormatter formatterTxt;
	
	/**
	 * Static method which configures the global logger to allow for its use in TNotes
	 * @throws IOException I/O Error when opening the specified log file
	 */
	public static void setUp() throws IOException {
		LogManager.getLogManager().reset();
		fileTxt = new FileHandler(TNOTE_LOG_FILE, true);
		formatterTxt = new SimpleFormatter();
		logger = Logger.getGlobal();
		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);
	}
}
