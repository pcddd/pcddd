package com.beimi.config.web;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beimi.util.MD5Utils;
import com.beimi.web.model.Token;
import com.beimi.web.service.repository.es.TokenESRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.beimi.core.BMDataContext;
import com.beimi.util.cache.CacheHelper;

public class ApiRequestMatchingFilter implements Filter {
    private RequestMatcher[] ignoredRequests;
    private RequestMatcher[] exRequests;

	@Autowired
	private TokenESRepository tokenESRes;

    @Value("${pc.app.key}")
    private String appKey;

    @Value("${pc.app.secrt}")
    private String appSecrt;

    public ApiRequestMatchingFilter(RequestMatcher[] exRequests , RequestMatcher... matcher) {
    	this.exRequests = exRequests ;
        this.ignoredRequests = matcher;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
         HttpServletRequest request = (HttpServletRequest) req;
         boolean matchAnyRoles = false ;
         for(RequestMatcher anyRequest : ignoredRequests ){
        	 if(anyRequest.matches(request)){
        		 matchAnyRoles = true ;
        	 }
         }
         if(exRequests!=null){
	         for(RequestMatcher anyRequest : exRequests ){
	        	 if(anyRequest.matches(request)){
	        		 matchAnyRoles = false ;
	        		 break;
	        	 }
	         }
         }
         if (matchAnyRoles){
         	//不需要验证token
			 String sign = request.getHeader("sign");
			 if (StringUtils.isNotEmpty(sign)){
				 String csign = verifySign(request);
				 if (StringUtils.isNotEmpty(csign) && csign.equals(sign)){
					 chain.doFilter(req,resp);
				 }
			 }
		 }else{
			 String token = request.getHeader("token");
			 Token userToken = null;
			 if (!StringUtils.isBlank(token)){
				 userToken = tokenESRes.findById(token);
				 if (userToken != null && !org.apache.commons.lang.StringUtils.isBlank(userToken.getId()) && userToken.getExptime() != null && userToken.getExptime().after(new Date())) {
					 String sign = request.getHeader("sign");
					 if (StringUtils.isNotEmpty(sign)){
					     String csign = verifySign(request);
					     if (StringUtils.isNotEmpty(csign) && csign.equals(sign)){
                             chain.doFilter(req,resp);
                         }
					 }
				 }else{
					 //token无效
					 HttpServletResponse response = (HttpServletResponse) resp ;
					 response.sendRedirect("/api/token");
				 }
			 }
		 }
    }

	@Override
	public void destroy() {
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

	private String verifySign(HttpServletRequest request){
        StringBuilder sb = new StringBuilder(appKey);
		Enumeration em = request.getParameterNames();
		while (em.hasMoreElements()) {
			String name = (String) em.nextElement();
			String value = request.getParameter(name);
            sb.append(",").append(value);
		}
        String sign = sb.toString();

        try {
            sign = MD5Utils.BASE64Encode(MD5Utils.aesEncrypt(sign, appSecrt));
            sign = sign.replaceAll("\r\n", "");
            sign = MD5Utils.getMD5String(sign);
            sign = MD5Utils.getMD5String(sign);
            return sign;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
}