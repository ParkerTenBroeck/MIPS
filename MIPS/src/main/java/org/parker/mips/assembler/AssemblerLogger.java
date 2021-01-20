package org.parker.mips.assembler;

import org.parker.mips.assembler.data.UserLine;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class AssemblerLogger extends  Logger{
    public AssemblerLogger(String name){
        super(name, null);
        LogManager.getLogManager().addLogger(this);
    }

    public void log(Level level, String message, UserLine ul){
        if (!isLoggable(level)) {
            return;
        }
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];

        super.logp(level,
                e.getClassName(),e.getMethodName(),
                "On line: " + ul + " " + message);
    }

    public void log(Level level, String message, UserLine ul, Throwable cause){
        if (!isLoggable(level)) {
            return;
        }
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];

        super.logp(level,
                e.getClassName(),e.getMethodName(),
                "On line: " + ul + " " + message, cause);
    }

    public void log(Level level, AssemblerException cause){
        if (!isLoggable(level)) {
            return;
        }
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];

        super.logp(level,
                e.getClassName(),e.getMethodName(),
                "", cause);
    }
}