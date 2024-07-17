package codesquad.webserver.init;

import codesquad.webserver.annotation.DataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHandlerInitializer {
    private static final Logger log = LoggerFactory.getLogger(DataHandlerInitializer.class);

    public static Map<String, Object> init(List<Class<?>> classes){
        Map<String, Object> dataHandlerInstances = new HashMap<>();
        for(Class<?> clazz : classes){
            if(clazz.isAnnotationPresent(DataHandler.class)){
                try{
                    DataHandler dataHandler = clazz.getAnnotation(DataHandler.class);
                    Object dataHandlerInstance = clazz.getDeclaredConstructor().newInstance();
                    dataHandlerInstances.put(dataHandler.value(), dataHandlerInstance);
                } catch (Exception e){
                    log.error(e.getMessage(), e);
                }
            }
        }
        return dataHandlerInstances;
    }

}
