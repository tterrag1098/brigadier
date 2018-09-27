package com.mojang.brigadier.arguments;


public abstract class SimpleArgumentType<T> implements ArgumentType<T> {
    
    private final String name;
    
    protected SimpleArgumentType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SimpleArgumentType)) return false;
        
        return ((SimpleArgumentType<?>)obj).getName().equals(name);
    }
}
