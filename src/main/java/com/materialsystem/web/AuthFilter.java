package com.materialsystem.web;

import com.materialsystem.entity.Usuario;
import com.materialsystem.util.Roles;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Set;

public class AuthFilter implements Filter {

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

        // Recursos públicos/estáticos
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

        // Autorização por rota (grosseira). A lógica fina fica nos servlets.
        String papel = u.getPapel();

        boolean allowed = false;
        if (Roles.is(papel, Roles.GERENTE)) {
            allowed = true; // tudo liberado
        } else if (Roles.is(papel, Roles.VENDEDOR)) {
            allowed = Set.of("/home", "/venda", "/comprador", "/usuario", "/produto").contains(path);
        } else if (Roles.is(papel, Roles.CAIXA)) {
            allowed = Set.of("/home", "/venda", "/produto").contains(path);
        } else if (Roles.is(papel, Roles.COMPRADOR)) {
            allowed = Set.of("/home", "/produto", "/venda").contains(path);
        }

        if (!allowed) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso não permitido para seu papel.");
            return;
        }

        chain.doFilter(request, response);
    }

    private static String urlEncode(String s){
        try { return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8.name()); }
        catch (Exception e) { return ""; }
    }
}
