package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICINE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PATIENTS;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.medicine.Medicine;
import seedu.address.model.patient.Patient;

/**
 * Removes specific medication or all medications from an existing patient in the address book.
 * This command takes an index representing a patient in the displayed list
 * and removes either a specific medicine or all medicines from that patient's medical record.
 */
public class UnprescribeCommand extends Command {
    public static final String COMMAND_WORD = "unprescribe";

    public static final String MESSAGE_REMOVE_MED_SUCCESS = "Removed medication %1$s from patient: %2$s";
    public static final String MESSAGE_REMOVE_ALL_MED_SUCCESS = "Removed all medication from patient: %1$s";
    public static final String MESSAGE_EMPTY_MED_LIST = "Patient: %1$s currently has no prescribed medication";
    public static final String MESSAGE_MED_NOT_FOUND = "Medication %1$s is not found in patient: "
            + "%2$s's prescription list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": removes specific medication or all medication from the"
            + " patient specified by the index number used in the last patient listing.\n"
            + "Parameters: INDEX (must be a positive integer) [m/MEDICINE_NAME] OR [m/all]\n"
            + "If m/MEDICINE_NAME is not provided or 'all' is specified, all medications will be removed.\n"
            + "Example 1 : " + COMMAND_WORD + " 1 " + PREFIX_MEDICINE + "Paracetamol"
            + "Example 2 : " + COMMAND_WORD + " 1 " + PREFIX_MEDICINE + "all";

    private final Index index;
    private final Optional<String> medicineToRemove;

    /**
     * Creates an UnprescribeCommand to remove the specified medication from the specified patient
     *
     * @param index index of the patient in the filtered patient list
     * @param medicineToRemove the medicine to remove, or empty to remove all medicines
     */
    public UnprescribeCommand(Index index, Optional<String> medicineToRemove) {
        this.index = index;
        this.medicineToRemove = medicineToRemove;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Patient> lastShownList = model.getFilteredPatientList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }

        Patient patientToEdit = lastShownList.get(index.getZeroBased());
        Set<Medicine> currentMedicines = patientToEdit.getMedicines();

        // Check if medicine list is already empty
        if (currentMedicines.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_EMPTY_MED_LIST, Messages.format(patientToEdit)));
        }

        // Case: remove all medication
        if (medicineToRemove.isEmpty() || medicineToRemove.get().equalsIgnoreCase("all")) {
            return removeAllMedication(model, patientToEdit);
        }

        // Case: remove specific medication
        return removeSpecificMedication(model, patientToEdit, medicineToRemove.get());
    }

    /**
     * Removes all medication from the patient
     */
    private CommandResult removeAllMedication(Model model, Patient patientToEdit) {
        Set<Medicine> newMedicines = new HashSet<>();
        Patient editedPatient = createEditedPatient(patientToEdit, newMedicines);

        model.setPatient(patientToEdit, editedPatient);
        model.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);

        return new CommandResult(String.format(MESSAGE_REMOVE_ALL_MED_SUCCESS,
                Messages.format(editedPatient)));
    }

    /**
     * Removes a specific medication from the patient
     */
    private CommandResult removeSpecificMedication(Model model, Patient patientToEdit, String medicineName)
            throws CommandException {
        Set<Medicine> currentMedicines = patientToEdit.getMedicines();
        Set<Medicine> newMedicines = new HashSet<>();
        boolean medicineFound = false;

        // Copy all medicines except the one to be removed
        for (Medicine medicine : currentMedicines) {
            if (medicine.getMedicineName().equals(medicineName)) {
                medicineFound = true;
            } else {
                newMedicines.add(medicine);
            }
        }

        // If medicine was not found, throw an exception
        if (!medicineFound) {
            throw new CommandException(String.format(MESSAGE_MED_NOT_FOUND,
                    medicineName, Messages.format(patientToEdit)));
        }

        Patient editedPatient = createEditedPatient(patientToEdit, newMedicines);

        model.setPatient(patientToEdit, editedPatient);
        model.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);

        return new CommandResult(String.format(MESSAGE_REMOVE_MED_SUCCESS,
                medicineName, Messages.format(editedPatient)));
    }

    /**
     * Creates and returns a {@code Patient} with the details of {@code patientToEdit}
     * with updated medicines.
     */
    private Patient createEditedPatient(Patient patientToEdit, Set<Medicine> medicines) {
        return new Patient(
                patientToEdit.getName(),
                patientToEdit.getPhone(),
                patientToEdit.getEmail(),
                patientToEdit.getAddress(),
                patientToEdit.getLastVisit(),
                patientToEdit.getTags(),
                medicines);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof to check if other is also an UnprescribeCommand
        if (!(other instanceof UnprescribeCommand e)) {
            return false;
        }

        return index.equals(e.index)
                && medicineToRemove.equals(e.medicineToRemove);
    }
}
