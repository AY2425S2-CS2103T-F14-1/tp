package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Objects;

import seedu.address.model.Model;
import seedu.address.ui.DisplayPreferences;
import seedu.address.ui.UiManager;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    // Command word for ListCommand
    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all persons in the address book. "
            + "Parameters: [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] "
            + "[s/STUDENT_ID] [t/TAG] [tut/TUTORIAL] [te/TELEGRAM]\n"
            + "Example: " + COMMAND_WORD + " n/ p/ e/";

    private final boolean showName;
    private final boolean showPhone;
    private final boolean showEmail;
    private final boolean showAddress;
    private final boolean showStudentId;
    private final boolean showTags;
    private final boolean showTutorials;
    private final boolean showTelegram;

    /**
     * Creates a ListCommand to list all persons with the specified display preferences.
     */
    public ListCommand(boolean showName, boolean showPhone, boolean showEmail, boolean showAddress,
                       boolean showStudentId, boolean showTags, boolean showTutorials, boolean showTelegram) {
        // Ensure at least one display preference is true
        assert showName || showPhone || showEmail || showAddress || showStudentId || showTags
                || showTutorials || showTelegram
                : "At least one display preference must be true";

        this.showName = showName;
        this.showPhone = showPhone;
        this.showEmail = showEmail;
        this.showAddress = showAddress;
        this.showStudentId = showStudentId;
        this.showTags = showTags;
        this.showTutorials = showTutorials;
        this.showTelegram = showTelegram;
    }
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        // Update display preferences
        DisplayPreferences.setShowName(showName);
        DisplayPreferences.setShowPhone(showPhone);
        DisplayPreferences.setShowEmail(showEmail);
        DisplayPreferences.setShowAddress(showAddress);
        DisplayPreferences.setShowStudentId(showStudentId);
        DisplayPreferences.setShowTags(showTags);
        DisplayPreferences.setShowTutorials(showTutorials);
        DisplayPreferences.setShowTelegram(showTelegram);

        // Assertions to ensure valid state
        assert DisplayPreferences.isShowName() == showName : "DisplayPreferences for name is incorrect";
        assert DisplayPreferences.isShowPhone() == showPhone : "DisplayPreferences for phone is incorrect";
        assert DisplayPreferences.isShowEmail() == showEmail : "DisplayPreferences for email is incorrect";
        assert DisplayPreferences.isShowAddress() == showAddress : "DisplayPreferences for address is incorrect";
        assert DisplayPreferences.isShowStudentId() == showStudentId : "DisplayPreferences for student ID is incorrect";
        assert DisplayPreferences.isShowTags() == showTags : "DisplayPreferences for tags is incorrect";
        assert DisplayPreferences.isShowTutorials() == showTutorials : "DisplayPreferences for tutorials is incorrect";
        assert DisplayPreferences.isShowTelegram() == showTelegram : "DisplayPreferences for telegram is incorrect";

        // Update the filtered person list and refresh the UI
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        UiManager.refreshPersonListPanel();

        return new CommandResult(MESSAGE_SUCCESS);
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ListCommand)) {
            return false;
        }
        ListCommand otherCommand = (ListCommand) other;
        return showName == otherCommand.showName
                && showPhone == otherCommand.showPhone
                && showEmail == otherCommand.showEmail
                && showAddress == otherCommand.showAddress
                && showStudentId == otherCommand.showStudentId
                && showTelegram == otherCommand.showTelegram
                && showTags == otherCommand.showTags
                && showTutorials == otherCommand.showTutorials;
    }

    @Override
    public int hashCode() {
        return Objects.hash(showName, showPhone, showEmail, showAddress, showStudentId,
                showTags, showTutorials, showTelegram);
    }
}
