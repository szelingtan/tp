package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PATIENTS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.patient.Patient;

/**
 * Deletes the last visit info of a patient by setting it to "NA".
 */
public class DelLastVisitCommand extends Command {
    public static final String COMMAND_WORD = "delLastVisit";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the last visit of the patient identified "
            + "by the index number used in the last patient listing "
            + "by setting it to NA. "
            + '\n'
            + "Parameters: INDEX (must be a positive integer) "
            + '\n'
            + "Example: " + COMMAND_WORD + " 1 ";

    private final Index index;

    /**
     * Creates a {@code DelLastVisitCommand}.
     *
     * @param index The index of the patient to modify.
     */
    public DelLastVisitCommand(Index index) {
        this.index = index;
    }

    /**
     * Generates the msg to send upon successful deletion.
     *
     * @param patient The newly modified patient.
     * @return The msg to send upon successful deletion.
     */
    public String generateSuccessMessage(Patient patient) {
        return "Last visit info successfully deleted from "
                + patient.getName();
    }

    /**
     * Executes the stored `delLastVisit` cmd.
     *
     * @param model {@code Model} which the command should operate on.
     * @return The {@code CommandResult}.
     * @throws CommandException if the index is out of range.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Patient> lastShownList = model.getFilteredPatientList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }

        // Create a new Patient with `null` last visit
        Patient patientToEdit = lastShownList.get(index.getZeroBased());
        Patient editedPatient = new Patient(
                patientToEdit.getName(),
                patientToEdit.getPhone(),
                patientToEdit.getEmail(),
                patientToEdit.getAddress(),
                null,
                patientToEdit.getTags(),
                patientToEdit.getMedicines()
        );

        // Update the patient in the model
        model.setPatient(patientToEdit, editedPatient);
        model.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);

        return new CommandResult(generateSuccessMessage(editedPatient));
    }

    /**
     * Checks for {@code DelLastVisitCommand} equality.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof DelLastVisitCommand)) {
            return false;
        }
        DelLastVisitCommand o = (DelLastVisitCommand) other;
        return index.equals(o.index);
    }
}
