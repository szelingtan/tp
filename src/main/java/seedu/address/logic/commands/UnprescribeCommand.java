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
 * Removes the specified medication from an existing patient in the patient list.
 * This command takes an index representing a patient in the displayed list
 * and removes the specified medicine from that patient's medical record.
 */
public class UnprescribeCommand extends Command {
    public static final Medicine REMOVE_ALL_PLACEHOLDER = new Medicine("-");
    public static final String COMMAND_WORD = "unprescribe";

    public static final String MESSAGE_REMOVE_MED_SUCCESS = "Removed medication(s) %1$s from patient: %2$s";
    public static final String MESSAGE_REMOVE_ALL_MED_SUCCESS = "Removed all medication from patient: %1$s";
    public static final String MESSAGE_EMPTY_MED_LIST = "Patient: %1$s currently has no prescribed medication";
    public static final String MESSAGE_MED_NOT_FOUND = "Medication %1$s is not found in patient: "
            + "%2$s's prescription list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": removes one or more medications "
            + "from the patient specified "
            + "by the index number used in the last patient listing. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_MEDICINE + "[medicine name] OR "
            + PREFIX_MEDICINE + "all\n"
            + "Example 1: " + COMMAND_WORD + " 1 "
            + PREFIX_MEDICINE + "Paracetamol\n"
            + "Example 2: " + COMMAND_WORD + " 1 "
            + PREFIX_MEDICINE + "all";

    private final Index index;
    private final Set<Medicine> medicinesToRemove;

    /**
     * @param index of the patient in the filtered patient list to remove the medication from
     * @param medicinesToRemove the set of medicines to be removed from the patient
     */
    public UnprescribeCommand(Index index, Set<Medicine> medicinesToRemove) {
        requireAllNonNull(index);
        requireAllNonNull(medicinesToRemove);
        this.index = index;
        this.medicinesToRemove = medicinesToRemove;
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
            throw new CommandException(String.format(MESSAGE_EMPTY_MED_LIST, patientToEdit.getName()));
        }

        // Special case for "all" to remove all medications
        if (medicinesToRemove.contains(REMOVE_ALL_PLACEHOLDER)) {
            return removeAllMedication(model, patientToEdit);
        }

        // Remove specified medications
        return removeSpecifiedMedications(model, patientToEdit);
    }

    /**
     * Removes all medication from the patient
     */
    private CommandResult removeAllMedication(Model model, Patient patientToEdit) {
        Patient editedPatient = createEditedPatient(patientToEdit, new HashSet<>());

        model.setPatient(patientToEdit, editedPatient);
        model.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);

        return new CommandResult(generateSuccessMessageForAll(editedPatient));
    }

    /**
     * Removes a set of medications from the patient
     */
    private CommandResult removeSpecifiedMedications(Model model, Patient patientToEdit) throws CommandException {
        Set<Medicine> currentMedicines = patientToEdit.getMedicines();
        Set<Medicine> updatedMedicines = new HashSet<>();

        // Ensure that the patient has all medicines to be removed
        for (Medicine medicineToRemove : medicinesToRemove) {
            if (!currentMedicines.contains(medicineToRemove)) {
                throw new CommandException(String.format(MESSAGE_MED_NOT_FOUND,
                        medicineToRemove.getMedicineName(), patientToEdit.getName()));
            }
        }

        // Create new set of medicines
        for (Medicine currentMedicine : currentMedicines) {
            if (!medicinesToRemove.contains(currentMedicine)) {
                updatedMedicines.add(currentMedicine);
            }
        }

        Patient editedPatient = createEditedPatient(patientToEdit, updatedMedicines);

        model.setPatient(patientToEdit, editedPatient);
        model.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);

        return new CommandResult(generateSuccessMessage(editedPatient));
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

    /**
     * Generates a command execution success message when removing a specific medication from
     * {@code patientToEdit}.
     */
    private String generateSuccessMessage(Patient patientToEdit) {
        StringBuilder medStringBuilder = new StringBuilder();
        for (Medicine medicine : medicinesToRemove) {
            medStringBuilder.append(medicine.toString()).append(" ");
        }
        String medsString = medStringBuilder.toString().trim();
        return String.format(MESSAGE_REMOVE_MED_SUCCESS, medsString, patientToEdit.getName());
    }

    /**
     * Generates a command execution success message when removing all medications from
     * {@code patientToEdit}.
     */
    private String generateSuccessMessageForAll(Patient patientToEdit) {
        return String.format(MESSAGE_REMOVE_ALL_MED_SUCCESS, patientToEdit.getName());
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

        return index.equals(e.index) && Objects.equals(medicinesToRemove, e.medicinesToRemove);
    }
}
