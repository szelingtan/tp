package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICINE;
import static seedu.address.logic.parser.ParserUtil.findDuplicateInputs;

import java.util.List;
import java.util.Set;

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

        // Check for medicine parameter
        if (!argMultimap.getValue(PREFIX_MEDICINE).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    PrescribeCommand.MESSAGE_USAGE));
        }

        // Parse and validate Index
        String preamble = argMultimap.getPreamble().trim();

        // Check specifically for negative numbers and zero (index issue)
        int indexValue = Integer.parseInt(preamble);
        if (indexValue <= 0) {
            throw new ParseException(MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }

        // Double check for duplicated inputs
        List<String> listMedStrsToAdd = argMultimap.getAllValues(PREFIX_MEDICINE);
        String duplicate = findDuplicateInputs(listMedStrsToAdd);
        if (duplicate != null) {
            throw new ParseException(String.format(PrescribeCommand.REPEATED_MED_ERROR, duplicate));
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    PrescribeCommand.MESSAGE_USAGE), ive);
        }

        Set<Medicine> medSet = ParserUtil.parseMeds(argMultimap.getAllValues(PREFIX_MEDICINE));

        return new PrescribeCommand(index, medSet);
    }

}
