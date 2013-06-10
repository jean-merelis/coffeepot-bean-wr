/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.parser;

import coffeepot.bean.wr.typeHandler.TypeHandlerFactory;
import coffeepot.bean.wr.typeHandler.TypeHandlerFactoryImpl;
import coffeepot.bean.wr.types.FormatType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeandeson O. Merelis
 */
public final class ObjectParserFactory {

    private Set<Class> noResolved = new HashSet<>();
    private Map<Class, ObjectParser> parses = new HashMap<>();
    private TypeHandlerFactory handlerFactory = new TypeHandlerFactoryImpl();
    private FormatType formatType;

    public ObjectParserFactory(FormatType formatType) {
        this.formatType = formatType;
    }

    public void create(Class<?> clazz) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
        ObjectParser objectParser = parses.get(clazz);
        if (objectParser != null) {
            return;
        }

        try {
            objectParser = new ObjectParser(clazz, this);
            parses.put(clazz, objectParser);
            noResolved.remove(clazz);
            while (!noResolved.isEmpty()) {
                Class next = noResolved.iterator().next();
                createHelper(next);
            }
        } catch (UnresolvedObjectParserException ex) {
            Logger.getLogger(ObjectParserFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    private void createHelper(Class<?> clazz) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
        ObjectParser objectParser = parses.get(clazz);
        if (objectParser != null) {
            noResolved.remove(clazz);
            return;
        }

        objectParser = new ObjectParser(clazz, this);
        parses.put(clazz, objectParser);
        noResolved.remove(clazz);
    }

    public Set<Class> getNoResolved() {
        return noResolved;
    }

    public Map<Class, ObjectParser> getParsers() {
        return parses;
    }

    public TypeHandlerFactory getHandlerFactory() {
        return handlerFactory;
    }

    public void setHandlerFactory(TypeHandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    public FormatType getFormatType() {
        return formatType;
    }
}
