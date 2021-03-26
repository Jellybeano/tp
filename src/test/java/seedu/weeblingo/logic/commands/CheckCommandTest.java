package seedu.weeblingo.logic.commands;

import static seedu.weeblingo.logic.commands.CheckCommand.MESSAGE_SUCCESS;
import static seedu.weeblingo.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.weeblingo.model.Model;
import seedu.weeblingo.model.ModelManager;

public class CheckCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_check_success() {
        model.startQuiz();
        model.getMode().switchModeQuizSession();
        String attempt = model.getQuizInstance().getCurrentQuestion().getAnswer().toString();
        CommandResult expectedCommandResult = new CommandResult(
                MESSAGE_SUCCESS, false, false);
        assertCommandSuccess(new CheckCommand(attempt), model, expectedCommandResult, expectedModel);
    }
}
