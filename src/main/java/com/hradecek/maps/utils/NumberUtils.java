package com.hradecek.maps.utils;

import java.util.OptionalDouble;
import java.util.function.DoublePredicate;

 /**
 * Various utility functions for numbers.
 */
public class NumberUtils {

    private NumberUtils() {
        throw new AssertionError("Utility class cannot be instantiated.");
    }

     /**
      * Converts input {@code string} to double if possible.
      *
      * @param string string to be converted
      * @return converted double or
      *         {@code Optional.empty()} if {@code string} cannot be converted
      */
    public static OptionalDouble stringToDouble(final String string) {
        try {
            return OptionalDouble.of(Double.parseDouble(string));
        } catch (NumberFormatException ex) {
            return OptionalDouble.empty();
        }
    }

     /**
      * Converts input {@code string} to double if possible and resulting double must hold provided {@code predicate}.
      *
      * @param string string to be converted
      * @param predicate predicate to be tested
      * @return converted double or
      *         {@code Optional.empty()} if {@code string} cannot be converted or {@code predicate} does not hold
      */
     public static OptionalDouble stringToDouble(final String string, DoublePredicate predicate) {
         final var converted = stringToDouble(string);
         return converted.isPresent() && predicate.test(converted.getAsDouble()) ? converted : OptionalDouble.empty();
     }

     /**
      * Check if {@code number} is in closed range from both sided <{@code lowBound}; {@code highBound}>.
      * <p>
      * If {@code lowerBound} is higher than {@code highBound}, bounds are swapped automatically to correct order.
      *
      * @param number number to be checked
      * @param lowBound range's low bound
      * @param highBound range's high bound
      * @param <T> type parameter of provided {@code number}
      * @return true if {@code number} is in provided range, otherwise false
      */
     /* Suppress warnings note:
      *  - S2234: we are intentionally swapping arguments, so names doesn't match with function argument names
      */
     @SuppressWarnings("java:S2234")
     public static <T extends Comparable<T>> boolean isInRangeClosedBoth(T number, T lowBound, T highBound) {
         if (lowBound.compareTo(highBound) > 0) {
             return isInRangeClosedBoth(number, highBound, lowBound);
         }
         return number.compareTo(lowBound) >= 0 && number.compareTo(highBound) <= 0;
     }
}
