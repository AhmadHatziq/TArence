package seedu.tarence.logic.commands;

import static seedu.tarence.logic.parser.CliSyntax.PREFIX_UNDO_NUM_OF_STATES;
import seedu.tarence.logic.commands.exceptions.CommandException;
import seedu.tarence.model.Model;
import seedu.tarence.storage.Storage;

public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undoes the previous command. "
            + "Parameters: "
            + PREFIX_UNDO_NUM_OF_STATES + "NUMBER_OF_COMMANDS_TO_UNDO "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_UNDO_NUM_OF_STATES + "1 ";

    public static final String MESSAGE_SUCCESS = "Previous command successfully undone.";

    private static final String[] COMMAND_SYNONYMS = {COMMAND_WORD.toLowerCase()};

    private Storage storage;
    private int numOfStatesToUndo;

    public UndoCommand (Storage storage) {
        this.storage = storage;
    }

    public UndoCommand (int numOfStatesToUndo) {
        this.numOfStatesToUndo = numOfStatesToUndo;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        return new CommandResult("Hello from undo command result with num of states = " + numOfStatesToUndo);
    }
    
    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        return execute(model);
    }

    /**
     * Returns true if matches command word.
     *
     * @param userCommand String user command.
     * @return Boolean
     */
    public static boolean isMatchingCommandWord(String userCommand) {
        for (String synonym : COMMAND_SYNONYMS) {
            if (synonym.equals(userCommand.toLowerCase())) {
                return true;
            }
        }

        return false;
    }


}
