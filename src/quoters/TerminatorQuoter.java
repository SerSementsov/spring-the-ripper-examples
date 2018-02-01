package quoters;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import quoters.annotation.DeprecatedClass;
import quoters.annotation.InjectRandomInt;
import quoters.annotation.PostProxy;
import quoters.annotation.Profiling;

import javax.annotation.PostConstruct;

@Component
@Profiling
@DeprecatedClass(newImpl = T1000.class)
public class TerminatorQuoter implements Quoter {

    @Value("I'll be back")
    private String quote;

    @InjectRandomInt(min = 2, max = 5)
    private int count;

    public TerminatorQuoter() {
        System.out.println("phase 1");
    }

    @PostConstruct
    public void init() {
        System.out.println("phase 2");
        System.out.println("count = " + count);
    }

    @Override
    @PostProxy
    public void sayQuote() {
        System.out.println("phase 3");
        for (int i = 0; i < count; i++) {
            System.out.println("message = " + quote);
        }
    }
}
