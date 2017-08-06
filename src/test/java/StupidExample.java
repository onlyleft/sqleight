import com.onlyleft.sqleight.NullableHelper;
import com.onlyleft.sqleight.extractor.FieldExtractor;
import com.onlyleft.sqleight.extractor.ObjectExtractor;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import static com.onlyleft.sqleight.SqlEight.queryForList;
import static com.onlyleft.sqleight.SqlEight.queryForOptional;
import static com.onlyleft.sqleight.extractor.FieldExtractor.extract;

public class StupidExample {

    private DataSource dataSource = null;

    private ObjectExtractor<Person> buildPerson = (resultSet) -> {
        final Person p = new Person();
        extract(resultSet, "age", p, NullableHelper::getInteger, Person::setAge);
//        extract(resultSet, "age", p, ResultSet::getInt, Person::setAge);
        extract(resultSet, "name", p, ResultSet::getString, Person::setName);
        return p;
    };

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
