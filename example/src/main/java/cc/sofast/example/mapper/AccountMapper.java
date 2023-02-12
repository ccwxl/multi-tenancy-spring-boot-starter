package cc.sofast.example.mapper;

import cc.sofast.example.bean.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author apple
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {

}
