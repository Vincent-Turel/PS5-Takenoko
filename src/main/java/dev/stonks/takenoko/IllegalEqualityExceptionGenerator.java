package dev.stonks.takenoko;

/**
 * Allows to generate exception for illegal equality exception.
 */
public abstract class IllegalEqualityExceptionGenerator {
    private static String generateMessage(Class expectedClass, Object gotObject) {
        StringBuilder rhsName = new StringBuilder();

        if (gotObject == null) {
            rhsName.append("*null pointer*");
        } else {
            rhsName.append(gotObject.getClass().getName());
        }

        return "Impossible comparison: `"
                + expectedClass.getName()
                + "` can not be compared with `"
                + rhsName
                + "`.";
    }

    /**
     * Generates an exception with a correct message.
     *
     * @param expectedClass the class that should have been provided when
     *                      calling equals
     * @param gotObject     the class that was got by the callee
     * @return an IllegalCallerException with a proper error message.
     */
    public static IllegalCallerException create(Class expectedClass, Object gotObject) {
        String msg = generateMessage(expectedClass, gotObject);
        return new IllegalCallerException(msg);
    }
}
