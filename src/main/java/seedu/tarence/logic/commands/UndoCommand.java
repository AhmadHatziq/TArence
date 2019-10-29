package seedu.tarence.logic.commands;

import static seedu.tarence.logic.parser.CliSyntax.PREFIX_UNDO_NUM_OF_STATES;

import java.io.IOException;

import seedu.tarence.logic.commands.exceptions.CommandException;
import seedu.tarence.model.Model;
import seedu.tarence.model.ReadOnlyApplication;
import seedu.tarence.storage.Storage;

public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undoes the previous command. "
            + "Parameters: "
            + PREFIX_UNDO_NUM_OF_STATES + "NUMBER_OF_COMMANDS_TO_UNDO "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_UNDO_NUM_OF_STATES + "1 ";

    public static final String MESSAGE_SUCCESS = "Previous command successfully undone.";

    public static final String MESSAGE_INVALID_INDEX = "The given states to undo, %1$s, is invalid. "
            + "Please enter a number lower than %2$s";

    private static final String[] COMMAND_SYNONYMS = {COMMAND_WORD.toLowerCase()};

    private int numOfStatesToUndo;

    public UndoCommand (int numOfStatesToUndo) {
        this.numOfStatesToUndo = numOfStatesToUndo;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        assert true: "Undo command requires Storage to function properly";
        throw new CommandException("Error with UndoCommand.java's execution path");
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {

        if (!storage.isValidNumberOfRollbacks(numOfStatesToUndo)) {
            String errorMessage = String.format(MESSAGE_INVALID_INDEX, numOfStatesToUndo, storage.maxNumberOfRollbacksAllowed());
            throw new CommandException(errorMessage);
        }

        try {
            Integer stateToUndoTo = storage.getLatestStateIndex() - numOfStatesToUndo;
            ReadOnlyApplication retrievedState = storage.getSpecifiedState(stateToUndoTo);
            model.setModel(retrievedState);
        } catch (IOException e) {
            throw new CommandException("Error in undo command. Possible corrupted state json file");
        }

        return new CommandResult(MESSAGE_SUCCESS);
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
