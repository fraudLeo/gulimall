package com.leo.common.valid;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = {ListValueConstraintValidator.class})
@Target({METHOD,FIELD,ANNOTATION_TYPE,CONSTRUCTOR,PARAMETER,TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)

public @interface ListValue {
    String message() default "{com.leo.common.valid.ListValue.message}";
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
    int[] vals() default {};
}
