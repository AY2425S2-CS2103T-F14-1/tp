package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

public class UiManagerTest {

    private static boolean isToolkitInitialized = false;

    private UiManager uiManager;
    private LogicStub logicStub;
    private MainWindowStub mainWindowStub;

    @BeforeAll
    public static void initToolkit() {
        if (!isToolkitInitialized) {
            System.setProperty("javafx.platform", "offscreen");
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            try {
                latch.await(); // Wait for the JavaFX Toolkit to initialize
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            isToolkitInitialized = true;
        }
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        // Use stubs instead of mocks
        logicStub = new LogicStub();

        // Use CountDownLatch to wait for JavaFX Application Thread
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Stage dummyStage = new Stage();
            mainWindowStub = new MainWindowStub(dummyStage, logicStub);

            // Initialize UiManager with the stubbed Logic
            uiManager = new UiManager(logicStub);

            // Set the static MainWindow field in UiManager to the stubbed MainWindow
            UiManager.setMainWindow(mainWindowStub);

            latch.countDown();
        });
        latch.await(); // Wait for JavaFX Application Thread to complete
    }

    @Test
    public void refreshPersonListPanel_mainWindowNotNull_callsMainWindowRefresh() {
        // Call the method under test
        UiManager.refreshPersonListPanel();

        // Verify that the MainWindow's refreshPersonListPanel method was called
        assertTrue(mainWindowStub.isRefreshCalled());
    }

    @Test
    public void refreshPersonListPanel_mainWindowNull_doesNotThrow() {
        // Set the static MainWindow field to null
        UiManager.setMainWindow(null);
        // Call the method under test and ensure no exception is thrown
        assertDoesNotThrow(UiManager::refreshPersonListPanel);
    }

    @Test
    public void start_initializesMainWindow() {
        // Use CountDownLatch to wait for JavaFX Application Thread
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Create a dummy Stage
            Stage primaryStage = new Stage();

            // Call the start method and ensure no exception is thrown
            assertDoesNotThrow(() -> uiManager.start(primaryStage));

            latch.countDown();
        });
        assertDoesNotThrow(() -> latch.await()); // Wait for JavaFX Application Thread to complete
    }

    // Stub for Logic
    private static class LogicStub implements Logic {
        @Override
        public ObservableList<Person> getFilteredPersonList() {
            // Return an empty ObservableList for testing purposes
            return FXCollections.observableArrayList();
        }

        @Override
        public Path getAddressBookFilePath() {
            // Return a dummy file path
            return Path.of("dummy/path/to/addressbook.json");
        }

        @Override
        public GuiSettings getGuiSettings() {
            // Return a dummy GuiSettings object
            return new GuiSettings(800, 600, 0, 0);
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            // Do nothing for testing purposes
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            // Return a dummy ReadOnlyAddressBook
            return new AddressBook();
        }

        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            // Throw an exception for unsupported commands
            throw new CommandException("Unsupported command for testing.");
        }
    }

    // Stub for MainWindow
    private static class MainWindowStub extends MainWindow {
        private boolean refreshCalled = false;

        public MainWindowStub(Stage stage, Logic logic) {
            super(stage, logic);
        }

        @Override
        public void refreshPersonListPanel() {
            refreshCalled = true;
        }

        public boolean isRefreshCalled() {
            return refreshCalled;
        }
    }
}
