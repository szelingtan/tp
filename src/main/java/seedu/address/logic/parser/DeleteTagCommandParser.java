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
        boolean removeAllTags = false;

        if (tagStrsToDelete.contains(DeleteTagCommand.ALL_TAGS_KEYWORD)) {
            removeAllTags = true;
            tagsToDelete.clear(); // Ensure no specific tags are processed if 't/all' is used
        } else {
            for (String s : tagStrsToDelete) {
                if (s.trim().isEmpty()) {
                    throw new ParseException("Empty tag is not accepted. Please provide at least one valid tag.");
                }
                if (s.contains("  ")) {
                    throw new ParseException(
                            "Tags cannot contain consecutive spaces. Ensure tags are properly formatted.");
                }
                tagsToDelete.add(new Tag(s));
            }
        }

        if (tagsToDelete.isEmpty() && !removeAllTags) {
            throw new ParseException("At least one tag must be provided, or use 't/all' to remove all tags.");
        }

        return new DeleteTagCommand(index, tagsToDelete, removeAllTags);
    }
}
