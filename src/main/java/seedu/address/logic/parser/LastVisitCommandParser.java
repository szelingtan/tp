package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_FUTURE_LAST_VISIT_DATE;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_DATE_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_NUMBER_OF_DATES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.LastVisitCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.patient.LastVisit;

/**
 * Parses input arguments and creates a new {@code RemarkCommand} object
 */
public class LastVisitCommandParser implements Parser<LastVisitCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code RemarkCommand}
     * and returns a {@code RemarkCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public LastVisitCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DATE);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    LastVisitCommand.MESSAGE_USAGE), pe);
        }

        if (!argMultimap.getValue(PREFIX_DATE).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    LastVisitCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getAllValues(PREFIX_DATE).size() > 1) {
            throw new ParseException(MESSAGE_INVALID_NUMBER_OF_DATES + "\n" + LastVisitCommand.MESSAGE_USAGE);
        }

        String dateString = argMultimap.getValue(PREFIX_DATE).get();

        LocalDate date;

        // Try to parse the input string into a LocalDate
        try {
            date = LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            throw new ParseException(MESSAGE_INVALID_DATE_FORMAT);
        }

        // Check if the date is valid
        if (!LastVisit.isValidLastVisit(date)) {
            throw new ParseException(MESSAGE_FUTURE_LAST_VISIT_DATE);
        }

        return new LastVisitCommand(index, new LastVisit(date));
    }
}
