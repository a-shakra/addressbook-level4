# yungyung04
###### \java\seedu\address\logic\commands\AddPersonalTaskCommandTest.java
``` java
public class AddPersonalTaskCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddPersonalTaskCommand(null);
    }

    @Test
    public void execute_taskAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonalTaskAdded modelStub = new ModelStubAcceptingPersonalTaskAdded();
        PersonalTask validTask = new TaskBuilder().buildPersonalTask();

        CommandResult commandResult = getAddPersonalTaskCommandForTask(validTask, modelStub).execute();

        assertEquals(String.format(AddPersonalTaskCommand.MESSAGE_SUCCESS, validTask), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validTask), modelStub.tasksAdded);
    }

    @Test
    public void execute_clashingTask_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingTimingClashException();
        PersonalTask validTask = new TaskBuilder().buildPersonalTask();

        thrown.expect(CommandException.class);
        thrown.expectMessage(MESSAGE_TASK_TIMING_CLASHES);

        getAddPersonalTaskCommandForTask(validTask, modelStub).execute();
    }

    @Test
    public void equals() {
        PersonalTask firstPersonalTask = new TaskBuilder().withDateTime(VALID_DATE_TIME_AMY).buildPersonalTask();
        PersonalTask secondPersonalTask = new TaskBuilder().withDateTime(VALID_DATE_TIME_BOB).buildPersonalTask();

        AddPersonalTaskCommand addFirstTask = new AddPersonalTaskCommand(firstPersonalTask);
        AddPersonalTaskCommand addFirstTaskCopy = new AddPersonalTaskCommand(firstPersonalTask);
        AddPersonalTaskCommand addSecondTask = new AddPersonalTaskCommand(secondPersonalTask);

        LocalDateTime tuitionDateTime = LocalDateTime.parse(VALID_DATE_TIME_AMY, FORMATTER);
        AddTuitionTaskCommand addTuitionTask = new AddTuitionTaskCommand(
                INDEX_FIRST_PERSON, tuitionDateTime, VALID_DURATION_AMY, VALID_TASK_DESC_AMY);

        // same object -> returns true
        assertTrue(addFirstTask.equals(addFirstTask));

        // same values -> returns true
        assertTrue(addFirstTask.equals(addFirstTaskCopy));

        // different types -> returns false
        assertFalse(addFirstTask.equals(1));

        // null -> returns false
        assertFalse(addFirstTask.equals(null));

        // different task type -> returns false
        assertFalse(addFirstTask.equals(addTuitionTask));

        // different detail -> returns false
        assertFalse(addFirstTask.equals(addSecondTask));
    }

    /**
     * Generates a new AddPersonalTaskCommand with the details of the given personal task.
     */
    private AddPersonalTaskCommand getAddPersonalTaskCommandForTask(PersonalTask task, Model model) {
        AddPersonalTaskCommand command = new AddPersonalTaskCommand(task);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A Model stub that always throw a TimingClashException when trying to add a task.
     */
    private class ModelStubThrowingTimingClashException extends ModelStub {
        @Override
        public void addTask(Task task) throws TimingClashException {
            throw new TimingClashException();
        }



        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always accept the task being added.
     */
    private class ModelStubAcceptingPersonalTaskAdded extends ModelStub {
        final ArrayList<Task> tasksAdded = new ArrayList<>();

        @Override
        public void addTask(Task task) throws TimingClashException {
            requireNonNull(task);
            tasksAdded.add(task);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
```
###### \java\seedu\address\logic\commands\AddTuitionTaskCommandTest.java
``` java
public class AddTuitionTaskCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook2(), new UserPrefs());
    private LocalDateTime taskDateTimeAmy = LocalDateTime.parse(VALID_DATE_TIME_AMY, FORMATTER);

    @Test
    public void constructor_nullTaskDetail_throwsNullPointerException() {
        //one of the other 3 task details is null.
        thrown.expect(NullPointerException.class);
        new AddTuitionTaskCommand(INDEX_FIRST_PERSON, taskDateTimeAmy, VALID_DURATION_AMY, null);
    }

    @Test
    public void execute_taskAcceptedByModel_addSuccessful() throws Exception {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        AddTuitionTaskCommand addTuitionAmy = getAddTuitionTaskCommandForTask(
                INDEX_THIRD_PERSON, taskDateTimeAmy, VALID_DURATION_AMY, VALID_TASK_DESC_AMY);

        String expectedMessage = String.format(AddTuitionTaskCommand.MESSAGE_SUCCESS, TASK_AMY);
        expectedModel.addTask(TASK_AMY);

        assertCommandSuccess(addTuitionAmy, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        AddTuitionTaskCommand command = getAddTuitionTaskCommandForTask(outOfBoundIndex, taskDateTimeAmy,
                VALID_DURATION_AMY, VALID_TASK_DESC_AMY);
        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_clashingTask_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(MESSAGE_TASK_TIMING_CLASHES);

        getAddTuitionTaskCommandForTask(INDEX_THIRD_PERSON, taskDateTimeAmy, VALID_DURATION_AMY,
                VALID_TASK_DESC_AMY).execute();

        getAddTuitionTaskCommandForTask(INDEX_FIRST_PERSON, taskDateTimeAmy, VALID_DURATION_AMY,
                VALID_TASK_DESC_AMY).execute();
    }


    @Test
    public void equals() {
        LocalDateTime taskDateTimeBob = LocalDateTime.parse(VALID_DATE_TIME_BOB, FORMATTER);

        AddTuitionTaskCommand addTuitionAmy = getAddTuitionTaskCommandForTask(
                INDEX_THIRD_PERSON, taskDateTimeAmy, VALID_DURATION_AMY, VALID_TASK_DESC_AMY);
        AddTuitionTaskCommand addTuitionAmyCopy = getAddTuitionTaskCommandForTask(
                INDEX_THIRD_PERSON, taskDateTimeAmy, VALID_DURATION_AMY, VALID_TASK_DESC_AMY);
        AddTuitionTaskCommand addTuitionBob = new AddTuitionTaskCommand(
                INDEX_SECOND_PERSON, taskDateTimeBob, VALID_DURATION_BOB, VALID_TASK_DESC_BOB);

        // an AddPersonalTaskCommand object with same task details as addTuitionAmy
        AddPersonalTaskCommand addPersonalTask =
                new AddPersonalTaskCommand(new TaskBuilder(TASK_AMY).buildPersonalTask());

        // same value -> returns true
        assertTrue(addTuitionAmy.equals(addTuitionAmyCopy));

        // same object -> returns true
        assertTrue(addTuitionAmy.equals(addTuitionAmy));

        // different types -> returns false
        assertFalse(addTuitionAmy.equals(1));

        // null -> returns false
        assertFalse(addTuitionAmy.equals(null));

        // different task type -> returns false
        assertFalse(addTuitionAmy.equals(addPersonalTask));

        // different detail -> returns false
        assertFalse(addTuitionAmy.equals(addTuitionBob));
    }

    /**
     * Generates a new AddTuitionTaskCommand with the details of the given tuition task.
     */
    private AddTuitionTaskCommand getAddTuitionTaskCommandForTask(Index tuteeIndex, LocalDateTime taskDateTime,
                                                                  String duration, String description) {
        AddTuitionTaskCommand command = new AddTuitionTaskCommand(tuteeIndex, taskDateTime, duration, description);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\address\logic\commands\DeleteTaskCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteTaskCommand}.
 */
public class DeleteTaskCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook1(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        Task taskToDelete = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        DeleteTaskCommand deleteTaskCommand = prepareCommand(INDEX_FIRST_TASK);

        String expectedMessage = String.format(DeleteTaskCommand.MESSAGE_SUCCESS, taskToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteTask(taskToDelete);

        assertCommandSuccess(deleteTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        DeleteTaskCommand deleteTaskCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showTaskAtIndex(model, INDEX_FIRST_TASK);

        Task taskToDelete = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        DeleteTaskCommand deleteTaskCommand = prepareCommand(INDEX_FIRST_TASK);

        String expectedMessage = String.format(DeleteTaskCommand.MESSAGE_SUCCESS, taskToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteTask(taskToDelete);
        showNoTask(expectedModel);

        assertCommandSuccess(deleteTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showTaskAtIndex(model, INDEX_FIRST_TASK);

        Index outOfBoundIndex = INDEX_SECOND_TASK;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getTaskList().size());

        DeleteTaskCommand deleteTaskCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Task taskToDelete = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        DeleteTaskCommand deleteTaskCommand = prepareCommand(INDEX_FIRST_TASK);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        // delete -> first task deleted
        deleteTaskCommand.execute();
        undoRedoStack.push(deleteTaskCommand);

        // undo -> reverts addressbook back to previous state and filtered task list to show all tasks
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first task deleted again
        expectedModel.deleteTask(taskToDelete);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        DeleteTaskCommand deleteTaskCommand = prepareCommand(outOfBoundIndex);

        // execution failed -> deleteTaskCommand not pushed into undoRedoStack
        assertCommandFailure(deleteTaskCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Deletes a {@code task} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted task in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the task object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameTaskDeleted() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        DeleteTaskCommand deleteTaskCommand = prepareCommand(INDEX_FIRST_TASK);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        showTaskAtIndex(model, INDEX_SECOND_TASK);
        Task taskToDelete = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        // delete -> deletes second task in unfiltered task list / first task in filtered task list
        deleteTaskCommand.execute();
        undoRedoStack.push(deleteTaskCommand);

        // undo -> reverts addressbook back to previous state and filtered task list to show all tasks
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.deleteTask(taskToDelete);
        assertNotEquals(taskToDelete, model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased()));
        // redo -> deletes same second task in unfiltered task list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        DeleteTaskCommand deleteFirstCommand = prepareCommand(INDEX_FIRST_TASK);
        DeleteTaskCommand deleteSecondCommand = prepareCommand(INDEX_SECOND_TASK);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteTaskCommand deleteFirstCommandCopy = prepareCommand(INDEX_FIRST_TASK);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // one command preprocessed when previously equal -> returns false
        deleteFirstCommandCopy.preprocessUndoableCommand();
        assertFalse(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Returns a {@code DeleteTaskCommand} with the parameter {@code index}.
     */
    private DeleteTaskCommand prepareCommand(Index index) {
        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(index);
        deleteTaskCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteTaskCommand;
    }

    /**
     * Updates {@code model}'s filtered tasks list to show no one.
     */
    private void showNoTask(Model model) {
        model.updateFilteredTaskList(t -> false);

        assertTrue(model.getFilteredTaskList().isEmpty());
    }
}
```
###### \java\seedu\address\logic\commands\FindPersonCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code FindPersonCommand}.
 */
