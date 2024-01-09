package org.example.data.mapper;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class FieldsUpdaterMapper {
    public <T> T updateFields(T target, T source) {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object sourceValue = field.get(source);
                Object targetValue = field.get(target);

                if (sourceValue != null && !sourceValue.equals(targetValue)) {

                    if (sourceValue.equals("")) {
                        field.set(target, targetValue);
                    } else {
                        field.set(target, sourceValue);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return target;
    }
}
