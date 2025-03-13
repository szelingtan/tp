package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICINE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.PrescribeCommand;
import seedu.address.model.medicine.Medicine;


/**
 * Tests the PrescribeCommandParser class
 * Adapted from https://se-education.org/guides/tutorials/ab3AddRemark.html
 */
public class PrescribeCommandParserTest {
    private PrescribeCommandParser parser = new PrescribeCommandParser();
    private final String nonEmptyMedName = "Panadol";

    @Test
    public void parse_indexSpecified_success() {
        // have medicine name
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_MEDICINE + nonEmptyMedName;
        PrescribeCommand expectedCommand = new PrescribeCommand(INDEX_FIRST_PERSON, new Medicine(nonEmptyMedName));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, PrescribeCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, PrescribeCommand.COMMAND_WORD, expectedMessage);

        // no index
        assertParseFailure(parser, PrescribeCommand.COMMAND_WORD + " " + nonEmptyMedName, expectedMessage);
    }
}