public class FindPersonCommandTest {
    private static final int INDEX_FIRST_ELEMENT = 0;
    private static final int INDEX_SECOND_ELEMENT = 1;

    private Model model = new ModelManager(getTypicalAddressBook2(), new UserPrefs());

    private final String[] firstNameKeywords = {VALID_NAME_BOB.split("\\s+")[INDEX_FIRST_ELEMENT],
            VALID_NAME_AMY.split("\\s+")[INDEX_SECOND_ELEMENT]};
    private final String[] secondNameKeywords = {VALID_NAME_BOB.split("\\s+")[INDEX_FIRST_ELEMENT]};

    private final FindPersonCommand findFirstName = new FindPersonCommand(CATEGORY_NAME, firstNameKeywords);
    private final FindPersonCommand findSecondName = new FindPersonCommand(CATEGORY_NAME, secondNameKeywords);

    @Test
    public void equals() {
        // same object -> returns true
        assertTrue(findFirstName.equals(findFirstName));

        // same values -> returns true
        FindPersonCommand findFirstCommandCopy = new FindPersonCommand(CATEGORY_NAME, firstNameKeywords);
        assertTrue(findFirstName.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstName.equals(1));

        // null -> returns false
        assertFalse(findFirstName.equals(null));

        // different person -> returns false
        assertFalse(findFirstName.equals(findSecondName));
    }

