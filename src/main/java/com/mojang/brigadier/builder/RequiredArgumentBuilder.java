// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package com.mojang.brigadier.builder;

import com.mojang.brigadier.arguments.Argument;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;

public class RequiredArgumentBuilder<S, T> extends ArgumentBuilder<S, RequiredArgumentBuilder<S, T>> {
    private final Argument<T> arg;
    private SuggestionProvider<S> suggestionsProvider = null;

    private RequiredArgumentBuilder(final Argument<T> arg) {
        this.arg = arg;
    }

    public static <S, T> RequiredArgumentBuilder<S, T> argument(final String name, final ArgumentType<T> type) {
        return argument(new Argument.Impl<>(type, name));
    }

    public static <S, T> RequiredArgumentBuilder<S, T> argument(final Argument<T> arg) {
        return new RequiredArgumentBuilder<>(arg);
    }

    public RequiredArgumentBuilder<S, T> suggests(final SuggestionProvider<S> provider) {
        this.suggestionsProvider = provider;
        return getThis();
    }

    public SuggestionProvider<S> getSuggestionsProvider() {
        return suggestionsProvider;
    }

    @Override
    protected RequiredArgumentBuilder<S, T> getThis() {
        return this;
    }
    
    @Deprecated
    public ArgumentType<T> getType() {
        return getArg();
    }

    public Argument<T> getArg() {
        return arg;
    }

    public String getName() {
        return arg.getName();
    }

    public ArgumentCommandNode<S, T> build() {
        final ArgumentCommandNode<S, T> result = new ArgumentCommandNode<>(getArg(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork(), getSuggestionsProvider());

        for (final CommandNode<S> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }
}
