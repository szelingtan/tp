package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.LastVisit;
import seedu.address.model.person.Patient;

public class DelLastVisitCommand extends Command {
    public static final String COMMAND_WORD = "delLastVisit";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the last visit of the person identified "
            + "by the index number used in the last person listing "
            + "by setting it to NA. "
            + '\n'
            + "Parameters: INDEX (must be a positive integer) "
            + '\n'
            + "Example: " + COMMAND_WORD + " 1 ";

    private final Index index;

    public DelLastVisitCommand(Index ind) {
        index = ind;
    }

    public String generateSuccessMessage(Patient patient) {
        return "Last visit info successfully deleted from "
                + patient.getName();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Patient> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        // Create new Patient with NA last visit
        Patient patientToEdit = lastShownList.get(index.getZeroBased());
        Patient editedPatient = new Patient(
                patientToEdit.getName(),
                patientToEdit.getPhone(),
                patientToEdit.getEmail(),
                patientToEdit.getAddress(),
                new LastVisit("NA"),
                patientToEdit.getTags(),
                patientToEdit.getMedicines()
        );

        // Update the patient in the model
        model.setPerson(patientToEdit, editedPatient);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(editedPatient));
    }
}
