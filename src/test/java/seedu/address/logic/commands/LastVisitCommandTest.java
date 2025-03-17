package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showpatientAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PATIENT;
import static seedu.address.testutil.TypicalPatients.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.patient.LastVisit;
import seedu.address.model.patient.Patient;
import seedu.address.testutil.PatientBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for RemarkCommand.
 */
public class LastVisitCommandTest {

    private static final String REMARK_STUB = "Some remark";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addRemarkUnfilteredList_success() {
        Patient firstpatient = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Patient editedpatient = new PatientBuilder(firstpatient).withLastVisit(REMARK_STUB).build();

        LastVisitCommand lastVisitCommand = new LastVisitCommand(INDEX_FIRST_PATIENT,
                new LastVisit(editedpatient.getLastVisit().value));

        String expectedMessage = String.format(LastVisitCommand.MESSAGE_ADD_LAST_VISIT_SUCCESS,
                editedpatient.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(firstpatient, editedpatient);

        assertCommandSuccess(lastVisitCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteRemarkUnfilteredList_success() {
        Patient firstpatient = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Patient editedpatient = new PatientBuilder(firstpatient).withLastVisit("").build();

        LastVisitCommand lastVisitCommand = new LastVisitCommand(INDEX_FIRST_PATIENT,
                new LastVisit(editedpatient.getLastVisit().toString()));

        String expectedMessage = String.format(LastVisitCommand.MESSAGE_ADD_LAST_VISIT_SUCCESS,
                editedpatient.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(firstpatient, editedpatient);

        assertCommandSuccess(lastVisitCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showpatientAtIndex(model, INDEX_FIRST_PATIENT);

        Patient firstpatient = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Patient editedpatient = new PatientBuilder(model.getFilteredPatientList()
                .get(INDEX_FIRST_PATIENT.getZeroBased()))
                .withLastVisit(REMARK_STUB).build();

        LastVisitCommand lastVisitCommand = new LastVisitCommand(INDEX_FIRST_PATIENT,
                new LastVisit(editedpatient.getLastVisit().value));

        String expectedMessage = String.format(LastVisitCommand.MESSAGE_ADD_LAST_VISIT_SUCCESS,
                editedpatient.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(firstpatient, editedpatient);

        assertCommandSuccess(lastVisitCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidpatientIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPatientList().size() + 1);
        LastVisitCommand lastVisitCommand = new LastVisitCommand(outOfBoundIndex, new LastVisit(VALID_REMARK_BOB));

        assertCommandFailure(lastVisitCommand, model, Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidpatientIndexFilteredList_failure() {
        showpatientAtIndex(model, INDEX_FIRST_PATIENT);
        Index outOfBoundIndex = INDEX_SECOND_PATIENT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getpatientList().size());

        LastVisitCommand lastVisitCommand = new LastVisitCommand(outOfBoundIndex, new LastVisit(VALID_REMARK_BOB));

        assertCommandFailure(lastVisitCommand, model, Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final LastVisitCommand standardCommand = new LastVisitCommand(INDEX_FIRST_PATIENT,
                new LastVisit(VALID_REMARK_AMY));

        // same values -> returns true
        LastVisitCommand commandWithSameValues = new LastVisitCommand(INDEX_FIRST_PATIENT,
                new LastVisit(VALID_REMARK_AMY));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new LastVisitCommand(INDEX_SECOND_PATIENT,
                new LastVisit(VALID_REMARK_AMY))));

        // different remark -> returns false
        assertFalse(standardCommand.equals(new LastVisitCommand(INDEX_FIRST_PATIENT,
                new LastVisit(VALID_REMARK_BOB))));
    }
}
