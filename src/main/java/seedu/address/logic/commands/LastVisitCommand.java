package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PATIENTS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.patient.LastVisit;
import seedu.address.model.patient.Patient;

/**
 * Changes the Last Visit of an existing patient in the patient list.
 */
public class LastVisitCommand extends Command {

    public static final String COMMAND_WORD = "lastVisit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the last visit of the patient identified "
            + "by the index number used in the last patient listing. "
            + "Existing last visit will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_DATE + "[LAST VISIT DATE]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DATE + "2025-05-01";

    public static final String MESSAGE_ADD_LAST_VISIT_SUCCESS = "Set last visit of patient %1$s to %2$s";
    private final Index index;
    private final LastVisit lastVisit;

    /**
     * @param index of the patient in the filtered patient list to edit the Last Visit
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
     * Generates a command execution success message after setting the last visit date.
     * {@code patientToEdit}.
     */
    private String generateSuccessMessage(Patient patientToEdit) {
        return String.format(MESSAGE_ADD_LAST_VISIT_SUCCESS,
                patientToEdit.getName(), lastVisit.lastVisitDate.toString());
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
