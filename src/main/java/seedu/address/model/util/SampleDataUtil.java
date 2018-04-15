package seedu.address.model.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.Task;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.personal.PersonalTask;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.exceptions.TimingClashException;
import seedu.address.model.tutee.EducationLevel;
import seedu.address.model.tutee.Grade;
import seedu.address.model.tutee.School;
import seedu.address.model.tutee.Subject;
import seedu.address.model.tutee.TuitionTask;
import seedu.address.model.tutee.Tutee;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static final String DATETIME1 = "16/04/2018 15:15";
    public static final String DATETIME2 = "19/04/2018 09:25";
    public static final String DATETIME3 = "07/06/2018 16:45";
    public static final String DATETIME4 = "03/06/2019 12:10";
    public static final String DATETIME5 = "05/07/2020 18:45";
    public static final String DATETIME6 = "15/07/2018 06:55";
    public static final String DATETIME7 = "20/10/2018 11:11";
    public static final String DATETIME8 = "16/12/2018 08:18";
    public static final String DATETIME9 = "23/12/2018 10:28";
    public static final String DATETIME10 = "13/01/2018 11:30";
    public static final String DATETIME11 = "29/01/2018 12:30";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);

    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                getTagSet("friends")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                getTagSet("colleagues", "friends")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                getTagSet("neighbours")),
            new Tutee(new Name("Anas Shakra"), new Phone("514552256"), new Email("shakra.a@hotmail.com"),
                new Address("590 Souart"), new Subject("Chemistry"), new Grade("A"),
                new EducationLevel("secondary"), new School("NUS"), getTagSet("family")),
            new Tutee(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.co"),
                new Address("Blk 47 Tampines Street 20"), new Subject("Chemistry"), new Grade("C"),
                new EducationLevel("secondary"), new School("NUS"), getTagSet("family")),
            new Tutee(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street"), new Subject("Physics"), new Grade("A"),
                new EducationLevel("secondary"), new School("NUS"), getTagSet("family")),
            new Tutee(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street"), new Subject("Math"), new Grade("C"),
                new EducationLevel("primary"), new School("NUS"), getTagSet("friend")),
        };
    }
    public static Task[] getSampleTasks() {
        return new Task[]{
            new PersonalTask(LocalDateTime.parse(DATETIME1, formatter), "2h15m", "exampleTask1"),
            new TuitionTask("Anas Shakra", LocalDateTime.parse(DATETIME2, formatter), "5h25m", "exampleTask2"),
            new PersonalTask(LocalDateTime.parse(DATETIME3, formatter), "3h45m", "exampleTask3"),
            new PersonalTask(LocalDateTime.parse(DATETIME4, formatter), "12h10m", "exampleTask4"),
            new PersonalTask(LocalDateTime.parse(DATETIME5, formatter), "02h45m", "exampleTask5"),
            new PersonalTask(LocalDateTime.parse(DATETIME6, formatter), "06h55m", "exampleTask6"),
            new PersonalTask(LocalDateTime.parse(DATETIME7, formatter), "03h11m", "exampleTask7"),
            new PersonalTask(LocalDateTime.parse(DATETIME8, formatter), "08h18m", "exampleTask8"),
            new TuitionTask("Roy Balakrishnan", LocalDateTime.parse(DATETIME9, formatter), "10h28m",
                    "exampleTask9"),
            new TuitionTask("Irfan Ibrahim", LocalDateTime.parse(DATETIME10, formatter), "11h30m",
                    "exampleTask10"),
            new TuitionTask("David Li", LocalDateTime.parse(DATETIME11, formatter), "12h30m",
                    "exampleTask11"),
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        try {
            AddressBook sampleAb = new AddressBook();
            for (Person samplePerson : getSamplePersons()) {
                sampleAb.addPerson(samplePerson);
            }
            for (Task sampleTask : getSampleTasks()) {
                sampleAb.addTask(sampleTask);
            }
            return sampleAb;
        } catch (DuplicatePersonException e) {
            throw new AssertionError("sample data cannot contain duplicate persons", e);
        } catch (TimingClashException tce) {
            throw new AssertionError("sample data cannot contain duplicate Tasks, tce");
        }
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

}
