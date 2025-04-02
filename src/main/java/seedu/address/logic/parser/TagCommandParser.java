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
        Index ind;
        try {
            ind = ParserUtil.parseIndex(argMM.getPreamble());
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

        // Double check for duplicated inputs
        List<String> listTagStrsToAdd = argMM.getAllValues(PREFIX_TAG);
        for (int i = 0; i < listTagStrsToAdd.size(); i++) {
            for (int j = i + 1; j < listTagStrsToAdd.size(); j++) {
                if (listTagStrsToAdd.get(i).equals(listTagStrsToAdd.get(j))) {
                    throw new ParseException(
                            String.format(
                                    TagCommand.REPEATED_TAG_ERROR,
                                    listTagStrsToAdd.get(i)
                            )
                    );
                }
            }
        }

        // Extract the tags
        HashSet<String> tagStrsToAdd = new HashSet<String>(
                argMM.getAllValues(PREFIX_TAG)
        );

        for (String tagStr : tagStrsToAdd) {
            if (tagStr.isBlank()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.EMPTY_TAG_ERROR));
            }
        }

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

        if (tagsToAdd.size() == 0) {
            throw new ParseException(
                    String.format(
                            MESSAGE_INVALID_COMMAND_FORMAT,
                            TagCommand.NO_TAG_INCLUDED_ERROR
                    )
            );
        }

        return new TagCommand(ind, tagsToAdd);
    }
}
