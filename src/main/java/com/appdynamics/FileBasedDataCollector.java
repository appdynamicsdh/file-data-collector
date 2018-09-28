package com.appdynamics;

import com.appdynamics.instrumentation.sdk.SDKClassMatchType;
import com.appdynamics.instrumentation.sdk.SDKStringMatchType;
import com.appdynamics.instrumentation.sdk.contexts.ISDKDataContext;
import com.appdynamics.instrumentation.sdk.template.ADataCollector;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflector;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;
import com.appdynamics.instrumentation.sdk.Rule;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileBasedDataCollector extends ADataCollector {

    private static List<String> fileContents;
    private static final String CLASS_TO_INSTRUMENT = System.getProperty("isdk.file.based.data.collector.class");
    private static final String METHOD_TO_INSTRUMENT = System.getProperty("isdk.file.based.data.collector.method");

    @Override
    public List<Rule> initializeRules() {
        ArrayList<Rule> rules = new ArrayList<Rule>();
        rules.add(new Rule.Builder(CLASS_TO_INSTRUMENT).methodMatchString(METHOD_TO_INSTRUMENT).build());
        return rules;
    }

    @Override
    public void storeData(Object invokedObject, String className, String methodName, Object[] paramValues,
            Throwable thrownException, Object returnValue, ISDKDataContext sdkContext) throws ReflectorException {
        try {
            if(fileContents==null) {
                fileContents = Files.lines(Paths.get(System.getProperty("isdk.file.based.data.collector.file.name")))
                        .map(String::new)
                        .collect(Collectors.toList());
            }

            sdkContext.storeData("Version", fileContents.get(0));

        }catch(Exception e){
            getLogger().info("EXCEPTION",e);
        }
    }

    public boolean addToSnapshot() {
        return true;
    }

    public boolean addToAnalytics() {
        return true;
    }
}
