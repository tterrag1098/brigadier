// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package com.mojang.brigadier.arguments;

import com.google.common.testing.EqualsTester;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.mojang.brigadier.arguments.LongArgumentType.longArg;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class LongArgumentTypeTest {
    private LongArgumentType type;
    @Mock
    private CommandContextBuilder<Object> context;

    @Before
    public void setUp() throws Exception {
        type = longArg("long", -100, 1_000_000_000_000L);
    }

    @Test
    public void parse() throws Exception {
        final StringReader reader = new StringReader("15");
        assertThat(longArg("long").parse(reader), is(15L));
        assertThat(reader.canRead(), is(false));
    }

    @Test
    public void parse_tooSmall() throws Exception {
        final StringReader reader = new StringReader("-5");
        try {
            longArg("long", 0, 100).parse(reader);
            fail();
        } catch (final CommandSyntaxException ex) {
            assertThat(ex.getType(), is(CommandSyntaxException.BUILT_IN_EXCEPTIONS.longTooLow()));
            assertThat(ex.getCursor(), is(0));
        }
    }

    @Test
    public void parse_tooBig() throws Exception {
        final StringReader reader = new StringReader("5");
        try {
            longArg("long", -100, 0).parse(reader);
            fail();
        } catch (final CommandSyntaxException ex) {
            assertThat(ex.getType(), is(CommandSyntaxException.BUILT_IN_EXCEPTIONS.longTooHigh()));
            assertThat(ex.getCursor(), is(0));
        }
    }

    @Test
    public void testEquals() throws Exception {
        new EqualsTester()
            .addEqualityGroup(longArg("long"), longArg("long"))
            .addEqualityGroup(longArg("long", -100, 100), longArg("long", -100, 100))
            .addEqualityGroup(longArg("long", -100, 50), longArg("long", -100, 50))
            .addEqualityGroup(longArg("long", -50, 100), longArg("long", -50, 100))
            .testEquals();
    }

    @Test
    public void testToString() throws Exception {
        assertThat(longArg("long"), hasToString("longArg()"));
        assertThat(longArg("long", -100), hasToString("longArg(-100)"));
        assertThat(longArg("long", -100, 100), hasToString("longArg(-100, 100)"));
        assertThat(longArg("long", Long.MIN_VALUE, 100), hasToString("longArg(-9223372036854775808, 100)"));
    }
}
