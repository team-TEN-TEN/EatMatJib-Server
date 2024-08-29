package com.tenten.eatmatjib.common.config.auth;

import static com.tenten.eatmatjib.common.exception.ErrorCode.*;

import com.tenten.eatmatjib.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthUserResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(AuthUser.class);
        boolean isString = parameter.getParameterType().equals(String.class);

        return hasAnnotation && isString;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String bearerToken = webRequest.getHeader("Authorization");

        if (bearerToken == null || bearerToken.isEmpty()) {
            throw new BusinessException(LOGIN_UNAUTHORIZED);
        }

        String token = bearerToken.substring(7);
        if (!jwtUtil.verifyToken(token)) {
            throw new BusinessException(INVALID_TOKEN_UNAUTHORIZED);
        }

        String memberId = jwtUtil.getMemberId(token);

        return memberId;
    }
}
