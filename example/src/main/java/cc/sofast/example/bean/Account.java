package cc.sofast.example.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author apple
 */
@Data
@TableName("account")
public class Account {

    @TableId(type = IdType.AUTO)
    private Long id;
    public String nickname;
    public Long age;
    public String address;


}
