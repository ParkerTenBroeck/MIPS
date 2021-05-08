/*
 *    Copyright 2021 ParkerTenBroeck
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.parker.mips.assembler.exception;

public class FieldOverflow extends RuntimeException{
    public final int field;
    public final long value;
    public final long max;
    public final long min;

    public FieldOverflow(String message, int field, long value, long max, long min){
        super(message);
        this.field = field;
        this.value = value;
        this.max = max;
        this.min = min;
    }

    public FieldOverflow(int field, long value, long max, long min){
        this("Field: " + field + " is too large/small to fit into its allocated space found: " + value + " max: " + max + " min: " + min, field, value, max, min);
    }
}
