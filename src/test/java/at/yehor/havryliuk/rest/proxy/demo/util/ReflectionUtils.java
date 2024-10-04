package at.yehor.havryliuk.rest.proxy.demo.util;

import java.lang.reflect.Field;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectionUtils {

    public static void setField(Object object, String fieldName, Object fieldValue) {
        Class<?> clazz = object.getClass();
        Field field = null;
        while (clazz != null && field == null) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }

        if (field == null) {
            throw new IllegalArgumentException(fieldName + " not found");
        }

        try {
            field.setAccessible(true);
            field.set(object, fieldValue);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
