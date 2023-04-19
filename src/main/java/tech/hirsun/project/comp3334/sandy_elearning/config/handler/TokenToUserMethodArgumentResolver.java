package tech.hirsun.project.comp3334.sandy_elearning.config.handler;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.WebUtils;
import tech.hirsun.project.comp3334.sandy_elearning.config.annotation.TokenToUser;
import tech.hirsun.project.comp3334.sandy_elearning.entity.GeneralUser;
import tech.hirsun.project.comp3334.sandy_elearning.service.GeneralUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.IOException;

@Component
public class TokenToUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private GeneralUserService generalUserService;

    public TokenToUserMethodArgumentResolver() {
    }

    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(TokenToUser.class)) {
            return true;
        }
        return false;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (parameter.getParameterAnnotation(TokenToUser.class) instanceof TokenToUser) {
            GeneralUser generalUser = null;
            HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
            Cookie tokenCookie = WebUtils.getCookie(request, "token");
            String token = tokenCookie != null ? tokenCookie.getValue() : null;
            if (null != token && !"".equals(token) && token.length() == 32) {
                generalUser = generalUserService.queryByToken(token);
            }
            return generalUser;
        }
        return null;
    }

    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

}