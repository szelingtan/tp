package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICINE;

import java.util.Optional;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnprescribeCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnprescribeCommand object
 */
public class UnprescribeCommandParser implements Parser<UnprescribeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnprescribeCommand
     * and returns an UnprescribeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnprescribeCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_MEDICINE);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnprescribeCommand.MESSAGE_USAGE), pe);
        }

        Optional<String> medicineToRemove = Optional.empty();

        if (arePrefixesPresent(argMultimap, PREFIX_MEDICINE)) {
            medicineToRemove = Optional.of(argMultimap.getValue(PREFIX_MEDICINE).get());
        }

        return new UnprescribeCommand(index, medicineToRemove);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
