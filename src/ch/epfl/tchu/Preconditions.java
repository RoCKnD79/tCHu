package ch.epfl.tchu;

public final class Preconditions {

    private Preconditions() {}

    public static void checkArgument(boolean shouldBeTrue) throws IllegalArgumentException {
        if (shouldBeTrue == false) {
            throw new IllegalArgumentException();
        }
    }

}
