package com.code.aes;

import com.code.aes.Functions;
import com.code.aes.Tables;

/**
 * I nomi delle variabili, i nomi dei metodi e i commenti al codice sono per la maggior
 * parte basati sul documento ufficiale dello standard (fips-197.pdf) pubblicato dalla
 * Federal Information Processing Standards Publications (FIPS PUBS) e rilasciato dal
 * National Institute of Standards and Technology (NIST) nel novembre del 2001.
 * http://csrc.nist.gov/publications/fips/fips197/fips-197.pdf
 * 
 * L'algoritmo originale è stato ideato da Joan Daemen e Vincent Rijmen
 * http://en.wikipedia.org/wiki/Advanced_Encryption_Standard
 * 
 * @author Davide Violante
 * 
 */

public class AES {
	
	// Numero di colonne delle matrici State. In questo standard: 4 (sempre)
	protected int Nb = 4;
	
	/* Le seguenti 2 variabili assumeranno nell'algoritmo i valori in
	 * tabella, in base alla lunghezza della chiave in ingresso.
	 * [vedi fips-197 paragrafo 5]
	 * 	Key 	| Nk   Nr
	 * 	128bit 	| 4    10
	 * 	192bit 	| 6    12
	 * 	256bit 	| 8	   14
	 */
	
	protected int Nk; // Numero delle word della chiper-key
	protected int Nr; // Numero di round
	
	// Per richiamare tutte le funzioni di supporto non inerenti con l'AES
	Functions f = new Functions();
	
	// Classe contenente le look-up tables (Sbox, inverted-Sbox, Rcon)
	Tables table = new Tables();

	
	/**
	 * Metodo che aggiunge la round key allo State con l'operazione di
	 * XOR bit a bit. Ogni round key consiste in Nb parole del key schedule.
	 * Ognuna di queste Nb parole è aggiunta nelle colonne dello State.
	 * La sua inversa è se stessa in quanto consiste solo in degli XOR.
	 * [vedi fips-197 paragrafo 5.1.4 e 5.3.4]
	 * @param state - matrice State
	 * @param w - word
	 * @param round - round corrente
	 */
	public void AddRoundKey(byte[][] state, byte[][] w, int round) {
		for (int c = 0; c < Nb; c++) {
			for (int r = 0; r < 4; r++)
				state[r][c] = (byte) (state[r][c] ^ w[round * Nb + c][r]);
		}
	}
	
	/**
	 * Metodo che copia la sequenza di byte in input nella matrice State
	 * [vedi fips-197 paragrafo 3.4]
	 * @param in - array di byte in input
	 * @return matrice State
	 */
	public byte[][] in2state(byte[] in) {
		byte[][] state = new byte[4][Nb];
		for (int r = 0; r < 4; r++)
			for(int c = 0; c < Nb; c++)
				state[r][c] = in[r + 4 * c];
		return state;
	}
	
	/**
	 * Metodo che copia la matrice State in una sequenza di byte in output
	 * [vedi fips-197 paragrafo 3.4]
	 * @param state - matrice State
	 * @return sequenza di output
	 */
	public byte[] state2out(byte[][] state) {
		byte[] out = new byte[16];
		for (int r = 0; r < 4; r++)
			for(int c = 0; c < Nb; c++)
				out[r + 4 * c] = state[r][c];
		return out;
	}
	
	/**
	 * Metodo che prende la chiper key ed esegue la key expansion
	 * per generare la key schedule.
	 * [vedi fips-197 paragrafo 5.2 e fig 11]
	 * @param key - chiper key
	 * @param Nk 
	 * @return key schedule, word di 4 byte
	 */
	public byte[][] KeyExpansion(byte[] key) {
		// il valore di Nk e Nr dipendono dalla lunghezza della chiave!
		Nk = key.length / Nb;
		Nr = Nk + 6;
		
		byte[][] word = new byte[Nb*(Nr+1)][4];
		int i;
		
		for (i = 0; i < Nk; i++) {
			word[i][0] = key[4*i];
			word[i][1] = key[4*i+1];
			word[i][2] = key[4*i+2];
			word[i][3] = key[4*i+3];
		}
		i = Nk;
		while (i < Nb*(Nr+1)) {
			byte[] temp = word[i - 1];
			if (i % Nk == 0) {
				temp = SubWord(RotWord(temp));
				temp[0] ^= table.Rcon[i/Nk];
			// se la key è da 256bit
			} else if (Nk > 6 && i % Nk == 4) {
				temp = SubWord(temp);
			}
			word[i] = f.xorArrays(word[i-Nk], temp);
			i = i + 1;
		}
		return word;
	}

	/**
	 * Metodo che prende una word di 4 byte e applica la S-box
	 * ad ognuno dei byte per produrre una word in output.
	 * [vedi fips-197 paragrafo 5.2]
	 * @param word - word di 4 byte
	 * @return word alterata
	 */
	public byte[] SubWord(byte[] word) {
		byte[] newWord = new byte[word.length];
		for (int i = 0; i < newWord.length; i++)
			newWord[i] = (byte) (table.Sbox[word[i] & 0xff]);
		return newWord;
	}

	/**
	 * Metodo che prende una word di 4 byte e ne esegue una
	 * permutazione ciclica. Eg: 0 1 2 3 -> 1 2 3 0 
	 * [vedi fips-197 paragrafo 5.2]
	 * @param word - word di 4 byte
	 * @return word alterata
	 */
	public byte[] RotWord(byte[] word) {
		byte[] newWord = new byte[word.length];
		newWord[0] = word[1];
		newWord[1] = word[2];
		newWord[2] = word[3];
		newWord[3] = word[0];
		return newWord;
	}
		
}
