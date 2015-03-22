package ee.idu.vc.auth;

import ee.idu.vc.model.AccountStatus;
import ee.idu.vc.model.UserType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAuth {
    String[] userTypes() default {UserType.COMPANY};
    String[] accountStatuses() default {AccountStatus.ACTIVE, AccountStatus.INACTIVE};
}