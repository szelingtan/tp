package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import seedu.address.model.medicine.Medicine;

import org.junit.jupiter.api.Test;

public class MedicineTest {

    @Test
    public void equals() {
        Medicine medicine = new Medicine("panadol");

        // same object -> returns true
        assertTrue(medicine.equals(medicine));

        // same values -> returns true
        Medicine medCopy = new Medicine(medicine.medName);
        assertTrue(medicine.equals(medCopy));

        // different types -> returns false
        assertFalse(medicine.equals(1));

        // null -> returns false
        assertFalse(medicine.equals(null));

        // different remark -> returns false
        Medicine editedMedicine = new Medicine("paracetamol");
        assertFalse(medicine.equals(editedMedicine));
    }
}