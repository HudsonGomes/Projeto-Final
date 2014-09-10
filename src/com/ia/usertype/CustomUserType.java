package com.ia.usertype;

import java.io.Externalizable;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.type.NullableType;
import org.hibernate.usertype.UserType;

public abstract class CustomUserType<K extends Serializable> implements Comparable<CustomUserType<K>>, Serializable, UserType, Externalizable {

	@SuppressWarnings("unchecked")
	private static final Map userTypes = new HashMap();

	protected K key;
	protected String value;

	protected CustomUserType() {
		super();
	}

	@SuppressWarnings("unchecked")
	protected CustomUserType(K key, String value) {
		this.key = key;
		this.value = value;
		Class<? extends CustomUserType<K>> clazz = returnedClass(); // NOPMD
		Map<String, CustomUserType<K>> entries = (Map<String, CustomUserType<K>>) userTypes.get(clazz);
		if (entries == null) {
			entries = new LinkedHashMap<String, CustomUserType<K>>();
			userTypes.put(clazz, entries);
		}
		if (entries.containsKey(key.toString())) {
			throw new IllegalArgumentException("A chave '" + key + "' já existe para o tipo " + returnedClass().getName());
		}
		entries.put(key.toString(), this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomUserType<K> other = (CustomUserType<K>) obj;
		if (this.key == null) {
			if (other.key != null)
				return false;
		} else if (!this.key.equals(other.key))
			return false;
		return true;
	}

	public final K getKey() {
		return this.key;
	}

	public final String getValue() {
		return this.value;
	}

	@SuppressWarnings("unchecked")
	public Collection<CustomUserType<K>> getCollection() {
		Map entries = (Map) userTypes.get(returnedClass());
		if (entries != null)
			return Collections.unmodifiableCollection(entries.values());
		else
			return Collections.EMPTY_LIST;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Serializable, U extends CustomUserType<T>> Collection<U> getCollection(Class<U> clazz) {
		Map<String, CustomUserType<T>> entries = (Map<String, CustomUserType<T>>) userTypes.get(clazz);
		return (entries != null) ? Collections.unmodifiableCollection(entries.values()) : Collections.EMPTY_LIST;
	}

	@SuppressWarnings("unchecked")
	public CustomUserType<K> get(K key) {
		Map<String, CustomUserType<K>> entries = (Map<String, CustomUserType<K>>) userTypes.get(returnedClass());
		return (entries != null) ? entries.get(key.toString()) : null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> CustomUserType<T> get(Class<? extends CustomUserType<T>> clazz, T key) {
		Map<String, CustomUserType<T>> entries = (Map<String, CustomUserType<T>>) userTypes.get(clazz);
		return (entries != null) ? entries.get(key.toString()) : null;
	}

	protected abstract NullableType getType();

	public int[] sqlTypes() {
		return new int[] { getType().sqlType() };
	}

	@Override
	public String toString() {
		return this.key.toString();
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public boolean isMutable() {
		return false;
	}

	@SuppressWarnings("unchecked")
	public Class<? extends CustomUserType<K>> returnedClass() {
		return (Class<? extends CustomUserType<K>>) this.getClass();
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y) {
			return true;
		} else if (x == null || y == null) {
			return false;
		} else {
			return x.equals(y);
		}
	}

	@SuppressWarnings("unchecked")
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		Object result;
		try {
			K key = (K) getType().nullSafeGet(rs, names[0]);
			Map entries = (Map) userTypes.get(returnedClass());
			result = ((entries != null) ? entries.get(key.toString()) : null);
		} catch (Throwable t) {
			return null;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		if ((value != null) && !returnedClass().isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException("O valor recebido não é do tipo [" + returnedClass().getName() + "] e sim do tipo ["	+ value.getClass() + "]");
		}
		final CustomUserType typedValue = (CustomUserType) value;
		K key;
		if (typedValue != null) {
			key = (K) typedValue.getKey();
		} else {
			key = null;
		}
		st.setObject(index, key, getType().sqlType());
	}

	public int hashCode(Object arg0) throws HibernateException {
		return arg0.hashCode();
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	public Object assemble(Serializable state, Object owner) throws HibernateException {
		return state;
	}

	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

}