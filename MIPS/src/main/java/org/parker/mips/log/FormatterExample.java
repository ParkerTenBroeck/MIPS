package org.parker.mips.log;

import java.io.IOException;
import java.util.logging.*;
import java.util.logging.Formatter;

public class FormatterExample {

	private static final Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());
	public static void main(String[] args) {

		Handler fileHandler = null;
		Formatter simpleFormatter = null;
		try{
			
			// Creating FileHandler
			fileHandler = new FileHandler("./javacodegeeks.formatter.log");
			
			// Creating SimpleFormatter
			simpleFormatter = new SimpleFormatter();
			
			// Assigning handler to logger
			LOGGER.addHandler(fileHandler);
			
			// Logging message of Level info (this should be publish in the default format i.e. XMLFormat)
			LOGGER.info("Finnest message: Logger with DEFAULT FORMATTER");
			
			// Setting formatter to the handler
			fileHandler.setFormatter(simpleFormatter);
			
			// Setting Level to ALL
			fileHandler.setLevel(Level.ALL);
			LOGGER.setLevel(Level.ALL);
			
			// Logging message of Level finest (this should be publish in the simple format)
			LOGGER.finest("Finnest message: Logger with SIMPLE FORMATTER");
		}catch(IOException exception){
			LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
		}
	}

}
