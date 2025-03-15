package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICINE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnprescribeCommand;

/**
 * Tests the UnprescribeCommandParser class
 */
public class UnprescribeCommandParserTest {
    private UnprescribeCommandParser parser = new UnprescribeCommandParser();

    @Test
    public void parse_indexSpecified_success() {
        // basic index only
        Index targetIndex = INDEX_FIRST_PATIENT;
        String userInput = targetIndex.getOneBased() + "";
        UnprescribeCommand expectedCommand = new UnprescribeCommand(INDEX_FIRST_PATIENT);
        assertParseSuccess(parser, userInput, expectedCommand);

        // index with medicine prefix (should still work as parser doesn't use it)
        userInput = targetIndex.getOneBased() + " " + PREFIX_MEDICINE;
        expectedCommand = new UnprescribeCommand(INDEX_FIRST_PATIENT);
        assertParseSuccess(parser, userInput, expectedCommand);

        // index with medicine prefix and medicine (should still work as parser doesn't process it)
        String medicineName = "Panadol";
        userInput = targetIndex.getOneBased() + " " + PREFIX_MEDICINE + medicineName;
        expectedCommand = new UnprescribeCommand(INDEX_FIRST_PATIENT);
        assertParseSuccess(parser, userInput, expectedCommand);
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