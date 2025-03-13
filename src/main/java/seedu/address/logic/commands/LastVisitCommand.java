package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VISIT;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PATIENTS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.patient.LastVisit;
import seedu.address.model.patient.Patient;

/**
 * Changes the remark of an existing patient in the address book.
 */
public class LastVisitCommand extends Command {

    public static final String COMMAND_WORD = "lastVisit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the last visit of the patient identified "
            + "by the index number used in the last patient listing. "
            + "Existing last visit will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_VISIT + "[LAST VISIT]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_VISIT + "Last visited on 5 March 2025. Followed medication schedule well.";

    public static final String MESSAGE_ADD_LAST_VISIT_SUCCESS = "Added last visit to patient: %1$s";
    public static final String MESSAGE_DELETE_LAST_VISIT_SUCCESS = "Removed last visit from patient: %1$s";

    private final Index index;
    private final LastVisit lastVisit;

    /**
     * @param index of the patient in the filtered patient list to edit the remark
     * @param lastVisit of the patient to be updated to
     */
    public LastVisitCommand(Index index, LastVisit lastVisit) {
        requireAllNonNull(index, lastVisit);

        this.index = index;
        this.lastVisit = lastVisit;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Patient> lastShownList = model.getFilteredPatientList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }

        Patient patientToEdit = lastShownList.get(index.getZeroBased());
        Patient editedpatient = new Patient(patientToEdit.getName(), patientToEdit.getPhone(), patientToEdit.getEmail(),
                patientToEdit.getAddress(), lastVisit, patientToEdit.getTags(), patientToEdit.getMedicines());

        model.setPatient(patientToEdit, editedpatient);
        model.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);

        return new CommandResult(generateSuccessMessage(editedpatient));
    }

    /**
     * Generates a command execution success message based on whether the remark is added to or removed from
     * {@code patientToEdit}.
     */
    private String generateSuccessMessage(Patient patientToEdit) {
        String message = !lastVisit.value.isEmpty() ? MESSAGE_ADD_LAST_VISIT_SUCCESS
                : MESSAGE_DELETE_LAST_VISIT_SUCCESS;
        return String.format(message, patientToEdit);
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
