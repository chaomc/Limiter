## Limiter

Limiter是一款注解驱动的，适应于业务层面的分布式限流降级框架。Limiter可以使你的SpringBoot项目获得分布式锁和各种限流器能力，帮助你轻松解决业务层面的竞态条件问题，在不提高业务复杂度的同时增强接口的安全性。

##  Quick Start

添加依赖

```xml

<dependency>
	<groupId>site.higgs.limiter</groupId>
    	<artifactId>limiter-support</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

添加`@EnableLimiter` 注解

```java
@SpringBootApplication
@EnableLimiter
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```



##  How  To Use



  以`Lock`组件为例



**Step 0. 定义锁资源**

  下面的代码向`BeanFactory`中注册了一个`BeanId`为 `jdkLock`的实例

```java
@Bean
Lock jdkLock() {
    JdkLock jdkLock = new JdkLock("mnyJdkLock");
    return jdkLock;
}
```

**Step 1. 编写业务接口**

  我们假设有这样一个业务场景，用户可以使用一个Vip兑换码来延长自己的VIP期限，理所当然的是，每个兑换码只能被使用一次，通常情况下，我们会在数据库中查询该兑换码是否存在并且是否已经使用。

```java
@RequestMapping(method = RequestMethod.POST, value = "/exchangeVip")
public ResponseMessage exchangeVip(@RequestBody ExchangeVipRequest request) {
    return demoService.exchangeVip(request, ContextUtils.getCurrentUser());
}
```

**Step 2.添加`HLock`注解**

  上面的接口并不安全，假如在极短的的时间内用户发起了多次相同兑换的请求，由于数据库的事务隔离特性，该兑换码便会被多次兑换，这个漏洞可能被用户恶意使用，造成损失。  这里涉及的重放攻击问题此处不再深入讨论,(欢迎移步我的[博客](https://blog.higgs.site/2019/06/24/从接口幂等性到重放攻击/#more))。现在我们添加HLock注解保护该接口。

```java
@RequestMapping(method = RequestMethod.POST, value = "/exchangeVip")
@HLock(limiter = "jdkLock", key = "#request.vipCode", fallback = "fallbackToBusy")
public ResponseMessage exchangeVip(@RequestBody ExchangeVipRequest request) {
   return demoService.exchangeVip(request, ContextUtils.getCurrentUser());
}
```

 该注解的含义是，在请求到达时，使用`jdkLock`这个锁锁住`#request.vipCode`这个资源，如果锁成功了，后面的逻辑继续进行，在业务逻辑完成后便会释放该资源，如果`#request.vipCode`这个资源已经被锁定，便会降级到`fallbackToBusy`方法进行。这样其他相同 `#request.vipCode`的请求便会被拦截，

在同一class下添加降级方法 `fallbackToBusy`

```java
ResponseMessage fallbackToBusy(ExchangeVipRequest request) {
   return ResponseMessage.error("服务繁忙，请稍后再试！");
 }
```

**Step 3. 使用分布式锁**

  随着业务发展，单实例应用不能再满足业务的需求，分布式改造开始了。上面的jdkLock是一个存储在内存的锁，这意味着切换到多实例环境后，仍然可能在多个实例上同时发起多个相同的请求。要解决这个问题，只需要将锁切换至分布式锁即可，这里我们以RedisLock为例。

注入一个RedisLock

```java
@Bean
    Lock redisLock() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379")
                .setDatabase(1);
        RedissonClient redissonClient = null;
        try {
            redissonClient = Redisson.create(config);
        } catch (Exception e) {
            logger.info("redis连接失败");
         
        }
        logger.info("redis连接成功");
        RedisLock redisLock = new RedisLock(redissonClient, "myRedisLock");

        return redisLock;
}
```

修改注解

```java
@HLock(limiter = "redisLock", key = "#request.vipCode", fallback = "fallback")
```



**Step 4. star this niubility project**



## Document

- [详细文档](./doc.md)

- [Demo](https://github.com/jjj124/limtierDemo)


## FAQ

