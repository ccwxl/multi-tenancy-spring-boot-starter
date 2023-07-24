### template-schema-sync

- 根据模板库初始化的数据库
- schema之间的和DDL对比报告
- schema之间生成差异的DDL，执行同步更新
- 数据库 Schema 差异管理
  租户数据库理论上要求同构，但实际工作中因为开发团队手工管理变更脚本，或是一些临时性的紧急情况，部分库总会出现或多或少的
  Schema 差异，导致后续的统一变更经常在部分库上发布失败，排查过程费时费力。
- 新租户库的 Schema 同步
  新业务租户的创建一般是由业务侧发起。在很多 SaaS 企业中，这一过程并不会第一时间通知管理团队，导致新租户库的纳管存在少则数小时多则数天的时间差，这一时间差足够产生大量的
  Schema 差异。

## 下载连接

- https://www.enterprisedb.com/downloads/postgres-postgresql-downloads