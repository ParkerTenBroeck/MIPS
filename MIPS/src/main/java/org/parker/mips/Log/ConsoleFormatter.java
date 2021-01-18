package org.parker.mips.log;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends java.util.logging.Formatter {

    public ConsoleFormatter(){

    }

    @Override
    public String format(LogRecord record) {
        String log = "";
        log += record.getSourceClassName() + " " + record.getSourceMethodName() + "\n";
        log += record.getMessage();
        if(record.getThrown() != null){
            StringWriter sw = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(sw));
            log +=  "\n" + sw.toString();
        }
        log += "\n";
        return log;
    }

}
