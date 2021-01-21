package org.parker.mips.assembler;

import java.util.logging.Level;

public class AssemblerLevel extends Level {

    public static final AssemblerLevel ASSEMBLER_MESSAGE = new AssemblerLevel("ASSEMBLER_MESSAGE", 810);
    public static final AssemblerLevel ASSEMBLER_WARNING = new AssemblerLevel("ASSEMBLER_WARNING", 820);
    public static final AssemblerLevel ASSEMBLER_ERROR = new AssemblerLevel("ASSEMBLER_ERROR", 830);

    private AssemblerLevel(String name, int level) {
        super(name, level);
    }

}
