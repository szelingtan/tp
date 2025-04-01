package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.patient.Patient;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PatientBuilder;
import seedu.address.testutil.TypicalPatients;

public class UntagCommandTest {
    @Test
    public void execute_onePatient_normal() {
        // Typical patient + copy with some tags removed
        Patient p0 = TypicalPatients.ALICE;
        p0 = new PatientBuilder(p0)
                .withTags("a", "b", "c", "d", "e")
                .build();
        Patient p1 = new PatientBuilder(p0)
                .withTags("a", "b", "c")
                .build();

        // Create a model with `p0` then run deleteTag
        AddressBook ab0 = new AddressBook();
        ab0.addPatient(p0);
        Model m0 = new ModelManager(ab0, new UserPrefs());
        try {
            CommandResult cmdRes = new UntagCommand(
                    Index.fromZeroBased(0),
                    new HashSet<>(Set.of(
                            new Tag("d"), new Tag("e")
                    )),
                    false // Ensure removeAllTags is set to false
            ).execute(m0);
        } catch (Exception e) {
            fail("Execution failed unexpectedly: " + e.getMessage());
        }

        // Create a model with `p1` only and compare
        AddressBook ab1 = new AddressBook();
        ab1.addPatient(p1);
        Model m1 = new ModelManager(ab1, new UserPrefs());
        assertEquals(m0, m1);
    }

    @Test
    public void execute_removeAllTags_success() {
        // Typical patient with multiple tags
        Patient p0 = TypicalPatients.ALICE;
        p0 = new PatientBuilder(p0)
                .withTags("a", "b", "c")
                .build();
        Patient p1 = new PatientBuilder(p0)
                .withTags() // No tags after removal
                .build();

        // Create a model with `p0` then run deleteTag with `t/all`
        AddressBook ab0 = new AddressBook();
        ab0.addPatient(p0);
        Model m0 = new ModelManager(ab0, new UserPrefs());
        try {
            CommandResult cmdRes = new UntagCommand(
                    Index.fromZeroBased(0),
                    new HashSet<>(),
                    true // removeAllTags set to true
            ).execute(m0);
        } catch (Exception e) {
            fail("Execution failed unexpectedly: " + e.getMessage());
        }

        // Create a model with `p1` only and compare
        AddressBook ab1 = new AddressBook();
        ab1.addPatient(p1);
        Model m1 = new ModelManager(ab1, new UserPrefs());
        assertEquals(m0, m1);
    }

    @Test
    public void execute_removeNonExistentTag_throwsException() {
        // Patient with predefined tags
        Patient p0 = TypicalPatients.ALICE;
        p0 = new PatientBuilder(p0)
                .withTags("a", "b", "c")
                .build();
        AddressBook ab = new AddressBook();
        ab.addPatient(p0);
        Model model = new ModelManager(ab, new UserPrefs());

        UntagCommand command = new UntagCommand(
                Index.fromZeroBased(0),
                new HashSet<>(Set.of(new Tag("x"))),
                false // Trying to remove a non-existent tag
        );

        assertThrows(CommandException.class, () -> command.execute(model),
                "Expected CommandException for removing a tag not in patient's tag list");
    }

    @Test
    public void execute_removeFromPatientWithoutTags_throwsException() {
        // Patient without any tags
        Patient p0 = TypicalPatients.ALICE;
        p0 = new PatientBuilder(p0)
                .withTags() // No tags
                .build();
        AddressBook ab = new AddressBook();
        ab.addPatient(p0);
        Model model = new ModelManager(ab, new UserPrefs());

        UntagCommand command = new UntagCommand(
                Index.fromZeroBased(0),
                new HashSet<>(Set.of(new Tag("a"))),
                false // Trying to remove from a patient with no tags
        );

        assertThrows(CommandException.class, () -> command.execute(model),
                "Expected CommandException for removing tags from a patient with no tags");
    }
}
