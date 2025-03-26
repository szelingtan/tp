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
    private final Medicine medicine = new Medicine("Paracetamol");

    @Test
    public void execute_removingSpecificMedicine_success() {
        // First, add a medicine to the patient
        Patient patientToUpdate = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());

        // Create a set with the medicine and update the patient
        HashSet<Medicine> medicineSet = new HashSet<>();
        medicineSet.add(medicine);
        Patient patientWithMedicine = new PatientBuilder(patientToUpdate).withMeds(medicineSet).build();
        model.setPatient(patientToUpdate, patientWithMedicine);

        // Now test unprescribing
        UnprescribeCommand unprescribeCommand = new UnprescribeCommand(INDEX_FIRST_PATIENT, medicine);

        // Create a patient with empty medicine set for expected result
        Patient editedPatient = new PatientBuilder(patientWithMedicine).withMeds(new HashSet<>()).build();
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(patientWithMedicine, editedPatient);

        String expectedMessage = String.format(UnprescribeCommand.MESSAGE_REMOVE_MED_SUCCESS,
                medicine.getMedicineName(), Messages.format(editedPatient));

        assertCommandSuccess(unprescribeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_removingAllMedicines_success() {
        // First, add multiple medicines to the patient
        Patient patientToUpdate = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());

        // Create a set with multiple medicines and update the patient
        HashSet<Medicine> medicineSet = new HashSet<>();
        medicineSet.add(medicine);
        medicineSet.add(new Medicine("Aspirin"));
        medicineSet.add(new Medicine("Lyrica"));
        medicineSet.add(new Medicine("Lactofort"));
        Patient patientWithMedicines = new PatientBuilder(patientToUpdate).withMeds(medicineSet).build();
        model.setPatient(patientToUpdate, patientWithMedicines);

        // Now test unprescribing all medicines
        UnprescribeCommand unprescribeCommand = new UnprescribeCommand(INDEX_FIRST_PATIENT,
                new Medicine("all"));

        // Create a patient with empty medicine set for expected result
        Patient editedPatient = new PatientBuilder(patientWithMedicines).withMeds(new HashSet<>()).build();
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(patientWithMedicines, editedPatient);

        String expectedMessage = String.format(UnprescribeCommand.MESSAGE_REMOVE_ALL_MED_SUCCESS,
                editedPatient.getName());
    }

    @Test
    public void execute_emptyMedicineList_throwsCommandException() {
        Patient patientToUpdate = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());

        // Ensure patient has no medicines
        Patient patientWithNoMedicine = new PatientBuilder(patientToUpdate).withMeds(new HashSet<>()).build();
        model.setPatient(patientToUpdate, patientWithNoMedicine);

        UnprescribeCommand unprescribeCommand = new UnprescribeCommand(INDEX_FIRST_PATIENT, medicine);

        String expectedMessage = String.format(UnprescribeCommand.MESSAGE_EMPTY_MED_LIST,
                patientWithNoMedicine.getName());

        assertCommandFailure(unprescribeCommand, model, expectedMessage);
    }

    @Test
    public void execute_medicineNotFound_throwsCommandException() {
        // First, add a different medicine to the patient
        Patient patientToUpdate = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());

        // Add Aspirin but try to remove Paracetamol
        HashSet<Medicine> medicineSet = new HashSet<>();
        medicineSet.add(new Medicine("Aspirin"));
        Patient patientWithDifferentMedicine = new PatientBuilder(patientToUpdate).withMeds(medicineSet).build();
        model.setPatient(patientToUpdate, patientWithDifferentMedicine);

        UnprescribeCommand unprescribeCommand = new UnprescribeCommand(INDEX_FIRST_PATIENT, medicine);

        String expectedMessage = String.format(UnprescribeCommand.MESSAGE_MED_NOT_FOUND,
                medicine.getMedicineName(), Messages.format(patientWithDifferentMedicine));

        assertCommandFailure(unprescribeCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidPatientIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPatientList().size() + 1);
        UnprescribeCommand unprescribeCommand = new UnprescribeCommand(outOfBoundIndex, medicine);

        assertCommandFailure(unprescribeCommand, model, Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final UnprescribeCommand standardCommand = new UnprescribeCommand(INDEX_FIRST_PATIENT,
                medicine);

        // Same values -> returns true
        UnprescribeCommand commandWithSameValues = new UnprescribeCommand(INDEX_FIRST_PATIENT,
                medicine);
        assertEquals(standardCommand, commandWithSameValues);

        // Same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // Different index -> returns false
        assertNotEquals(standardCommand, new UnprescribeCommand(INDEX_SECOND_PATIENT, medicine));

        // Different medicine -> returns false
        assertNotEquals(standardCommand, new UnprescribeCommand(INDEX_FIRST_PATIENT,
                new Medicine("Aspirin")));

        // Different command -> returns false
        assertNotEquals(standardCommand, new ClearCommand());

        // null -> returns false
        assertNotEquals(null, standardCommand);
    }
}
