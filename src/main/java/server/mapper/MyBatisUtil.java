package server.mapper;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MyBatisUtil {
    private static final SqlSessionFactory sqlSessionFactory;

    static {
        String resource = "mybatis/config.xml";
        InputStream inputStream = MyBatisUtil.class.getClassLoader().getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    public static SqlSession getSqlSessionFactory() {
        return sqlSessionFactory.openSession();
    }
}
