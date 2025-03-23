package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.medicine.Medicine;

/**
 * Jackson-friendly version of {@link Medicine}.
 */
class JsonAdaptedMed {

    private final String medName;

    /**
     * Constructs a {@code JsonAdaptedMed} with the given {@code medName}.
     */
    @JsonCreator
    public JsonAdaptedMed(String medName) {
        this.medName = medName;
    }

    /**
     * Converts a given {@code Medicine} into this class for Jackson use.
     */
    public JsonAdaptedMed(Medicine source) {
        medName = source.medName;
    }

    @JsonValue
    public String getTagName() {
        return medName;
    }

    /**
     * Converts this Jackson-friendly adapted medicine object into the model's {@code Medicine} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted tag.
     */
    public Medicine toModelType() throws IllegalValueException {
        if (!Medicine.isValidMedName(medName)) {
            throw new IllegalValueException(Medicine.MESSAGE_CONSTRAINTS);
        }
        return new Medicine(medName);
    }

}
