package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICINE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.medicine.Medicine;
import seedu.address.model.person.Patient;
/**
 * Removes all medication from an existing patient in the address book.
 * This command takes an index representing a patient in the displayed list
 * and removes all medicine from that patient's medical record.
 */

public class UnprescribeCommand extends Command {
    public static final String COMMAND_WORD = "unprescribe";

    private static final String MESSAGE_REMOVE_MED_SUCCESS = "Removed medication from Person: %1$s";
    private static final String MESSAGE_EMPTY_MED_LIST = "Person: %1$s currently has no "
            + "prescribed medication";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": removes all medication from the"
            + " person specified "
            + "by the index number used in the last person listing. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_MEDICINE + "[medicine name]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_MEDICINE + "Paracetamol";


    private final Index index;

    public UnprescribeCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Patient> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
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

        model.setPerson(patientToEdit, editedPatient);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(editedPatient, isEmpty));
    }

    /**
     * Generates a command execution success message based on whether
     * the initial set of medication is empty or non-empty
     * {@code patientToEdit}.
     */
    private String generateSuccessMessage(Patient patientToEdit, boolean isEmpty) {
        String message = isEmpty ? MESSAGE_EMPTY_MED_LIST : MESSAGE_REMOVE_MED_SUCCESS;
        return String.format(message, Messages.format(patientToEdit));
    }
}
