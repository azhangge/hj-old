package com.huajie.web.jsf.converter;

import java.beans.FeatureDescriptor;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyNotFoundException;

/**
 *
 * @author huajie
 */
public class RereScopeELResolver extends ELResolver {

    @Override
    public Object getValue(final ELContext elContext, final Object base, final Object property) {

        if (property == null) {
            throw new PropertyNotFoundException();
        }

        FacesContext facesContext = (FacesContext) elContext.getContext(FacesContext.class);

        if ((null == base) && RereScoped.SCOPE_NAME.equals(property.toString())) {

            // Scope is referenced directly
            //用于直接解析RereScoped中的配置参数
            RereScoped scope = getScope(facesContext);
            elContext.setPropertyResolved(true);
            return scope;

        } else if ((null != base) && (base instanceof RereScoped)) {

            // An object within the scope is referenced
            return resolve(facesContext, (RereScoped) base, property.toString());

        } else if (null == base) {
            RereScoped customScope = getScope(facesContext);
            return null != customScope ? resolve(facesContext, customScope, property.toString()) : null;

        }
        return null;
    }

    /**
     * Resolve the key on the given {@link RereScoped}
     *
     * @param facesContext
     * @param scope
     * @param key
     * @return scoped
     */
    public Object resolve(final FacesContext facesContext, final RereScoped scope, final String key) {

        Object value = scope.get(key);
        facesContext.getELContext().setPropertyResolved(value != null);
        return value;

    }

    /**
     * Responsible to retrieve the scope
     *
     * @param facesContext
     * @return
     */
    private RereScoped getScope(final FacesContext facesContext) {

        Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
        RereScoped customScope = (RereScoped) sessionMap.get(RereScoped.SCOPE_NAME);

        return customScope;
    }

    @Override
    public Class<?> getType(final ELContext elContext, final Object base, final Object property) {
        return Object.class;
    }

    @Override
    public void setValue(final ELContext elContext, final Object base, final Object property, final Object value) {
        // do nothing
    }

    @Override
    public boolean isReadOnly(final ELContext elContext, final Object base, final Object property) {
        return true;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext elContext, final Object base) {
        return Collections.<FeatureDescriptor>emptyList().iterator();
    }

    @Override
    public Class<?> getCommonPropertyType(final ELContext elContext, final Object base) {
        if (base != null) {
            return null;
        }
        return String.class;
    }

}
