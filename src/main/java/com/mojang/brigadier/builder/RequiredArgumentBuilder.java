// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package com.mojang.brigadier.builder;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;

public class RequiredArgumentBuilder<S, T> extends ArgumentBuilder<S, RequiredArgumentBuilder<S, T>> {

    private final ArgumentType<T> type;
    private SuggestionProvider<S> suggestionsProvider = null;

    private RequiredArgumentBuilder(final ArgumentType<T> type) {
        this.type = type;
    }

    public static <S, T> RequiredArgumentBuilder<S, T> argument(final ArgumentType<T> type) {
        return new RequiredArgumentBuilder<>(type);
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

    public ArgumentType<T> getType() {
        return type;
    }

    public ArgumentCommandNode<S, T> build() {
        final ArgumentCommandNode<S, T> result = new ArgumentCommandNode<>(getType(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork(), getSuggestionsProvider());

        for (final CommandNode<S> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }
}
