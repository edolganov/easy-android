package easydroid.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.app.Activity;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnActivityResult {
	
	int resultCode() default Activity.RESULT_OK;
	
	int requestCode();

}
