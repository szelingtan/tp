package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditpatientDescriptor;
import seedu.address.model.patient.Address;
import seedu.address.model.patient.Email;
import seedu.address.model.patient.Name;
import seedu.address.model.patient.Patient;
import seedu.address.model.patient.Phone;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building EditpatientDescriptor objects.
 */
public class EditPatientDescriptorBuilder {

    private EditpatientDescriptor descriptor;

    public EditPatientDescriptorBuilder() {
        descriptor = new EditpatientDescriptor();
    }

    public EditPatientDescriptorBuilder(EditpatientDescriptor descriptor) {
        this.descriptor = new EditpatientDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditpatientDescriptor} with fields containing {@code patient}'s details
     */
    public EditPatientDescriptorBuilder(Patient patient) {
        descriptor = new EditpatientDescriptor();
        descriptor.setName(patient.getName());
        descriptor.setPhone(patient.getPhone());
        descriptor.setEmail(patient.getEmail());
        descriptor.setAddress(patient.getAddress());
        descriptor.setTags(patient.getTags());
        descriptor.setMedicines(patient.getMedicines());
    }

    /**
     * Sets the {@code Name} of the {@code EditpatientDescriptor} that we are building.
     */
    public EditPatientDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditpatientDescriptor} that we are building.
     */
    public EditPatientDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditpatientDescriptor} that we are building.
     */
    public EditPatientDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditpatientDescriptor} that we are building.
     */
    public EditPatientDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditpatientDescriptor}
     * that we are building.
     */
    public EditPatientDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditpatientDescriptor build() {
        return descriptor;
    }
}
