/*
 * SPDX-License-Identifier: EUPL-1.2
 * 
 * (C) Copyright 2019 - 2021 Regione Piemonte
 * 
 */
package org.csi.yucca.adminapi.conf;

import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import javax.servlet.Filter;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { AppConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/*" };
	}

//    @Override
//    protected Filter[] getServletFilters() {
//          /*
//        If the JwtTokenAuthenticationFilter was diretly used as a ServletFilter, then only this filter would be applied.
//        In this case, chained filters managed by Spring Security (ExceptionTranslationFilter, SessionManagementFilter et FilterSecurityInterceptor, etc.)
//        wouldn't be applied. As such, URL filtering wouln't be secured as expected by the configuration).
//
//        We need to specify the springSecurityFilterChain as the initial Servlet filter. This proxy takes care of chaining filter calls as they
//         are indicated in the WebSecurityConfiguration class.
//        **/
//        return new Filter[]{ new DelegatingFilterProxy("springSecurityFilterChain") };
//    }
	
}