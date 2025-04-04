package seedu.address.model.patient;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;

/**
 * Represents a patient's last visit in the address book.
 * Can be null.
 * Guarantees: immutable; is always valid
 */
public class LastVisit {

    public static final String MESSAGE_CONSTRAINTS =
            "Last visit must take in a valid date of format YYYY-MM-DD";

    public final LocalDate lastVisitDate;

    /**
     * Constructs a {@code LastVisit}.
     *
     * @param lastVisitDate a valid last visit date.
     */
    public LastVisit(LocalDate lastVisitDate) {
        requireNonNull(lastVisitDate);
        checkArgument(isValidLastVisit(lastVisitDate), MESSAGE_CONSTRAINTS);
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
     * Check if the date given is a valid last visit date (cannot be more recent than the current date).
     */
    public static boolean isValidLastVisit(LocalDate testDate) {
        if (testDate == null) {
            return false;
        }
        LocalDate currentDate = LocalDate.now();
        // The most recent last visit date can be today, but not after.
        return testDate.isBefore(currentDate.plusDays(1));
    }

    @Override
    public int hashCode() {
        return lastVisitDate.hashCode();
    }

}