    @Test
    public void execute_findName_foundSuccessfully() {
        //multiple keywords
        findFirstName.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = String.format(FindPersonCommand.MESSAGE_SUCCESS + "\n"
                + MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        assertCommandSuccess(findFirstName, expectedMessage, Arrays.asList(AMYTUTEE, BOBTUTEE));

        //single keyword
        findSecondName.setData(model, new CommandHistory(), new UndoRedoStack());
        expectedMessage = String.format(FindPersonCommand.MESSAGE_SUCCESS + "\n" + MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        assertCommandSuccess(findSecondName, expectedMessage, Arrays.asList(BOBTUTEE));
    }

    @Test
    public void execute_findEducatonLevel_foundSuccessfully() {
        String[] educationLevelKeywords = {VALID_EDUCATION_LEVEL_AMY};
        FindPersonCommand findEducationLevel = new FindPersonCommand(CATEGORY_EDUCATION_LEVEL, educationLevelKeywords);
        findEducationLevel.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = String.format(FindPersonCommand.MESSAGE_SUCCESS + "\n"
                + MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        //Alice and Amy have the same education level
        assertCommandSuccess(findEducationLevel, expectedMessage, Arrays.asList(ALICETUTEE, AMYTUTEE));
    }

    @Test
    public void execute_findGrade_foundSuccessfully() {
        String[] gradeKeywords = {VALID_GRADE_AMY, VALID_GRADE_BOB};
        FindPersonCommand findGrade = new FindPersonCommand(CATEGORY_GRADE, gradeKeywords);
        findGrade.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = String.format(FindPersonCommand.MESSAGE_SUCCESS + "\n"
                + MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        assertCommandSuccess(findGrade, expectedMessage, Arrays.asList(AMYTUTEE, BOBTUTEE));
    }

    @Test
    public void execute_findSchool_foundSuccessfully() {
        String[] schoolKeywords = {VALID_SCHOOL_AMY.split("\\s+")[INDEX_FIRST_ELEMENT]};
        FindPersonCommand findSchool = new FindPersonCommand(CATEGORY_SCHOOL, schoolKeywords);
        findSchool.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = String.format(FindPersonCommand.MESSAGE_SUCCESS + "\n"
                + MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        assertCommandSuccess(findSchool, expectedMessage, Arrays.asList(AMYTUTEE));
    }

    @Test
    public void execute_findSubject_foundSuccessfully() {
        String[] subjectKeywords = {VALID_SUBJECT_BOB};
        FindPersonCommand findSubject = new FindPersonCommand(CATEGORY_SUBJECT, subjectKeywords);
        findSubject.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = String.format(FindPersonCommand.MESSAGE_SUCCESS + "\n"
                + MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        //Alice and Bob learn the same subject.
        assertCommandSuccess(findSubject, expectedMessage, Arrays.asList(ALICETUTEE, BOBTUTEE));
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Person>} is equal to {@code expectedList}<br>
     *     - the {@code AddressBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindPersonCommand command, String expectedMessage, List<Person> expectedList) {
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedAddressBook, model.getAddressBook());
    }
}
```
###### \java\seedu\address\logic\commands\ListTuteeCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for ListTuteeCommand.
 */
public class ListTuteeCommandTest {

    private Model model;
    private Model expectedModel;
    private ListTuteeCommand listTuteeCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook2(), new UserPrefs());
        expectedModel = setExpectedModel(model);

        listTuteeCommand = new ListTuteeCommand();
        listTuteeCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_tuteeListIsNotFiltered_showsSameList() {
        assertCommandSuccess(listTuteeCommand, model, ListTuteeCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_tuteeListIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(listTuteeCommand, model, ListTuteeCommand.MESSAGE_SUCCESS, expectedModel);
    }

    /**
     * Returns a model that has been filtered to show only tutees
     */
    private ModelManager setExpectedModel(Model model) {
        ModelManager modelManager = new ModelManager(model.getAddressBook(), new UserPrefs());
        modelManager.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_TUTEES);
        return modelManager;
    }

}
```
###### \java\seedu\address\logic\commands\SortPersonCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code SortPersonCommand}.
 */
public class SortPersonCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook2(), new UserPrefs());

    private final SortPersonCommand sortName = new SortPersonCommand(CATEGORY_NAME);

    @Test
    public void equals() {
        // same object -> returns true
        assertTrue(sortName.equals(sortName));

        // same values -> returns true
        SortPersonCommand sortNameCopy = new SortPersonCommand(CATEGORY_NAME);
        assertTrue(sortName.equals(sortNameCopy));

        // different types -> returns false
        assertFalse(sortName.equals(1));

        // null -> returns false
        assertFalse(sortName.equals(null));

        // different category -> returns false
        SortPersonCommand sortGrade = new SortPersonCommand(CATEGORY_GRADE);
        assertFalse(sortName.equals(sortGrade));
    }

    @Test
    public void execute_sortName_sortedSuccessfully() {
        sortName.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = String.format(SortPersonCommand.MESSAGE_SUCCESS);
        assertCommandSuccess(sortName, expectedMessage,
                Arrays.asList(ALICETUTEE, AMYTUTEE, BOBTUTEE, DANIEL));
    }

    @Test
    public void execute_sortEducatonLevel_sortedSuccessfully() {
        SortPersonCommand sortEducationLevel = new SortPersonCommand(CATEGORY_EDUCATION_LEVEL);
        sortEducationLevel.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = String.format(SortPersonCommand.MESSAGE_SUCCESS);
        assertCommandSuccess(sortEducationLevel, expectedMessage,
                Arrays.asList(BOBTUTEE, ALICETUTEE, AMYTUTEE, DANIEL));
    }

    @Test
    public void execute_sortGrade_sortedSuccessfully() {
        SortPersonCommand sortGrade = new SortPersonCommand(CATEGORY_GRADE);
        sortGrade.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = String.format(SortPersonCommand.MESSAGE_SUCCESS);
        assertCommandSuccess(sortGrade, expectedMessage,
                Arrays.asList(BOBTUTEE, AMYTUTEE, ALICETUTEE, DANIEL));
    }

    @Test
    public void execute_sortSchool_sortedSuccessfully() {
        SortPersonCommand sortSchool = new SortPersonCommand(CATEGORY_SCHOOL);
        sortSchool.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = String.format(SortPersonCommand.MESSAGE_SUCCESS);
        assertCommandSuccess(sortSchool, expectedMessage,
                Arrays.asList(ALICETUTEE, AMYTUTEE, BOBTUTEE, DANIEL));
    }

    @Test
    public void execute_sortSubject_sortedSuccessfully() {
        SortPersonCommand sortSubject = new SortPersonCommand(CATEGORY_SUBJECT);
        sortSubject.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = String.format(SortPersonCommand.MESSAGE_SUCCESS);
        assertCommandSuccess(sortSubject, expectedMessage,
                Arrays.asList(AMYTUTEE, ALICETUTEE, BOBTUTEE, DANIEL));
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Person>} is equal to {@code expectedList}<br>
     *     - the {@code AddressBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(SortPersonCommand command, String expectedMessage, List<Person> expectedList) {
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedAddressBook, model.getAddressBook());
    }
}
```
###### \java\seedu\address\logic\parser\FindPersonCommandParserTest.java
``` java
/**
 * Contains tests for {@code FindPersonCommandParser}.
 */
public class FindPersonCommandParserTest {
    private static final int INDEX_FIRST_ELEMENT = 0;
    public static final String VALID_FIRST_NAME_BOB = VALID_NAME_BOB.toLowerCase().split("\\s+")[INDEX_FIRST_ELEMENT];
    private FindPersonCommandParser parser = new FindPersonCommandParser();

    private final String[] nameKeywords = {VALID_FIRST_NAME_BOB};
    private final String[] educationLevelKeywords = {VALID_EDUCATION_LEVEL_AMY.toLowerCase()};
    private final String[] gradeKeywords = {VALID_GRADE_AMY.toLowerCase(), VALID_GRADE_BOB.toLowerCase()};
    private final String[] schoolKeywords = VALID_SCHOOL_AMY.toLowerCase().split("\\s+");
    private final String[] subjectKeywords = {VALID_SUBJECT_AMY.toLowerCase(), VALID_SUBJECT_BOB.toLowerCase()};

    private final String invalidCategory = "age";

    @Test
    public void parse_invalidArg_throwsParseException() {
        //empty input
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPersonCommand.MESSAGE_USAGE));

        //not enough arguments
        assertParseFailure(parser, CATEGORY_GRADE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPersonCommand.MESSAGE_USAGE));

        //invalid category
        assertParseFailure(parser, invalidCategory + " " + schoolKeywords[INDEX_FIRST_ELEMENT],
                String.format(MESSAGE_INVALID_FILTER_CATEGORY, FindPersonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // filter by name using a single keyword
        FindPersonCommand expectedFindName = new FindPersonCommand(CATEGORY_NAME, nameKeywords);
        assertParseSuccess(parser, CATEGORY_NAME + " Bob", expectedFindName);

        // filter by education level using a single keyword
        FindPersonCommand expectedFindEducatonLevel =
                new FindPersonCommand(CATEGORY_EDUCATION_LEVEL, educationLevelKeywords);
        assertParseSuccess(parser,
                CATEGORY_EDUCATION_LEVEL + " " + VALID_EDUCATION_LEVEL_AMY, expectedFindEducatonLevel);

        // filter by grade using 2 different keywords
        FindPersonCommand expectedFindGrade = new FindPersonCommand(CATEGORY_GRADE, gradeKeywords);
        assertParseSuccess(parser, CATEGORY_GRADE + " " + VALID_GRADE_AMY
                + " " + VALID_GRADE_BOB, expectedFindGrade);

        // filter by school using multiple keywords from a single school
        FindPersonCommand expectedFindSchool = new FindPersonCommand(CATEGORY_SCHOOL, schoolKeywords);
        assertParseSuccess(parser, CATEGORY_SCHOOL + " " + VALID_SCHOOL_AMY, expectedFindSchool);

        // filter by subject using 2 different keywords
        FindPersonCommand expectedFindSubject = new FindPersonCommand(CATEGORY_SUBJECT, subjectKeywords);
        assertParseSuccess(parser, CATEGORY_SUBJECT + " " + VALID_SUBJECT_AMY
                + " " + VALID_SUBJECT_BOB, expectedFindSubject);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, CATEGORY_NAME + " \n\t  " + "Bob", expectedFindName);
    }
}
```
###### \java\seedu\address\logic\parser\NaturalLanguageIdentifierTest.java
``` java
public class NaturalLanguageIdentifierTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private NaturalLanguageIdentifier identifier = NaturalLanguageIdentifier.getInstance();

    @Test
    public void getInstance_firstTimeCalled_returnInstanceOfClass() {
        assertTrue(identifier instanceof NaturalLanguageIdentifier);
    }

    @Test
    public void getInstance_subsequentCalls_returnSameInstance() {
        NaturalLanguageIdentifier identifierCopy = NaturalLanguageIdentifier.getInstance();
        assertEquals(identifier, identifierCopy);
    }

    @Test
    public void getMonthAsString_recognizableInput_returnMonth() {
        LocalDateTime current = LocalDateTime.now();

        //natural languages which refer to current month
        assertEquals(current.getMonth().name(), identifier.getMonthAsString(NATURAL_CURRENT_MONTH));
        assertEquals(current.getMonth().name(), identifier.getMonthAsString(NATURAL_NOW));

        //natural language which refers to last month
        assertEquals(current.getMonth().minus(1).name(), identifier.getMonthAsString(NATURAL_LAST_MONTH));

        //natural language which refers to next month
        assertEquals(current.getMonth().plus(1).name(), identifier.getMonthAsString(NATURAL_NEXT_MONTH));
    }

    @Test
    public void getMonthAsString_unrecognizableInput_returnInput() {
        LocalDateTime current = LocalDateTime.now();
        String unrecognizable = "unrecognizable input";
        assertEquals(unrecognizable, identifier.getMonthAsString(unrecognizable));
    }

