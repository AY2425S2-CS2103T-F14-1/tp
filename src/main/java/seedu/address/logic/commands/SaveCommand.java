package seedu.address.logic.commands;

import java.io.IOException;
import java.nio.file.Path;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.storage.JsonAddressBookStorage;

/**
 * Saves the current address book to a specified JSON file in the data folder.
 */
public class SaveCommand extends Command {

    public static final String COMMAND_WORD = "save";
    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Saves the current address book to a specified JSON file in the data folder.\n"
            + "Syntax: " + COMMAND_WORD + " <filename>";
    public static final String MESSAGE_SUCCESS = "Address book saved to %s";
    public static final String MESSAGE_FAILURE = "Failed to save address book: %s";

    private final String fileName;

    public SaveCommand(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        // Get the data folder path from UserPrefs and append the fileName
        Path dataFolderPath = model.getUserPrefs().getAddressBookFilePath().getParent();
        Path filePath = dataFolderPath.resolve(fileName + ".json");

        JsonAddressBookStorage jsonStorage = new JsonAddressBookStorage(filePath);

        try {
            jsonStorage.saveAddressBook(model.getAddressBook());
            return new CommandResult(
                    String.format(MESSAGE_SUCCESS, filePath));
        } catch (IOException e) {
            throw new CommandException(
                    String.format(MESSAGE_FAILURE, e.getMessage()));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SaveCommand // instanceof handles nulls
                && fileName.equals(((SaveCommand) other).fileName));
    }
}
