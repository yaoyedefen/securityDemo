package example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import example.entity.Role;
import example.mapper.RoleMapper;
import example.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * @author shuaishuai.zhang1
 * @description 针对表【role】的数据库操作Service实现
 * @createDate 2023-05-18 10:47:01
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
        implements RoleService {

}




