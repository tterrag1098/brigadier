package com.mojang.brigadier.arguments;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

public interface Argument<T> extends ArgumentType<T> {
    
    String getName();
    
    class Impl<T> implements Argument<T> {
        
        private final ArgumentType<T> delegate;
        private final String name;
        
        public Impl(ArgumentType<T> delegate, String name) {
            this.delegate = delegate;
            this.name = name;
        }

        @Override
        public T parse(StringReader reader) throws CommandSyntaxException {
            return delegate.parse(reader);
        }
        
        @Override
        public Collection<String> getExamples() {
            return delegate.getExamples();
        }
        
        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return delegate.listSuggestions(context, builder);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((delegate == null) ? 0 : delegate.hashCode());
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Argument)) return false;
            if (!getName().equals(((Argument)obj).getName())) return false;
            
            if (obj instanceof Impl) {
                return this.delegate.equals(((Impl) obj).delegate);
            } else {
                return this.delegate.equals(obj);
            }
        }
    }

}
