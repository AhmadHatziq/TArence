package seedu.tarence.logic.commands;

import static java.util.Objects.requireNonNull;

import static seedu.tarence.logic.parser.CliSyntax.PREFIX_TUTORIAL_DAY;
import static seedu.tarence.logic.parser.CliSyntax.PREFIX_TUTORIAL_DURATION_IN_MINUTES;
import static seedu.tarence.logic.parser.CliSyntax.PREFIX_TUTORIAL_NAME;
import static seedu.tarence.logic.parser.CliSyntax.PREFIX_TUTORIAL_START_TIME;
import static seedu.tarence.logic.parser.CliSyntax.PREFIX_TUTORIAL_WEEKS;

import seedu.tarence.logic.commands.exceptions.CommandException;
import seedu.tarence.model.Model;
import seedu.tarence.model.tutorial.Tutorial;

/**
 * Adds a tutorial into T.A.rence.
 */
public class AddTutorialCommand extends Command {

    public static final String COMMAND_WORD = "addTutorial";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a Tutorial to the Application. "
            + "Parameters: "
            + PREFIX_TUTORIAL_NAME + "TUTORIAL_NAME "
            + PREFIX_TUTORIAL_DAY + "TUTORIAL_DAY "
            + PREFIX_TUTORIAL_START_TIME + "TUTORIAL_START_TIME "
            + PREFIX_TUTORIAL_WEEKS + "TUTORIAL_WEEK "
            + PREFIX_TUTORIAL_DURATION_IN_MINUTES + "TUTORIAL_DURATION_IN_MINUTES "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TUTORIAL_NAME + "LAB SESSION "
            + PREFIX_TUTORIAL_DAY + "MONDAY,FRIDAY "
            + PREFIX_TUTORIAL_START_TIME + "1200 "
            + PREFIX_TUTORIAL_WEEKS + "7,10,12"
            + PREFIX_TUTORIAL_DURATION_IN_MINUTES + "120";

    public static final String[] COMMAND_SYNONYMS = {COMMAND_WORD.toLowerCase(), "addtut"};

    private Tutorial tutorial;

    public AddTutorialCommand(Tutorial newTutorial) {
        requireNonNull(newTutorial);
        tutorial = newTutorial;
    }

    @Override
    public CommandResult execute (Model model) throws CommandException {
        requireNonNull(model);
        throw new CommandException("Hello from Add Tutorial Command");
    }

    /**
     * Returns true if user command matches command word or any defined synonyms, and false otherwise.
     *
     * @param userCommand command word from user.
     * @return whether user command matches specified command word or synonyms.
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
