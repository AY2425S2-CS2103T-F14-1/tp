package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class SaveCommandTest {

    @Test
    public void execute_saveCommand_createsFile() throws Exception {
        // Setup
        Model model = new ModelManager();
        UserPrefs userPrefs = new UserPrefs();
        Path tempDirectory = Files.createTempDirectory("testSaveCommand");
        userPrefs.setAddressBookFilePath(tempDirectory.resolve("addressbook.json"));
        model.setUserPrefs(userPrefs);

        String testFileName = "testSaveFile";
        SaveCommand saveCommand = new SaveCommand(testFileName);

        // Execute
        saveCommand.execute(model);

        // Verify
        Path expectedFilePath = tempDirectory.resolve(testFileName + ".json");
        assertTrue(Files.exists(expectedFilePath), "File should exist after SaveCommand execution");

        // Cleanup
        Files.deleteIfExists(expectedFilePath);
        Files.deleteIfExists(tempDirectory);
    }
}
