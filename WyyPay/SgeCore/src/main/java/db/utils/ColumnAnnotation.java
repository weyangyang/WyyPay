package db.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnAnnotation {
	public String column();
	public String info() default "text";
	public String defaultValue() default DBConfig.TABLE_BEAN_DEFAULT_VALUE;
}
