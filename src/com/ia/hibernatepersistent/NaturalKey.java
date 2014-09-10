package com.ia.hibernatepersistent;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to mark a field to compose the natural key of a persistent object. Natural keys are used to calculate equals and hash code by
 * default, and can be used to make queries easily.
 * 
 * @author bruno.medeiros
 * 
 */
@Target({FIELD}) 
@Retention(RUNTIME)
public @interface NaturalKey {
	//
}
