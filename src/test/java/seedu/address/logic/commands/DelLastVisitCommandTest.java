package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Patient;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

public class DelLastVisitCommandTest {
    @Test
    public void execute_onePerson_normal() {
        // Get a typical person, make the last visit guaranteed
        // to be non-empty for testing, then created the expected
        // version after running `delLastVisit`.
        Patient p0 = TypicalPersons.ALICE;
        p0 = new PersonBuilder(p0)
                .withLastVisit("Meduka become Meguca")
                .build();
        Patient p1 = new PersonBuilder(p0)
                .withLastVisit(DelLastVisitCommand.NONE)
                .build();

        // Create a model with `p0` only, then run DelLastVisit
        AddressBook ab0 = new AddressBook();
        ab0.addPerson(p0);
        Model m0 = new ModelManager(ab0, new UserPrefs());

        try {
            CommandResult cmdRes = new DelLastVisitCommand(
                    Index.fromZeroBased(0)
            ).execute(m0);
        } catch (Exception e) {
            fail("skull emoji");
        }

        // Create a model with `p1` only
        AddressBook ab1 = new AddressBook();
        ab1.addPerson(p1);
        Model m1 = new ModelManager(ab1, new UserPrefs());

        // Check that actual model = expected model
        assertEquals(m0, m1);
    }
}
