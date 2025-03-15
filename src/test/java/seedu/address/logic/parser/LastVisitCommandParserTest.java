package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VISIT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.LastVisitCommand;
import seedu.address.model.patient.LastVisit;

public class LastVisitCommandParserTest {
    private LastVisitCommandParser parser = new LastVisitCommandParser();
    private final String nonEmptyRemark = "Some remark.";

    @Test
    public void parse_indexSpecified_success() {
        // have remark
        Index targetIndex = INDEX_FIRST_PATIENT;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_VISIT + nonEmptyRemark;
        LastVisitCommand expectedCommand = new LastVisitCommand(INDEX_FIRST_PATIENT, new LastVisit(nonEmptyRemark));
        assertParseSuccess(parser, userInput, expectedCommand);

        // no remark
        userInput = targetIndex.getOneBased() + " " + PREFIX_VISIT;
        expectedCommand = new LastVisitCommand(INDEX_FIRST_PATIENT, new LastVisit(""));
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
