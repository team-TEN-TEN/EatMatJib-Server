package com.tenten.eatmatjib.common.config.auth;

import static com.tenten.eatmatjib.common.exception.ErrorCode.INVALID_TOKEN_UNAUTHORIZED;
import static com.tenten.eatmatjib.common.exception.ErrorCode.LOGIN_UNAUTHORIZED;

import com.tenten.eatmatjib.common.exception.BusinessException;
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
        boolean isLong = parameter.getParameterType().equals(Long.class);

        return hasAnnotation && isLong;
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

        return jwtUtil.getMemberId(token);
    }
}
