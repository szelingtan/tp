package seedu.address.model.medicine;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;


public class MedicineTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Medicine(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Medicine(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Medicine.isValidMedName(null));
    }

}
