package com.tendersaucer.collector.script;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Alex on 4/24/2016.
 */
public class ScriptFactory {

    private ScriptFactory() {
    }

    public static Script buildScript(ScriptDefinition scriptDef) {
        Script script = null;
        try {
            String scriptType = scriptDef.getType();
            String className = ScriptConfig.getInstance().getClassName(scriptType);
            Class<?> c = Class.forName(className);
            Constructor<?> constructor = c.getConstructor(ScriptDefinition.class);
            script = (Script)constructor.newInstance(scriptDef);
        } catch (ClassNotFoundException e) {
            // TODO:
        } catch (NoSuchMethodException e) {
            // TODO:
        } catch (InstantiationException e) {
            // TODO:
        } catch (IllegalAccessException e) {
            // TODO:
        } catch (InvocationTargetException e) {
            // TODO:
        }

        return script;
    }
}
