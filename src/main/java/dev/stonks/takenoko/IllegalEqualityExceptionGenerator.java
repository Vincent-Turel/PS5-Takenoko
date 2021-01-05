package dev.stonks.takenoko;

/**
 * Allows to generate exception for illegal equality exception.
 */
public abstract class IllegalEqualityExceptionGenerator {
    private static String generateMessage(Class expectedClass, Class gotClass) {
        return "Impossible comparison: `"
                + expectedClass.getName()
                + "` can not be compared with `"
                + gotClass.getName()
                + "`.";
    }

    /**
     * Generates an exception with a correct message.
     *
     * @param expectedClass the class that should have been provided when
     *                      calling equals
     * @param gotClass      the class that was got by the callee
     * @return an IllegalCallerException with a proper error message.
     */
    public static IllegalCallerException create(Class expectedClass, Class gotClass) {
        String msg = generateMessage(expectedClass, gotClass);
        return new IllegalCallerException(msg);
    }
}
