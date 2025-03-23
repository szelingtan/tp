package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICINE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnprescribeCommand;
import seedu.address.model.medicine.Medicine;

import java.util.Optional;

/**
 * Tests the UnprescribeCommandParser class
 */
public class UnprescribeCommandParserTest {
    private UnprescribeCommandParser parser = new UnprescribeCommandParser();

    @Test
    public void parse_indexSpecified_success() {
        // Valid index with specific medicine
        Index targetIndex = INDEX_FIRST_PATIENT;
        String medicineName = "Panadol";
        String userInput = targetIndex.getOneBased() + " " + PREFIX_MEDICINE + medicineName;
        UnprescribeCommand expectedCommand = new UnprescribeCommand(INDEX_FIRST_PATIENT,
                new Medicine(medicineName));
        assertParseSuccess(parser, userInput, expectedCommand);

        // Valid index with "all" medicine to remove all medication
        userInput = targetIndex.getOneBased() + " " + PREFIX_MEDICINE + "all";
        expectedCommand = new UnprescribeCommand(INDEX_FIRST_PATIENT, new Medicine("all"));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingMedicineName_throwsParseException() {
        // No medicine name provided
        Index targetIndex = INDEX_FIRST_PATIENT;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_MEDICINE;
        String expectedMessage = "Medicine names should be alphanumeric";

        assertParseFailure(parser, userInput,
                expectedMessage);
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
    public void parse_invalidIndex_throwsParseException() {
        // Negative index
        String userInput = "-5 " + PREFIX_MEDICINE + "Panadol";
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnprescribeCommand.MESSAGE_USAGE));

        // Zero index
        userInput = "0 " + PREFIX_MEDICINE + "Panadol";
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnprescribeCommand.MESSAGE_USAGE));

        // Non-numeric index
        userInput = "abc " + PREFIX_MEDICINE + "Panadol";
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnprescribeCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnprescribeCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, "", expectedMessage);

        // non-integer index
        assertParseFailure(parser, "abc", expectedMessage);

        // negative index
        assertParseFailure(parser, "-1", expectedMessage);

        // zero index (index should be 1-based)
        assertParseFailure(parser, "0", expectedMessage);
    }
}
