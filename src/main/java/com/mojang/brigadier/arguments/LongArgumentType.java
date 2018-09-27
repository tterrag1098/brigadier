// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package com.mojang.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Arrays;
import java.util.Collection;

public class LongArgumentType extends SimpleArgumentType<Long> {
    private static final Collection<String> EXAMPLES = Arrays.asList("0", "123", "-123");

    private final long minimum;
    private final long maximum;

    private LongArgumentType(String name, final long minimum, final long maximum) {
        super(name);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public static LongArgumentType longArg(String name) {
        return longArg(name, Long.MIN_VALUE);
    }

    public static LongArgumentType longArg(String name, final long min) {
        return longArg(name, min, Long.MAX_VALUE);
    }

    public static LongArgumentType longArg(String name, final long min, final long max) {
        return new LongArgumentType(name, min, max);
    }

    public long getMinimum() {
        return minimum;
    }

    public long getMaximum() {
        return maximum;
    }

    @Override
    public Long parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final long result = reader.readLong();
        if (result < minimum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.longTooLow().createWithContext(reader, result, minimum);
        }
        if (result > maximum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.longTooHigh().createWithContext(reader, result, maximum);
        }
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof LongArgumentType)) return false;

        final LongArgumentType that = (LongArgumentType) o;
        return maximum == that.maximum && minimum == that.minimum;
    }

    @Override
    public int hashCode() {
        return 31 * Long.hashCode(minimum) + Long.hashCode(maximum);
    }

    @Override
    public String toString() {
        if (minimum == Long.MIN_VALUE && maximum == Long.MAX_VALUE) {
            return "longArg()";
        } else if (maximum == Long.MAX_VALUE) {
            return "longArg(" + minimum + ")";
        } else {
            return "longArg(" + minimum + ", " + maximum + ")";
        }
    }
}
