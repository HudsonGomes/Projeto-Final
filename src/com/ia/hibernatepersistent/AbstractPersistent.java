package com.ia.hibernatepersistent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.ia.util.ReflectionUtil;




/**
 * Esta classe tem como objetivo diminuir muito o trabalho e a possibilidade de
 * erro ao construir o hashCode() e equals() dos VO's. Herdando desta classe,
 * basta que seus VO's anotem com a annotation {@link NaturalKey} os atribuitos
 * do VO que compoem sua chave. Assim, n„o È necess·rio implementar o hashCode()
 * e equals(), sendo estes implementados por esta classe de forma abstrata.
 * 
 * @author Marco Barcellos
 */
public abstract class AbstractPersistent implements IPersistent {

	private static final long serialVersionUID = 1L;
	
	// Caches for getters
	private static final Map<Class<? extends AbstractPersistent>, Map<Field, Method>> getterCacheMethods = new HashMap<Class<? extends AbstractPersistent>, Map<Field, Method>>();
	private static final Map<Class<? extends Object>, Method> hashCodeCache = new HashMap<Class<? extends Object>, Method>();

	public boolean isPersisted() {
		return getId() != null;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
	
		Class<? extends AbstractPersistent> class1 = this.getClass();
		Class<? extends Object> class2 = o.getClass();
	
		if (!(class2.isAssignableFrom(class1) || class1.isAssignableFrom(class2))) {
			return false;
		}
	
		Collection<Method> thisCandidateKeysGetters = this.getNaturalKeysGetters().values();
	
		for (Iterator<Method> it = thisCandidateKeysGetters.iterator(); it.hasNext();) {
			Method candidateKeyGetter = it.next();
			final Object valorAtributo1 = ReflectionUtil.getGetterValue(this, candidateKeyGetter);
			final Object valorAtributo2 = ReflectionUtil.getGetterValue(o, candidateKeyGetter);
			if (valorAtributo1 == null && valorAtributo2 == null) {
				continue;
			}
			if (valorAtributo1 == null || !valorAtributo1.equals(valorAtributo2)) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;

		Collection<Method> thisCandidateKeysGetters = this.getNaturalKeysGetters().values();
		for (Iterator<Method> iterator = thisCandidateKeysGetters.iterator(); iterator.hasNext();) {
			Method keyGetter = iterator.next();
			Object valorAtributo = null;
			valorAtributo = ReflectionUtil.getGetterValue(this, keyGetter);

			if (valorAtributo == null) {
				result = result * prime;
			} else {
				Class<? extends Object> classeAtributo = valorAtributo.getClass();
				Object fieldHashCode = Integer.valueOf(0);
				Method hashCodeMethod = hashCodeCache.get(classeAtributo);
				if (hashCodeMethod == null) {
					Method metodo;
					try {
						metodo = ReflectionUtil.getMethod(classeAtributo, "hashCode", new Class[0]);
					} catch (Exception e) {
						throw new IllegalStateException("Erro ao procurar m√©todo HashCode na classe: " + valorAtributo.getClass(), e);
					}
					hashCodeCache.put(classeAtributo, metodo);
					hashCodeMethod = metodo;
				}
				try {
					fieldHashCode = hashCodeMethod.invoke(valorAtributo, new Object[0]);
				} catch (Exception e) {
					throw new IllegalStateException("Erro ao executar m√©todo HashCode na classe: " + valorAtributo.getClass(), e);
				}
				result = prime * result + ((Integer) (fieldHashCode)).intValue();
			}

		}

		return result;
	}

	protected Map<Field, Method> getNaturalKeysGetters() {
		Class<? extends AbstractPersistent> clazz = getClass();
		synchronized (getterCacheMethods) {
			Map<Field, Method> methods = getterCacheMethods.get(clazz);
			if (methods != null) {
				return methods;
			}
			methods = ReflectionUtil.gettersMapForFieldsContainingAnnotation(clazz, NaturalKey.class);
			getterCacheMethods.put(clazz, methods);
			return methods;
		}
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(getClass().getSimpleName());
		sb.append("{");
		
		if (getNaturalKeysGetters().size() > 0) {
			boolean first = true;
			for (Iterator<Method> it = getNaturalKeysGetters().values().iterator(); it.hasNext();) {
				Method candidateKeyGetter = it.next();
				final Object valorKey = ReflectionUtil.getGetterValue(this, candidateKeyGetter);
				if (first) first = false; else sb.append(", ");
				sb.append(valorKey);
			}
		} else {
			sb.append(getId());
		}
		
		sb.append("}");
		
		return sb.toString();
	}

}
