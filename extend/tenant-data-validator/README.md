## 租户数据验证器

防止跨租户数据泄露, 验证当前返回的数据是否属于当前租户.

## 顶级接口
```java
public interface TenantData {
    String getTenantId();
}
```