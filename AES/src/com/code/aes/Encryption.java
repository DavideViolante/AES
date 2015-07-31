package com.code.aes;

import com.code.aes.AES;

/**
 * Classe contenente tutte le operazioni di cifratura.
 * @author Davide Violante
 *
 */
public class Encryption extends AES {
		
	/**
	 * Metodo che consiste in una sostituzione non lineare di byte che opera
	 * indipendentemente su ogni byte della matrice State facendo uso della S-Box.
	 * [vedi fips-197 paragrafo 5.1.1]
	 * @param state - matrice State
	 */
	public void SubBytes(byte[][] state) {
		for (int r = 0; r < 4; r++)
			for (int c = 0; c < Nb; c++)
				state[r][c] = (byte) (table.Sbox[(state[r][c]) & 0xff]);
	}
	
	/**
	 * Metodo che shifta circolarmente su un numero differente di byte (offsets)
	 * le ultime tre righe della matrice State. La prima non viene alterata.
	 * L'offset corrisponde al numero di riga della matrice. Eg:
	 * 0 1 2 3	->	0 1 2 3
	 * 0 1 2 3	->	1 2 3 0
	 * 0 1 2 3	->	2 3 0 1
	 * 0 1 2 3	->	3 0 1 2
	 * [vedi fips-197 paragrafo 5.1.2]
	 * @param state - matrice State
	 */
	public void ShiftRows(byte[][] state) {
		byte[] tmp = new byte[4];
		// la prima riga non si altera, quindi r=1
		for (int r = 1; r < 4; r++) {
			for (int c = 0; c < Nb; c++)
				tmp[c] = state[r][(c + r) % Nb];
			for (int c = 0; c < Nb; c++)
				state[r][c] = tmp[c];
		}
	}

	/**
	 * Metodo che prende i quattro byte di ogni colonna e li combina
	 * utilizzando una trasformazione lineare invertibile.
	 * [vedi fips-197 paragrafo 5.1.3]
	 * @param state - matrice State
	 */
	public void MixColumns(byte[][] state) {
		int[] s = new int[4];
		byte hex02 = 0x02;
		byte hex03 = 0x03;
		for (int c = 0; c < Nb; c++) {
			s[0] = f.FiniteFieldMult(hex02, state[0][c]) ^ f.FiniteFieldMult(hex03, state[1][c]) ^ state[2][c] ^ state[3][c];
			s[1] = state[0][c] ^ f.FiniteFieldMult(hex02, state[1][c]) ^ f.FiniteFieldMult(hex03, state[2][c]) ^ state[3][c];
			s[2] = state[0][c] ^ state[1][c] ^ f.FiniteFieldMult(hex02, state[2][c]) ^ f.FiniteFieldMult(hex03, state[3][c]);
			s[3] = f.FiniteFieldMult(hex03, state[0][c]) ^ state[1][c] ^ state[2][c] ^ f.FiniteFieldMult(hex02, state[3][c]);
			for (int i = 0; i < Nb; i++)
				state[i][c] = (byte) s[i];
		}
	}
	
	/**
	 * Metodo principale per l'encryption.
	 * [vedi fips-197 paragrafo 5.1 e figura 5]
	 * @param in - sequenza in input da cifrare
	 * @param w - key schedule
	 * @return sequenza di input cifrata
	 */
	public byte[] Cipher(byte[] in, byte[] key) {
		byte[][] state = in2state(in);
		
		byte[][] w = KeyExpansion(key);
		
		AddRoundKey(state, w, 0);
		for (int round = 1; round < Nr; round++) {
			SubBytes(state);
			ShiftRows(state);
			MixColumns(state);
			AddRoundKey(state, w, round);
		}
		SubBytes(state);
		ShiftRows(state);
		AddRoundKey(state, w, Nr);
		
		return state2out(state);
	}
	
	/**
	 * Metodo che verifica di quanti blocchi è composto l'input, 
	 * poi manda i corretti blocchi al Cipher
	 * @param in - input
	 * @param key - key in input
	 * @return blocchi cifrati
	 */
	@SuppressLint("NewApi")
	public byte[] CipherBlock(byte[] in, byte[] key) {
		 int len = in.length;
		 int numBlocchi = len/16;
		 byte[] total = new byte[16*numBlocchi];
		 
		 // check sull'input
		 if(!(len % 16 == 0)) {
			 System.err.println("L'input non è valido.\nDeve essere almeno di 128bit o un suo multiplo.");
		 } else if(len % 16 == 0) {
			 for(int i = 0, j = 0; i < numBlocchi; i++, j = j + 16) {
				 // elaboro l'i-esimo blocco
				 byte[] inPart = Arrays.copyOfRange(in, j, 16 + j);
				 byte[] inPartC = Cipher(inPart, key);
				 // copio blocco a blocco nel totale
				 System.arraycopy(inPartC, 0, total, j, 16);
			 }
		 }
		 return total;
	 }
}

