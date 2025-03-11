package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MEDICATION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MEDICATION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Patient;
import seedu.address.testutil.PersonBuilder;
import seedu.address.model.medicine.Medicine;

/**
 * Contains integration tests (interaction with the Model) and unit tests for PrescribeCommand.
 * CURRENTLY INCOMPLETE.
 * Solution below adapted from https://se-education.org/guides/tutorials/ab3AddPrescribe.html
 */
public class PrescribeCommandTest {

    private static final String MEDICATION_STUB = "PARACETAMOL";


    @Test
    public void equals() {
        final PrescribeCommand standardCommand = new PrescribeCommand(INDEX_FIRST_PERSON,
                new Medicine(VALID_MEDICATION_AMY));

        // Same medicine name -> returns true
        PrescribeCommand sameMedNameCommand = new PrescribeCommand(INDEX_FIRST_PERSON,
                new Medicine(VALID_MEDICATION_AMY));
        assertTrue(standardCommand.equals(sameMedNameCommand));

        // Same object -> returns True
        assertTrue(standardCommand.equals(standardCommand));

        // different index -> returns false
        assertFalse(standardCommand.equals(new PrescribeCommand(INDEX_SECOND_PERSON,
                new Medicine(VALID_MEDICATION_AMY))));

        // different medicine name -> returns false
        assertFalse(standardCommand.equals(new PrescribeCommand(INDEX_FIRST_PERSON,
                new Medicine(VALID_MEDICATION_BOB))));

        // different command -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // null -> returns false
        assertFalse(standardCommand.equals(null));
    }
}