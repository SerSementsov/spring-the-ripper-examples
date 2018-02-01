package scopesproblem;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * Some solutions to the problem of injecting (and updating) prototype bean into singleton
 */
@Configuration
public class Config {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext("scopesproblem");

        System.out.println("not working:");
        context.getBean(SingletonBean1.class).printCurrentTime();
        Thread.sleep(1000L);
        context.getBean(SingletonBean1.class).printCurrentTime();

        System.out.println("working - but introduces coupling between spring and business modules");
        context.getBean(SingletonBean2.class).printCurrentTime();
        Thread.sleep(1000L);
        context.getBean(SingletonBean2.class).printCurrentTime();

        System.out.println("working - preferred way of solving this problem");
        context.getBean(SingletonBean.class).printCurrentTime();
        Thread.sleep(1000L);
        context.getBean(SingletonBean.class).printCurrentTime();
    }
}

@Component
@Scope("prototype")
class PrototypeBean {

    private final LocalTime localTime = LocalTime.now();

    public String getCurrentTime() {
        return localTime.toString();
    }
}

@Component
@Scope("singleton")
class SingletonBean1 {

    @Autowired
    private PrototypeBean prototypeBean;


    public void printCurrentTime() {
        System.out.println(prototypeBean.getCurrentTime());
    }
}


@Component
@Scope("singleton")
class SingletonBean2 implements ApplicationContextAware {

    private ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }

    public void printCurrentTime() {
        PrototypeBean prototypeBean = appContext.getBean(PrototypeBean.class);
        System.out.println(prototypeBean.getCurrentTime());
    }
}


@Component
@Scope("singleton")
class SingletonBean {

    @Lookup
    protected PrototypeBean getPrototypeBean() {
        return null;
    }


    public void printCurrentTime() {
        PrototypeBean prototypeBean = getPrototypeBean();
        System.out.println(prototypeBean.getCurrentTime());
    }
}
