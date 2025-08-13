package com.materialsystem.web;

import com.materialsystem.entity.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Set;

public class AuthFilter implements Filter {

    // caminhos públicos (sem login)
    private static final Set<String> PUBLIC_PATHS = Set.of(
        "/login", "/welcome", "/logout", "/styleLogin.css"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String ctx  = req.getContextPath();
        String path = req.getServletPath();

        // permitir imagens e recursos estáticos
        if (path.startsWith("/image/") || path.startsWith("/static/") || PUBLIC_PATHS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession s = req.getSession(false);
        Usuario u = (s != null) ? (Usuario) s.getAttribute("usuarioLogado") : null;

        if (u == null) {
            String next = ctx + path + (req.getQueryString() != null ? "?" + req.getQueryString() : "");
            resp.sendRedirect(ctx + "/login?next=" + urlEncode(next));
            return;
        }

        chain.doFilter(request, response);
    }

    private static String urlEncode(String s){
        try { return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8.name()); }
        catch (Exception e) { return ""; }
    }
}
