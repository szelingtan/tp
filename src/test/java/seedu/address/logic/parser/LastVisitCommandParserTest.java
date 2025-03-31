package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_DATE_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_LAST_VISIT_DATE;
import static seedu.address.logic.commands.CommandTestUtil.LAST_VISIT_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.LastVisitCommand;
import seedu.address.model.patient.LastVisit;

public class LastVisitCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, LastVisitCommand.MESSAGE_USAGE);

    private final LastVisitCommandParser parser = new LastVisitCommandParser();
    private final String validDate = "2020-01-01";
    private final String invalidFutureDate = "2030-01-02";
    private final String invalidFormatDate = "2020-014-21";

    @Test
    public void parse_indexSpecified_success() {
        // valid date given
        String userInput = INDEX_FIRST_PATIENT.getOneBased() + " " + PREFIX_DATE + validDate;
        LastVisitCommand expectedCommand = new LastVisitCommand(INDEX_FIRST_PATIENT,
                new LastVisit(LocalDate.parse(validDate)));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, LastVisitCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, LastVisitCommand.COMMAND_WORD, expectedMessage);

        // no index
        assertParseFailure(parser, LastVisitCommand.COMMAND_WORD + " " + validDate, expectedMessage);

        // no prefix
        assertParseFailure(parser, LastVisitCommand.COMMAND_WORD + " " + invalidFutureDate, expectedMessage);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + LAST_VISIT_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + LAST_VISIT_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ 2020-01-01", MESSAGE_INVALID_FORMAT);
    }


    @Test
    public void parse_invalidFutureDate_failure() {
        // Given a future date (not valid), parsing should fail
        String userInput = INDEX_FIRST_PATIENT.getOneBased()
                + " " + PREFIX_DATE + invalidFutureDate;

        assertParseFailure(parser, userInput, MESSAGE_INVALID_LAST_VISIT_DATE);
    }

    @Test
    public void parse_invalidDate_failure() {
        // Given a malformed date (like "2020-014-21"), parsing should fail
        String userInput = INDEX_FIRST_PATIENT.getOneBased()
                + " " + PREFIX_DATE + invalidFormatDate;

        assertParseFailure(parser, userInput, MESSAGE_INVALID_DATE_FORMAT);
    }


}
