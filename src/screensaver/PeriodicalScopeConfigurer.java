package screensaver;

import javafx.util.Pair;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class PeriodicalScopeConfigurer implements Scope {

    private Map<String, Pair<LocalTime, Object>> map = new HashMap<>();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        if (map.containsKey(name)) {
            Pair<LocalTime, Object> pair = map.get(name);
            Duration between = Duration.between(pair.getKey(), LocalTime.now());
            if (between.getSeconds() > 2) {
                putNewPairToMap(name, objectFactory);
            }
        } else {
            putNewPairToMap(name, objectFactory);
        }
        return map.get(name).getValue();
    }

    private void putNewPairToMap(String name, ObjectFactory<?> objectFactory) {
        map.put(name, new Pair<>(LocalTime.now(), objectFactory.getObject()));
    }

    @Override
    public Object remove(String name) {
        return null;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {

    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
