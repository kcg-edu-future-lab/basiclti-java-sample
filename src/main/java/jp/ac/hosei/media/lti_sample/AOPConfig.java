package jp.ac.hosei.media.lti_sample;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.imsglobal.aspect.LtiKeySecretService;
import org.imsglobal.aspect.LtiLaunchVerifier;
import org.imsglobal.lti.launch.LtiOauthVerifier;
import org.imsglobal.lti.launch.LtiVerificationException;
import org.imsglobal.lti.launch.LtiVerificationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AOPConfig {
	@Autowired
	private LtiKeySecretService keyService;
	
	@Bean
	public LtiLaunchVerifier ltiLaunchVerifier() {
		return new LtiLaunchVerifier(keyService, new LtiOauthVerifier(){
			@Override
			public LtiVerificationResult verify(HttpServletRequest request, String secret)
			throws LtiVerificationException {
				if(request.getHeader("X-Forwarded-Proto").equals("https") &&
					request.getRequestURL().subSequence(0, 5).equals("http:")){
					request = new HttpServletRequestWrapper(request){
						@Override
						public StringBuffer getRequestURL() {
							return super.getRequestURL().replace(0, 4, "https");
						}
					};
				}
				return super.verify(request, secret);
			}
		});
	}
}
