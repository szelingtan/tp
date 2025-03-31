package seedu.address.logic.parser;
import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new DeleteTagCommand.
 */
public class DeleteTagCommandParser implements Parser<DeleteTagCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the
     * {@code DeleteTagCommand} and returns a {@code DeleteTagCommand} object for
     * execution.
     *
     * @throws ParseException if the user input does not conform to the
     *     expected format.
     */
    public DeleteTagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMM = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        // Retrieve the index from the user input
        Index index;
        try {
            index = ParserUtil.parseIndex(argMM.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(
                            MESSAGE_INVALID_COMMAND_FORMAT,
                            DeleteTagCommand.MESSAGE_USAGE
                    ),
                    pe
            );
        }

        // Extract the tags to be deleted
        HashSet<String> tagStrsToDelete = new HashSet<>(argMM.getAllValues(PREFIX_TAG));
        HashSet<Tag> tagsToDelete = new HashSet<>();
        for (String s : tagStrsToDelete) {
            tagsToDelete.add(new Tag(s));
        }

        return new DeleteTagCommand(index, tagsToDelete);
    }
}
