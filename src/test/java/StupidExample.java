import com.onlyleft.sqleight.ObjectBuilder;
import com.onlyleft.sqleight.NullableReader;
import com.onlyleft.sqleight.FieldExtractor;
import com.onlyleft.sqleight.Extractor;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import static com.onlyleft.sqleight.SqlEight.queryForList;
import static com.onlyleft.sqleight.SqlEight.queryForOptional;
import static com.onlyleft.sqleight.FieldExtractor.extract;

public class StupidExample {

    private DataSource dataSource = null;

    private Extractor<Person> buildPerson = (resultSet) -> {
        final Person p = new Person();
        p.setName(resultSet.getString("name"));
        p.setAge(resultSet.getInt("age"));
        return p;
    };

    private Extractor<Person> buildPerson1 = (resultSet) -> {
        final Person person = new Person();
        person.setName(resultSet.getString("name"));
        int age = resultSet.getInt("age");
        if (age != 0 && !resultSet.wasNull()) {
            person.setAge(age);
        }
        return person;
    };

    private Extractor<Person> buildPerson2 = (resultSet) -> {
        final Person person = new Person();
        person.setName(resultSet.getString("name"));
        person.setAge(NullableReader.getInteger(resultSet, "age"));
        return person;
    };

    private Extractor<Person> buildPerson3 = (resultSet) -> {
        final Person person = new Person();
        extract(resultSet, "age", person, NullableReader::getInteger, Person::setAge);
//        extract(resultSet, "age", person, ResultSet::getInt, Person::setAge);
        extract(resultSet, "name", person, ResultSet::getString, Person::setName);
        extract(resultSet, 1, person, ResultSet::getString, Person::setName);
        return person;
    };

    private Extractor<Person> buildPerson4 = (resultSet) -> {
        final Person person = new Person();
        new FieldExtractor<>(resultSet, person)
                .extract("name", ResultSet::getString, Person::setName)
                .extract("age", NullableReader::getInteger, Person::setAge);
        return person;
    };

    private Extractor<Person> buildPerson5 = (resultSet) -> new ObjectBuilder<>(resultSet, Person.class)
            .extract("name", ResultSet::getString, Person::setName)
            .extract("age", NullableReader::getInteger, Person::setAge)
            .getResult();

    public List<Person> getAll() {
        return queryForList(dataSource, "Select * from People", buildPerson);
    }

    public Optional<Person> getById(final int id) {
        return queryForOptional(dataSource,
                "Select * FROM people WHERE id = ?",
                (ps) -> {
                    ps.setInt(1, id);
                    return ps;
                },
                buildPerson);
    }
}
