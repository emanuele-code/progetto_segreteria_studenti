package Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe di utilità per l'hashing delle password usando l'algoritmo SHA-256.
 * Fornisce un metodo statico per ottenere l'hash esadecimale di una stringa password.
 *
 * @author Emanuele
 * @version 1.0
 */
public class UtilHash {

    /**
     * Genera l'hash SHA-256 della password passata come parametro.
     *
     * @param password la password in chiaro da hashare
     * @return una stringa esadecimale che rappresenta l'hash SHA-256 della password
     * @throws RuntimeException se l'algoritmo SHA-256 non è supportato
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errore durante la generazione dell'hash", e);
        }
    }

    /**
     * Converte un array di byte in una stringa esadecimale.
     *
     * @param hash array di byte da convertire
     * @return stringa esadecimale corrispondente ai byte passati
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
