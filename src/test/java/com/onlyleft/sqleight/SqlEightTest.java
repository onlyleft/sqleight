package com.onlyleft.sqleight;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import static com.onlyleft.sqleight.DatasourceTestFactory.getConnection;
import static com.onlyleft.sqleight.DatasourceTestFactory.setupTables;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class SqlEightTest {

    private static Connection connection = getConnection();

    private Extractor<Person> buildPerson = (resultSet) -> new ObjectBuilder<>(resultSet, Person.class)
            .extract("name", ResultSet::getString, Person::setName)
            .extract("age", NullableReader::getInteger, Person::setAge)
            .getResult();

    @BeforeClass
    public static void setup() {
        setupTables(connection);
    }

    @Test
    public void checkConnection() {
        assertThat(connection, not(nullValue()));
    }

    @Test
    public void getAll() {
        List<Person> personList = SqlEight.queryForList(connection, "SELECT name, age FROM Person", buildPerson);

        assertThat(personList.isEmpty(), is(false));
        assertThat(personList.size(), is(2));
    }

    @Test
    public void getPeter() {
        Optional<Person> optionalPerson = SqlEight.queryForOptional(connection, "SELECT name, age FROM Person WHERE LOWER(name) = LOWER(?)", (ps) -> {
            ps.setString(1, "peter");
            return ps;
        }, buildPerson);

        assertThat(optionalPerson.map(Person::getAge), is(Optional.of(31)));
        assertThat(optionalPerson.map(Person::getName), is(Optional.of("Peter")));
    }
}