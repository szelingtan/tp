package seedu.address.model.patient;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

/**
 * Represents a patient's address in the address book.
 * Guarantees: immutable; is always valid
 */
public class LastVisit {

    public static final String MESSAGE_CONSTRAINTS =
            "Last visit must take in a valid date of format YYYY-MM-DD";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[^\\s].*";

    public final LocalDate lastVisitDate;

    /**
     * Constructs an {@code LastVisit}.
     *
     * @param lastVisitDate a valid last visit date.
     */
    public LastVisit(LocalDate lastVisitDate) {
        requireNonNull(lastVisitDate);
        this.lastVisitDate = lastVisitDate;
    }

    @Override
    public String toString() {
        return "Last visit: " + lastVisitDate;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LastVisit)) {
            return false;
        }

        LastVisit otherLastVisit = (LastVisit) other;
        return lastVisitDate.equals(otherLastVisit.lastVisitDate);
    }

    /**
     * Check if date given is a valid last visit date (cannot be more recent than current date).
     */
    public static boolean isValidLastVisit(LocalDate testDate) {
        if (testDate == null) {
            return false;
        }
        LocalDate currentDate = LocalDate.now();
        // Most recent last visit date can be today, but not after.
        return testDate.isBefore(currentDate.plusDays(1));
    }

    @Override
    public int hashCode() {
        return lastVisitDate.hashCode();
    }

}
