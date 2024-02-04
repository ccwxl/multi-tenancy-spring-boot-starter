### 动态接口

以SQL为基础,实现租户的定制化的业务. 适用于的场景包含: 定制列表, 数据报表, 定制独立的crud的功能.

### 主要思路

- 注册接口，在线写SQL为租户扩展。
- 数据库字段下划线转驼峰的映射
- cache 是否加入到redis cache中,
    - cache key
    - cache 更新
    - cache 失效
- Get
    - 分页查询
    - 列表查询
    - 详情查询
- Post
    - 增加
- Put
    - 更新
    - 局部更新
- Delete
    - 删除

### 实现方式

- 接口层
    - 动态注册restful接口
    - 解析参数,通用校验规则
- 处理层
    - 使用脚本引擎对数据进行二次处理,转换等操作
- SQL执行层
    - 基于mybatis-plus(mybatis)实现
    - 抽取公共的Mapper ,使用SQLProvider的注解的方式来达到定制化sql
    - 对字段进行驼峰转换

### 数据库设计

- 分类表
    - id
    - label
    - parent_id

- 定义表
    - id
    - tenant text
    - catalog_id bigint
    - rest_url text
    - rest_param json ? 校验
    - function_script text sandbox?
    - exec_sql text sql注入校验
    - active smallint

### 参考

- https://gitee.com/ssssssss-team/magic-api
- https://github.com/ClouGence/hasor
- https://gitee.com/Tencent/APIJSON
- https://gitee.com/linebyte/crabc

