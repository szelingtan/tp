package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DelLastVisitCommand;
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
            ind = ParserUtil.parseIndex(args);
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

        // Extract the tags
        HashSet<String> tagStrsToAdd = new HashSet<String>(
                argMM.getAllValues(PREFIX_TAG)
        );
        HashSet<Tag> tagsToAdd = new HashSet<Tag>();
        for (String s : tagStrsToAdd) {
            tagsToAdd.add(new Tag(s));
        }

        return new TagCommand(ind, tagsToAdd);
    }
}
