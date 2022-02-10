import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;

public class Bonus2 {
	public static void main(String[] args) throws IOException {
		int[] primes = {31,37,41,43,47,53,59,61,67,71,73,79,83,89,97,101,103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199,211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293,307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397};
		ArrayList<Integer> values = new ArrayList<>();
		for (int i = 0; i < primes.length; i++) {
			for (int j = 0; j < primes.length; j++) {
				int x = primes[i] * primes[j];
				if (!values.contains(x) && x>999 && x<10000) {
					values.add(x);
				}
			}
		}
		Collections.sort(values);
		
		
		for(int i = values.size()-1;i>=0;i--) {
			//System.out.print(values.get(i)+", ");
			if(dFinder(values.get(i),"p2.rsa")) {
			break;
			}
		}
		
		
//		for(int i = 0;i<values.size();i++) {
//			if(dFinder(values.get(i),"p2.rsa"))
//				System.exit(0);
//		}

	}
public static boolean dFinder(long N,String file_path) throws IOException {
		boolean found = false;
		System.out.println("Searching for an n= "+N);
		BigInteger p = null;
		BigInteger q = null;
		int n_factor1 = 0;
		int n_factor2 = 0;
		int limit =BigInteger.valueOf(N).sqrt().intValue();
		for (int i = 2; i < limit*2; i++) {
			if(N%i == 0) {
				n_factor1 = i;
				break;
			}
		}
		for (int i = 2; i < N; i++) {
			if(n_factor1*i == N) {
				n_factor2 = i;
				break;
			}
		}
		int pMinusOne = n_factor1-1;
		int qMinusOne = n_factor2-1;
		Scanner kb = new Scanner(System.in);
		int phi = pMinusOne*qMinusOne;
		boolean search = true ; 
		int d_possible = 2;
		BigInteger e_possible = new BigInteger("2");
		BigInteger x=null;
		while(search) {
				x = BigInteger.valueOf(d_possible);
				if(d_possible>N) {
					search = false;
					break;
				}
				d_possible++;
			try {
			e_possible = x.modInverse(BigInteger.valueOf(phi));
			if(e_possible.equals(BigInteger.ZERO)) {
				continue;
			}
			}catch(Exception e) {
//				d_possible++;
				continue;
			}
			System.out.println("n_possible: "+N);
			System.out.println("im e_possible: "+e_possible);
			System.out.println("im d_possible: "+(d_possible-1));
			String decrypted = "";
			try {
			decrypted = decrypt(file_path, N, d_possible-1);
			}catch(Exception e) {
//				d_possible++;
				System.out.println("-------------------------------------------");
				continue;
			}
//			if(!(decrypted.contains("\n")&&(decrypted.contains("The")||decrypted.contains("the")))) {
//				System.out.println("-------------------------------------------");
//				continue;
//			}
			System.out.println("is the message understandable? enter 0 for yes and 1 for no ");
			int ans = kb.nextInt();
			if(ans == 1) {
//				d_possible++;
			}
			else {
				System.out.println("N = "+N);
				System.out.println("the public key is = "+e_possible);
				System.out.println("the private key is = "+(d_possible-1));
				System.out.println("data saved in .dec file. thank you for using the system!");
				search = false;
				found = true;
				return found;
			}
			System.out.println("-------------------------------------------");
		}
		return found;
	}
	public static String decrypt(String filepath, long N, int D) throws IOException {
		int d = D;
		String st = N + "";
		int nlen = st.length();
		File input = new File(filepath);
		Scanner sc = new Scanner(input);
		String encrypted_message = sc.next();
		sc.close();
		String decrypted_message = "";
		Dictionary dict_dec = dict_dec();
		int numofletters_in_block = 0;
		String s = 78 + "";
		while (Integer.parseInt(s) < N) {
			s += "78";
			numofletters_in_block++;
		}
		while (nlen <= encrypted_message.length()) {
			String cut = encrypted_message.substring(0, nlen);
			encrypted_message = encrypted_message.substring(nlen);
			BigInteger encrypted = new BigInteger(cut);
			int decrypted_value = encrypted.pow(d).mod(BigInteger.valueOf(N)).intValue();
			String dv = decrypted_value + "";
			if (dv.length() % 2 != 0) {
				dv = "0" + dv;
			}
			if (dv.length() < numofletters_in_block * 2) {
				for (int j = dv.length(); j < numofletters_in_block * 2; j++) {
					dv = "0" + dv;
				}
			}
			for (int i = 0; i < numofletters_in_block * 2; i += 2) {
				//System.out.println(dv.substring(i, i + 2));
				char res = (char) dict_dec.get(Integer.parseInt(dv.substring(i, i + 2)));
				
				decrypted_message += res;
			}

		}
		System.out.println(decrypted_message);
	String decpath = filepath.substring(0, filepath.indexOf(".") + 1) + "dec";
	File output = new File(decpath);
	FileWriter writer = new FileWriter(decpath);
	writer.write(decrypted_message);
	writer.close();
	return decrypted_message;
	//System.out.println("Message decrypted successfully and saved in \"" + decpath + "\"");
	}

	public static Dictionary dict_enc() {
		Dictionary<Character, Integer> dict = new Hashtable<Character, Integer>();
		int x = 0;
		for (int i = 65; i < 91; i++) {
			char ch = (char) i;
			dict.put(ch, x);

			x++;
		}
		int y = 26;
		for (int i = 97; i < 123; i++) {
			char ch = (char) i;
			dict.put(ch, y);

			y++;
		}
		int z = 52;
		for (int i = 48; i < 58; i++) {
			char ch = (char) i;
			dict.put(ch, z);

			z++;
		}

		char[] rest = { '.', '?', '!', ',', ';', ':', '-', '(', ')', '[', ']', '{', '}', '\'', '"', ' ', '\n' };

		int f = 62;
		for (int i = 0; i < rest.length; i++) {
			char ch = rest[i];
			dict.put(ch, f);
			f++;
		}
		return dict;
	}

	public static Dictionary dict_dec() {
		Dictionary<Integer, Character> dict = new Hashtable<Integer, Character>();
		int x = 0;
		for (int i = 65; i < 91; i++) {
			char ch = (char) i;
			dict.put(x, ch);

			x++;
		}

		int y = 26;
		for (int i = 97; i < 123; i++) {
			char ch = (char) i;
			dict.put(y, ch);

			y++;
		}
		int z = 52;
		for (int i = 48; i < 58; i++) {
			char ch = (char) i;
			dict.put(z, ch);

			z++;
		}

		char[] rest = { '.', '?', '!', ',', ';', ':', '-', '(', ')', '[', ']', '{', '}', '\'', '"', ' ', '\n' };

		int f = 62;
		for (int i = 0; i < rest.length; i++) {
			char ch = rest[i];
			dict.put(f, ch);
			f++;
		}
		return dict;
	}
}
