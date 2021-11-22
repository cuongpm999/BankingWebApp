package vn.ptit.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired private UserDetailsService userDetailsService;

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable()
        .authorizeRequests()  
        .antMatchers("/admin").hasAnyAuthority("TELLER", "MANAGER")
        .antMatchers("/admin/*").hasAnyAuthority("TELLER", "MANAGER") 
        .antMatchers("/admin/transaction/*").hasAnyAuthority("TELLER", "MANAGER")   
        .antMatchers("/admin/manage/**").hasAuthority("MANAGER")
        
        .and()
        .formLogin()
        .loginPage("/login")
        .and()
        .logout()
//        .logoutUrl("/logout")
        .logoutSuccessUrl("/login")
        .invalidateHttpSession(true)
        .clearAuthentication(true);
//        .and()
//        .httpBasic()
//        .and()
//        .exceptionHandling().accessDeniedPage("/error-403");
	}

	@Bean
    public PasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
    }

	@Autowired public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
}