    @Test
    public void getMonthAsString_nullInput_returnInput() {
        LocalDateTime current = LocalDateTime.now();
        String unrecognizable = null;
        thrown.expect(NullPointerException.class);
        String result = identifier.getMonthAsString(unrecognizable);
    }

    @Test
    public void mergeTwoWordedNaturalLanguage_emptyString_returnEmptyString() {
        String[] userInputs = {};
        String[] expectedResults = {};
        String[] results = identifier.mergeTwoWordedNaturalLanguage(userInputs);
        assertArrayEquals(expectedResults, results);
    }

    @Test
    public void mergeTwoWordedNaturalLanguage_oneRecognizableElement_returnInputtedArray() {
        String[] userInputs = {"this"};
        String[] expectedResults = {"this"};
        String[] results = identifier.mergeTwoWordedNaturalLanguage(userInputs);
        assertArrayEquals(expectedResults, userInputs);
    }

    @Test
    public void mergeTwoWordedNaturalLanguage_oneUnrecognizableElement_returnInputtedArray() {
        String[] userInputs = {"unrecognizable"};
        String[] expectedResults = {"unrecognizable"};
        String[] results = identifier.mergeTwoWordedNaturalLanguage(userInputs);
        assertArrayEquals(expectedResults, results);
    }

    @Test
    public void mergeTwoWordedNaturalLanguage_multipleElements_returnMergedArray() {
        String[] userInputs = {"this", "month", "today", "unrecognized", "last", "month", "unrecognized"};
        String[] expectedResults = {"this month", "today", "unrecognized", "last month", "unrecognized"};
        String[] results = identifier.mergeTwoWordedNaturalLanguage(userInputs);
        assertArrayEquals(expectedResults, results);
    }
}
```
###### \java\seedu\address\logic\parser\ParserUtilTest.java
``` java
    @Test
    public void parseDateTime_invalidInput_throwsDateTimeParseException() {
        //null date and time
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseDateTime(null));

        //invalid date in non leap year
        Assert.assertThrows(DateTimeParseException.class, () -> ParserUtil
                .parseDateTime("29/02/2018 " + VALID_TIME));

        //invalid date in century year
        Assert.assertThrows(DateTimeParseException.class, () -> ParserUtil
                .parseDateTime("29/02/1900 " + VALID_TIME));

        //invalid date in month with 30 days
        Assert.assertThrows(DateTimeParseException.class, () -> ParserUtil
                .parseDateTime("31/04/2018 " + VALID_TIME));

        //invalid date in month with 31 days
        Assert.assertThrows(DateTimeParseException.class, () -> ParserUtil
                .parseDateTime("32/03/2018 " + VALID_TIME));

        //invalid hour
        Assert.assertThrows(DateTimeParseException.class, () -> ParserUtil
                .parseDateTime(VALID_DATE + " 25:00"));

        //invalid minute
        Assert.assertThrows(DateTimeParseException.class, () -> ParserUtil
                .parseDateTime(VALID_DATE + "12:60"));
    }

    @Test
    public void parseDateTime_validInput_parsedSuccessfully() {
        //beginning of the month
        LocalDateTime expectedDateTime = LocalDateTime.parse("01/10/2018 " + VALID_TIME, FORMATTER);
        assertEquals(expectedDateTime, parseDateTime("01/10/2018 " + VALID_TIME));

        //leap year
        expectedDateTime = LocalDateTime.parse("29/02/2020 " + VALID_TIME, FORMATTER);
        assertEquals(expectedDateTime, parseDateTime("29/02/2020 " + VALID_TIME));

        //month with 30 days
        expectedDateTime = LocalDateTime.parse("30/04/2020 " + VALID_TIME, FORMATTER);
        assertEquals(expectedDateTime, parseDateTime("30/04/2020 " + VALID_TIME));

        //month with 31 days
        expectedDateTime = LocalDateTime.parse("31/03/2020 " + VALID_TIME, FORMATTER);
        assertEquals(expectedDateTime, parseDateTime("31/03/2020 " + VALID_TIME));

        //valid time at boundary value
        expectedDateTime = LocalDateTime.parse(VALID_DATE + " 12:00", FORMATTER);
        assertEquals(expectedDateTime, parseDateTime(VALID_DATE + " 12:00"));
    }

    @Test
    public void parseDuration_invalidInput_throwsDateTimeParseException() {
        //null duration
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseDuration(null));

        //invalid duration
        Assert.assertThrows(DurationParseException.class, () -> ParserUtil
                .parseDuration(INVALID_DURATION));
    }

    @Test
    public void parseDuration_validInput_parsedSuccessfully() throws Exception {
        String expectedDuration = VALID_DURATION;
        assertEquals(expectedDuration, parseDuration(VALID_DURATION));
    }

    @Test
    public void parseDescription_noDescriptionWithinInput_returnsEmptyString() {
        //user input without description
        String[] validInputs = VALID_TASK_WITHOUT_DESCRIPTION.split("\\s+", MAXIMUM_AMOUNT_OF_PARAMETERS);
        String expectedDescription = "";
        assertEquals(expectedDescription, ParserUtil.parseDescription(validInputs, MAXIMUM_AMOUNT_OF_PARAMETERS));

        //user input with description
        validInputs = VALID_TASK_WITH_DESCRIPTION.split("\\s+", MAXIMUM_AMOUNT_OF_PARAMETERS);
        expectedDescription = VALID_DESCRIPTION;
        assertEquals(expectedDescription, ParserUtil.parseDescription(validInputs, MAXIMUM_AMOUNT_OF_PARAMETERS));
    }
}
```
###### \java\seedu\address\logic\parser\SortPersonCommandParserTest.java
``` java
/**
 * Contains tests for {@code SortPersonCommandParser}.
 */
public class SortPersonCommandParserTest {
    private SortPersonCommandParser parser = new SortPersonCommandParser();

    private final String invalidCategory = "age";

    @Test
    public void parse_invalidArg_throwsParseException() {
        //empty input
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortPersonCommand.MESSAGE_USAGE));

        //too many arguments
        assertParseFailure(parser, CATEGORY_GRADE + " " + CATEGORY_EDUCATION_LEVEL,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortPersonCommand.MESSAGE_USAGE));

        //invalid category
        assertParseFailure(parser, invalidCategory,
                String.format(MESSAGE_INVALID_SORTER_CATEGORY, SortPersonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // sort by name
        SortPersonCommand expectedSortName = new SortPersonCommand(CATEGORY_NAME);
        assertParseSuccess(parser, CATEGORY_NAME, expectedSortName);

        // sort by education level
        SortPersonCommand expectedSortEducatonLevel = new SortPersonCommand(CATEGORY_EDUCATION_LEVEL);
        assertParseSuccess(parser, CATEGORY_EDUCATION_LEVEL, expectedSortEducatonLevel);

        // sort by grade
        SortPersonCommand expectedSortGrade = new SortPersonCommand(CATEGORY_GRADE);
        assertParseSuccess(parser, CATEGORY_GRADE, expectedSortGrade);

        // sort by school
        SortPersonCommand expectedSortSchool = new SortPersonCommand(CATEGORY_SCHOOL);
        assertParseSuccess(parser, CATEGORY_SCHOOL, expectedSortSchool);

        // sort by subject
        SortPersonCommand expectedSortSubject = new SortPersonCommand(CATEGORY_SUBJECT);
        assertParseSuccess(parser, CATEGORY_SUBJECT, expectedSortSubject);

        // multiple whitespaces before and after sort category
        assertParseSuccess(parser, "   \n\t" + CATEGORY_NAME + "\n\t", expectedSortName);
    }
}
```
###### \java\seedu\address\model\person\PersonSortUtilTest.java
``` java
public class PersonSortUtilTest {
    private static final String LOWER_ORDER = "a";
    private static final String MIDDLE_ORDER = "b";
    private static final String HIGHER_ORDER = "c";
    private static final String EDUCATION_LEVEL_PRIMARY = "primary";
    private static final String EDUCATION_LEVEL_SECONDARY = "secondary";
    private static final String EDUCATION_LEVEL_JUNIOR_COLLEGE = "junior college";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Person lowerOrder = new TuteeBuilder().withName(MIDDLE_ORDER).withEducationLevel(EDUCATION_LEVEL_PRIMARY)
            .withGrade(MIDDLE_ORDER).withSchool(MIDDLE_ORDER).withSubject(MIDDLE_ORDER).build();
    private Person higherOrder = new TuteeBuilder().withName(HIGHER_ORDER).withEducationLevel(EDUCATION_LEVEL_SECONDARY)
            .withGrade(HIGHER_ORDER).withSchool(HIGHER_ORDER).withSubject(HIGHER_ORDER).build();
    private Person versatileOrder;

