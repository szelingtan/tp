package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UntagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new DeleteTagCommand.
 */
public class UntagCommandParser implements Parser<UntagCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the
     * {@code DeleteTagCommand} and returns a {@code DeleteTagCommand} object for
     * execution.
     *
     * @throws ParseException if the user input does not conform to the
     *     expected format.
     */
    public UntagCommand parse(String args) throws ParseException {
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
                            UntagCommand.MESSAGE_USAGE
                    ),
                    pe
            );
        }

        // Double check for duplicated inputs
        List<String> listTagStrsToDel = argMM.getAllValues(PREFIX_TAG);
        for (int i = 0; i < listTagStrsToDel.size(); i++) {
            for (int j = i + 1; j < listTagStrsToDel.size(); j++) {
                if (listTagStrsToDel.get(i).equals(listTagStrsToDel.get(j))) {
                    throw new ParseException(
                            String.format(
                                    UntagCommand.REPEATED_TAG_ERROR,
                                    listTagStrsToDel.get(i)
                            )
                    );
                }
            }
        }

        // Extract the tags to be deleted
        HashSet<String> tagStrsToDelete = new HashSet<>(argMM.getAllValues(PREFIX_TAG));
        HashSet<Tag> tagsToDelete = new HashSet<>();
        boolean removeAllTags = false;

        if (tagStrsToDelete.contains(UntagCommand.ALL_TAGS_KEYWORD)) {
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

        return new UntagCommand(index, tagsToDelete, removeAllTags);
    }
}
