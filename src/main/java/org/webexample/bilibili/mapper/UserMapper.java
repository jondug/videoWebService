package org.webexample.bilibili.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.webexample.bilibili.model.User;
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
