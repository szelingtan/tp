package seedu.address.testutil;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.medicine.Medicine;
import seedu.address.model.patient.Address;
import seedu.address.model.patient.Email;
import seedu.address.model.patient.LastVisit;
import seedu.address.model.patient.Name;
import seedu.address.model.patient.Patient;
import seedu.address.model.patient.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building patient objects.
 */
public class PatientBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_LAST_VISITDATE = "2020-01-01";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private LastVisit lastVisit;
    private Set<Tag> tags;
    private Set<Medicine> medicines;

    /**
     * Creates a {@code patientBuilder} with the default details.
     */
    public PatientBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        lastVisit = new LastVisit(LocalDate.parse(DEFAULT_LAST_VISITDATE));
        tags = new HashSet<>();
        medicines = new HashSet<>();
    }

    /**
     * Initializes the patientBuilder with the data of {@code patientToCopy}.
     */
    public PatientBuilder(Patient patientToCopy) {
        name = patientToCopy.getName();
        phone = patientToCopy.getPhone();
        email = patientToCopy.getEmail();
        address = patientToCopy.getAddress();
        lastVisit = patientToCopy.getLastVisit();
        tags = new HashSet<>(patientToCopy.getTags());
        medicines = new HashSet<>(patientToCopy.getMedicines());
    }

    /**
     * Sets the {@code Name} of the {@code patient} that we are building.
     */
    public PatientBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code patient} that we are building.
     */
    public PatientBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Parses the {@code medicines} into a {@code Set<Medicine>} and set it to the {@code patient} that we are building.
     */
    public PatientBuilder withMeds(String ... meds) {
        this.medicines = SampleDataUtil.getMedSet(meds);
        return this;
    }

    /**
     * Takes in a {@code Set<Medicine>} and set it to the {@code patient} that we are building.
     */
    public PatientBuilder withMeds(Set<Medicine> medicines) {
        this.medicines = medicines;
        return this;
    }


    /**
     * Sets the {@code Address} of the {@code patient} that we are building.
     */
    public PatientBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code patient} that we are building.
     */
    public PatientBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code patient} that we are building.
     */
    public PatientBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Last Visit} of the {@code patient} that we are building.
     */
    public PatientBuilder withLastVisit(String lastVisitDate) {
        if (lastVisitDate == null) {
            this.lastVisit = null;
        } else {
            this.lastVisit = new LastVisit(LocalDate.parse(lastVisitDate));
        }
        return this;
    }

    public Patient build() {
        return new Patient(name, phone, email, address, lastVisit, tags, medicines);
    }

}
