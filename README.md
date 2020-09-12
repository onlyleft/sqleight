# SqlEight

### Purpose
I wrote this library for my own personal use when needing to query database schemas which I don't own and only read a
subset of tables / views from. I usually use Hibernate or some other JPA implementation for schemas which I own and
store into. However there are times which a simple query or select is necessary but JPA or a full blown Spring setup is 
overkill. I was interested in using the Apache DbUtils library for this purpose but it's not kept up with modern Java 8 
idioms and I'm really not interested in using inheritance to control the behavior, I favor composition instead.

I wrote this in an afternoon after reviewing some training on generics and lambdas. Its mostly focused on helping with a
common pattern I have when reading a schema I don't own.

### NullableReader: The ResultSet getters you've always wanted when dealing with SQL NULL
A common issue I have with JDBC is when dealing with number or boolean columns which are nullable. The JDBC resultset
API returns primitive values which cannot be null in Java. For this reason, you must call `ResultSet::wasNull` to check 
if the previously read value was a SQL NULL or if it really is `0` or `false`. I've written the `NullableReader` class
to make this pattern easier to use. There is a column position and column name based version for every getter on 
`ResultSet` which deals with primitive values. Enjoy!

### `Extractor<T>` functional interface for quickly defining how to read a row into a `T`
Sample use as a bare lambda
```java
Extractor<Person> buildPerson = (resultSet) -> {
    final Person person = new Person();
    person.setName(resultSet.getString("name"));
    person.setAge(NullableReader.getInteger(resultSet, "age"));
    return person;
};
```
Sample use with `ObjectBuilder<T>`
```java
Extractor<Person> buildPerson = (resultSet) -> ObjectBuilder.of(Person.class, resultSet)
        .extract("name", ResultSet::getString, Person::setName)
        .extract("age", NullableReader::getInteger, Person::setAge)
        .get();
```

### Handle SQLException with a SQLExceptionHandler functional interface
```java
SQLExceptionHandler defaultLoggingHandler = (SQLException e) -> LOGGER.log(Level.SEVERE, "Could not complete query", e);
```

### Setup your prepared statements with a functional interface used as a closure
```java
public Optional<Person> getById(final int id) {
    return queryForOptional(dataSource,
            "SELECT name, age FROM people WHERE id = ?",
            (ps) -> {
                ps.setInt(1, id);
                return ps;
            },
            buildPerson);
}
```

### Static include SqlEight.* for quick use to query for an `Optional<T>` or a `List<T>`
```java
import com.onlyleft.sqleight.ObjectBuilder;
import com.onlyleft.sqleight.NullableReader;
import com.onlyleft.sqleight.Extractor;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import static com.onlyleft.sqleight.SqlEight.*;

public class StupidExample {
    private final Extractor<Person> buildPerson = (resultSet) -> ObjectBuilder.of(Person.class, resultSet)
            .extract("name", ResultSet::getString, Person::setName)
            .extract("age", NullableReader::getInteger, Person::setAge)
            .get();

    public List<Person> getAll() {
        return queryForList(dataSource, "SELECT name, age FROM people", buildPerson);
    }

    public Optional<Person> getById(final int id) {
        return queryForOptional(dataSource,
                "SELECT name, age FROM people WHERE id = ?",
                (ps) -> {
                    ps.setInt(1, id);
                    return ps;
                },
                buildPerson);
    }
}
```