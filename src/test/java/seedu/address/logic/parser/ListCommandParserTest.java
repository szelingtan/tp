package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ListCommandParserTest {

    private ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_emptyArgs_returnsListCommand() {
        // Empty string as argument
        assertParseSuccess(parser, "", new ListCommand());

        // Whitespace-only string as argument
        assertParseSuccess(parser, "   ", new ListCommand());
    }

    @Test
    public void parse_nonEmptyArgs_throwsParseException() {
        String expectedMessage = "Command format `list extra args` is invalid.\n"
                + "The list command does not accept additional parameters.\n"
                + "list: Lists all patients in the address book.";

        // Test with extra arguments
        assertParseFailure(parser, "extra args", expectedMessage);

        // Test with different extra arguments
        String otherArgs = "something else";
        String otherExpectedMessage = "Command format `list " + otherArgs + "` is invalid.\n" +
                "The list command does not accept additional parameters.\n" +
                "list: Lists all patients in the address book.";
        assertParseFailure(parser, otherArgs, otherExpectedMessage);
    }

    @Test
    public void parse_directUse_successfullyCreatesListCommand() throws ParseException {
        // Test parsing directly without using the test util methods
        ListCommand expected = new ListCommand();
        ListCommand actual = parser.parse("");

        // Since ListCommand likely doesn't override equals, we're checking that both
        // instances are of the same class
        assertEquals(expected.getClass(), actual.getClass());
    }
}
