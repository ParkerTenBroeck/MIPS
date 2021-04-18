package org.parker.mips.assembler.util.linking;

public interface LinkType {

        long link(long sourceAddr, long destAddr);

        LinkType RELATIVE_WORD = (s, d) -> (d >> 2) - ((s >> 2) + 1);
        LinkType ABSOLUTE_BYTE = (s, d) -> d;
}
