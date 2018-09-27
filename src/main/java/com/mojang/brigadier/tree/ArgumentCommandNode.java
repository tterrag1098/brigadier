// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package com.mojang.brigadier.tree;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.Argument;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class ArgumentCommandNode<S, T> extends CommandNode<S> {
    private static final String USAGE_ARGUMENT_OPEN = "<";
    private static final String USAGE_ARGUMENT_CLOSE = ">";

    private final Argument<T> arg;
    private final SuggestionProvider<S> customSuggestions;

    public ArgumentCommandNode(final Argument<T> arg, final Command<S> command, final Predicate<S> requirement, final CommandNode<S> redirect, final RedirectModifier<S> modifier, final boolean forks, final SuggestionProvider<S> customSuggestions) {
        super(command, requirement, redirect, modifier, forks);
        this.arg = arg;
        this.customSuggestions = customSuggestions;
    }

    public ArgumentType<T> getType() {
        return arg;
    }

    @Override
    public String getName() {
        return arg.getName();
    }

    @Override
    public String getUsageText() {
        return USAGE_ARGUMENT_OPEN + getName() + USAGE_ARGUMENT_CLOSE;
    }

    public SuggestionProvider<S> getCustomSuggestions() {
        return customSuggestions;
    }

    @Override
    public void parse(final StringReader reader, final CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final T result = arg.parse(reader);
        final ParsedArgument<S, T> parsed = new ParsedArgument<>(start, reader.getCursor(), result);

        contextBuilder.withArgument(arg, parsed);
        contextBuilder.withNode(this, parsed.getRange());
    }

    @Override
    public CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) throws CommandSyntaxException {
        if (customSuggestions == null) {
            return arg.listSuggestions(context, builder);
        } else {
            return customSuggestions.getSuggestions(context, builder);
        }
    }

    @Override
    public RequiredArgumentBuilder<S, T> createBuilder() {
        final RequiredArgumentBuilder<S, T> builder = RequiredArgumentBuilder.argument(arg);
        builder.requires(getRequirement());
        builder.forward(getRedirect(), getRedirectModifier(), isFork());
        builder.suggests(customSuggestions);
        if (getCommand() != null) {
            builder.executes(getCommand());
        }
        return builder;
    }

    @Override
    public boolean isValidInput(final String input) {
        try {
            final StringReader reader = new StringReader(input);
            arg.parse(reader);
            return !reader.canRead() || reader.peek() == ' ';
        } catch (final CommandSyntaxException ignored) {
            return false;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ArgumentCommandNode)) return false;

        final ArgumentCommandNode that = (ArgumentCommandNode) o;

        if (!arg.equals(that.arg)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int result = arg.hashCode();
        result = 31 * result + arg.hashCode();
        return result;
    }

    @Override
    protected String getSortedKey() {
        return getName();
    }

    @Override
    public Collection<String> getExamples() {
        return arg.getExamples();
    }

    @Override
    public String toString() {
        return "<argument " + getName() + ":" + arg +">";
    }
}
