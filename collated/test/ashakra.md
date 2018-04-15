# ashakra
###### \data\XmlAddressBookStorageTest\invalidAndValidTaskAddressBook.xml
``` xml
<addressbook>
    <!-- Valid Task -->
    <tasks>
        <description>exampleTask1</description>
        <duration>3h20m</duration>
        <dateandtime>05/02/2018 3:20</dateandtime>
    </tasks>
    <!-- Task with invalid duration field -->
    <tasks>
        <description>exampleTask1</description>
        <duration>3h2m</duration>
        <dateAndTime>05/03/2018 3:20</dateAndTime>
    </tasks>
</addressbook>
```
###### \data\XmlAddressBookStorageTest\invalidTaskAddressBook.xml
``` xml
<addressbook>
    <tasks>
        <description>exampleTask1</description>
        <duration>3333333:::</duration>
        <dateAndTime>2018-05-02T03:20</dateAndTime>
    </tasks>
</addressbook>
```
###### \data\XmlSerializableAddressBookTest\invalidTaskAddressBook.xml
``` xml
<addressbook>
    <!-- Task with an invalid duration field -->
    <tasks>
        <description>exampleDescription</description>
        <duration>3h2m</duration>
        <dateandtime>02/05/2018 03:20</dateandtime>
    </tasks>
</addressbook>
```
###### \data\XmlSerializableAddressBookTest\typicalTasksAddressBook.xml
``` xml
<addressbook>
    <tasks>
        <description>exampleTask1</description>
        <duration>3h20m</duration>
        <dateAndTime>02/05/2018T03:20</dateAndTime>
    </tasks>
    <tasks>
        <description>exampleTask2</description>
        <duration>3h20m</duration>
        <dateAndTime>02/04/2018T13:20</dateAndTime>
    </tasks>
    <tasks>
        <description>exampleTask3</description>
        <duration>3h20m</duration>
        <dateAndTime>02/06/2018T23:20</dateAndTime>
    </tasks>
    <tasks>
        <description>exampleTask4</description>
        <duration>3h20m</duration>
        <dateAndTime>02/07/2018T23:20</dateAndTime>
    </tasks>
</addressbook>
```
###### \data\XmlUtilTest\invalidTaskField.xml
``` xml
<tasks>
    <description>exampleTask1</description>
    <duration>3h20m</duration>
    <dateAndTime>2018-00-00T03:20</dateAndTime>
</tasks>
```
###### \data\XmlUtilTest\missingTaskField.xml
``` xml
<tasks>
    <description>exampleTask1</description>
    <duration>3:20</duration>
</tasks>
```
###### \data\XmlUtilTest\tempAddressBook.xml
``` xml
<addressbook>
    <persons>
        <id>1</id>
        <firstName>John</firstName>
        <lastName>Doe</lastName>
        <githubUsername></githubUsername>
        <street></street>
        <postalCode></postalCode>
        <city></city>
    </persons>
    <tags>
        <name>Friends</name>
    </tags>
    <tasks>
    </tasks>
</addressbook>
```
###### \data\XmlUtilTest\validAddressBook.xml
``` xml
    <tasks>
        <description>exampleTask1</description>
        <duration>3h20m</duration>
        <dateAndTime>2018-05-02T03:20</dateAndTime>
    </tasks>
    <tasks>
        <description>exampleTask2</description>
        <duration>3h20m</duration>
        <dateAndTime>2018-06-02T03:20</dateAndTime>
    </tasks>
    <tasks>
        <description>exampleTask3</description>
        <duration>3h20m</duration>
        <dateAndTime>2018-07-02T03:20</dateAndTime>
    </tasks>
</addressbook>
```
###### \data\XmlUtilTest\validTask.xml
``` xml
<tasks>
    <description>exampleTask1</description>
    <duration>3h20m</duration>
    <dateAndTime>02/03/2018T03:20</dateAndTime>
</tasks>
```
###### \java\seedu\address\commons\util\XmlUtilTest.java
``` java
public class XmlUtilTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/XmlUtilTest/");
    private static final File EMPTY_FILE = new File(TEST_DATA_FOLDER + "empty.xml");
    private static final File MISSING_FILE = new File(TEST_DATA_FOLDER + "missing.xml");
    private static final File VALID_FILE = new File(TEST_DATA_FOLDER + "validAddressBook.xml");
    private static final File MISSING_PERSON_FIELD_FILE = new File(TEST_DATA_FOLDER + "missingPersonField.xml");
    private static final File INVALID_PERSON_FIELD_FILE = new File(TEST_DATA_FOLDER + "invalidPersonField.xml");
    private static final File VALID_PERSON_FILE = new File(TEST_DATA_FOLDER + "validPerson.xml");
    private static final File MISSING_TASK_FIELD_FILE = new File(TEST_DATA_FOLDER + "missingTaskField.xml");
    private static final File INVALID_TASK_FIELD_FILE = new File(TEST_DATA_FOLDER + "invalidTaskField.xml");
    private static final File VALID_TASK_FILE = new File(TEST_DATA_FOLDER + "validTask.xml");
    private static final File TEMP_FILE = new File(TestUtil.getFilePathInSandboxFolder("tempAddressBook.xml"));

    private static final String VALID_DURATION = "3h20m";
    private static final String VALID_DESCRIPTION = "exampleTask1";
    private static final String VALID_DATEANDTIME = "02/03/2018T03:20";
    private static final String INVALID_DATEANDTIME = "2018-00-00T03:20";
```
###### \java\seedu\address\logic\commands\ListTaskCommandTest.java
``` java
public class ListTaskCommandTest {

    private Model model;
    private Model expectedModel;
    private ListTaskCommand listTaskCommand;

    @Before
    public void setUp() {

        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        listTaskCommand = new ListTaskCommand();
        listTaskCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(listTaskCommand, model, ListTaskCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showTaskAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(listTaskCommand, model, ListTaskCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
```
###### \java\seedu\address\model\AddressBookTest.java
``` java
    @Test
    public void resetData_withDuplicateTasks_throwsAssertionError() {
        // Repeat EXAMPLE1 twice
        List<Person> newPersons = Arrays.asList(ALICE);
        List<Tag> newTags = new ArrayList<>(ALICE.getTags());
        List<Task> newTasks = Arrays.asList(EXAMPLE1, EXAMPLE1);
        AddressBookStub newData = new AddressBookStub(newPersons, newTags, newTasks);

        thrown.expect(AssertionError.class);
        addressBook.resetData(newData);
    }
```
###### \java\seedu\address\model\AddressBookTest.java
``` java
    @Test
    public void getTaskList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getTaskList().remove(0);
    }
```
###### \java\seedu\address\model\TaskContainsKeywordsPredicateTest.java
``` java
public class TaskContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        TaskContainsKeywordsPredicate firstPredicate = new TaskContainsKeywordsPredicate(firstPredicateKeywordList);
        TaskContainsKeywordsPredicate secondPredicate = new TaskContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TaskContainsKeywordsPredicate firstPredicateCopy = new TaskContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));


        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_taskContainsKeywords_returnsTrue() {
        // One keyword
        TaskContainsKeywordsPredicate predicate = new TaskContainsKeywordsPredicate(Collections
                .singletonList("exampleTask1"));
        assertTrue(predicate.test(new TaskBuilder().withDescription("exampleTask1").buildPersonalTask()));

        // Multiple keywords
        predicate = new TaskContainsKeywordsPredicate(Arrays.asList("exampleTask1", "exampleTask2"));
        assertTrue(predicate.test(new TaskBuilder().withDescription("exampleTask1 exampleTask2").buildPersonalTask()));

        // Only one matching keyword
        predicate = new TaskContainsKeywordsPredicate(Arrays.asList("exampleTask2", "exampleTask3"));
        assertTrue(predicate.test(new TaskBuilder().withDescription("exampleTask1 exampleTask3").buildPersonalTask()));

        // Mixed-case keywords
        predicate = new TaskContainsKeywordsPredicate(Arrays.asList("eXampleTask1", "ExampleTask2"));
        assertTrue(predicate.test(new TaskBuilder().withDescription("exampleTask1 exampleTask2").buildPersonalTask()));
    }
}


```
###### \java\seedu\address\storage\XmlAdaptedTaskTest.java
``` java
public class XmlAdaptedTaskTest {
    private static final String VALID_DESCRIPTION = "A description";

    private static final String VALID_DURATION = EXAMPLE1.getDuration();
    private static final String VALID_DATEANDTIME = EXAMPLE1.getTaskDateTime().toString(); //Double check this

    @Test
    public void toModelType_validTaskDetails_returnsTask() throws Exception {
        XmlAdaptedTask task = new XmlAdaptedTask(EXAMPLE1);
        assertEquals(EXAMPLE1, task.toModelType());
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        XmlAdaptedTask task = new XmlAdaptedTask(null, VALID_DURATION, VALID_DATEANDTIME);
        String expectedMessage = "Task's Tasks Should have a non-empty description field is missing!";
        Assert.assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_nullDuration_throwsIllegalValueException() {
        XmlAdaptedTask task = new XmlAdaptedTask(VALID_DESCRIPTION, null, VALID_DATEANDTIME);
        String expectedMessage = "Task's Duration must be a non-null value field is missing!";
        Assert.assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_nullDateAndTime_throwsNullPointerException() {
        XmlAdaptedTask task = new XmlAdaptedTask(VALID_DESCRIPTION, VALID_DURATION, null);
        String expectedMessage = "text";
        Assert.assertThrows(NullPointerException.class, expectedMessage, task::toModelType);
    }

}
```
###### \java\seedu\address\storage\XmlAddressBookStorageTest.java
``` java
    @Test
    public void readAndSaveAddressBook_allInOrder_taskSuccess() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempAddressBook.xml";
        AddressBook original = TypicalTasks.getTypicalAddressBook();
        XmlAddressBookStorage xmlAddressBookStorage = new XmlAddressBookStorage(filePath);

        //Save in new file and read back
        xmlAddressBookStorage.saveAddressBook(original, filePath);
        ReadOnlyAddressBook readBack = xmlAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        //Modify data, overwrite exiting file, and read back
        original.removeTask(EXAMPLE2);
        original.addTask(EXAMPLE2);
        xmlAddressBookStorage.saveAddressBook(original, filePath);
        readBack = xmlAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        //Save and read without specifying file path
        original.addTask(EXAMPLE4);
        xmlAddressBookStorage.saveAddressBook(original); //file path not specified
        readBack = xmlAddressBookStorage.readAddressBook().get(); //file path not specified
        assertEquals(original, new AddressBook(readBack));

    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveAddressBook(null, "SomeFile.xml");
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveAddressBook(ReadOnlyAddressBook addressBook, String filePath) {
        try {
            new XmlAddressBookStorage(filePath).saveAddressBook(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAddressBook_nullFilePath_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        saveAddressBook(new AddressBook(), null);
    }

}
```
###### \java\seedu\address\storage\XmlSerializableAddressBookTest.java
``` java
    @Test
    public void toModelType_invalidTaskFile_throwsNullValueException() throws Exception {
        XmlSerializableAddressBook dataFromFile = XmlUtil.getDataFromFile(INVALID_TASK_FILE,
                XmlSerializableAddressBook.class);
        thrown.expect(NullPointerException.class);
        dataFromFile.toModelType();
    }
    @Test
    public void toModelType_invalidTagFile_throwsIllegalValueException() throws Exception {
        XmlSerializableAddressBook dataFromFile = XmlUtil.getDataFromFile(INVALID_TAG_FILE,
                XmlSerializableAddressBook.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }
}
```
###### \java\seedu\address\testutil\TaskBuilder.java
``` java
public class TaskBuilder {

    public static final String DEFAULT_TUTEE_NAME = "Alice Pauline";
    public static final String DEFAULT_DATE = "12/12/2016";
    public static final String DEFAULT_TIME = "12:00";
    public static final String DEFAULT_DATE_TIME = DEFAULT_DATE + " " + DEFAULT_TIME;
    public static final String DEFAULT_DURATION = "1h30m";
    public static final String DEFAULT_DESCRIPTION = "Alice's homework";

    private static final String EMPTY_STRING = "";


    protected String name;
    protected String description;
    protected String duration;
    protected LocalDateTime dateAndTime;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);

    public TaskBuilder() {
        name = DEFAULT_TUTEE_NAME;
        description = DEFAULT_DESCRIPTION;
        duration = DEFAULT_DURATION;
        dateAndTime = LocalDateTime.parse(DEFAULT_DATE_TIME, formatter);
    }

    /**
     * Initializes the TaskBuilder with the data of a given {@code Tuition Task}.
     */
    public TaskBuilder(TuitionTask taskToCopy) {
        name = taskToCopy.getPerson();
        description = taskToCopy.getDescription();
        duration = taskToCopy.getDuration();
        dateAndTime = taskToCopy.getTaskDateTime();
    }

    /**
     * Sets the {@code name} of the {@code Task} that we are building.
     */
    public TaskBuilder withTuteeName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the {@code description} of the {@code Task} that we are building.
     */
    public TaskBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the {@code description} of the {@code Task} that we are building to be empty.
     */
    public TaskBuilder withoutDescription() {
        this.description = EMPTY_STRING;
        return this;
    }

    /**
     * Sets the {@code duration} of the {@code Task} that we are building.
     */
    public TaskBuilder withDuration(String duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Sets the {@code DateAndTime} of the {@code Task} that we are building.
     */

    public TaskBuilder withDateTime(String dateAndTime) {

        this.dateAndTime = LocalDateTime.parse(dateAndTime, formatter);
        return this;
    }
    /**
     * Ideally, this return variable should be made to a Task class or this function should return
     * a tuition task as well
     */

    public PersonalTask buildPersonalTask() {
        return new PersonalTask(dateAndTime, duration, description);
    }
    public TuitionTask buildTuitionTask() {
        return new TuitionTask(name, dateAndTime, duration, description);
    }
}
```
###### \java\seedu\address\testutil\TaskUtil.java
``` java
public class TaskUtil {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Returns an add personal task command string for adding the {@code task}.
     */
    public static String getAddPersonalTaskCommand(Task task) {
        return AddPersonalTaskCommand.COMMAND_WORD + " " + getPersonalTaskDetails(task);
    }

    /**
     * Returns the part of command string for the given {@code task}'s details.
     */
    public static String getPersonalTaskDetails(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getStringTaskDateTime() + " ");
        sb.append(task.getDuration() + " ");
        sb.append(task.getDescription() + " ");
        return sb.toString();
    }

}
```
###### \java\seedu\address\testutil\TypicalTasks.java
``` java
public class TypicalTasks {

    public static final Task EXAMPLE1 = new TaskBuilder().withTuteeName(null).withDescription("exampleTask1")
            .withDuration("3h20m").withDateTime("02/05/2018 03:20").buildPersonalTask();
    public static final Task EXAMPLE2 = new TaskBuilder().withTuteeName(null).withDescription("exampleTask2")
            .withDuration("3h20m").withDateTime("02/04/2018 13:20").buildPersonalTask();
    public static final Task EXAMPLE3 = new TaskBuilder().withTuteeName(null).withDescription("exampleTask3")
            .withDuration("3h20m").withDateTime("02/06/2018 23:20").buildPersonalTask();
    public static final Task EXAMPLE4 = new TaskBuilder().withTuteeName(null).withDescription("exampleTask4")
            .withDuration("3h20m").withDateTime("02/07/2018 23:20").buildPersonalTask();

    private TypicalTasks() {} // prevents instantiation
    /**
     * Returns an {@code AddressBook} with all the typical tasks.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Task task : getTypicalTasks()) {
            try {
                ab.addTask(task);
            } catch (TimingClashException e) {
                throw new AssertionError("Timing Clash");
            }
        }
        return ab;
    }

    public static List<Task> getTypicalTasks() {
        return new ArrayList<>(Arrays.asList(EXAMPLE1, EXAMPLE2, EXAMPLE3));
    }

}
```
