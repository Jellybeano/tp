package seedu.weeblingo.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.weeblingo.model.Model.PREDICATE_SHOW_ALL_FLASHCARDS;

import seedu.weeblingo.model.Model;


/**
 * Starts the quiz.
 */
public class StartCommand extends Command {

    public static final String COMMAND_WORD = "start";

    public static final String MESSAGE_SUCCESS = "Here is the first question.\n"
            + "Enter \"end\" to end the quiz, \"check\" to check the answer, "
            + "and \"next\" to move to the next question.";

    @Override
    public CommandResult execute(Model model) {
        if (model.getMode().getCurrentMode() == 2) {
            requireNonNull(model);
            model.updateFilteredFlashcardList(PREDICATE_SHOW_ALL_FLASHCARDS);
            return new CommandResult(MESSAGE_SUCCESS, false, false, true);
        } else {
            return new CommandResult("Not in quiz mode", false, false, true);
        }
    }
}
