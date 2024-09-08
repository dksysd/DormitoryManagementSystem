package server.service;

import org.apache.ibatis.session.SqlSession;
import server.mapper.MyBatisUtil;
import server.mapper.UserMapper;
import shared.dto.UserDto;

public class UserService {
    public UserDto getUserDto(UserDto userDto) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory()) {
            return session.getMapper(UserMapper.class).getUserDto(userDto);
        }
    }
}
