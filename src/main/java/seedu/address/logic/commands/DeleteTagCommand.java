package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

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
 * Represents a command to remove specific tags from a patient in the system.
 */

public class DeleteTagCommand extends Command {

    /**
     * The command word to trigger this action.
     */
    public static final String COMMAND_WORD = "untag";
    public static final String ALL_TAGS_KEYWORD = "all";
    /**
     * The usage message for this command.
     */

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes the specified tags from the patient identified by the index "
            + "number used in the last patient listing. Use 't/all' to remove all tags."
            + '\n'
            + "Parameters: INDEX t/tag [t/more_tags] | INDEX t/all"
            + '\n'
            + "Example: " + COMMAND_WORD + " 39 t/High Blood Pressure t/Seafood Allergy"
            + "\nExample: " + COMMAND_WORD + " 2 t/all";

    /**
     * The index of the patient to modify.
     */
    private final Index index;

    /**
     * The set of tags to remove from the patient.
     */
    private final HashSet<Tag> tagsToDelete;
    private final boolean removeAllTags;

    /**
     * Creates a {@code DeleteTagCommand}.
     *
     * @param index The index of the patient to modify.
     * @param tagsToDelete The set of tags to remove.
     */
    public DeleteTagCommand(Index index, HashSet<Tag> tagsToDelete, boolean removeAllTags) {
        if (tagsToDelete.isEmpty() && !removeAllTags) {
            throw new IllegalArgumentException("At least one tag must be provided, or use 't/all' to remove all tags.");
        }
        this.index = index;
        this.tagsToDelete = new HashSet<>(tagsToDelete);
        this.removeAllTags = removeAllTags;
    }

    /**
     * Generates a message to confirm successful deletion of tags.
     *
     * @param patient The updated patient after tag removal.
     * @param tagsToDelete The set of tags that were removed.
     * @return A message confirming the successful removal of tags.
     */
    private static String generateSuccessMessage(Patient patient, HashSet<Tag> tagsToDelete, boolean removeAllTags) {
        if (removeAllTags) {
            return "All tags successfully removed from " + patient.getName();
        }
        HashSet<String> tagStrsToDelete = new HashSet<>();
        for (Tag t : tagsToDelete) {
            tagStrsToDelete.add(t.toString());
        }
        return "Tags " + String.join(", ", tagStrsToDelete)
                + " successfully removed from " + patient.getName();
    }


    /**
     * Executes the stored `untag` command, removing the specified tags from the patient.
     *
     * @param model The model that the command should operate on.
     * @return A {@code CommandResult} indicating the outcome of the command.
     * @throws CommandException if the provided index is out of range.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Patient> lastShownList = model.getFilteredPatientList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX
                    + " Please check the patient index again or use the find feature to locate the patient.");
        }

        Patient patient = lastShownList.get(index.getZeroBased());

        if (patient.getTags().isEmpty()) {
            throw new CommandException("No tags found for this patient. Cannot remove tags from an empty tag list.");
        }

        if (!removeAllTags) {
            Set<Tag> patientTags = patient.getTags();
            if (tagsToDelete.isEmpty()) {
                throw new CommandException("No tags provided for removal. Please specify at least one tag.");
            }
            for (Tag tag : tagsToDelete) {
                if (!patientTags.contains(tag)) {
                    throw new CommandException("The tag '" + tag + "' is not present in this patient's tag list.");
                }
            }
        }

        Patient editedPatient = removeTags(patient, this.tagsToDelete, this.removeAllTags);

        model.setPatient(patient, editedPatient);
        model.updateFilteredPatientList(Model.PREDICATE_SHOW_ALL_PATIENTS);
        return new CommandResult(
                generateSuccessMessage(editedPatient, tagsToDelete, removeAllTags)
        );
    }


    /**
         * Removes the specified tags from the given patient.
         *
         * @param p The patient from whom the tags should be removed.
         * @param tagsToDelete The set of tags to remove.
         * @return A new patient instance with the tags removed.
         */
    private static Patient removeTags(Patient p, HashSet<Tag> tagsToDelete, boolean removeAllTags) {
        assert p != null;
        Set<Tag> updatedTags = new HashSet<>();
        if (!removeAllTags) {
            updatedTags.addAll(p.getTags());
            updatedTags.removeAll(tagsToDelete);
        }
        return new Patient(
                p.getName(),
                p.getPhone(),
                p.getEmail(),
                p.getAddress(),
                p.getLastVisit(),
                updatedTags,
                p.getMedicines()
        );
    }


    /**
     * Checks if another object is equal to this {@code DeleteTagCommand}.
     *
     * @param other The other object to compare with.
     * @return True if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof DeleteTagCommand)) {
            return false;
        }
        DeleteTagCommand o = (DeleteTagCommand) other;
        return index.equals(o.index) && tagsToDelete.equals(o.tagsToDelete) && removeAllTags == o.removeAllTags;
    }
}
