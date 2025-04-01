package seedu.address.model.medicine;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Medicine in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidMedName(String)}
 */
public class Medicine {

    public static final String MESSAGE_CONSTRAINTS =
            "Medicine names should only contain letters, numbers, hyphens (-), and underscores (_).";
    public static final String VALIDATION_REGEX = "^[a-zA-Z0-9_-]+$";

    public final String medName;

    /**
     * Constructs a {@code Medicine}.
     *
     * @param medName A valid medicine name.
     */
    public Medicine(String medName) {
        requireNonNull(medName);
        checkArgument(isValidMedName(medName), MESSAGE_CONSTRAINTS);
        this.medName = medName;
    }

    /**
     * Returns true if a given string is a valid medicine name.
     */
    public static boolean isValidMedName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Medicine)) {
            return false;
        }

        Medicine otherMed = (Medicine) other;
        // ignore upper or lower case
        return medName.trim()
                .equalsIgnoreCase(otherMed.medName.trim());
    }

    @Override
    public int hashCode() {
        return medName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + medName + ']';
    }

    /**
     * Get medication name.
     */
    public String getMedicineName() {
        return medName;
    }

}
