package com.code.aes;

import com.code.aes.AES;

/**
 * Classe contenente tutte le operazioni di decrifratura.
 * @author Davide Violante
 *
 */
public class Decryption extends AES {
	
	/**
	 * Metodo inverso di SubBytes.
	 * [vedi fips-197 paragrafo 5.3.2]
	 * @param state
	 */
	public void InvSubBytes(byte[][] state) {
		for (int r = 0; r < 4; r++)
			for (int c = 0; c < Nb; c++)
				state[r][c] = (byte) (table.invertedSbox[(state[r][c] & 0xff)]);
	}
	
	/**
	 * Metodo inverso di ShiftRows.
	 * [vedi fips-197 paragrafo 5.3.1]
	 * @param state
	 */
	public void InvShiftRows(byte[][] state) {
		byte[] s = new byte[4];
		// la prima riga non si altera, quindi r=1
		for (int r = 1; r < 4; r++) {
			for (int c = 0; c < Nb; c++)
				s[(c + r) % Nb] = state[r][c]; // lo shift dipende dalla riga r
			for (int c = 0; c < Nb; c++)
				state[r][c] = s[c];
		}
	}
	
	/**
	 * Metodo inverso di MixColumns.
	 * [vedi fips-197 paragrafo 5.3.3]
	 * @param state
	 */
	public void InvMixColumns(byte[][] state) {
		int[] s = new int[4];
		byte hex0E = 0x0E;
		byte hex0B = 0x0B;
		byte hex0D = 0x0D;
		byte hex09 = 0x09;
		for (int c = 0; c < 4; c++) {
			s[0] = f.FiniteFieldMult(hex0E, state[0][c]) ^ f.FiniteFieldMult(hex0B, state[1][c])
				 ^ f.FiniteFieldMult(hex0D, state[2][c]) ^ f.FiniteFieldMult(hex09, state[3][c]);
			s[1] = f.FiniteFieldMult(hex09, state[0][c]) ^ f.FiniteFieldMult(hex0E, state[1][c])
				 ^ f.FiniteFieldMult(hex0B, state[2][c]) ^ f.FiniteFieldMult(hex0D, state[3][c]);
			s[2] = f.FiniteFieldMult(hex0D, state[0][c]) ^ f.FiniteFieldMult(hex09, state[1][c])
				 ^ f.FiniteFieldMult(hex0E, state[2][c]) ^ f.FiniteFieldMult(hex0B, state[3][c]);
			s[3] = f.FiniteFieldMult(hex0B, state[0][c]) ^ f.FiniteFieldMult(hex0D, state[1][c])
				 ^ f.FiniteFieldMult(hex09, state[2][c]) ^ f.FiniteFieldMult(hex0E, state[3][c]);
			for (int i = 0; i < 4; i++)
				state[i][c] = (byte) (s[i]);
		}
	}
	
	/**
	 * Metodo principale di decryption.
	 * [vedi fips-197 paragrafo 5.3 e figura 12]
	 * @param in - chipertext da decifrare
	 * @param w - key schedule
	 * @return sequenza decifrata
	 */
	public byte[] InvCipher(byte[] in,  byte[] key) {
		byte[][] state = in2state(in);
		
		byte[][] w = KeyExpansion(key);
		
		AddRoundKey(state, w, Nr);
		for (int round = Nr-1; round >= 1; round--) {
			InvShiftRows(state);
			InvSubBytes(state);
			AddRoundKey(state, w, round);
			InvMixColumns(state);
		}
		InvShiftRows(state);
		InvSubBytes(state);
		AddRoundKey(state, w, 0);

		return state2out(state);
	}
	
	/**
	 * Metodo che divide i blocchi e li passa all'InvCipher
	 * @param in - testo da decifrare
	 * @param key - chiave usata
	 * @return testo decifrato
	 */
	@SuppressLint("NewApi")
	public byte[] InvCipherBlock(byte[] in, byte[] key) {
		 int len = in.length;
		 int numBlocchi = len/16;
		 byte[] total = new byte[16*numBlocchi];
		 
		 for(int i = 0, j = 0; i < numBlocchi; i++, j = j + 16) {
			 // elaboro l'i-esimo blocco
			 byte[] inPart = Arrays.copyOfRange(in, j, 16 + j);
			 byte[] inPartC = InvCipher(inPart, key);
			 // copio blocco a blocco nel totale
			 System.arraycopy(inPartC, 0, total, j, 16);
		 }
		 return total;
	 }
}
