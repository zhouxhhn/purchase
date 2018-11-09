package cn.sipin.sales.cloud.purchase.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.siyue.platform.httplog.filter.HttpServletRequestReplacedFilter;

/**
 * 配置
 */
@Configuration
public class FilterBeanConfig {
	/**
	 * 用于统一日志配置时会用到
	 *
	 * 解决request流读取多次报错（java流只能读取一次）
	 * @return
	 */
	@Bean
    public HttpServletRequestReplacedFilter requestReplacedFilter() {
        return new HttpServletRequestReplacedFilter();
    }
}
