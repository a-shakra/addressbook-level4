# ashakra
###### \java\seedu\address\logic\commands\ListTaskCommand.java
``` java

public class ListTaskCommand extends Command {

    public static final String COMMAND_WORD = "listtask";
    public static final String COMMAND_ALIAS = "lt";

    public static final String MESSAGE_SUCCESS = "Listed all tasks";


    @Override
    public CommandResult execute() {
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
```
###### \java\seedu\address\logic\LogicManager.java
``` java
    @Override
    public ObservableList<Task> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }
```
###### \java\seedu\address\model\AddressBook.java
``` java
    public void setTasks(List<Task> tasks) throws TimingClashException {
        this.tasks.setTasks(tasks);
    }
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Adds a task to the address book.
     *
     */
    public void addTask(Task t) throws TimingClashException {
        tasks.add(t);
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Removes a task from the address book.
     *
     */
    public boolean removeTask(Task key) throws TaskNotFoundException {
        if (tasks.remove(key))        {
            return true;
        } else {
            System.out.println("Didn't work");
            return false;
        }
    }
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void addTask(Task aTask) throws TimingClashException {
        addressBook.addTask(aTask);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS); //Change to new predicate?
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void deleteTask(Task target) throws TaskNotFoundException {
        addressBook.removeTask(target);
        indicateAddressBookChanged();
    }

    @Override
    public ObservableList<Task> getFilteredTaskList() {
        return FXCollections.unmodifiableObservableList(sortedTasks);
    }

    @Override
    public void updateFilteredTaskList(Predicate<Task> predicate) {
        requireNonNull(predicate);
        filteredTasks.setPredicate(predicate);
    }
```
###### \java\seedu\address\model\ReadOnlyAddressBook.java
``` java
    /**
     * Returns an unmodifiable view of the tasks list
     * This list will not contain any duplicate task
     */
    ObservableList<Task> getTaskList();
```
###### \java\seedu\address\model\TaskContainsKeywordsPredicate.java
``` java
public class TaskContainsKeywordsPredicate implements Predicate<Task> {
    private final List<String> keywords;

    public TaskContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Task task) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(task.getDescription(), keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TaskContainsKeywordsPredicate) other).keywords)); // state check
    }

}
```
###### \java\seedu\address\model\UniqueTaskList.java
``` java
public class UniqueTaskList implements Iterable<Task> {

    private static final String HOUR_DELIMITER = "h";
    private static final String MINUTE_DELIMITER = "m";
    private final ObservableList<Task> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty TaskList.
     */
    public UniqueTaskList() {}

```
###### \java\seedu\address\model\UniqueTaskList.java
``` java
    /**
     * Replaces the person {@code target} in the list with {@code editedPerson}.
     */
    public void setTask(Task target, Task editedTask) throws TaskNotFoundException {
        requireNonNull(editedTask);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new TaskNotFoundException();
        }
        internalList.set(index, editedTask);
    }


    /**
     * Removes the equivalent task from the list.
     */
    public boolean remove(Task toRemove) throws TaskNotFoundException {
        requireNonNull(toRemove);
        final boolean taskFoundAndDeleted = internalList.remove(toRemove);
        if (!taskFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndDeleted;
    }

    public void setTasks(UniqueTaskList replacement) {
        this.internalList.setAll(replacement.internalList);
    }


    public void setTasks(List<Task> tasks) throws TimingClashException {
        requireAllNonNull(tasks);
        final UniqueTaskList replacement = new UniqueTaskList();
        for (final Task task : tasks) {
            replacement.add(task);
        }
        setTasks(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Task> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

```
###### \java\seedu\address\model\UniqueTaskList.java
``` java
    @Override
    public Iterator<Task> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTaskList // instanceof handles nulls
                && this.internalList.equals(((UniqueTaskList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
```
###### \java\seedu\address\storage\XmlAdaptedTask.java
``` java
public class XmlAdaptedTask {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Task's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String description;
    @XmlElement(required = true)
    private String duration;
    @XmlElement(required = true)
    private String dateAndTime;

    /**
     * Constructs an XmlAdaptedTask.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedTask() {}

    /**
     * Constructs an {@code XmlAdaptedTask} with given personal task details.
     */
    public XmlAdaptedTask(String description, String duration, String dateAndTime) {
        //this.name = "null";
        this.description = description;
        this.duration = duration;
        this.dateAndTime = dateAndTime;
    }

    public XmlAdaptedTask(String name, String description, String duration, String dateAndTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.dateAndTime = dateAndTime;
    }

    /**
     * Converts a given Task into this class for JAXB use.
     *
     */
    public XmlAdaptedTask(Task source) {
        description = source.getDescription();
        duration = source.getDuration();
        dateAndTime = source.getTaskDateTime().toString();
        if (source instanceof TuitionTask) {
            name = ((TuitionTask) source).getPerson();
        }
    }

    /**
     * Converts this jaxb-friendly adapted task object into the model's Task object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     * Because of the way Task was designed (As an interface), i'm forced to just input this as a PersonalTask
     * until a better solution can be found
     */

    public Task toModelType() throws IllegalValueException {
        LocalDateTime taskDateTime = LocalDateTime.parse(dateAndTime);
        if (this.description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Task.MESSAGE_DESCRIPTION_CONSTRAINTS));
        }
        if (this.duration == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Task.MESSAGE_DURATION_CONSTRAINTS));
        }
        if (this.dateAndTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Task.MESSAGE_DATETIME_CONSTRAINTS));
        }
        if (this.name == null) {
            return new PersonalTask(taskDateTime, duration, description);
        } else {
            return new TuitionTask(name, taskDateTime, duration, description);
        }
    }

    /**
     * Returns true if the two tasks are equal. Needs to be updated to reflect the name parameter
     */
    public boolean equals(Object other) {

        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedTask)) {
            return false;
        }

        XmlAdaptedTask otherTask = (XmlAdaptedTask) other;
        return Objects.equals(description, otherTask.description)
                && Objects.equals(duration, otherTask.duration)
                && Objects.equals(dateAndTime, otherTask.dateAndTime)
                && Objects.equals(name, otherTask.name);
    }
}
```
###### \java\seedu\address\storage\XmlSerializableAddressBook.java
``` java
@XmlRootElement(name = "addressbook")
public class XmlSerializableAddressBook {

    @XmlElement
    private List<XmlAdaptedPerson> persons;
    @XmlElement
    private List<XmlAdaptedTag> tags;
    @XmlElement
    private List<XmlAdaptedTask> tasks;

    /**
     * Creates an empty XmlSerializableAddressBook.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableAddressBook() {
        persons = new ArrayList<>();
        tags = new ArrayList<>();
        tasks = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableAddressBook(ReadOnlyAddressBook src) {
        this();
        persons.addAll(src.getPersonList().stream().map(XmlAdaptedPerson::new).collect(Collectors.toList()));
        tags.addAll(src.getTagList().stream().map(XmlAdaptedTag::new).collect(Collectors.toList()));
        tasks.addAll(src.getTaskList().stream().map(XmlAdaptedTask::new).collect(Collectors.toList()));
    }

    /**
     * Converts this addressbook into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedPerson} or {@code XmlAdaptedTag}.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (XmlAdaptedTag t : tags) {
            addressBook.addTag(t.toModelType());
        }
        for (XmlAdaptedPerson p : persons) {
            addressBook.addPerson(p.toModelType());
        }
        for (XmlAdaptedTask t: tasks) {
            addressBook.addTask(t.toModelType());
        }
        return addressBook;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableAddressBook)) {
            return false;
        }

        XmlSerializableAddressBook otherAb = (XmlSerializableAddressBook) other;
        // The tasks condition might be a problem because of the design of tasks
        return persons.equals(otherAb.persons) && tags.equals(otherAb.tags) && tasks.equals(otherAb.tasks);
    }
}
```
###### \java\seedu\address\ui\TaskCard.java
``` java
public class TaskCard extends UiPart<Region> {
    private static final String FXML = "TaskListCard.fxml";
    public final Task task;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label taskDateAndTime;
    @FXML
    private Label duration;
    @FXML
    private Label description;

    public TaskCard(Task task, int displayedIndex) {
        super(FXML);
        this.task = task;
        id.setText(displayedIndex + ". ");
        if (task instanceof TuitionTask) {
            description.setText(((TuitionTask) task).getTuitionTitle());
        } else {
            description.setText(task.getDescription());
        }
        duration.setText(task.getDuration());
        taskDateAndTime.setText(task.getTaskDateTime().format(formatter));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TaskCard)) {
            return false;
        }

        // state check
        TaskCard card = (TaskCard) other;
        return id.getText().equals(card.id.getText())
                && task.equals(card.task);
    }

}

```
###### \java\seedu\address\ui\TaskCardListPanel.java
``` java
public class TaskCardListPanel extends UiPart<Region> {
    private static final String FXML = "TaskCardListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TaskCardListPanel.class);

    @FXML
    private ListView<TaskCard> taskListView;

    public TaskCardListPanel(ObservableList<Task> taskList) {
        super(FXML);
        setConnections(taskList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Task> taskList) {
        ObservableList<TaskCard> mappedList = EasyBind.map(
                taskList, (task) -> new TaskCard(task, taskList.indexOf(task) + 1));
        taskListView.setItems(mappedList);
        taskListView.setCellFactory(listView -> new TaskListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }


    private void setEventHandlerForSelectionChangeEvent() {
        taskListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in Task list panel changed to : '" + newValue + "'");
                        raise(new TaskPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code PersonCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            taskListView.scrollTo(index);
            taskListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code PersonCard}.
     */
    class TaskListViewCell extends ListCell<TaskCard> {

        @Override
        protected void updateItem(TaskCard task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(task.getRoot());
            }
        }
    }

}
```
###### \resources\view\TaskCardListPanel.fxml
``` fxml
<VBox xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <ListView fx:id="taskListView" VBox.vgrow="ALWAYS" />
</VBox>
```
###### \resources\view\TaskListCard.fxml
``` fxml
<HBox id="cardPane" fx:id="cardPane" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <GridPane HBox.hgrow="ALWAYS">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10" prefWidth="150" />
        </columnConstraints>
        <VBox alignment="CENTER_LEFT" minHeight="105" GridPane.columnIndex="0">
            <padding>
                <Insets top="5" right="5" bottom="5" left="15" />
            </padding>
            <HBox spacing="5" alignment="CENTER_LEFT">
                <Label fx:id="id" styleClass="cell_big_label">
                    <minWidth>
                        <!-- Ensures that the label text is never truncated -->
                        <Region fx:constant="USE_PREF_SIZE" />
                    </minWidth>
                </Label>
                <Label fx:id="description" text="\$description" styleClass="cell_big_label" />
            </HBox>
            <Label fx:id="duration" styleClass="cell_small_label" text="\$duration" />
            <Label fx:id="taskDateAndTime" styleClass="cell_small_label" text="\$taskDateAndTime" />
        </VBox>
    </GridPane>
</HBox>
```
