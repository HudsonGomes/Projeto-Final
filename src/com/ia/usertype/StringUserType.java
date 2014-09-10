package com.ia.usertype;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.hibernate.Hibernate;
import org.hibernate.type.NullableType;

public abstract class StringUserType extends CustomUserType<String> {

	protected StringUserType() {
		super();
	}

	protected StringUserType(String name) {
		super(name, name);
	}

	protected StringUserType(String persistentString, String name) {
		super(persistentString, name);
	}

	public int compareTo(CustomUserType<String> other) {
		if (other == this) {
			return 0;
		}
		if (other == null) {
			return 1;
		}
		return getKey().compareTo(other.getKey());
	}

	@Override
	protected NullableType getType() {
		return Hibernate.STRING;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.key = in.readUTF();
		this.value = get(this.key).getValue();
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(getKey());
	}

}