    @Test
    public void getComparator_validNameCategory_compareSuccessfully() {
        //all first person's categories have lower lexicographical order
        Comparator<Person> comparator = getComparator(CATEGORY_NAME);
        int expected = lowerOrder.getName().fullName.compareTo(higherOrder.getName().fullName);
        assertCompareSuccessfully(comparator, expected, lowerOrder, higherOrder);

        //first person's name has lower lexicographical order and the other categories have equal order
        versatileOrder = new TuteeBuilder((Tutee) lowerOrder).withName(HIGHER_ORDER).build();
        expected = lowerOrder.getName().fullName.compareTo(versatileOrder.getName().fullName);
        assertCompareSuccessfully(comparator, expected, lowerOrder, versatileOrder);

        //first person's name has lower lexicographical order but the other categories have higher order
        versatileOrder = new TuteeBuilder((Tutee) higherOrder).withEducationLevel(EDUCATION_LEVEL_JUNIOR_COLLEGE)
                .withGrade(LOWER_ORDER).withSchool(LOWER_ORDER).withSubject(LOWER_ORDER).build();
        expected = lowerOrder.getName().fullName.compareTo(versatileOrder.getName().fullName);
        assertCompareSuccessfully(comparator, expected, lowerOrder, versatileOrder);

        //first person's name has equal lexicographical order
        expected = lowerOrder.getName().fullName.compareTo(lowerOrder.getName().fullName);
        assertCompareSuccessfully(comparator, expected, lowerOrder, lowerOrder);

        //first person's name has higher lexicographical order
        expected = higherOrder.getName().fullName.compareTo(lowerOrder.getName().fullName);
        assertCompareSuccessfully(comparator, expected, higherOrder, lowerOrder);
    }

    @Test
    public void getComparator_validEducationLevelCategory_compareSuccessfully() {
        //all first person's categories have lower lexicographical order
        Comparator<Person> comparator = getComparator(CATEGORY_EDUCATION_LEVEL);
        int expected = ((Tutee) lowerOrder).getEducationLevel().toString()
                .compareTo(((Tutee) higherOrder).getEducationLevel().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, higherOrder);

        //first person's education level has lower lexicographical order and the other categories have equal order
        versatileOrder = new TuteeBuilder((Tutee) lowerOrder).withEducationLevel(EDUCATION_LEVEL_SECONDARY).build();
        expected = ((Tutee) lowerOrder).getEducationLevel().toString()
                .compareTo(((Tutee) versatileOrder).getEducationLevel().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, versatileOrder);

        //first person's education level has lower lexicographical order but the other categories have higher order
        versatileOrder = new TuteeBuilder((Tutee) higherOrder).withName(LOWER_ORDER)
                .withGrade(LOWER_ORDER).withSchool(LOWER_ORDER).withSubject(LOWER_ORDER).build();
        expected = ((Tutee) lowerOrder).getEducationLevel().toString()
                .compareTo(((Tutee) versatileOrder).getEducationLevel().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, versatileOrder);

        //first person's education level has equal lexicographical order
        expected = ((Tutee) lowerOrder).getEducationLevel().toString()
                .compareTo(((Tutee) lowerOrder).getEducationLevel().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, lowerOrder);

        //first person's education level has higher lexicographical order
        expected = ((Tutee) higherOrder).getEducationLevel().toString()
                .compareTo(((Tutee) lowerOrder).getEducationLevel().toString());
        assertCompareSuccessfully(comparator, expected, higherOrder, lowerOrder);
    }

    @Test
    public void getComparator_validGradeCategory_compareSuccessfully() {
        //all first person's categories have lower lexicographical order
        Comparator<Person> comparator = getComparator(CATEGORY_GRADE);
        int expected = ((Tutee) lowerOrder).getGrade().toString()
                .compareTo(((Tutee) higherOrder).getGrade().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, higherOrder);

        //first person's grade has lower lexicographical order and the other categories have equal order
        versatileOrder = new TuteeBuilder((Tutee) lowerOrder).withGrade(HIGHER_ORDER).build();
        expected = ((Tutee) lowerOrder).getGrade().toString()
                .compareTo(((Tutee) versatileOrder).getGrade().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, versatileOrder);

        //first person's grade has lower lexicographical order but the other categories have higher order
        versatileOrder = new TuteeBuilder((Tutee) higherOrder).withEducationLevel(EDUCATION_LEVEL_JUNIOR_COLLEGE)
                .withName(LOWER_ORDER).withSchool(LOWER_ORDER).withSubject(LOWER_ORDER).build();
        expected = ((Tutee) lowerOrder).getGrade().toString()
                .compareTo(((Tutee) versatileOrder).getGrade().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, versatileOrder);

        //first person's grade has equal lexicographical order
        expected = ((Tutee) lowerOrder).getGrade().toString()
                .compareTo(((Tutee) lowerOrder).getGrade().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, lowerOrder);

        //first person's grade has higher lexicographical order
        expected = ((Tutee) higherOrder).getGrade().toString()
                .compareTo(((Tutee) lowerOrder).getGrade().toString());
        assertCompareSuccessfully(comparator, expected, higherOrder, lowerOrder);
    }

    @Test
    public void getComparator_validSchoolCategory_compareSuccessfully() {
        //all first person's categories have lower lexicographical order
        Comparator<Person> comparator = getComparator(CATEGORY_SCHOOL);
        int expected = ((Tutee) lowerOrder).getSchool().toString()
                .compareTo(((Tutee) higherOrder).getSchool().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, higherOrder);

        //first person's school has lower lexicographical order and the other categories have equal order
        versatileOrder = new TuteeBuilder((Tutee) lowerOrder).withSchool(HIGHER_ORDER).build();
        expected = ((Tutee) lowerOrder).getSchool().toString()
                .compareTo(((Tutee) versatileOrder).getSchool().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, versatileOrder);

        //first person's school has lower lexicographical order but the other categories have higher order
        versatileOrder = new TuteeBuilder((Tutee) higherOrder).withEducationLevel(EDUCATION_LEVEL_JUNIOR_COLLEGE)
                .withName(LOWER_ORDER).withGrade(LOWER_ORDER).withSubject(LOWER_ORDER).build();
        expected = ((Tutee) lowerOrder).getSchool().toString()
                .compareTo(((Tutee) versatileOrder).getSchool().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, versatileOrder);

        //first person's school has equal lexicographical order
        expected = ((Tutee) lowerOrder).getSchool().toString()
                .compareTo(((Tutee) lowerOrder).getSchool().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, lowerOrder);

        //first person's school has higher lexicographical order
        expected = ((Tutee) higherOrder).getSchool().toString()
                .compareTo(((Tutee) lowerOrder).getSchool().toString());
        assertCompareSuccessfully(comparator, expected, higherOrder, lowerOrder);
    }

