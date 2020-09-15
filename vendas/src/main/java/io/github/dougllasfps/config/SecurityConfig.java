package io.github.dougllasfps.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.dougllasfps.security.jwt.JwtAuthFilter;
import io.github.dougllasfps.security.jwt.JwtService;
import io.github.dougllasfps.service.impl.UsuarioServiceImpl;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UsuarioServiceImpl usuarioService;
	
    @Autowired
    private JwtService jwtService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, usuarioService);
    }

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService).passwordEncoder(passwordEncoder());
	}

    /**
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.passwordEncoder(passwordEncoder())
			.withUser("fulano")
			.password(passwordEncoder().encode("123"))
			.roles("USER", "ADMIN");
	}
	*/

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() // conf de seguranca entre app web e backend / nao precisa pois trabalharemos com stateless
			.authorizeRequests()
				.antMatchers("/api/clientes/**").hasAnyRole("USER", "ADMIN")
				.antMatchers("/api/pedidos/**").hasAnyRole("USER", "ADMIN")
				.antMatchers("/api/produtos/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/api/usuarios/**").permitAll()
				.anyRequest().authenticated()
			.and()
				// .formLogin()
				// .formLogin("/meu-login.html")
				// .httpBasic(); // guarda info de sessao
	            .sessionManagement()
		            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // cada requisicao guarda informacoes para executar a mesma
	        .and()
	        	// executa antes do UsernamePasswordAuthenticationFilter
				.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class); 
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}