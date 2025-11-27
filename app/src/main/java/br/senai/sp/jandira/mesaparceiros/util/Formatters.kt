package br.senai.sp.jandira.mesaparceiros.util

object Formatters {
    // Formata o telefone no padrão (XX) XXXXX-XXXX
    fun formatPhoneNumber(phone: String): String {
        val digits = phone.filter { it.isDigit() }
        return when {
            digits.length <= 2 -> digits
            digits.length <= 7 -> "(${digits.take(2)}) ${digits.drop(2)}"
            else -> "(${digits.take(2)}) ${digits.drop(2).take(5)}-${digits.drop(7).take(4)}"
        }
    }

    // Formata o CNPJ no padrão XX.XXX.XXX/XXXX-XX
    fun formatCnpj(cnpj: String): String {
        val digits = cnpj.filter { it.isDigit() }
        return when {
            digits.length <= 2 -> digits
            digits.length <= 5 -> "${digits.take(2)}.${digits.drop(2)}"
            digits.length <= 8 -> "${digits.take(2)}.${digits.drop(2).take(3)}.${digits.drop(5)}"
            digits.length <= 12 -> "${digits.take(2)}.${digits.drop(2).take(3)}.${digits.drop(5).take(3)}/${digits.drop(8)}"
            else -> "${digits.take(2)}.${digits.drop(2).take(3)}.${digits.drop(5).take(3)}/${digits.drop(8).take(4)}-${digits.drop(12).take(2)}"
        }
    }

    // Valida a força da senha
    data class PasswordValidationResult(
        val isValid: Boolean,
        val hasMinLength: Boolean = false,
        val hasUppercase: Boolean = false,
        val hasSpecialChar: Boolean = false
    )

    fun validatePassword(password: String): PasswordValidationResult {
        val hasMinLength = password.length >= 10
        val hasUppercase = password.any { it.isUpperCase() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() && !it.isWhitespace() }
        
        return PasswordValidationResult(
            isValid = hasMinLength && hasUppercase && hasSpecialChar,
            hasMinLength = hasMinLength,
            hasUppercase = hasUppercase,
            hasSpecialChar = hasSpecialChar
        )
    }
}