    @Test
    public void getComparator_validSubjectCategory_compareSuccessfully() {
        //all first person's categories have lower lexicographical order
        Comparator<Person> comparator = getComparator(CATEGORY_SUBJECT);
        int expected = ((Tutee) lowerOrder).getSubject().toString()
                .compareTo(((Tutee) higherOrder).getSubject().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, higherOrder);

        //first person's subject has lower lexicographical order and the other categories have equal order
        versatileOrder = new TuteeBuilder((Tutee) lowerOrder).withSubject(HIGHER_ORDER).build();
        expected = ((Tutee) lowerOrder).getSubject().toString()
                .compareTo(((Tutee) versatileOrder).getSubject().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, versatileOrder);

        //first person's subject has lower lexicographical order but the other categories have higher order
        versatileOrder = new TuteeBuilder((Tutee) higherOrder).withEducationLevel(EDUCATION_LEVEL_JUNIOR_COLLEGE)
                .withName(LOWER_ORDER).withGrade(LOWER_ORDER).withSchool(LOWER_ORDER).build();
        expected = ((Tutee) lowerOrder).getSubject().toString()
                .compareTo(((Tutee) versatileOrder).getSubject().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, versatileOrder);

        //first person's subject has equal lexicographical order
        expected = ((Tutee) lowerOrder).getSubject().toString()
                .compareTo(((Tutee) lowerOrder).getSubject().toString());
        assertCompareSuccessfully(comparator, expected, lowerOrder, lowerOrder);

        //first person's subject has higher lexicographical order
        expected = ((Tutee) higherOrder).getSubject().toString()
                .compareTo(((Tutee) lowerOrder).getSubject().toString());
        assertCompareSuccessfully(comparator, expected, higherOrder, lowerOrder);
    }

    @Test
    public void getComparator_invalidCategory_assertionErrorHappen() {
        thrown.expect(AssertionError.class);
        Comparator<Person> comparator = getComparator("email");
    }

    @Test
    public void compareNameLexicographically_validInput_compareSuccessfully() {
        lowerOrder = new PersonBuilder().withName("Albert").build();
        higherOrder = new PersonBuilder().withName("Alice").build();

        //first person has lower lexicographical order
        int expected = PersonSortUtil.compareNameLexicographically(lowerOrder, higherOrder);
        int actual = lowerOrder.getName().fullName.compareToIgnoreCase(higherOrder.getName().fullName);
        assertEquals(expected, actual);

        //first person has higher lexicographical order
        expected = PersonSortUtil.compareNameLexicographically(higherOrder, lowerOrder);
        actual = higherOrder.getName().fullName.compareToIgnoreCase(lowerOrder.getName().fullName);
        assertEquals(expected, actual);

        //both have exactly same name
        Person lowerOrderCopy = new PersonBuilder(lowerOrder).build();
        expected = PersonSortUtil.compareNameLexicographically(lowerOrder, lowerOrderCopy);
        actual = lowerOrder.getName().fullName.compareToIgnoreCase(lowerOrderCopy.getName().fullName);
        assertEquals(expected, actual);

        //both have same name with different cases -> treated as 2 same namess
        higherOrder = new PersonBuilder(lowerOrder).withName("ALBERT").build();
        int expectedFromSameName = expected;
        int expectedFromDiffName = compareNameLexicographically(lowerOrder, higherOrder);
        assertEquals(expectedFromSameName, expectedFromDiffName);
    }

