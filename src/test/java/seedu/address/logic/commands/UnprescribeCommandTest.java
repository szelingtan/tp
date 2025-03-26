package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PATIENT;
import static seedu.address.testutil.TypicalPatients.getTypicalAddressBook;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.medicine.Medicine;
import seedu.address.model.patient.Patient;
import seedu.address.testutil.PatientBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for UnprescribeCommand.
 */
public class UnprescribeCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_patientWithMedicines_success() {
        // First, add a medicine to the patient
        Patient patientToUpdate = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Medicine medicine = new Medicine("Paracetamol");

        // Create a set with the medicine and update the patient
        HashSet<Medicine> medicineSet = new HashSet<>();
        medicineSet.add(medicine);
        Patient patientWithMedicine = new PatientBuilder(patientToUpdate).withMeds(medicineSet).build();
        model.setPatient(patientToUpdate, patientWithMedicine);

        // Now test unprescribing
        UnprescribeCommand unprescribeCommand = new UnprescribeCommand(INDEX_FIRST_PATIENT);

        // Create a patient with empty medicine set for expected result
        Patient editedPatient = new PatientBuilder(patientWithMedicine).withMeds(new HashSet<>()).build();
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(patientWithMedicine, editedPatient);

        String expectedMessage = String.format(UnprescribeCommand.MESSAGE_REMOVE_MED_SUCCESS,
                editedPatient.getName());

        assertCommandSuccess(unprescribeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_patientWithNoMedicines_showsEmptyMessage() {
        Patient patientToUpdate = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());

        // Ensure patient has no medicines
        Patient patientWithNoMedicine = new PatientBuilder(patientToUpdate).withMeds(new HashSet<>()).build();
        model.setPatient(patientToUpdate, patientWithNoMedicine);

        UnprescribeCommand unprescribeCommand = new UnprescribeCommand(INDEX_FIRST_PATIENT);

        // The expected model should be the same since we are not changing anything
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        String expectedMessage = String.format(UnprescribeCommand.MESSAGE_EMPTY_MED_LIST,
                patientWithNoMedicine.getName());

        assertCommandSuccess(unprescribeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPatientIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPatientList().size() + 1);
        UnprescribeCommand unprescribeCommand = new UnprescribeCommand(outOfBoundIndex);

        assertCommandFailure(unprescribeCommand, model, Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final UnprescribeCommand standardCommand = new UnprescribeCommand(INDEX_FIRST_PATIENT);

        // Same values -> returns true
        UnprescribeCommand commandWithSameValues = new UnprescribeCommand(INDEX_FIRST_PATIENT);
        assertEquals(standardCommand, commandWithSameValues);

        // Same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // Different index -> returns false
        assertNotEquals(standardCommand, new UnprescribeCommand(INDEX_SECOND_PATIENT));

        // Different command -> returns false
        assertNotEquals(standardCommand, new ClearCommand());

        // null -> returns false
        assertNotEquals(null, standardCommand);
    }
}
