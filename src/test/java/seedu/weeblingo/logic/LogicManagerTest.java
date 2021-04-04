package seedu.weeblingo.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.weeblingo.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.weeblingo.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.weeblingo.logic.commands.CommandResult;
import seedu.weeblingo.logic.commands.exceptions.CommandException;
import seedu.weeblingo.logic.parser.exceptions.ParseException;
import seedu.weeblingo.model.Model;
import seedu.weeblingo.model.ModelManager;
import seedu.weeblingo.model.ReadOnlyFlashcardBook;
import seedu.weeblingo.model.UserPrefs;
import seedu.weeblingo.storage.JsonFlashcardBookStorage;
import seedu.weeblingo.storage.JsonUserPrefsStorage;
import seedu.weeblingo.storage.StorageManager;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonFlashcardBookStorage flashcardBookStorage =
                new JsonFlashcardBookStorage(temporaryFolder.resolve("flashcards.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(flashcardBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }


    //@Test
    //public void execute_validCommand_success() throws Exception {
    //    String learnCommand = LearnCommand.COMMAND_WORD;
    //    assertCommandSuccess(learnCommand, LearnCommand.MESSAGE_SUCCESS, model);
    //}

    //@Test
    //public void execute_storageThrowsIoException_throwsCommandException() {
    //    // Setup LogicManager with JsonFlashcardBookIoExceptionThrowingStub
    //    JsonFlashcardBookStorage addressBookStorage =
    //            new JsonFlashcardBookIoExceptionThrowingStub(temporaryFolder.resolve("ioExceptionAddressBook.json"));
    //    JsonUserPrefsStorage userPrefsStorage =
    //            new JsonUserPrefsStorage(temporaryFolder.resolve("ioExceptionUserPrefs.json"));
    //    StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
    //    logic = new LogicManager(model, storage);
    //
    //    // Execute add command
    //    String addCommand = AddCommand.COMMAND_WORD + QUESTION_DESC_A
    //            + ANSWER_DESC_A;
    //    Flashcard expectedFlashcard = new FlashcardBuilder(AMY).withTags().build();
    //    ModelManager expectedModel = new ModelManager();
    //    expectedModel.addFlashcard(expectedFlashcard);
    //    String expectedMessage = LogicManager.FILE_OPS_ERROR_MESSAGE + DUMMY_IO_EXCEPTION;
    //    assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    //}

    @Test
    public void getFilteredFlashcardList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredFlashcardList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getFlashcardBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * A stub class to throw an {@code IOException} when the save method is called.
     */
    private static class JsonFlashcardBookIoExceptionThrowingStub extends JsonFlashcardBookStorage {
        private JsonFlashcardBookIoExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveFlashcardBook(ReadOnlyFlashcardBook flashcardBook, Path filePath) throws IOException {
            throw DUMMY_IO_EXCEPTION;
        }
    }
}
