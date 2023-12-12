# 通用 Mapper

基于 **mybatis/provider**实现的通用 Mapper。

## 1. 快速入门

一个不需要任何配置就可以直接使用的通用Mapper，只需要简单的学习几个注解就能在项目中使用。

### 1.1 主要目标

1. 无需任何配置，继承BaseMapper 即可获得大量的通用方法；
2. 支持联合主键；
3. 字典自动翻译（开发中）；
4. 通用数据权限（待完成）。

### 1.2 系统要求

1. 依赖MyBatis 3.5.6，可根据需要更新为最新版本。
2. 最低需要Java 8。

### 1.3 安装

无。

### 1.4 快速入门

基本原理1：模仿Jpa，将实体和数据库表通过注解进行映射；
基本原理2：基于Mybatis3中的Prodiver，为接口动态生成sql。

#### 1.4.1 实体类配置

1. user表：

```sql
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
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
1. 实体类，主键字段都加上@Id注解

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

## 2. 注解说明
1. @Table
```java
public @interface Table {
    //表名，不指定则使用实体类名
    String value() default "";
}
```
2. @Column
```java
public @interface Column {
    //对应数据库列名
    String value() default "";
    //查询过滤类型
    FilterOperator filterOperator() default FilterOperator.EQ;
    //是否查询，select是否带上该字段
    boolean selectable() default true;
    //是否插入，insert是否带上该字段
    boolean insertable() default true;
    //是否更新，update是否带上该字段
    boolean updatable() default true;
}
```
3. @Id
```java
public @interface Id {
    //主键是否自动生成
    boolean auto() default false;
}
```
4. @OrderBy
```java
public @interface OrderBy {
    //排序
    Order order() default Order.ASC;
    //多个排序字段先后顺序
    int orderPriority() default 0;
}
```
5. @Transient
```java
//非数据库字段需加上该注解
public @interface Transient {
}
```
6. @Translate
```java
//需要进行翻译的字段需增加该注解
public @interface Transient {
}
```
