package com.yl.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CommonInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
//		String ua = ((HttpServletRequest) request).getHeader("user-agent").toLowerCase();
//		request.getSession();
//		if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
//		} else {
//			String Url = request.getRequestURL().toString();
//			if (!Url.contains("/Wechatpay/guideNotify")) {
//				response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":"
//						+ request.getServerPort() + "/ylwx/comm/redirect");
//			}
//		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
