package quoters.bpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import quoters.annotation.InjectRandomInt;

import java.lang.reflect.Field;
import java.util.Random;

@Component
public class InjectRandomIntBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        for (Field field : beanClass.getDeclaredFields()) {
            InjectRandomInt annotation = field.getAnnotation(InjectRandomInt.class);
            if (annotation != null) {
                int min = annotation.min();
                int max = annotation.max();

                Random r = new Random();
                int value = r.nextInt(max - min + 1) + min;

                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, value);
            }
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
