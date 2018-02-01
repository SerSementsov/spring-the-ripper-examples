package quoters.bfpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import quoters.annotation.DeprecatedClass;

@Component
public class DeprecatedClassBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String name : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
            Class<?> beanClass = ClassUtils.resolveClassName(beanDefinition.getBeanClassName(), this.getClass().getClassLoader());
            DeprecatedClass annotation = beanClass.getAnnotation(DeprecatedClass.class);
            if (annotation != null) {
                Class<?> newClass = annotation.newImpl();
                beanDefinition.setBeanClassName(newClass.getName());
            }
        }
    }
}
