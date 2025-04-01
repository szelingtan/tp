package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UntagCommand;
import seedu.address.model.tag.Tag;

public class UntagCommandParserTest {
    @Test
    public void parse_validArg_success() {
        // Testing successful parsing of valid delete tag command
        assertParseSuccess(
                new UntagCommandParser(),
                "1 t/Madoka t/Homura",
                new UntagCommand(
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
                new UntagCommandParser(),
                "1 t/all",
                new UntagCommand(
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
                new UntagCommandParser(),
                "maybe the real patients were the friends "
                        + "we made along the way",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        UntagCommand.MESSAGE_USAGE)
        );
    }

    @Test
    public void parse_noTags_error() {
        // Testing failure when no tags are provided
        assertParseFailure(
                new UntagCommandParser(),
                "1",
                "At least one tag must be provided, or use 't/all' to remove all tags."
        );
    }

    @Test
    public void parse_emptyTag_error() {
        // Testing failure when an empty tag is provided
        assertParseFailure(
                new UntagCommandParser(),
                "1 t/",
                "Empty tag is not accepted. Please provide at least one valid tag."
        );
    }

    @Test
    public void parse_consecutiveSpacesTag_error() {
        // Testing failure when a tag contains consecutive spaces
        assertParseFailure(
                new UntagCommandParser(),
                "1 t/High  Blood",
                "Tags cannot contain consecutive spaces. Ensure tags are properly formatted."
        );
    }
}

