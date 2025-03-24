package seedu.address.logic.commands;

import java.util.ArrayList;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Adds a bunch of tags to a specified patient.
 */
public class TagCommand extends Command {
    public static final String COMMAND_WORD = "tag";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds the listed tags to the patient identified by the index "
            + "numebr used in the last patient listing."
            + '\n'
            + "Parameters: INDEX t/tag [t/more_tags]"
            + '\n'
            + "Example: " + COMMAND_WORD + "39 t/Likes Madoka Magica "
            + "t/Plays rhythm games";

    private final Index index;
    private final ArrayList<String> tagsToAdd;


    public TagCommand(Index index, ArrayList<String> tagsToAdd) {
        this.index = index;
        this.tagsToAdd = tagsToAdd;
    }

    /**
     * Executes the stored `tag` cmd.
     *
     * @param model {@code Model} which the command should operate on.
     * @return The {@code CommandResult}.
     * @throws CommandException if the index is out of range.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        return null; // TODO
    }
}
