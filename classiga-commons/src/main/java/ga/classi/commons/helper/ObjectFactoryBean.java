/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.helper;

import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 *
 * @author muhammad
 */
public class ObjectFactoryBean extends AbstractFactoryBean<Object> {
    
    // To read https://stackoverflow.com/questions/2209010/is-it-possible-to-specify-a-class-name-for-spring-framework-in-an-external-file
    
    private Class targetClass;
    
    @Override
    public Class<?> getObjectType() {
        return targetClass;
    }

    @Override
    protected Object createInstance() throws Exception {
        return targetClass.newInstance();
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }
    
}
