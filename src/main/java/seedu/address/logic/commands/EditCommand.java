package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PATIENTS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.medicine.Medicine;
import seedu.address.model.patient.Address;
import seedu.address.model.patient.Email;
import seedu.address.model.patient.LastVisit;
import seedu.address.model.patient.Name;
import seedu.address.model.patient.Patient;
import seedu.address.model.patient.Phone;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing patient in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the patient identified "
            + "by the index number used in the displayed patient list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PATIENT_SUCCESS = "Edited patient: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PATIENT = "This patient already exists in the address book.";

    private final Index index;
    private final EditpatientDescriptor editpatientDescriptor;

    /**
     * @param index of the patient in the filtered patient list to edit
     * @param editpatientDescriptor details to edit the patient with
     */
    public EditCommand(Index index, EditpatientDescriptor editpatientDescriptor) {
        requireNonNull(index);
        requireNonNull(editpatientDescriptor);

        this.index = index;
        this.editpatientDescriptor = new EditpatientDescriptor(editpatientDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Patient> lastShownList = model.getFilteredPatientList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }

        Patient patientToEdit = lastShownList.get(index.getZeroBased());
        Patient editedPatient = createEditedpatient(patientToEdit, editpatientDescriptor);

        if (!patientToEdit.isSamePatient(editedPatient) && model.hasPatient(editedPatient)) {
            throw new CommandException(MESSAGE_DUPLICATE_PATIENT);
        }

        model.setPatient(patientToEdit, editedPatient);
        model.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);
        return new CommandResult(String.format(MESSAGE_EDIT_PATIENT_SUCCESS, Messages.format(editedPatient)));
    }

    /**
     * Creates and returns a {@code patient} with the details of {@code patientToEdit}
     * edited with {@code editpatientDescriptor}.
     */
    private static Patient createEditedpatient(Patient patientToEdit, EditpatientDescriptor editpatientDescriptor) {
        assert patientToEdit != null;

        Name updatedName = editpatientDescriptor.getName().orElse(patientToEdit.getName());
        Phone updatedPhone = editpatientDescriptor.getPhone().orElse(patientToEdit.getPhone());
        Email updatedEmail = editpatientDescriptor.getEmail().orElse(patientToEdit.getEmail());
        Address updatedAddress = editpatientDescriptor.getAddress().orElse(patientToEdit.getAddress());
        LastVisit updatedLastVisit = editpatientDescriptor.getLastVisit().orElse(patientToEdit.getLastVisit());
        Set<Tag> updatedTags = editpatientDescriptor.getTags().orElse(patientToEdit.getTags());
        Set<Medicine> updatedMedicines = editpatientDescriptor.getMeds().orElse(patientToEdit.getMedicines());
        // edit command does not allow editing medicines

        return new Patient(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedLastVisit,
                updatedTags, updatedMedicines);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editpatientDescriptor.equals(otherEditCommand.editpatientDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editpatientDescriptor", editpatientDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the patient with. Each non-empty field value will replace the
     * corresponding field value of the patient.
     */
    public static class EditpatientDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private LastVisit lastVisit;
        private Set<Tag> tags;
        private Set<Medicine> medicines;

        public EditpatientDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditpatientDescriptor(EditpatientDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setLastVisit(toCopy.lastVisit);
            setTags(toCopy.tags);
            setMedicines(toCopy.medicines);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, lastVisit, address, tags, medicines);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setLastVisit(LastVisit visit) {
            this.lastVisit = visit;
        }

        public Optional<LastVisit> getLastVisit() {
            return Optional.ofNullable(lastVisit);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        /**
         * Sets {@code medicines} to this object's {@code medicines}.
         * A defensive copy of {@code medicines} is used internally.
         */
        public void setMedicines(Set<Medicine> meds) {
            this.medicines = (meds != null) ? new HashSet<>(meds) : null;
        }

        /**
         * Returns the medicines set
         * Returns {@code Optional#empty()} if {@code medicines} is null.
         */
        public Optional<Set<Medicine>> getMeds() {
            return (medicines != null) ? Optional.of(medicines) : Optional.empty();
        }


        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditpatientDescriptor)) {
                return false;
            }

            EditpatientDescriptor otherEditpatientDescriptor = (EditpatientDescriptor) other;
            return Objects.equals(name, otherEditpatientDescriptor.name)
                    && Objects.equals(phone, otherEditpatientDescriptor.phone)
                    && Objects.equals(email, otherEditpatientDescriptor.email)
                    && Objects.equals(address, otherEditpatientDescriptor.address)
                    && Objects.equals(tags, otherEditpatientDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("lastVisit", lastVisit)
                    .add("tags", tags)
                    .add("medicines", medicines)
                    .toString();
        }
    }
}
