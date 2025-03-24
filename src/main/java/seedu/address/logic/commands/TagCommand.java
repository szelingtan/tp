package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.patient.Patient;
import seedu.address.model.tag.Tag;

/**
 * Adds a bunch of tags to a specified patient.
 */
public class TagCommand extends Command {
    public static final String COMMAND_WORD = "tag";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds the listed tags to the patient identified by the index "
            + "numebr used in the last patient listing."
            + '\n'
            + "Parameters: INDEX t/tag [t/more_tags]"
            + '\n'
            + "Example: " + COMMAND_WORD + "39 t/Likes Madoka Magica "
            + "t/Plays rhythm games";

    private final Index index;
    private final ArrayList<Tag> tagsToAdd;


    public TagCommand(Index index, ArrayList<Tag> tagsToAdd) {
        this.index = index;
        this.tagsToAdd = tagsToAdd;
    }

    /**
     * Executes the stored `tag` cmd.
     *
     * @param model {@code Model} which the command should operate on.
     * @return The {@code CommandResult}.
     * @throws CommandException if the index is out of range.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        // Basing off of `EditCommand.java`
        requireNonNull(model);
        List<Patient> lastShownList = model.getFilteredPatientList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }

        Patient patient = lastShownList.get(index.getZeroBased());
        Patient editedPatient = addTags(patient, this.tagsToAdd);

        model.setPatient(patient, editedPatient);
        model.updateFilteredPatientList(Model.PREDICATE_SHOW_ALL_PATIENTS);
        return new CommandResult(
                "" // TODO
        );
    }

    /**
     * Adds the list of tags to the given patient.
     *
     * @param p The patient we're adding tags to.
     * @param tagsToAdd The list of tags to add.
     * @return A new patient with the tags added.
     */
    private static Patient addTags(Patient p, ArrayList<Tag> tagsToAdd) {
        assert p != null;

        // This is not modifiable, so everything will have to be copied over
        Set<Tag> ogTags = p.getTags();
        Set<Tag> newTags = new HashSet<Tag>();
        newTags.addAll(ogTags);
        newTags.addAll(tagsToAdd);

        return new Patient(
                p.getName(),
                p.getPhone(),
                p.getEmail(),
                p.getAddress(),
                p.getLastVisit(),
                newTags,
                p.getMedicines()
        );
    }
}
