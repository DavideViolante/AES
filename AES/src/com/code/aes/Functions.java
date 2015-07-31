package com.code.aes;

/**
 * Classe contenente operazioni non inerenti con l'AES ma
 * necessarie per alcune operazioni matematiche e logiche.
 * @author Davide Violante
 *
 */
public class Functions {

	/**
	 * Metodo che converte un array di byte in una stringa.
	 * @param bytes - array di byte
	 * @return stringa esadecimale
	 */
	public String bytes2hexString(byte[] bytes) {
	    StringBuilder sb = new StringBuilder();
	    for (byte b : bytes) {
	        sb.append(String.format("%02X", b));
	    }
	    return sb.toString();
	}
	
	/**
	 * Metodo che converte una stringa espressa in esadecimale in un array di byte.
	 * @param str - stringa espressa in esadecimale
	 * @return array di byte
	 */
	public byte[] hexString2bytes(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	/**
	 * Metodo che calcola lo XOR bit a bit tra 2 array di byte
	 * @param a1 - 1° array di byte
	 * @param a2 - 2° array di byte
	 * @return array di byte risultato
	 */
	public byte[] xorArrays(byte[] a1, byte[] a2) {
		byte[] result = new byte[a1.length];
		for (int i = 0; i < a1.length; i++)
			result[i] = (byte) (a1[i] ^ a2[i]); // ^ è l'operatore XOR in Java
		return result;
	}
	
	/**
	 * Metodo che implementa la moltiplicazione nel Campo di Galois.
	 * http://en.wikipedia.org/wiki/Finite_field_arithmetic#Multiplication
	 * http://www.cs.utsa.edu/~wagner/laws/FFM.html
	 * @param a - byte
	 * @param b - byte
	 * @return risultato moltiplicazione
	 */
	public byte FiniteFieldMult(byte a, byte b) {
		byte r = 0, t;
		while (a != 0) {
			if ((a & 1) != 0)
				r = (byte)(r ^ b);
			t = (byte) (b & 0x80);
			b = (byte) (b << 1);
			if (t != 0)
				b = (byte) (b ^ 0x1b);
			a = (byte) (a >> 1);
		}
		return r;
	}
	
}

