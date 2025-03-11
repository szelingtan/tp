package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VISIT;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.LastVisit;
import seedu.address.model.person.Patient;

/**
 * Changes the remark of an existing person in the address book.
 */
public class LastVisitCommand extends Command {

    public static final String COMMAND_WORD = "lastVisit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the last visit of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing last visit will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_VISIT + "[LAST VISIT]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_VISIT + "Last visited on 5 March 2025. Followed medication schedule well.";

    public static final String MESSAGE_ADD_LAST_VISIT_SUCCESS = "Added last visit to Person: %1$s";
    public static final String MESSAGE_DELETE_LAST_VISIT_SUCCESS = "Removed last visit from Person: %1$s";

    private final Index index;
    private final LastVisit lastVisit;

    /**
     * @param index of the person in the filtered person list to edit the remark
     * @param lastVisit of the person to be updated to
     */
    public LastVisitCommand(Index index, LastVisit lastVisit) {
        requireAllNonNull(index, lastVisit);

        this.index = index;
        this.lastVisit = lastVisit;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Patient> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Patient personToEdit = lastShownList.get(index.getZeroBased());
        Patient editedPerson = new Patient(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), lastVisit, personToEdit.getTags());

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Generates a command execution success message based on whether the remark is added to or removed from
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Patient personToEdit) {
        String message = !lastVisit.value.isEmpty() ? MESSAGE_ADD_LAST_VISIT_SUCCESS
                : MESSAGE_DELETE_LAST_VISIT_SUCCESS;
        return String.format(message, personToEdit);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LastVisitCommand)) {
            return false;
        }

        // state check
        LastVisitCommand e = (LastVisitCommand) other;
        return index.equals(e.index)
                && lastVisit.equals(e.lastVisit);
    }
}
