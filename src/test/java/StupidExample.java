import com.onlyleft.sqleight.BeanBuilder;
import com.onlyleft.sqleight.NullableHelper;
import com.onlyleft.sqleight.FieldExtractor;
import com.onlyleft.sqleight.ObjectExtractor;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import static com.onlyleft.sqleight.SqlEight.queryForList;
import static com.onlyleft.sqleight.SqlEight.queryForOptional;
import static com.onlyleft.sqleight.FieldExtractor.extract;

public class StupidExample {

    private DataSource dataSource = null;

    private ObjectExtractor<Person> buildPerson = (resultSet) -> {
        final Person p = new Person();
        p.setName(resultSet.getString("name"));
        p.setAge(resultSet.getInt("age"));
        return p;
    };

    private ObjectExtractor<Person> buildPerson1 = (resultSet) -> {
        final Person person = new Person();
        person.setName(resultSet.getString("name"));
        int age = resultSet.getInt("age");
        if (age != 0 && !resultSet.wasNull()) {
            person.setAge(age);
        }
        return person;
    };

    private ObjectExtractor<Person> buildPerson2 = (resultSet) -> {
        final Person person = new Person();
        person.setName(resultSet.getString("name"));
        person.setAge(NullableHelper.getInteger(resultSet, "age"));
        return person;
    };

    private ObjectExtractor<Person> buildPerson3 = (resultSet) -> {
        final Person person = new Person();
        extract(resultSet, "age", person, NullableHelper::getInteger, Person::setAge);
//        extract(resultSet, "age", person, ResultSet::getInt, Person::setAge);
        extract(resultSet, "name", person, ResultSet::getString, Person::setName);
        extract(resultSet, 1, person, ResultSet::getString, Person::setName);
        return person;
    };

    private ObjectExtractor<Person> buildPerson4 = (resultSet) -> {
        final Person person = new Person();
        new FieldExtractor<>(resultSet, person)
                .extract("name", ResultSet::getString, Person::setName)
                .extract("age", NullableHelper::getInteger, Person::setAge);
        return person;
    };

    private ObjectExtractor<Person> buildPerson5 = (resultSet) -> new BeanBuilder<>(resultSet, Person.class)
            .extract("name", ResultSet::getString, Person::setName)
            .extract("age", NullableHelper::getInteger, Person::setAge)
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
