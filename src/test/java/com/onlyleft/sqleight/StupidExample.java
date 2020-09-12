package com.onlyleft.sqleight;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import static com.onlyleft.sqleight.FieldExtractor.extract;
import static com.onlyleft.sqleight.SqlEight.queryForList;
import static com.onlyleft.sqleight.SqlEight.queryForOptional;

public class StupidExample {

    private final Connection connection = null;

    private final Extractor<Person> buildPerson = (resultSet) -> {
        final Person p = new Person();
        p.setName(resultSet.getString("name"));
        p.setAge(resultSet.getInt("age"));
        return p;
    };

    private final Extractor<Person> buildPerson1 = (resultSet) -> {
        final Person person = new Person();
        person.setName(resultSet.getString("name"));
        int age = resultSet.getInt("age");
        if (age != 0 && !resultSet.wasNull()) {
            person.setAge(age);
        }
        return person;
    };

    private final Extractor<Person> buildPerson2 = (resultSet) -> {
        final Person person = new Person();
        person.setName(resultSet.getString("name"));
        person.setAge(NullableReader.getInteger(resultSet, "age"));
        return person;
    };

    private final Extractor<Person> buildPerson3 = (resultSet) -> {
        final Person person = new Person();
        extract(resultSet, "age", person, NullableReader::getInteger, Person::setAge);
//        extract(resultSet, "age", person, ResultSet::getInt, Person::setAge);
        extract(resultSet, "name", person, ResultSet::getString, Person::setName);
        extract(resultSet, 1, person, ResultSet::getString, Person::setName);
        return person;
    };

    private final Extractor<Person> buildPerson4 = (resultSet) -> {
        final Person person = new Person();
        FieldExtractor.of(person, resultSet)
                .extract("name", ResultSet::getString, Person::setName)
                .extract("age", NullableReader::getInteger, Person::setAge);
        return person;
    };

    private final Extractor<Person> buildPerson5 = (resultSet) -> ObjectBuilder.of(Person::new, resultSet)
            .extract("name", ResultSet::getString, Person::setName)
            .extract("age", NullableReader::getInteger, Person::setAge)
            .get();

    public List<Person> getAll() {
        return queryForList(connection, "Select * from People", buildPerson);
    }

    public Optional<Person> getById(final int id) {
        return queryForOptional(connection,
                "Select * FROM people WHERE id = ?",
                (ps) -> {
                    ps.setInt(1, id);
                    return ps;
                },
                buildPerson);
    }
}
