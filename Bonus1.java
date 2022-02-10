import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;

public class Bonus1 {
	public static void main(String[] args) throws IOException {
		dFinder(797527);
	}

	public static void dFinder(long N) throws IOException {
		
		System.out.println("Searching for an n= "+N);
		BigInteger p = null;
		BigInteger q = null;
	
		int n_factor1 = 0;
		int n_factor2 = 0;
		int limit =BigInteger.valueOf(N).sqrt().intValue();
		//System.out.println(limit);
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

		
		
		
		Scanner kn = new Scanner(System.in);
		System.out.println("please Enter the file path for the .rsa file: ");
		String file_path = kn.next();
		
		int phi = pMinusOne*qMinusOne;
		
		boolean search = true ; 
		int e_possible = 2;
		//System.out.println(n_factor1+" , "+n_factor2);
		//System.out.println("im e_possible first: "+e_possible);
		BigInteger d_possible = new BigInteger("2");
		BigInteger x=null;
		while(search) {
			
			while(true) {
				x = BigInteger.valueOf(e_possible);
				if(x.isProbablePrime(5)&&x.gcd(BigInteger.valueOf(phi)).equals(BigInteger.ONE)&& x.gcd(BigInteger.valueOf(N)).equals(BigInteger.ONE)) {
					break;
				}
				e_possible++;
				//System.out.println("im e_possible 2: "+e_possible);
			}
			
			d_possible = x.modInverse(BigInteger.valueOf(phi));
			System.out.println("im e_possible: "+e_possible);
			System.out.println("im d_possible: "+d_possible);
			try {
			decrypt(file_path, N, d_possible.intValue());
			}catch(Exception e) {
				e_possible++;
				System.out.println("-------------------------------------------");
				continue;
			}
			System.out.println("is the message understandable? enter 0 for yes and 1 for no ");
			int ans = kn.nextInt();
			if(ans == 1) {
				e_possible++;
				//System.out.println("im e_possible 2: "+e_possible);
			}
			else {
				System.out.println("the public key is = "+e_possible);
				System.out.println("the private key is = "+d_possible);
				System.out.println("data saved in .dec file. thank you for using the system!");
				search = false;
				kn.close();
			}
			System.out.println("-------------------------------------------");
		}
		

	}

	public static void decrypt(String filepath, long N, int D) throws IOException {
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
