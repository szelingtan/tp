package seedu.address.model.util;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.medicine.Medicine;
import seedu.address.model.patient.Address;
import seedu.address.model.patient.Email;
import seedu.address.model.patient.LastVisit;
import seedu.address.model.patient.Name;
import seedu.address.model.patient.Patient;
import seedu.address.model.patient.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {

    public static Patient[] getSamplePatients() {
        return new Patient[] {
            new Patient(new Name("Tan Ah Kow"), new Phone("91234567"), new Email("ahkow88@yahoo.com"),
                    new Address("Blk 30 Ang Mo Kio Ave 4 , #05-67"),
                    new LastVisit(LocalDate.parse("2025-03-04")),
                    getTagSet("diabetes", "osteoarthritis"), getMedSet("metformin", "paracetamol")),
            new Patient(new Name("Lim Siew Eng"), new Phone("91234567"), new Email("sieweng1950@hotmail.com"),
                    new Address("Blk 456 Tampines Ave 1 , #10-187"),
                    new LastVisit(LocalDate.parse("2025-03-08")),
                    getTagSet("dementia", "copd"), getMedSet("donepezil", "inhaler")),
            new Patient(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@gmail.com"),
                    new Address("Blk 30 Geylang Street 29, #06-40"), new LastVisit(LocalDate.parse("2025-02-27")),
                    getTagSet("diabetes"), getMedSet("insulin")),
            new Patient(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@yahoo.com"),
                    new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    new LastVisit(LocalDate.parse("2025-03-01")),
                    getTagSet("hypertension"), getMedSet("amlodipine")),
            new Patient(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                    new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                    new LastVisit(LocalDate.parse("2025-02-25")),
                    getTagSet("osteoporosis"), getMedSet("alendronate")),
            new Patient(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                    new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                    new LastVisit(LocalDate.parse("2025-03-03")),
                    getTagSet("cholesterol"), getMedSet("atorvastatin")),
            new Patient(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                    new Address("Blk 47 Tampines Street 20, #17-35"),
                    new LastVisit(LocalDate.parse("2025-03-05")),
                    getTagSet("depression"), getMedSet("sertraline")),
            new Patient(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                    new Address("Blk 45 Aljunied Street 85, #11-31"),
                    new LastVisit(LocalDate.parse("2025-03-07")),
                    getTagSet("arthritis"), getMedSet("paracetamol"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Patient samplePatient : getSamplePatients()) {
            sampleAb.addPatient(samplePatient);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a medicine set containing the list of strings given.
     */
    public static Set<Medicine> getMedSet(String... strings) {
        return Arrays.stream(strings)
                .map(Medicine::new)
                .collect(Collectors.toSet());
    }

}
