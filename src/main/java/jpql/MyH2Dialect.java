package jpql;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class MyH2Dialect extends H2Dialect {
    public MyH2Dialect() { // 실제 소스 코드를 열어보면 자세히 나와 있음
        registerFunction("group_concat",
                new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
    }

}
