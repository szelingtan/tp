package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX;
import static seedu.address.logic.commands.UnprescribeCommand.REMOVE_ALL_PLACEHOLDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICINE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnprescribeCommand;
import seedu.address.model.medicine.Medicine;

/**
 * Tests the UnprescribeCommandParser class
 */
public class UnprescribeCommandParserTest {
    private final UnprescribeCommandParser parser = new UnprescribeCommandParser();

    @Test
    public void parse_indexSpecified_success() {
        // Valid index with specific medicine
        Index targetIndex = INDEX_FIRST_PATIENT;
        String medicineName = "Panadol";
        String userInput = targetIndex.getOneBased() + " " + PREFIX_MEDICINE + medicineName;
        UnprescribeCommand expectedCommand = new UnprescribeCommand(INDEX_FIRST_PATIENT,
                new HashSet<>(List.of(new Medicine(medicineName))));
        assertParseSuccess(parser, userInput, expectedCommand);

        // Valid index with "all" medicine to remove all medication
        userInput = targetIndex.getOneBased() + " " + PREFIX_MEDICINE + "all";
        expectedCommand = new UnprescribeCommand(INDEX_FIRST_PATIENT, new HashSet<>(List.of(REMOVE_ALL_PLACEHOLDER)));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingMedicineName_throwsParseException() {
        // No medicine name provided
        Index targetIndex = INDEX_FIRST_PATIENT;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_MEDICINE;

        assertParseFailure(parser, userInput, Medicine.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_missingMedicinePrefix_throwsParseException() {
        // Medicine name without prefix
        Index targetIndex = INDEX_FIRST_PATIENT;
        String userInput = targetIndex.getOneBased() + " Panadol";

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnprescribeCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidFormatArgs_throwsParseException() {
        // Non-numeric index
        String userInput = "abc " + PREFIX_MEDICINE + "Panadol";
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnprescribeCommand.MESSAGE_USAGE));

        // No parameters
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnprescribeCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndexArgs_throwsParseException() {
        // Negative index
        String userInput = "-5 " + PREFIX_MEDICINE + "Panadol";
        assertParseFailure(parser, userInput, MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);

        // Zero index
        userInput = "0 " + PREFIX_MEDICINE + "Panadol";
        assertParseFailure(parser, userInput, MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnprescribeCommand.MESSAGE_USAGE);

        // No parameters
        assertParseFailure(parser, "", expectedMessage);

        // Empty index
        assertParseFailure(parser, " " + PREFIX_MEDICINE + "Panadol", expectedMessage);

        // Missing medicine parameter
        assertParseFailure(parser, "1", expectedMessage);
    }
}
