package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PATIENTS;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.medicine.Medicine;
import seedu.address.model.patient.Patient;
/**
 * Removes all medication from an existing patient in the address book.
 * This command takes an index representing a patient in the displayed list
 * and removes all medicine from that patient's medical record.
 */

public class UnprescribeCommand extends Command {
    public static final String COMMAND_WORD = "unprescribe";

    public static final String MESSAGE_REMOVE_MED_SUCCESS = "Removed medication from patient: %1$s";
    public static final String MESSAGE_EMPTY_MED_LIST = "patient: %1$s currently has no "
            + "prescribed medication";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": removes all medication from the"
            + " patient specified "
            + "by the index number used in the last patient listing. "
            + "Parameters: INDEX (must be a positive integer) "
            + "Example: " + COMMAND_WORD + " 1 ";

    private final Index index;

    public UnprescribeCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Patient> lastShownList = model.getFilteredPatientList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }

        Patient patientToEdit = lastShownList.get(index.getZeroBased());
        Set<Medicine> newMedicines = patientToEdit.getMedicines();
        boolean isEmpty = newMedicines.isEmpty();
        newMedicines.clear();
        Patient editedPatient = new Patient(
                patientToEdit.getName(), patientToEdit.getPhone(), patientToEdit.getEmail(),
                patientToEdit.getAddress(),
                patientToEdit.getLastVisit(),
                patientToEdit.getTags(),
                newMedicines);

        model.setPatient(patientToEdit, editedPatient);
        model.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);

        return new CommandResult(generateSuccessMessage(editedPatient, isEmpty));
    }

    /**
     * Generates a command execution success message based on whether
     * the initial set of medication is empty or non-empty
     * {@code patientToEdit}.
     */
    private String generateSuccessMessage(Patient patientToEdit, boolean isEmpty) {
        String message = isEmpty ? MESSAGE_EMPTY_MED_LIST : MESSAGE_REMOVE_MED_SUCCESS;
        return String.format(message, patientToEdit.getName());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof to check if other is also a UnprescribeCommand
        if (!(other instanceof UnprescribeCommand e)) {
            return false;
        }

        return index.equals(e.index);
    }
}
