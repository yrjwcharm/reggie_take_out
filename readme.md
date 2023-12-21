#### SpringBoot Controller入参

Get请求的话，注解@GetMapping 入参直接使用实体类 不用加前缀注解，如果用单个参数去接受的话，需要写多个，


此外还有一种方式是使用Map<String,Object>接受，前边必须添加注解 get 用@RequstParam
post请求 要改成@RequstBody
同理List也一样