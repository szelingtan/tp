package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICINE;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.PrescribeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.medicine.Medicine;

/**
 * Parses input arguments and creates a new {@code PrescribeCommand} object
 */
public class PrescribeCommandParser implements Parser<PrescribeCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code PrescribeCommand}
     * and returns a {@code PrescribeCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public PrescribeCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_MEDICINE);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    PrescribeCommand.MESSAGE_USAGE), ive);
        }

        String medicineName = argMultimap.getValue(PREFIX_MEDICINE).orElse("");

        return new PrescribeCommand(index, new Medicine(medicineName));
    }
}
