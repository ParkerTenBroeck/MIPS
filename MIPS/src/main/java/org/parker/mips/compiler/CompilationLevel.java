package org.parker.mips.compiler;

import java.util.logging.Level;

public class CompilationLevel extends Level {

    public static final CompilationLevel COMPILATION_MESSAGE = new CompilationLevel("COMPILATION_MESSAGE", 810);
    public static final CompilationLevel COMPILATION_WARNING = new CompilationLevel("COMPILATION_WARNING", 820);
    public static final CompilationLevel COMPILATION_ERROR = new CompilationLevel("COMPILATION_ERROR", 830);

    private CompilationLevel(String name, int level) {
        super(name, level);
    }

}
