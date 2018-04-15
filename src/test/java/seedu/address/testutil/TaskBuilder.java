package seedu.address.testutil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import seedu.address.model.personal.PersonalTask;
import seedu.address.model.tutee.TuitionTask;

/**
 * A utility class to help with building PersonalTask and TuitionTask objects
 */
//@@author a-shakra
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
