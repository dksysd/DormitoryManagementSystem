package server.service;

import org.apache.ibatis.session.SqlSession;
import server.mapper.AddressMapper;
import server.mapper.MyBatisUtil;
import shared.dto.AddressDto;

public class AddressService {
    public AddressDto getAddressDto(AddressDto addressDto) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory()) {
            AddressMapper addressMapper = session.getMapper(AddressMapper.class);
            return addressMapper.getAddressDto(addressDto);
        }
    }
}
