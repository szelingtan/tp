package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagCommand
 */
public class TagCommandParser implements Parser<TagCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the
     * {@code TagCommand} and returns a {@code TagCommand} object for
     * execution.
     *
     * @throws ParseException if the user input does not conform to the
     *     expected format
     */
    public TagCommand parse(String args) throws ParseException {
        // Based on EditCommandParser.java
        requireNonNull(args);
        ArgumentMultimap argMM = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        // Retrieve the index from the user input
        Index ind = getIndex(argMM);

        // Double check for duplicated inputs
        List<String> listTagStrsToAdd = argMM.getAllValues(PREFIX_TAG);
        String duplicate = findDuplicateInput(listTagStrsToAdd);
        if (duplicate != null) {
            throw new ParseException(String.format(TagCommand.REPEATED_TAG_ERROR, duplicate));
        }

        // Extract the tags
        HashSet<String> tagStrsToAdd = new HashSet<String>(
                argMM.getAllValues(PREFIX_TAG)
        );

        // Do not allow blank tags
        for (String tagStr : tagStrsToAdd) {
            if (tagStr.isBlank()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.EMPTY_TAG_ERROR));
            }
        }

        HashSet<Tag> tagsToAdd = parseTags(tagStrsToAdd);

        if (tagsToAdd.size() == 0) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    TagCommand.NO_TAG_INCLUDED_ERROR));
        }

        return new TagCommand(ind, tagsToAdd);
    }

    /**
     * Retrieves the index from the argument multimap.
     *
     * @param argMM The argument multimap.
     * @return The index from the argument multimap.
     * @throws ParseException if there is an error parsing the index.
     */
    private Index getIndex(ArgumentMultimap argMM) throws ParseException {
        try {
            return ParserUtil.parseIndex(argMM.getPreamble());
        } catch (ParseException pe) {
            // Copied from DelLastVisitCommandParser.java
            throw new ParseException(
                    String.format(
                            MESSAGE_INVALID_COMMAND_FORMAT,
                            TagCommand.MESSAGE_USAGE
                    ),
                    pe
            );
        }
    }

    /**
     * Checks if there is any duplicate String in the list.
     *
     * @param listTagStrsToAdd List of Strings being added to check.
     * @return A duplicated String if any and null if none exist.
     */
    private String findDuplicateInput(List<String> listTagStrsToAdd) {
        for (int i = 0; i < listTagStrsToAdd.size(); i++) {
            for (int j = i + 1; j < listTagStrsToAdd.size(); j++) {
                if (listTagStrsToAdd.get(i).equals(listTagStrsToAdd.get(j))) {
                    return listTagStrsToAdd.get(i);
                }
            }
        }
        return null;
    }

    /**
     * Parses the tags to add.
     *
     * @param tagStrsToAdd The tags to add as Strings.
     * @return The tags to add.
     * @throws ParseException if there is an error parsing any tag.
     */
    private HashSet<Tag> parseTags(HashSet<String> tagStrsToAdd) throws ParseException {
        HashSet<Tag> tagsToAdd = new HashSet<Tag>();
        Tag t = null;
        for (String s : tagStrsToAdd) {
            // Provided tag is invalid
            try {
                t = new Tag(s);
            } catch (IllegalArgumentException e) {
                throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
            }
            tagsToAdd.add(t);
        }
        return tagsToAdd;
    }
}
