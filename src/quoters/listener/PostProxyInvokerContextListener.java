package quoters.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import quoters.annotation.PostProxy;

import java.lang.reflect.Method;

@Component
public class PostProxyInvokerContextListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        for (String name : context.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
            try {
                Class<?> beanClass = Class.forName(beanDefinition.getBeanClassName());
                for (Method method : beanClass.getMethods()) {
                    if (method.isAnnotationPresent(PostProxy.class)) {
                        Object bean = context.getBean(name);
                        Method originalMethod = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                        originalMethod.invoke(bean);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
