package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LASTVISITDATE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LASTVISITDATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPatientAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PATIENT;
import static seedu.address.testutil.TypicalPatients.getTypicalAddressBook;

import java.time.LocalDate;

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
 * Contains integration tests (interaction with the Model) and unit tests for LastVisitCommand.
 */
public class LastVisitCommandTest {

    private static final String LASTVISIT_STUB = "2025-01-01";

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addRemarkUnfilteredList_success() {
        Patient firstpatient = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Patient editedpatient = new PatientBuilder(firstpatient).withLastVisit(LASTVISIT_STUB).build();

        LastVisitCommand lastVisitCommand = new LastVisitCommand(INDEX_FIRST_PATIENT,
                new LastVisit(editedpatient.getLastVisit().lastVisitDate));

        String expectedMessage = String.format(LastVisitCommand.MESSAGE_ADD_LAST_VISIT_SUCCESS,
                editedpatient.getName(), editedpatient.getLastVisit().lastVisitDate.toString());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(firstpatient, editedpatient);

        assertCommandSuccess(lastVisitCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPatientAtIndex(model, INDEX_FIRST_PATIENT);

        Patient firstPatient = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Patient editedPatient = new PatientBuilder(model.getFilteredPatientList()
                .get(INDEX_FIRST_PATIENT.getZeroBased()))
                .withLastVisit(LASTVISIT_STUB).build();

        LastVisitCommand lastVisitCommand = new LastVisitCommand(INDEX_FIRST_PATIENT,
                new LastVisit(editedPatient.getLastVisit().lastVisitDate));

        String expectedMessage = String.format(LastVisitCommand.MESSAGE_ADD_LAST_VISIT_SUCCESS,
                editedPatient.getName(), editedPatient.getLastVisit().lastVisitDate.toString());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(firstPatient, editedPatient);

        assertCommandSuccess(lastVisitCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPatientIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPatientList().size() + 1);
        LastVisitCommand lastVisitCommand = new LastVisitCommand(outOfBoundIndex,
                new LastVisit(LocalDate.parse(VALID_LASTVISITDATE_BOB)));

        assertCommandFailure(lastVisitCommand, model, Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPatientIndexFilteredList_failure() {
        showPatientAtIndex(model, INDEX_FIRST_PATIENT);
        Index outOfBoundIndex = INDEX_SECOND_PATIENT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPatientList().size());

        LastVisitCommand lastVisitCommand = new LastVisitCommand(outOfBoundIndex,
                new LastVisit(LocalDate.parse(VALID_LASTVISITDATE_BOB)));

        assertCommandFailure(lastVisitCommand, model, Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final LastVisitCommand standardCommand = new LastVisitCommand(INDEX_FIRST_PATIENT,
                new LastVisit(LocalDate.parse(VALID_LASTVISITDATE_AMY)));

        // same values -> returns true
        LastVisitCommand commandWithSameValues = new LastVisitCommand(INDEX_FIRST_PATIENT,
                new LastVisit(LocalDate.parse(VALID_LASTVISITDATE_AMY)));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new LastVisitCommand(INDEX_SECOND_PATIENT,
                new LastVisit(LocalDate.parse(VALID_LASTVISITDATE_AMY)))));

        // different remark -> returns false
        assertFalse(standardCommand.equals(new LastVisitCommand(INDEX_FIRST_PATIENT,
                new LastVisit(LocalDate.parse(VALID_LASTVISITDATE_BOB)))));
    }
}
