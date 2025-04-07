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
 * Adds one or more tags to a specified patient.
 */
public class TagCommand extends Command {
    public static final String COMMAND_WORD = "tag";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds the listed tags to the patient identified by the index "
            + "number used in the last patient listing."
            + '\n'
            + "Parameters: INDEX t/tag [t/more_tags]..."
            + '\n'
            + "Example: " + COMMAND_WORD + " 3 t/High Blood Pressure "
            + "t/Seafood Allergy";
    public static final String NO_TAG_INCLUDED_ERROR =
            "Please add at least one tag when using the tag command."
            + '\n'
            + "Tags can be specified using the `t/` prefix";
    public static final String EMPTY_TAG_ERROR = "Tag must be non-empty."
            + '\n'
            + "Example: " + COMMAND_WORD + " 2 t/Diabetes";
    public static final String REPEATED_TAG_ERROR =
            "Tag [%1$s] was inputted multiple times";

    private final Index index;
    private final HashSet<Tag> tagsToAdd;

    /**
     * Creates a {@code TagCommand}.
     *
     * @param index The index of the patient to modify.
     * @param tagsToAdd The set of tags to add.
     */
    public TagCommand(Index index, HashSet<Tag> tagsToAdd) {
        this.index = index;
        this.tagsToAdd = tagsToAdd;
    }

    /**
     * Generates the msg to send upon successful addition.
     *
     * @param patient The newly modified patient.
     * @return The msg to send upon successful addition.
     */
    private static String generateSuccessMessage(Patient patient,
              HashSet<Tag> tagsToAdd) {
        HashSet<String> tagStrsToAdd = new HashSet<String>();
        for (Tag t : tagsToAdd) {
            tagStrsToAdd.add(t.toString());
        }
        return "Tags " + String.join(", ", tagStrsToAdd)
                + " successfully added to " + patient.getName();
    }

    private static String generateExistingTagErrMsg(
        HashSet<Tag> duplicateTags, Patient patient) {
        String tagsStr = "";
        for (Tag t : duplicateTags) {
            tagsStr += t.toString() + " ";
        }
        return "Tag(s) " + tagsStr + "already exist for " + patient.getName();
    }

    /**
     * Executes the stored `tag` command.
     *
     * @param model {@code Model} which the command should operate on.
     * @return The {@code CommandResult}.
     * @throws CommandException if the index is out of range.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Patient> lastShownList = model.getFilteredPatientList();

        // Index must be valid
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }

        Patient patient = lastShownList.get(index.getZeroBased());
        Set<Tag> patientTags = patient.getTags();

        // Do not add already existing tags
        HashSet<Tag> duplicateTags = new HashSet<Tag>();
        for (Tag t : tagsToAdd) {
            if (patientTags.contains(t)) {
                duplicateTags.add(t);
            }
        }
        if (duplicateTags.size() != 0) {
            throw new CommandException(
                    generateExistingTagErrMsg(duplicateTags, patient)
            );
        }

        Patient editedPatient = addTags(patient, this.tagsToAdd);

        model.setPatient(patient, editedPatient);
        model.updateFilteredPatientList(Model.PREDICATE_SHOW_ALL_PATIENTS);
        return new CommandResult(
                generateSuccessMessage(editedPatient, tagsToAdd)
        );
    }

    /**
     * Adds the list of tags to the given patient.
     *
     * @param p The patient we're adding tags to.
     * @param tagsToAdd The set of tags to add.
     * @return A new patient with the tags added.
     */
    private static Patient addTags(Patient p, HashSet<Tag> tagsToAdd) {
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

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof TagCommand)) {
            return false;
        }
        TagCommand o = (TagCommand) other;
        return index.equals(o.index) && tagsToAdd.equals(o.tagsToAdd);
    }
}