    /**
     * Checks whether comparator is able to perform the desired comparison.
     */
    private void assertCompareSuccessfully(Comparator<Person> comparator, int expected, Person first, Person second) {
        int actual = comparator.compare(first, second);
        assertEquals(expected, actual);
    }
}
```
###### \java\seedu\address\model\tutee\EducationLevelContainsKeywordsPredicateTest.java
``` java
public class EducationLevelContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        EducationLevelContainsKeywordsPredicate firstPredicate =
                new EducationLevelContainsKeywordsPredicate(firstPredicateKeywordList);
        EducationLevelContainsKeywordsPredicate secondPredicate =
                new EducationLevelContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        EducationLevelContainsKeywordsPredicate firstPredicateCopy =
                new EducationLevelContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different education levels -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_educationLevelContainsKeywords_returnsTrue() {
        // One keyword
        EducationLevelContainsKeywordsPredicate predicate =
                new EducationLevelContainsKeywordsPredicate(Collections.singletonList(VALID_EDUCATION_LEVEL_AMY));
        assertTrue(predicate.test(new TuteeBuilder().withEducationLevel(VALID_EDUCATION_LEVEL_AMY).build()));

        // Multiple keywords
        predicate = new EducationLevelContainsKeywordsPredicate(Arrays
                .asList("junior", "college"));
        assertTrue(predicate.test(new TuteeBuilder()
                .withEducationLevel("junior college").build()));

        // Only one matching keyword
        predicate = new EducationLevelContainsKeywordsPredicate(Arrays.asList("junior"));
        assertTrue(predicate.test(new TuteeBuilder()
                .withEducationLevel("junior college").build()));

        // Mixed-case keywords
        predicate = new EducationLevelContainsKeywordsPredicate(Arrays.asList("JuNiOr", "colLEGE"));
        assertTrue(predicate.test(new TuteeBuilder().withEducationLevel("junior college").build()));
    }

    @Test
    public void test_educationLevelDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        EducationLevelContainsKeywordsPredicate predicate =
                new EducationLevelContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new TuteeBuilder().withEducationLevel(VALID_EDUCATION_LEVEL_AMY).build()));

        // Non-matching keyword
        predicate = new EducationLevelContainsKeywordsPredicate(Arrays.asList(VALID_EDUCATION_LEVEL_ROBERT));
        assertFalse(predicate.test(new TuteeBuilder().withEducationLevel(VALID_EDUCATION_LEVEL_AMY).build()));

        // Keywords match grade, school and subject, but does not match education level
        predicate = new EducationLevelContainsKeywordsPredicate(Arrays.asList("school", "B", "mathematics"));
        assertFalse(predicate.test(new TuteeBuilder().withEducationLevel(VALID_EDUCATION_LEVEL_AMY).withSchool("school")
                .withGrade("B").withSubject("mathematics").build()));

        // Keywords match email and address, but does not match education level
        predicate = new EducationLevelContainsKeywordsPredicate(Arrays
                .asList("Bob", "alice@email.com", "Main", "Street"));
        assertFalse(predicate.test(new TuteeBuilder().withEducationLevel(VALID_EDUCATION_LEVEL_AMY)
                .withEmail("alice@email.com").withAddress("Main Street").build()));

    }
}
```
###### \java\seedu\address\model\tutee\GradeContainsKeywordsPredicateTest.java
``` java
public class GradeContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        GradeContainsKeywordsPredicate firstPredicate =
                new GradeContainsKeywordsPredicate(firstPredicateKeywordList);
        GradeContainsKeywordsPredicate secondPredicate =
                new GradeContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        GradeContainsKeywordsPredicate firstPredicateCopy =
                new GradeContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different education levels -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_gradeContainsKeywords_returnsTrue() {
        // One keyword
        GradeContainsKeywordsPredicate predicate =
                new GradeContainsKeywordsPredicate(Collections.singletonList(VALID_GRADE_AMY));
        assertTrue(predicate.test(new TuteeBuilder().withGrade(VALID_GRADE_AMY).build()));

        // Only one matching keyword
        predicate = new GradeContainsKeywordsPredicate(Arrays.asList(VALID_GRADE_AMY, VALID_GRADE_BOB));
        assertTrue(predicate.test(new TuteeBuilder()
                .withGrade(VALID_GRADE_AMY).build()));

        // Mixed-case keywords
        predicate = new GradeContainsKeywordsPredicate(Arrays.asList("a"));
        assertTrue(predicate.test(new TuteeBuilder().withGrade("A").build()));
    }

    @Test
    public void test_gradeDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        GradeContainsKeywordsPredicate predicate =
                new GradeContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new TuteeBuilder().withGrade(VALID_GRADE_AMY).build()));

        // Non-matching keyword
        predicate = new GradeContainsKeywordsPredicate(Arrays.asList(VALID_GRADE_BOB));
        assertFalse(predicate.test(new TuteeBuilder().withGrade(VALID_GRADE_AMY).build()));

        // Keywords match education level, school and subject, but does not match grade
        predicate = new GradeContainsKeywordsPredicate(Arrays.asList("school", "primary", "mathematics"));
        assertFalse(predicate.test(new TuteeBuilder().withGrade(VALID_GRADE_AMY).withSchool("school")
                .withEducationLevel("primary").withSubject("mathematics").build()));

        // Keywords match email and address, but does not match grade
        predicate = new GradeContainsKeywordsPredicate(Arrays
                .asList(VALID_GRADE_BOB, "alice@email.com", "Main", "Street"));
        assertFalse(predicate.test(new TuteeBuilder().withGrade(VALID_GRADE_AMY)
                .withEmail("alice@email.com").withAddress("Main Street").build()));

    }
}
```
###### \java\seedu\address\model\tutee\SchoolContainsKeywordsPredicateTest.java
``` java
public class SchoolContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        SchoolContainsKeywordsPredicate firstPredicate =
                new SchoolContainsKeywordsPredicate(firstPredicateKeywordList);
        SchoolContainsKeywordsPredicate secondPredicate =
                new SchoolContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        SchoolContainsKeywordsPredicate firstPredicateCopy =
                new SchoolContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different schools -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_schoolContainsKeywords_returnsTrue() {
        // One keyword
        SchoolContainsKeywordsPredicate predicate =
                new SchoolContainsKeywordsPredicate(Collections.singletonList("nan"));
        assertTrue(predicate.test(new TuteeBuilder().withSchool("nan hua high school").build()));

        // Only one matching keyword
        predicate = new SchoolContainsKeywordsPredicate(Arrays.asList("nan", "victoria"));
        assertTrue(predicate.test(new TuteeBuilder().withSchool("victoria").build()));

        // Mixed-case keywords
        predicate = new SchoolContainsKeywordsPredicate(Arrays.asList("nan"));
        assertTrue(predicate.test(new TuteeBuilder().withSchool("NAN").build()));
    }

    @Test
    public void test_schoolDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        SchoolContainsKeywordsPredicate predicate =
                new SchoolContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new TuteeBuilder().withSchool(VALID_SCHOOL_AMY).build()));

        // Non-matching keyword
        predicate = new SchoolContainsKeywordsPredicate(Arrays.asList("victoria"));
        assertFalse(predicate.test(new TuteeBuilder().withSchool("nan hua high school").build()));

        // Keywords match education level, grade and subject, but does not match school
        predicate = new SchoolContainsKeywordsPredicate(Arrays.asList("B", "primary", "mathematics"));
        assertFalse(predicate.test(new TuteeBuilder().withSchool(VALID_SCHOOL_AMY).withGrade("B")
                .withEducationLevel("primary").withSubject("mathematics").build()));

        // Keywords match email and address, but does not match school
        predicate = new SchoolContainsKeywordsPredicate(Arrays
                .asList("victoria", "alice@email.com", "Main", "Street"));
        assertFalse(predicate.test(new TuteeBuilder().withSchool("nan hua high school")
                .withEmail("alice@email.com").withAddress("Main Street").build()));

    }
}
```
###### \java\seedu\address\model\tutee\SubjectContainsKeywordsPredicateTest.java
``` java
public class SubjectContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        SubjectContainsKeywordsPredicate firstPredicate =
                new SubjectContainsKeywordsPredicate(firstPredicateKeywordList);
        SubjectContainsKeywordsPredicate secondPredicate =
                new SubjectContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        SubjectContainsKeywordsPredicate firstPredicateCopy =
                new SubjectContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different subjects -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_subjectContainsKeywords_returnsTrue() {
        // One keyword
        SubjectContainsKeywordsPredicate predicate =
                new SubjectContainsKeywordsPredicate(Collections.singletonList(VALID_SUBJECT_AMY));
        assertTrue(predicate.test(new TuteeBuilder().withSubject(VALID_SUBJECT_AMY).build()));

        // Only one matching keyword
        predicate = new SubjectContainsKeywordsPredicate(Arrays.asList(VALID_SUBJECT_AMY, VALID_SUBJECT_BOB));
        assertTrue(predicate.test(new TuteeBuilder().withSubject(VALID_SUBJECT_AMY).build()));

        // Mixed-case keywords
        predicate = new SubjectContainsKeywordsPredicate(Arrays.asList("MatheMAtics"));
        assertTrue(predicate.test(new TuteeBuilder().withSubject("mathematics").build()));
    }

    @Test
    public void test_subjectDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        SubjectContainsKeywordsPredicate predicate =
                new SubjectContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new TuteeBuilder().withSubject(VALID_SUBJECT_AMY).build()));

        // Non-matching keyword
        predicate = new SubjectContainsKeywordsPredicate(Arrays.asList(VALID_SUBJECT_BOB));
        assertFalse(predicate.test(new TuteeBuilder().withSubject(VALID_SUBJECT_AMY).build()));

        // Keywords match education level, grade and school, but does not match subject
        predicate = new SubjectContainsKeywordsPredicate(Arrays.asList("B", "primary", "school"));
        assertFalse(predicate.test(new TuteeBuilder().withSubject(VALID_SUBJECT_AMY).withGrade("B")
                .withEducationLevel("primary").withSchool("school").build()));

        // Keywords match email and address, but does not match subject
        predicate = new SubjectContainsKeywordsPredicate(Arrays
                .asList(VALID_SUBJECT_BOB, "alice@email.com", "Main", "Street"));
        assertFalse(predicate.test(new TuteeBuilder().withSubject(VALID_SUBJECT_AMY)
                .withEmail("alice@email.com").withAddress("Main Street").build()));

    }
}
```
###### \java\seedu\address\storage\XmlAdaptedPersonTest.java
``` java
    //=========== Tutee Related Tests =============================================================

    @Test
    public void toModelType_validTuteeDetails_returnsTutee() throws Exception {
        XmlAdaptedPerson tutee = new XmlAdaptedPerson(ALICETUTEE);
        assertEquals(ALICETUTEE, tutee.toModelType());
    }

    @Test
    public void toModelType_invalidTuteeName_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(INVALID_NAME, VALID_TUTEE_PHONE, VALID_TUTEE_EMAIL, VALID_TUTEE_ADDRESS,
                        VALID_TUTEE_SUBJECT, VALID_TUTEE_GRADE, VALID_TUTEE_EDUCATION_LEVEL,
                        VALID_TUTEE_SCHOOL, VALID_TUTEE_TAGS);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullTuteeName_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(null, VALID_TUTEE_PHONE, VALID_TUTEE_EMAIL, VALID_TUTEE_ADDRESS,
                        VALID_TUTEE_SUBJECT, VALID_TUTEE_GRADE, VALID_TUTEE_EDUCATION_LEVEL,
                        VALID_TUTEE_SCHOOL, VALID_TUTEE_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTuteePhone_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_TUTEE_NAME, INVALID_PHONE, VALID_TUTEE_EMAIL, VALID_TUTEE_ADDRESS,
                        VALID_TUTEE_SUBJECT, VALID_TUTEE_GRADE, VALID_TUTEE_EDUCATION_LEVEL,
                        VALID_TUTEE_SCHOOL, VALID_TUTEE_TAGS);
        String expectedMessage = Phone.MESSAGE_PHONE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullTuteePhone_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_TUTEE_NAME, null, VALID_TUTEE_EMAIL, VALID_TUTEE_ADDRESS,
                        VALID_TUTEE_SUBJECT, VALID_TUTEE_GRADE, VALID_TUTEE_EDUCATION_LEVEL,
                        VALID_TUTEE_SCHOOL, VALID_TUTEE_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTuteeEmail_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_TUTEE_NAME, VALID_TUTEE_PHONE, INVALID_EMAIL, VALID_TUTEE_ADDRESS,
                        VALID_TUTEE_SUBJECT, VALID_TUTEE_GRADE, VALID_TUTEE_EDUCATION_LEVEL,
                        VALID_TUTEE_SCHOOL, VALID_TUTEE_TAGS);
        String expectedMessage = Email.MESSAGE_EMAIL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullTuteeEmail_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_TUTEE_NAME, VALID_TUTEE_PHONE, null, VALID_TUTEE_ADDRESS,
                        VALID_TUTEE_SUBJECT, VALID_TUTEE_GRADE, VALID_TUTEE_EDUCATION_LEVEL,
                        VALID_TUTEE_SCHOOL, VALID_TUTEE_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTuteeAddress_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_TUTEE_NAME, VALID_TUTEE_PHONE, VALID_TUTEE_EMAIL, INVALID_ADDRESS,
                        VALID_TUTEE_SUBJECT, VALID_TUTEE_GRADE, VALID_TUTEE_EDUCATION_LEVEL,
                        VALID_TUTEE_SCHOOL, VALID_TUTEE_TAGS);
        String expectedMessage = Address.MESSAGE_ADDRESS_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullTuteeAddress_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_TUTEE_NAME, VALID_TUTEE_PHONE, VALID_TUTEE_EMAIL, null,
                        VALID_TUTEE_SUBJECT, VALID_TUTEE_GRADE, VALID_TUTEE_EDUCATION_LEVEL,
                        VALID_TUTEE_SCHOOL, VALID_TUTEE_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidSubject_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_TUTEE_NAME, VALID_TUTEE_PHONE, VALID_TUTEE_EMAIL, VALID_TUTEE_ADDRESS,
                        INVALID_SUBJECT, VALID_TUTEE_GRADE, VALID_TUTEE_EDUCATION_LEVEL,
                        VALID_TUTEE_SCHOOL, VALID_TUTEE_TAGS);
        String expectedMessage = Subject.MESSAGE_SUBJECT_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullSubject_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_TUTEE_NAME, VALID_TUTEE_PHONE, VALID_TUTEE_EMAIL, VALID_TUTEE_ADDRESS,
                null, VALID_TUTEE_GRADE, VALID_TUTEE_EDUCATION_LEVEL,
                VALID_TUTEE_SCHOOL, VALID_TUTEE_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Subject.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidGrade_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_TUTEE_NAME, VALID_TUTEE_PHONE, VALID_TUTEE_EMAIL, VALID_TUTEE_ADDRESS,
                        VALID_TUTEE_SUBJECT, INVALID_GRADE, VALID_TUTEE_EDUCATION_LEVEL,
                        VALID_TUTEE_SCHOOL, VALID_TUTEE_TAGS);
        String expectedMessage = Grade.MESSAGE_GRADE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullGrade_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_TUTEE_NAME, VALID_TUTEE_PHONE, VALID_TUTEE_EMAIL, VALID_TUTEE_ADDRESS,
                VALID_TUTEE_SUBJECT, null, VALID_TUTEE_EDUCATION_LEVEL,
                VALID_TUTEE_SCHOOL, VALID_TUTEE_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Grade.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEducationLevel_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_TUTEE_NAME, VALID_TUTEE_PHONE, VALID_TUTEE_EMAIL, VALID_TUTEE_ADDRESS,
                        VALID_TUTEE_SUBJECT, VALID_TUTEE_GRADE, INVALID_EDUCATION_LEVEL,
                        VALID_TUTEE_SCHOOL, VALID_TUTEE_TAGS);
        String expectedMessage = EducationLevel.MESSAGE_EDUCATION_LEVEL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEducationLevel_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_TUTEE_NAME, VALID_TUTEE_PHONE, VALID_TUTEE_EMAIL, VALID_TUTEE_ADDRESS,
                VALID_TUTEE_SUBJECT, VALID_TUTEE_GRADE, null,
                VALID_TUTEE_SCHOOL, VALID_TUTEE_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, EducationLevel.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidSchool_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_TUTEE_NAME, VALID_TUTEE_PHONE, VALID_TUTEE_EMAIL, VALID_TUTEE_ADDRESS,
                        VALID_TUTEE_SUBJECT, VALID_TUTEE_GRADE, VALID_TUTEE_EDUCATION_LEVEL,
                        INVALID_SCHOOL, VALID_TUTEE_TAGS);
        String expectedMessage = School.MESSAGE_SCHOOL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullSchool_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_TUTEE_NAME, VALID_TUTEE_PHONE, VALID_TUTEE_EMAIL, VALID_TUTEE_ADDRESS,
                VALID_TUTEE_SUBJECT, VALID_TUTEE_GRADE, VALID_TUTEE_EDUCATION_LEVEL,
                null, VALID_TUTEE_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, School.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_tuteeHasInvalidTags_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_TUTEE_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_TAG));
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_TUTEE_NAME, VALID_TUTEE_PHONE, VALID_TUTEE_EMAIL, VALID_TUTEE_ADDRESS,
                VALID_TUTEE_SUBJECT, VALID_TUTEE_GRADE, VALID_TUTEE_EDUCATION_LEVEL,
                VALID_TUTEE_SCHOOL, invalidTags);
        Assert.assertThrows(IllegalValueException.class, person::toModelType);
    }
}
```
###### \java\systemtests\AddressBookSystemTest.java
``` java
    /**
     * Adds a tutee into the current model and expected model.
     */
    protected void addTutee(String command, Tutee tutee, Model expectedModel) {
        try {
            expectedModel.addPerson(tutee);
        } catch (DuplicatePersonException dpe) {
            System.out.println("a tutee with the same name exists in the expected model");
        }
        executeCommand(command);
    }
