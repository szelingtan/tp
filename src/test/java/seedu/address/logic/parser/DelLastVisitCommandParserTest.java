package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DelLastVisitCommand;

public class DelLastVisitCommandParserTest {
    @Test
    public void parse_validArg_success() {
        // Copied from DeleteCommandParserTest.java
        assertParseSuccess(
                new DelLastVisitCommandParser(),
                "1",
                new DelLastVisitCommand(INDEX_FIRST_PERSON)
        );
    }

    @Test
    public void parse_invArg_error() {
        // Copied from DeleteCommandParserTest.java
        assertParseFailure(
                new DelLastVisitCommandParser(),
                "Homura",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        DelLastVisitCommand.MESSAGE_USAGE)
        );
    }
}
