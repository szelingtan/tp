package seedu.address.model.patient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class LastVisitTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LastVisit(null));
    }

    @Test
    public void constructor_invalidDate_throwsIllegalArgumentException() {
        LocalDate invalidDate = LocalDate.now().plusDays(1);
        assertThrows(IllegalArgumentException.class, () -> new LastVisit(invalidDate));
    }

    @Test
    public void isValidLastVisit() {
        // null date
        assertFalse(LastVisit.isValidLastVisit(null));

        // invalid dates
        assertFalse(LastVisit.isValidLastVisit(LocalDate.now().plusDays(1)));
        assertFalse(LastVisit.isValidLastVisit(LocalDate.now().plusDays(1000)));

        // valid dates
        assertTrue(LastVisit.isValidLastVisit(LocalDate.now()));
        assertTrue(LastVisit.isValidLastVisit(LocalDate.parse("2000-01-01")));
    }

    @Test
    public void equals() {
        LastVisit lastVisit = new LastVisit(LocalDate.parse("2000-01-01"));

        // same values -> returns true
        assertTrue(lastVisit.equals(new LastVisit(LocalDate.parse("2000-01-01"))));

        // same object -> returns true
        assertTrue(lastVisit.equals(lastVisit));

        // null -> returns false
        assertFalse(lastVisit.equals(null));

        // different types -> returns false
        assertFalse(lastVisit.equals(5));

        // different values -> returns false
        assertFalse(lastVisit.equals(new LastVisit(LocalDate.parse("2001-01-01"))));
    }
}
