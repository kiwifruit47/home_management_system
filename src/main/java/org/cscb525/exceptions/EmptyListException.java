package org.cscb525.exceptions;

public class EmptyListException extends RuntimeException {
    public EmptyListException(Class<?> entityClass) {
        super("No " + getPluralName(entityClass) + " found.");
    }

    private static String getPluralName(Class<?> entityClass) {
        String name = entityClass.getSimpleName();
        String suffix;
        if (
                name.endsWith("s") ||
                name.endsWith("ch") ||
                name.endsWith("sh") ||
                name.endsWith("x") ||
                name.endsWith("z")
        ) {
            suffix = "es";
        } else {
            suffix = "s";
        }
        return name + suffix;
    }
}
