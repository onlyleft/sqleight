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
        extract(resultSet, "age", p, NullableHelper::getInteger, Person::setAge);
//        extract(resultSet, "age", p, ResultSet::getInt, Person::setAge);
        extract(resultSet, "name", p, ResultSet::getString, Person::setName);
        extract(resultSet, 1, p, ResultSet::getString, Person::setName);
        return p;
    };
    private ObjectExtractor<Person> buildPerson2 = (resultSet) -> {
        final Person p = new Person();
        new FieldExtractor<>(resultSet, p)
                .extract("name", ResultSet::getString, Person::setName)
                .extract("age", NullableHelper::getInteger, Person::setAge);
        return p;
    };

    private ObjectExtractor<Person> buildPerson3 = (resultSet) -> new BeanBuilder<>(resultSet, Person.class)
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
                }
                , buildPerson);
    }
}
