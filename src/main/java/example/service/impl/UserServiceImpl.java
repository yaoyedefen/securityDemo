package example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.entity.User;
import example.mapper.UserMapper;
import example.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author shuaishuai.zhang1
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2023-05-16 09:31:22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

}




