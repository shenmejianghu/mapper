# 通用 Mapper

基于 **mybatis/provider**实现的通用 Mapper。

## 1. 快速入门

一个不需要任何配置就可以直接使用的通用Mapper，只需要简单的学习几个注解就能在项目组使用。

### 1.1 主要目标

1. 无需任何配置，继承BaseMapper 即可获得大量的通用方法；
2. 支持联合主键；
3. 字典自动翻译（待完成）；
4. 通用数据权限（待完成）。

### 1.2 系统要求

1. 依赖MyBatis 3.5.6，可根据需要更新为最新版本。
2. 最低需要Java 8。

### 1.3 安装



### 1.4 快速入门

基本原理是模仿Jpa，将实体和数据库表通过注解进行映射。

#### 1.4.1 实体类配置

1. user表：

```sql
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
```

2. User实体类：

```java
@Table("user")
public class User {
    @Id(auto = true)
    @Column(value = "id")
    private Long id;

    @Column(value = "name", filterOperator = FilterOperator.LIKE)
    @OrderBy(orderPriority = 0)
    private String name;

    @OrderBy(order = Order.DESC, orderPriority = 1)
    private Integer age;

    @Column(value = "email", filterOperator = FilterOperator.EQ)
    private String email1;

    @Transient
    private String test;
}
```


#### 1.4.2 Mapper接口定义

有了 `User` 实体后，直接创建一个继承了 `Mapper` 的接口即可：

```java
public interface UserMapper extends BaseMapper<User, Long> {

}
```

#### 1.4.3 接口方法

    int insert(T t);

    int batchInsert(List<T> entity);

    int deleteById(K id);

    int deleteBatchIds(Collection<K> ids);

    int updateById(T entity);

    int updateSelectiveById(T entity);

    T selectById(K id);

    List<T> selectBatchIds(Collection<K> ids);

    List<T> selectAll();

    List<T> selectPage(PageRequest<T> pageRequest);

    Long selectCount(T entity);
    
#### 1.4.3 联合主键用法
1. 实体类，主键自动都加上@Id注解

```java
public class User1 {
    @Id
    @Column(value = "id1")
    private String id1;
    
    @Id
    @Column(value = "id2")
    private String id2;
    
    @Column(value = "name", filterOperator = FilterOperator.LIKE)
    @OrderBy(orderPriority = 0)
    private String name;
    
    @OrderBy(order = Order.DESC, orderPriority = 1)
    private Integer age;
    
    @Column(value = "email", filterOperator = FilterOperator.EQ)
    private String email;
    
    @Transient
    private String test;
}
```

2. 定一个一个主键类

```java
public class User1Id {
    private String id1;
    private String id2;
}
```

3. 定义mapper

```java
public interface User1Mapper extends BaseMapper<User1,User1Id> {
}
```
