package org.parker.mips.compiler;

import org.parker.mips.compiler.data.UserLine;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class CompilationLogger extends  Logger{
    public CompilationLogger(String name){
        super(name, null);
        LogManager.getLogManager().addLogger(this);
    }

    public void log(Level level, String message, UserLine ul){
        super.log(level, "On line: " + ul + " " + message);
    }

    public void log(Level level, String message, UserLine ul, Throwable cause){
        super.log(level, "On line: " + ul + " " + message, cause);
    }

    public void log(Level level, CompilationException cause){
        super.log(level, "", cause);
    }
}