package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.medicine.Medicine;
import seedu.address.model.patient.Address;
import seedu.address.model.patient.Email;
import seedu.address.model.patient.LastVisit;
import seedu.address.model.patient.Name;
import seedu.address.model.patient.Patient;
import seedu.address.model.patient.Phone;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Patient}.
 */
class JsonAdaptedPatient {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "patient's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String lastVisit;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedMed> meds = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedpatient} with the given patient details.
     */
    @JsonCreator
    public JsonAdaptedPatient(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                              @JsonProperty("email") String email, @JsonProperty("address") String address,
                              @JsonProperty("last visit") String lastVisit,
                              @JsonProperty("tags") List<JsonAdaptedTag> tags,
                              @JsonProperty("meds") List<JsonAdaptedMed> meds) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.lastVisit = lastVisit;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (meds != null) {
            this.meds.addAll(meds);
        }
    }

    /**
     * Converts a given {@code patient} into this class for Jackson use.
     */
    public JsonAdaptedPatient(Patient source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        if (!(source.getLastVisit() == null)) {
            lastVisit = source.getLastVisit().value;
        } else {
            lastVisit = null;
        }
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .toList());
        meds.addAll(source.getMedicines().stream()
                .map(JsonAdaptedMed::new)
                .toList());
    }

    /**
     * Converts this Jackson-friendly adapted patient object into the model's {@code patient} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted patient.
     */
    public Patient toModelType() throws IllegalValueException {
        final List<Tag> patientTags = new ArrayList<>();
        final List<Medicine> patientMeds = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            patientTags.add(tag.toModelType());
        }

        for (JsonAdaptedMed med : meds) {
            patientMeds.add(med.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

//        if (lastVisit == null) {
//            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
//                    LastVisit.class.getSimpleName()));
//        }
//        final LastVisit modelLastVisit = new LastVisit(lastVisit);

        final LastVisit modelLastVisit;
        if (lastVisit != null) {
            modelLastVisit = new LastVisit(lastVisit);
        } else {
            modelLastVisit = null;
        }

        final Set<Tag> modelTags = new HashSet<>(patientTags);

        final Set<Medicine> modelMedicines = new HashSet<>(patientMeds);

        return new Patient(modelName, modelPhone, modelEmail, modelAddress, modelLastVisit, modelTags, modelMedicines);
    }

}
