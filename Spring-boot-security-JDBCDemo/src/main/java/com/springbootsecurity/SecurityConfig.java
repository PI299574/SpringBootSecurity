package com.springbootsecurity;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		// super.configure(auth);
		/*
		 * auth.jdbcAuthentication().dataSource(dataSource).withDefaultSchema()
		 * .withUser(User.withUsername("user").password("{noop}user").roles("USER"))
		 * .withUser(User.withUsername("admin").password("{noop}admin").roles("ADMIN"));
		 */

		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select username,password,enabled " + "from users " + "where username =?")
				.authoritiesByUsernameQuery("select username,authority " + "from authorities " + "where username =?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		// super.configure(http);

		http.authorizeRequests().antMatchers("/admin").hasRole("ADMIN").antMatchers("/user").hasAnyRole("USER", "ADMIN")
				.antMatchers("/").permitAll().and().formLogin();
	}

	@Bean
	public PasswordEncoder getPassworEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}
