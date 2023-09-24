package com.sns.yourconnection.utils.loader;

import com.sns.yourconnection.model.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

@Slf4j
public class ClassUtil {

    public static <T> T castingInstance(Object o, Class<T> clazz) {
        return clazz != null && clazz.isInstance(o) ? clazz.cast(o) : null;
    }

//    public static User getUserFromAuthentication(Authentication authentication) {
//        return castingInstance(authentication.getPrincipal(), User.class);
//    }
}
