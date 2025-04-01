package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class HelpCommandParserTest {

    private HelpCommandParser parser = new HelpCommandParser();

    @Test
    public void parse_emptyArgs_returnsHelpCommand() {
        // Empty string as argument
        assertParseSuccess(parser, "", new HelpCommand());

        // Whitespace-only string as argument
        assertParseSuccess(parser, "   ", new HelpCommand());
    }

    @Test
    public void parse_nonEmptyArgs_throwsParseException() {
        String expectedMessage = "Command format `help extra args` is invalid.\n"
                + "The help command does not accept additional parameters.\n"
                + HelpCommand.MESSAGE_USAGE;

        // Test with extra arguments
        assertParseFailure(parser, "extra args", expectedMessage);

        // Test with different extra arguments
        String otherArgs = "something else";
        String otherExpectedMessage = "Command format `help " + otherArgs + "` is invalid.\n"
                + "The help command does not accept additional parameters.\n"
                + HelpCommand.MESSAGE_USAGE;
        assertParseFailure(parser, otherArgs, otherExpectedMessage);
    }

    @Test
    public void parse_directUse_successfullyCreatesHelpCommand() throws ParseException {
        // Test parsing directly without using the test util methods
        HelpCommand expected = new HelpCommand();
        HelpCommand actual = parser.parse("");

        // Since we've added equals to HelpCommand, we can directly compare them
        assertEquals(expected, actual);
    }
}
