package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MEDICATION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MEDICATION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PATIENT;
import static seedu.address.testutil.TypicalPatients.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

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
 * Contains integration tests (interaction with the Model) and unit tests for PrescribeCommand.
 * CURRENTLY INCOMPLETE.
 * Solution below adapted from https://se-education.org/guides/tutorials/ab3AddPrescribe.html
 */
public class PrescribeCommandTest {

    private static final String MEDICATION_STUB = "PARACETAMOL";
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validMedicine_success() {
        // Get a patient to update and the medicine to add
        Patient patientToUpdate = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Medicine medicineToAdd = new Medicine(VALID_MEDICATION_BOB);
        PrescribeCommand prescribeCommand = new PrescribeCommand(INDEX_FIRST_PATIENT, medicineToAdd);

        // The expected patient we want to see after adding the medicine
        Set<Medicine> updatedMedicines = new HashSet<>(patientToUpdate.getMedicines());
        updatedMedicines.add(medicineToAdd);
        Patient editedPatient = new PatientBuilder(patientToUpdate).withMeds(updatedMedicines).build();

        // The expected model with the updated patient
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(patientToUpdate, editedPatient);

        String expectedMessage = String.format(PrescribeCommand.MESSAGE_ADD_MED_SUCCESS,
                Messages.format(editedPatient));

        assertCommandSuccess(prescribeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPatientIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPatientList().size() + 1);
        PrescribeCommand prescribeCommand = new PrescribeCommand(outOfBoundIndex, new Medicine(VALID_MEDICATION_AMY));

        assertCommandFailure(prescribeCommand, model, Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateMedicine_throwsCommandException() {
        PrescribeCommand prescribeCommand = new PrescribeCommand(INDEX_FIRST_PATIENT,
                new Medicine(VALID_MEDICATION_AMY));
        String expectedFailureMessage = String.format(PrescribeCommand.MESSAGE_DUPLICATE_MED,
                new Medicine(VALID_MEDICATION_AMY));

        assertCommandFailure(prescribeCommand, model, expectedFailureMessage);
    }

    @Test
    public void equals() {
        final PrescribeCommand standardCommand = new PrescribeCommand(INDEX_FIRST_PATIENT,
                new Medicine(VALID_MEDICATION_AMY));

        // Same medicine name -> returns true
        PrescribeCommand sameMedNameCommand = new PrescribeCommand(INDEX_FIRST_PATIENT,
                new Medicine(VALID_MEDICATION_AMY));
        assertEquals(standardCommand, sameMedNameCommand);

        // Same object -> returns True
        assertTrue(standardCommand.equals(standardCommand));

        // different index -> returns false
        assertNotEquals(standardCommand, new PrescribeCommand(INDEX_SECOND_PATIENT,
                new Medicine(VALID_MEDICATION_AMY)));

        // different medicine name -> returns false
        assertNotEquals(standardCommand, new PrescribeCommand(INDEX_FIRST_PATIENT,
                new Medicine(VALID_MEDICATION_BOB)));

        // different command -> returns false
        assertNotEquals(standardCommand, new ClearCommand());

        // null -> returns false
        assertNotEquals(null, standardCommand);
    }

}
