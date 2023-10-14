package br.com.thieres.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.thieres.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter{

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain

    ) throws ServletException, IOException {

        var servletPath = request.getServletPath();
        if( servletPath.startsWith("/tasks/") )
        {

            // Get user authentication ( username and password )
            var authorization = request.getHeader("Authorization");
            var authEncoded = authorization.substring("Basic".length()).trim();

            byte[] authDecode = Base64.getDecoder().decode(authEncoded);
            String[] credentials = new String(authDecode).split(":");

            String Username = credentials[0];
            String Password = credentials[1];
            
            // User validation
            var user = this.userRepository.findByUsername(Username);
            if(user == null)
            {
                response.sendError(401);
            }
            else
            {
                // Password Validation
                var passwordVerify = BCrypt.verifyer().verify(Password.toCharArray(), user.getPassword());
                if(passwordVerify.verified){
                    request.setAttribute("codUser", user.getCodUser());
                    filterChain.doFilter(request, response);
                }
                else
                {
                    response.sendError(401);
                }
            }

        }
        else
        {
            filterChain.doFilter(request, response);
        }

    }

    
}
