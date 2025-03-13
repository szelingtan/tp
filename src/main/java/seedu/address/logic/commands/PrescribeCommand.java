package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRESCRIBE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.medicine.Medicine;
import seedu.address.model.person.Patient;

/**
 * Adds a medication to an existing patient in the address book.
 * This command takes an index representing a patient in the displayed list
 * and adds the specified medicine to that patient's medical record.
 */
public class PrescribeCommand extends Command {
    public static final String COMMAND_WORD = "prescribe";

    private static final String MESSAGE_ADD_MED_SUCCESS = "Added medication to Person: %1$s";
    private static final String MESSAGE_DELETE_MED_SUCCESS = "Removed medication from Person: %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": adds a new medication to the person specified "
            + "by the index number used in the last person listing. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_PRESCRIBE + "[medicine name]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PRESCRIBE + "Paracetamol";

    private final Index index;
    private final Medicine medicine;

    /**
     * @param index of the person in the filtered person list to edit the remark
     * @param medicine (name of the medicine to be added to the patient)
     */
    public PrescribeCommand(Index index, Medicine medicine) {
        requireAllNonNull(index, medicine);

        this.index = index;
        this.medicine = medicine;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Patient> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Patient patientToEdit = lastShownList.get(index.getZeroBased());
        Set<Medicine> newMedicines = patientToEdit.getMedicines();
        newMedicines.add(medicine);
        Patient editedPatient = new Patient(
                patientToEdit.getName(), patientToEdit.getPhone(), patientToEdit.getEmail(),
                patientToEdit.getAddress(),
                patientToEdit.getLastVisit(),
                patientToEdit.getTags(),
                newMedicines);

        model.setPerson(patientToEdit, editedPatient);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(editedPatient));
    }

    /**
     * Generates a command execution success message based on whether
     * the remark is added to or removed from
     * {@code patientToEdit}.
     */
    private String generateSuccessMessage(Patient patientToEdit) {
        String message = !medicine.medName.isEmpty() ? MESSAGE_ADD_MED_SUCCESS : MESSAGE_DELETE_MED_SUCCESS;
        // should this be a del med message or an error message that the medName does not exist - sl
        return String.format(message, Messages.format(patientToEdit));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof to check if other is also a PrescribeCommand
        if (!(other instanceof PrescribeCommand e)) {
            return false;
        }

        return index.equals(e.index) && Objects.equals(medicine, e.medicine);
    }
}
