---
layout: page
title: conTAct Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* The features add, list, edit, find, delete, clear and exit (including the code) was reused with 
changes made from [AB3](https://github.com/se-edu/addressbook-level3).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S2-CS2103T-F14-1/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2425S2-CS2103T-F14-1/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/DeleteArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete n/Alice` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target User Profile**:

*Primary Users:*
- Teaching Assistants (TAs) managing multiple students.

*Secondary Users:*
- Students accessing their records (future feature consideration).

**Value Proposition**:
conTActs helps TAs efficiently organize and manage student information, reducing administrative workload and improving accessibility. The app provides a centralized platform for tracking student details, communication, and academic progress.

### User Stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​     | I want to …​                     | So that I can…​                    |
|---------|-------------| ------------------------------ | ---------------------------------- |
| `* * *` | new TA      | import a list of students and professors    | don’t have to manually enter all the contacts. |
| `* * `  | new TA      | send a message to multiple students at once |   make announcements efficien      |
| `* * *` | TA          | delete the contact details of a student                | update my list of students         |
| `* * *` | TA          | add the contact details of a student          | keep track of my students |
| `* * *` | TA          | list the contact details of all my students   | display and view my list of students |
| `* * *` | TA          | find a student’s contact information quickly           | contact them conveniently          |
| `* * ` | TA          | filter contacts | find relevant contacts easily|


### Excluded from Initial Release
- Student self-service portal.
- Mobile application version.

### Use Cases
### Adding a Tag to a Student
1. TA logs into the system.
2. TA searches for the student using the search bar.
3. TA selects the student from the results.
4. TA clicks on "Edit Profile".
5. TA adds a tag (e.g., "Needs Assistance") to the profile.
6. TA saves the changes.
7. System updates the student profile with the new tag.

### Assigning a Student to a Course
1. TA logs into the system.
2. TA searches for the student.
3. TA selects the student’s profile.
4. TA clicks on "Assign Course".
5. TA selects the appropriate course from a dropdown list.
6. TA confirms the selection.
7. System updates the student’s profile with the assigned course.

### Non-Functional Requirements
- **Performance:** The system should handle up to 10,000 student records efficiently.
- **Usability:** The interface should be simple and intuitive, requiring minimal training.
- **Scalability:** The system should support multiple TAs accessing student records concurrently.
- **Security:** Student data must be encrypted and accessible only to authorized users.
- **Availability:** The system should have 99.9% uptime to ensure reliability.

### Glossary
- **TA (Teaching Assistant):** An academic assistant helping professors with student management.
- **Student Profile:** A record containing a student's personal, academic, and contact information.
- **Tag:** A keyword or label assigned to a student for categorization.
- **Course Assignment:** The process of linking a student to a specific course in the system.

--------------------------------------------------------------------------------------------------------------------

## Appendix: Instructions for Manual Testing

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on; testers are expected to do more *exploratory* testing.</div>

---

### Launch and Shutdown

#### Initial Launch

1. Ensure you have Java `17` or above installed on your computer. You can learn how to do so [here](https://www.java.com/en/download/help/download_options.html).
2. Download the latest `.jar` file from [here](https://github.com/AY2425S2-CS2103T-F14-1/tp/releases).
3. Copy the file to the folder you want to use as the _home folder_ for your address book.
4. Open a command terminal (learn how to do so [here](https://www.freecodecamp.org/news/command-line-for-beginners/)).
5. Type `cd [YOUR_FOLDER_LOCATION]` where `[YOUR_FOLDER_LOCATION]` is the path to the folder containing the jar file. (Learn more [here](https://www.wikihow.com/Change-Directories-in-Command-Prompt)).
6. Type `java -jar <filename>.jar` and press Enter. A GUI should appear in a few seconds.

#### Shutdown

- Type `exit` in the command box to close the application.
- **OR** click the `File` button and select `Exit` from the dropdown menu.

#### Saving Window Preferences

1. Resize the window to an optimum size and move it to a different location.
2. Close the window.
3. Re-launch the app by double-clicking the jar file.
    - **Expected:** The most recent window size and location should be retained.

---

### Testing the Core Commands

#### 1. Help Command

- **Test Case:** Type `help` in the command box.
- **Expected Result:** A help message is displayed explaining how to access the help page along with the list of available commands and their formats.
- **Notes:** Check that the help content includes examples and tips for each command.

---

#### 2. Adding a Person

- **Valid Input:**
    - **Test Case:**
      ```
      add n/Mai p/12341234 e/student@example.com s/A1234567X
      ```  
      **Expected:** A new person is added to conTAct.
    - **Test Case (with optional fields):**
      ```
      add n/Mai p/12341234 e/student@example.com s/A1234567X t/needs-care tut/CS2103 tut/CS2109S a/Kent Ridge Hall
      ```  
      **Expected:** The new person is added with tags, tutorials, and an address.
- **Invalid Input:**
    - **Test Case:** Try omitting any required field (for example, leave out the student ID).
    - **Expected:** An error message is displayed in the status bar indicating the missing required field.
- **Edge Cases:**
    - Test with extra spaces or unusual but valid characters in optional fields.
    - Verify that input constraints (e.g., student ID format, alphanumeric tags/tutorial names) are enforced.

---

#### 3. Listing All Persons

- **Default Listing:**
    - **Test Case:** Type `list`
    - **Expected:** A list of all persons is displayed with all fields.
- **Customized Listing:**
    - **Test Case:**
      ```
      list n/ p/
      ```  
      **Expected:** Only the names and phone numbers of all persons are displayed.
    - **Test Case:**
      ```
      list n/ e/ t/
      ```  
      **Expected:** Only the names, emails, and tags are displayed.
- **Notes:** Confirm that the list reflects any changes made by previous commands (such as add, edit, or delete).

---

#### 4. Editing a Person

- **Valid Edits:**
    - **Preparation:** Use `list` to note the index of the person you want to edit.
    - **Test Case:**
      ```
      edit 1 p/91234567 e/johndoe@example.com
      ```  
      **Expected:** The 1st person’s phone number and email are updated accordingly.
    - **Test Case (Clearing Tags):**
      ```
      edit 2 n/Betsy Crower t/
      ```  
      **Expected:** The 2nd person’s name is changed to “Betsy Crower” and all tags are cleared.
- **Invalid Edits:**
    - **Test Case:** Use an index that does not exist (e.g., `edit 99 n/Name`)
    - **Expected:** An error message indicating an invalid index is displayed.
    - **Test Case:** Enter `edit` with no optional fields.
    - **Expected:** An error message prompting that at least one field is required for editing.
- **Notes:** Verify that changes are reflected immediately in the list command output.

---

#### 5. Finding Persons by Attributes

- **Search by Name (Full Word Matching):**
    - **Test Case:**
      ```
      find n/John
      ```  
      **Expected:** Only persons whose names match exactly “John” (as a full word) are shown.
- **Search by Phone Number or Email (Partial Matching):**
    - **Test Case:**
      ```
      find p/123
      ```  
      **Expected:** Persons with phone numbers containing “123” are displayed.
    - **Test Case:**
      ```
      find e/example
      ```  
      **Expected:** Persons with email addresses that include “example” are shown.
- **Search by Tag or Tutorial (Full Word Matching):**
    - **Test Case:**
      ```
      find t/friend
      ```  
      **Expected:** Only persons tagged exactly as “friend” are listed.
    - **Test Case:**
      ```
      find tut/CS2103T
      ```  
      **Expected:** Only persons in tutorial “CS2103T” are shown.
- **Notes:** Verify that the search is case-insensitive and that the ordering of keywords in names does not affect the results.

---

#### 6. Deleting a Person or a Group of Persons

- **Deleting by Name:**
    - **Test Case:**
      ```
      delete n/John
      ```  
      **Expected:** All persons whose full name matches “John” are deleted.
- **Deleting by Tag:**
    - **Test Case:**
      ```
      delete t/friend
      ```  
      **Expected:** All persons tagged as “friend” are deleted.
- **Deleting by Student ID:**
    - **Test Case:**
      ```
      delete s/A1234567X
      ```  
      **Expected:** Only the person with the exact student ID is deleted.
- **Invalid Delete Commands:**
    - **Test Case:**
      ```
      delete
      ```  
      **Expected:** An error is shown because the command lacks a field/value.
    - **Test Case:**
      ```
      delete x/
      ```  
      **Expected:** An error is shown due to an invalid prefix.
- **Notes:** After deletion, run the `list` command to verify the updated list of persons.

---

#### 7. Clearing All Entries

- **Test Case:** Type `clear`
- **Expected:** All entries are removed from conTAct. Verify by using the `list` command; the list should now be empty.
- **Notes:** Confirm that a confirmation or warning is displayed (if applicable) before clearing all entries.

---

#### 8. Exiting the Program

- **Test Case (Command):** Type `exit`
    - **Expected:** The application terminates gracefully.
- **Test Case (Menu Option):** Click the `File` button and select `Exit`
    - **Expected:** The application terminates gracefully.

---

### Saving, Loading, and Managing Files

#### Saving Data

1. **Test Case:**
    ```
    save addressbook
    ```  
   **Expected:** The current address book data is saved to a file named `addressbook.json` in the default directory, and a success message is displayed in the status bar.
2. **Test Case:**
    ```
    save
    ```  
   **Expected:** An error message indicating that a filename is required.
3. **Test Case:**
    ```
    save invalid/filename
    ```  
   **Expected:** An error message indicating that the filename contains invalid characters.

#### Loading Data

1. **Preparation:** Ensure a valid JSON file (e.g., `example.json`) exists in the default directory.
2. **Test Case:**
    ```
    load example
    ```  
   **Expected:** The address book data is replaced with the contents of `example.json`, with a success message in the status bar.
3. **Test Case:**
    ```
    load nonExistentFile
    ```  
   **Expected:** An error message indicating that the specified file does not exist.
4. **Test Case:**
    ```
    load corruptedFile
    ```  
   **Expected:** An error message indicating that the file is corrupted or invalid.

#### Listing All Saved Files

1. **Test Case:**
    ```
    files
    ```  
   **Expected:** A list of all saved files in the default directory is displayed in the result panel.
2. **Test Case:**
    ```
    files
    ```  
   when no files exist  
   **Expected:** A message is displayed indicating that no saved files are available.

#### Dealing with Missing/Corrupted Data Files

1. **Simulating a Missing File:**
    - Manually rename or remove the data file (e.g., `addressbook.json`).
    - Launch the application.
    - **Expected Behavior:** The app detects the absence of the file, recreates it with default data, and logs a warning message.
2. **Simulating a Corrupted File:**
    - Open the data file (e.g., `addressbook.json`) in a text editor.
    - Modify its content so that it is not valid JSON (for example, delete or alter key structural elements).
    - Launch the application.
    - **Expected Behavior:** The app notifies the user about the corrupted file, attempts to restore data from a backup if available, or resets to a default state while logging the error.

---

### Additional Exploratory Testing

- **Invalid Command Formats:**  
  Try commands with typos or extra spaces (e.g., `add  n/Mai  p/12341234`) and verify that the app returns a clear error message.
- **Field Boundary Testing:**  
  Test the input constraints for each field. For example, provide an incorrectly formatted student ID, non-alphanumeric characters in tag names, or an invalid phone number format.
- **User Interface:**  
  Verify that all GUI components (such as the status bar, menus, and command box) behave as expected during and after executing commands.
- **Error Logging:**  
  Check that the status bar and any log files (if applicable) accurately report errors and warnings.

  
---------------------------------------------------------------------------------------------------------------------

## **Appendix: Effort**

---------------------------------------------------------------------------------------------------------------------

## **Appendix: Planned Enhancements**

**Team Size:** 4

1. **Make email field unique and follows NUS email format**: Currently, the add command allows the same email to be added,
and it does not have to follow NUS email format (@nus.edu.sg). We plan to improve it by making the email field unique,
and only NUS email can be added.


2. **Make tutorial field follow NUS courses format**: Currently, the tutorial can just take in any string, not necessarily 
in the format of a course code (e.g CS2103T). We plan to improve it by making it follow NUS course code format.<br>


3. **List all students after delete command**: If a user use the find command to filter out a student, and then use delete
command to delete the student, the app will display a blank list. We plan to update it to display the whole list after
the delete command is called.<br>


