package com.boki.realworld.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /* preHandle: 클라이언트의 요청을 컨트롤러에 전달하기 전에 호출됨
        여기서 false 를 리턴하면 다음 내용(Controller)을 실행하지 않음
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        log.info("컨트롤러 가기 전 인터셉터 거치는중");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication.getCredentials())) {
            request.setAttribute("LoginUser", authentication.getCredentials());
            request.setAttribute("OptionalUser", authentication.getCredentials());
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /* postHandle : 클라이언트의 요청을 처리한 뒤에 호출된다.
        컨트롤러에서 예외가 발생되면 실행되지 않는다.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /* afterCompletion : 클라이언트 요청을 마치고
        클라이언트에서 뷰를 통해 응답을 전송한뒤 실행이 된다.
        뷰를 생성할 때에 예외가 발생할 경우에도 실행이 된다.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}