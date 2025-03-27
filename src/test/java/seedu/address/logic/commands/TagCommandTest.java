package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.patient.Patient;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PatientBuilder;
import seedu.address.testutil.TypicalPatients;

public class TagCommandTest {
    @Test
    public void execute_onePatient_normal() {
        // Referencing DelLastVisitCommandTest.java

        // Typical patient + copy with added tags
        Patient p0 = TypicalPatients.ALICE;
        p0 = new PatientBuilder(p0)
                .withTags("a", "b", "c")
                .build();
        Patient p1 = new PatientBuilder(p0)
                .withTags("a", "b", "c", "d", "e")
                .build();

        // Create a model with `p0` then run tag
        AddressBook ab0 = new AddressBook();
        ab0.addPatient(p0);
        Model m0 = new ModelManager(ab0, new UserPrefs());
        try {
            CommandResult cmdRes = new TagCommand(
                    Index.fromZeroBased(0),
                    new HashSet<Tag>(Set.of(
                            new Tag("d"), new Tag("e")
                    ))
            ).execute(m0);
        } catch (Exception e) {
            fail("fail");
        }

        // Create a model with `p1` only and compare
        AddressBook ab1 = new AddressBook();
        ab1.addPatient(p1);
        Model m1 = new ModelManager(ab1, new UserPrefs());
        assertEquals(m0, m1);
    }
}
