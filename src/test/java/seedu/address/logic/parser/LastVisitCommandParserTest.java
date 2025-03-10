package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.LastVisitCommand;
import seedu.address.model.person.LastVisit;

public class LastVisitCommandParserTest {
    private LastVisitCommandParser parser = new LastVisitCommandParser();
    private final String nonEmptyRemark = "Some remark.";

    @Test
    public void parse_indexSpecified_success() {
        // have remark
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_REMARK + nonEmptyRemark;
        LastVisitCommand expectedCommand = new LastVisitCommand(INDEX_FIRST_PERSON, new LastVisit(nonEmptyRemark));
        assertParseSuccess(parser, userInput, expectedCommand);

        // no remark
        userInput = targetIndex.getOneBased() + " " + PREFIX_REMARK;
        expectedCommand = new LastVisitCommand(INDEX_FIRST_PERSON, new LastVisit(""));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, LastVisitCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, LastVisitCommand.COMMAND_WORD, expectedMessage);

        // no index
        assertParseFailure(parser, LastVisitCommand.COMMAND_WORD + " " + nonEmptyRemark, expectedMessage);
    }
}