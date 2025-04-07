package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICINE;
import static seedu.address.logic.parser.ParserUtil.findDuplicateInputs;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.PrescribeCommand;
import seedu.address.logic.commands.UnprescribeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.medicine.Medicine;

/**
 * Parses input arguments and creates a new {@code UnprescribeCommand} object
 */
public class UnprescribeCommandParser implements Parser<UnprescribeCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code UnprescribeCommand}
     * and returns a {@code UnprescribeCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnprescribeCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_MEDICINE);

        // Check for medicine parameter
        if (!argMultimap.getValue(PREFIX_MEDICINE).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UnprescribeCommand.MESSAGE_USAGE));
        }

        // Parse and validate the index
        String preamble = argMultimap.getPreamble().trim();

        // Check if index is empty or contains non-digit characters (format issue)
        if (preamble.isEmpty() || !preamble.matches("-?\\d+")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnprescribeCommand.MESSAGE_USAGE));
        }

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

        // If we get here, the index format is valid
        try {
            Index index = ParserUtil.parseIndex(preamble);
            Set<Medicine> medsToRemove = ParserUtil.parseMedsUnprescribe(argMultimap.getAllValues(PREFIX_MEDICINE));
            return new UnprescribeCommand(index, medsToRemove);
        } catch (ParseException pe) {
            // For any other parsing issues (like too large numbers)
            throw new ParseException(MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }
    }
}
