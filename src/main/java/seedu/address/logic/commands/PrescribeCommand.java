package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICINE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PATIENTS;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.medicine.Medicine;
import seedu.address.model.patient.Patient;

/**
 * Adds one or more medications to an existing patient in the address book.
 * This command takes an index representing a patient in the displayed list
 * and adds the specified medicine(s) to that patient's medical record.
 */
public class PrescribeCommand extends Command {
    public static final String COMMAND_WORD = "prescribe";

    public static final String MESSAGE_ADD_MED_SUCCESS = "Added medication(s) %1$s to patient: %2$s";
    public static final String MESSAGE_DUPLICATE_MED = "This patient already has medicine: %1$s";
    public static final String REPEATED_MED_ERROR = "Medicine [%1$s] was entered multiple times.\n"
            + "Duplicate inputs are not allowed.";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": adds new medication(s) to the patient specified "
            + "by the index number used in the last patient listing. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_MEDICINE + "[medicine name]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_MEDICINE + "Paracetamol";

    private final Index index;
    private final Set<Medicine> medicinesToAdd;

    /**
     * @param index of the patient in the filtered patient list to add the medication
     * @param medicinesToAdd (A set of medicines to be added to the patient)
     */
    public PrescribeCommand(Index index, Set<Medicine> medicinesToAdd) {
        requireAllNonNull(index, medicinesToAdd);

        this.index = index;
        this.medicinesToAdd = medicinesToAdd;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Patient> filteredPatientList = model.getFilteredPatientList();

        if (index.getZeroBased() >= filteredPatientList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }

        Patient patientToEdit = filteredPatientList.get(index.getZeroBased());
        Set<Medicine> currentMedicines = patientToEdit.getMedicines();

        assert currentMedicines != null;

        // Ensure no duplicates
        for (Medicine medicine : medicinesToAdd) {
            if (currentMedicines.contains(medicine)) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_MED, medicine.medName));
            }
        }

        // Create a new set of medicines
        Set<Medicine> updatedMedicines = new HashSet<>(currentMedicines);
        updatedMedicines.addAll(medicinesToAdd);

        Patient editedPatient = new Patient(
                patientToEdit.getName(),
                patientToEdit.getPhone(),
                patientToEdit.getEmail(),
                patientToEdit.getAddress(),
                patientToEdit.getLastVisit(),
                patientToEdit.getTags(),
                updatedMedicines);

        model.setPatient(patientToEdit, editedPatient);
        model.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);

        return new CommandResult(generateSuccessMessage(editedPatient));
    }

    /**
     * Generates a command execution success message about the medication(s) being successfully
     * added to {@code patientToEdit}.
     */
    private String generateSuccessMessage(Patient patientToEdit) {
        StringBuilder medicinesToAddStringBuilder = new StringBuilder();
        for (Medicine medicine : medicinesToAdd) {
            medicinesToAddStringBuilder.append(medicine.toString()).append(" ");
        }
        String medicinesToAddString = medicinesToAddStringBuilder.toString().trim();
        return String.format(MESSAGE_ADD_MED_SUCCESS,
                medicinesToAddString,
                patientToEdit.getName());
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

        return index.equals(e.index) && Objects.equals(medicinesToAdd, e.medicinesToAdd);
    }
}
