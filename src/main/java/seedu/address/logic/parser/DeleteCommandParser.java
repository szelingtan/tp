package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        // Check if string is empty or contains non-digit characters (format issue)
        if (trimmedArgs.isEmpty() || !trimmedArgs.matches("-?\\d+")) {
            throw new ParseException(
                    String.format("Invalid command format!\n%s", DeleteCommand.MESSAGE_USAGE));
        }

        // Check specifically for negative numbers and zero (index issue)
        int indexValue = Integer.parseInt(trimmedArgs);
        if (indexValue <= 0) {
            throw new ParseException(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }

        // If we get here, the index is valid
        try {
            Index index = ParserUtil.parseIndex(trimmedArgs);
            return new DeleteCommand(index);
        } catch (ParseException pe) {
            // For any other parsing issues (like too large numbers)
            throw new ParseException(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }
    }
}
