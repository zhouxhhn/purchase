package cn.sipin.sales.cloud.purchase.service.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.sipin.sales.cloud.purchase.service.util.UserLoginUtil;
import cn.siyue.platform.httplog.aspect.LogAspect;

@Aspect
@Component
public class MemberServiceLogAspect extends LogAspect {
    @Autowired
    private UserLoginUtil userLoginUtil;

    @Override
    public Long getUserId() {
        return userLoginUtil.getUserId();
    }

    @Override
    public String getSystemName() {
        return "Sales Member service";
    }
}