```
###### \java\systemtests\FindPersonCommandSystemTest.java
``` java
        /* Adding some tutees into the filtered person list to test whether Find Person command can find tutees */
        // adds AMYTUTEE
        expectedModel = getModel();
        command = AddTuteeCommand.COMMAND_WORD + "  " + NAME_DESC_AMY + "  " + PHONE_DESC_AMY + " "
                + EMAIL_DESC_AMY + "   " + ADDRESS_DESC_AMY + "   " + SUBJECT_DESC_AMY + " " + GRADE_DESC_AMY + " "
                + EDUCATION_LEVEL_DESC_AMY + " " + SCHOOL_DESC_AMY + " " + TAG_DESC_FRIEND + " ";
        addTutee(command, AMYTUTEE, expectedModel);

        //adds Bob whose subject and school are same as Amy's
        Tutee modifiedBobTutee = new TuteeBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withSubject(VALID_SUBJECT_AMY)
                .withGrade(VALID_GRADE_BOB).withEducationLevel(VALID_EDUCATION_LEVEL_BOB).withSchool(VALID_SCHOOL_AMY)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        command = AddTuteeCommand.COMMAND_WORD + "  " + NAME_DESC_BOB + "  " + PHONE_DESC_BOB + " "
                + EMAIL_DESC_BOB + "   " + ADDRESS_DESC_BOB + "   " + SUBJECT_DESC_AMY + " " + GRADE_DESC_BOB + " "
                + EDUCATION_LEVEL_DESC_BOB + " " + SCHOOL_DESC_AMY + " " + TAG_DESC_HUSBAND + " " + TAG_DESC_FRIEND;
        addTutee(command, modifiedBobTutee, expectedModel);

        /* Case: find education level of a tutee in address book -> 1 person found */
        ModelHelper.setFilteredList(expectedModel, AMYTUTEE);
        command = FindPersonCommand.COMMAND_WORD + " " + CATEGORY_EDUCATION_LEVEL + " "
                + AMYTUTEE.getEducationLevel().toString();
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find grade of a tutee using command alias in address book -> 1 person found */
        command = FindPersonCommand.COMMAND_ALIAS + " " + CATEGORY_GRADE + " "
                + AMYTUTEE.getGrade().toString();
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find school of a tutee in address book -> 2 persons found */
        ModelHelper.setFilteredList(expectedModel, AMYTUTEE, modifiedBobTutee);
        command = FindPersonCommand.COMMAND_WORD + " " + CATEGORY_SCHOOL + " "
                + AMYTUTEE.getSchool().toString();
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find subject of a tutee in address book -> 2 persons found */
        command = FindPersonCommand.COMMAND_WORD + " " + CATEGORY_SUBJECT + " "
                + AMYTUTEE.getSubject().toString();
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();
```
###### \java\systemtests\ModelHelper.java
``` java
    /**
     * Updates {@code model}'s sorted list to display persons based on specified category.
     */
    public static void setSortedList(Model model, String category) {
        Comparator<Person> comparator = new PersonSortUtil().getComparator(category);
        model.sortFilteredPersonList(comparator);
    }
```
