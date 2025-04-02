package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

public class TagCommandParserTest {
    @Test
    public void parse_validArg_success() {
        // Referencing DelLastVisitCommandParserTest.java
        assertParseSuccess(
                new TagCommandParser(),
                "1 t/Madoka t/Homura",
                new TagCommand(
                        INDEX_FIRST_PATIENT,
                        new HashSet<Tag>(Set.of(
                                new Tag("Madoka"),
                                new Tag("Homura")
                        ))
                )
        );
    }

    @Test
    public void parse_invArg_error() {
        // Referencing DelLastVisitCommandParserTest.java
        assertParseFailure(
                new TagCommandParser(),
                "maybe the real patients were the friends "
                + "we made along the way",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        TagCommand.MESSAGE_USAGE)
        );
    }

    @Test
    public void parse_duplicateArg_error() {
        try {
            new TagCommandParser().parse(
                    "tag 1 t/Depression t/Depression t/etc"
            );
            fail("This should not pass");
        } catch (ParseException pe) {
            // Test passed
        }
    }

    @Test
    public void parse_blankTag_error() {
        try {
            new TagCommandParser().parse(
                    "tag 1 t/Dysphoria t/ t/PTSD"
            );
            fail("This should not pass");
        } catch (ParseException pe) {
            // Test passed
        }
    }
}
