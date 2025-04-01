package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteTagCommand;
import seedu.address.model.tag.Tag;

public class DeleteTagCommandParserTest {
    @Test
    public void parse_validArg_success() {
        // Testing successful parsing of valid delete tag command
        assertParseSuccess(
                new DeleteTagCommandParser(),
                "1 t/Madoka t/Homura",
                new DeleteTagCommand(
                        INDEX_FIRST_PATIENT,
                        new HashSet<>(Set.of(
                                new Tag("Madoka"),
                                new Tag("Homura")
                        )),
                        false // removeAllTags = false
                )
        );
    }

    @Test
    public void parse_validAllArg_success() {
        // Testing successful parsing of delete all tags command
        assertParseSuccess(
                new DeleteTagCommandParser(),
                "1 t/all",
                new DeleteTagCommand(
                        INDEX_FIRST_PATIENT,
                        new HashSet<>(),
                        true // removeAllTags = true
                )
        );
    }

    @Test
    public void parse_invalidArg_error() {
        // Testing failure when an invalid argument format is provided
        assertParseFailure(
                new DeleteTagCommandParser(),
                "maybe the real patients were the friends "
                        + "we made along the way",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        DeleteTagCommand.MESSAGE_USAGE)
        );
    }

    @Test
    public void parse_noTags_error() {
        // Testing failure when no tags are provided
        assertParseFailure(
                new DeleteTagCommandParser(),
                "1",
                "At least one tag must be provided, or use 't/all' to remove all tags."
        );
    }
}

