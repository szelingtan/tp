package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DelLastVisitCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DelLastVisitCommand
 */
public class DelLastVisitCommandParser
        implements Parser<DelLastVisitCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the
     * {@code DelLastVisitCommand} and returns a {@code DelLastVisitCommand}
     * object for execution.
     *
     * @throws ParseException if the user input does not conform the expected
     *     format
     */
    public DelLastVisitCommand parse(String args) throws ParseException {
        // Format copied from DeleteCommandParser.java
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DelLastVisitCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(
                            MESSAGE_INVALID_COMMAND_FORMAT,
                            DelLastVisitCommand.MESSAGE_USAGE
                    ),
                    pe
            );
        }
    }
}
