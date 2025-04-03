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
     * {@codtion.
     *      *
     *      * @throws ParseException if the user input does not conform to thee DeleteTagCommand} and returns a {@code DeleteTagCommand} object for
     * execu     *     expected format.
     */
    public UntagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMM = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMM.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE), pe
            );
        }

        List<String> listTagStrsToDel = argMM.getAllValues(PREFIX_TAG);
        checkForDuplicateTags(listTagStrsToDel);

        boolean isRemovingAllTags = listTagStrsToDel.contains(UntagCommand.ALL_TAGS_KEYWORD);
        HashSet<Tag> tagsToDelete = new HashSet<>();

        if (!isRemovingAllTags) {
            for (String tagName : listTagStrsToDel) {
                if (tagName.trim().isEmpty()) {
                    throw new ParseException("Empty tag is not accepted. Please provide at least one valid tag.");
                }

                if (tagName.contains("  ")) {
                    throw new ParseException("Tags cannot contain consecutive spaces. Ensure tags are properly formatted.");
                }

                try {
                    tagsToDelete.add(new Tag(tagName));
                } catch (IllegalArgumentException e) {
                    throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
                }
            }

            if (tagsToDelete.isEmpty()) {
                throw new ParseException("At least one tag must be provided, or use 't/all' to remove all tags.");
            }
        }

        return new UntagCommand(index, tagsToDelete, isRemovingAllTags);
    }

    private void checkForDuplicateTags(List<String> tags) throws ParseException {
        HashSet<String> seen = new HashSet<>();
        for (String tag : tags) {
            if (!seen.add(tag)) {
                throw new ParseException(String.format(UntagCommand.REPEATED_TAG_ERROR, tag));
            }
        }
    }

}
