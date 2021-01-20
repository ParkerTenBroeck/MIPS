package org.parker.mips.processor;

import java.util.logging.Level;

public class RunTimeLevel extends Level {

    public static final RunTimeLevel RUN_TIME_MESSAGE = new RunTimeLevel("RUN_TIME_MESSAGE", 810);
    public static final RunTimeLevel RUN_TIME_WARNING = new RunTimeLevel("RUN_TIME_WARNING", 820);
    public static final RunTimeLevel RUN_TIME_ERROR = new RunTimeLevel("RUN_TIME_ERROR", 830);

    private RunTimeLevel(String name, int level) {
        super(name, level);
    }

}
