package com.materialsystem.util;

public final class Roles {
    public static final String GERENTE   = "Gerente";
    public static final String VENDEDOR  = "Vendedor";
    public static final String COMPRADOR = "Comprador";
    public static final String CAIXA     = "Caixa";

    private Roles() {}

    public static boolean is(String papel, String role) {
        return papel != null && role != null && papel.equalsIgnoreCase(role);
    }
    public static boolean any(String papel, String... roles) {
        if (papel == null || roles == null) return false;
        for (String r : roles) if (is(papel, r)) return true;
        return false;
    }
}
