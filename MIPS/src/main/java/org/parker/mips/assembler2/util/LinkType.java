package org.parker.mips.assembler2.util;

public interface LinkType {

        long link(long sourceAddr, long destAddr);

        LinkType RELATIVE_WORD = (s, d) -> (d >> 2) - ((s >> 2) + 1);
        LinkType REGIONAL_WORD = (s, d) -> (d >> 2) - ((s >> 2) & 0xfc000000);
        LinkType ABSOLUTE_BYTE = (s, d) -> d;
}
