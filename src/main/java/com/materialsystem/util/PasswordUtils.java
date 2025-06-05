package com.materialsystem.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // Regras de validação
    private static final Pattern UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern DIGIT = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL = Pattern.compile("[@#$%^&+=!.*()_\\-]");

    public static List<String> validarForcaSenha(String senha) {
        List<String> erros = new ArrayList<>();

        if (senha.length() < 8)
            erros.add("A senha deve ter no mínimo 8 caracteres.");

        if (!UPPERCASE.matcher(senha).find())
            erros.add("A senha deve conter pelo menos uma letra MAIÚSCULA.");

        if (!LOWERCASE.matcher(senha).find())
            erros.add("A senha deve conter pelo menos uma letra minúscula.");

        if (!DIGIT.matcher(senha).find())
            erros.add("A senha deve conter pelo menos um número.");

        if (!SPECIAL.matcher(senha).find())
            erros.add("A senha deve conter pelo menos um caractere especial.");

        return erros;
    }

    public static String gerarHashSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public static boolean verificarSenha(String senhaDigitada, String hashSalvo) {
        return BCrypt.checkpw(senhaDigitada, hashSalvo);
    }
}
