package quoters.bpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import quoters.annotation.Profiling;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProfilingAnnotationBeanPostProcessor implements BeanPostProcessor {

    private Map<String, Class> beansToProfile = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> originalBeanClass = bean.getClass();
        if (originalBeanClass.isAnnotationPresent(Profiling.class)) {
            beansToProfile.put(beanName, originalBeanClass);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beansToProfile.containsKey(beanName)) {
            Class originalBeanClass = beansToProfile.get(beanName);
            return Proxy.newProxyInstance(getClassLoader(), originalBeanClass.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("-=- Profiling -=-");
                    long start = System.nanoTime();
                    Object result = method.invoke(bean, args);
                    long timeToRun = System.nanoTime() - start;
                    System.out.println("method \"" + method.getName() + "\"" +
                            " of \"" + originalBeanClass + "\"" +
                            " was running for " + timeToRun + " ns");
                    return result;
                }
            });
        }

        return bean;
    }

    private ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }
}









