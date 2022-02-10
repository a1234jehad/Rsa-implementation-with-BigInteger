import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;

public class Driver {
	public static void main(String[] args) throws IOException {
		Scanner kb = new Scanner(System.in);
		int state = 0;
		do {
			System.out.println("Enter int 0 to encrypt or int 1 to decrypt or int 2 to exit ");
			state = kb.nextInt();
			if (state == 0) {
				System.out.println("Please enter the file path(txt) to encrypt:");
				String path = kb.next();
				encrypt(path);
			} else if (state == 1) {
				System.out.println("Please enter the file path(rsa) to decrypt:");
				String path = kb.next();
				System.out.println("Please enter the value of N:");
				long N = kb.nextLong();
				System.out.println("Please enter the value of D (private key):");
				Long D = kb.nextLong();
				decrypt(path, N, D);
			}

		} while (state != 2);
		System.out.println("Thank you for using our program :) ... exiting");
		kb.close();
	}

	public static void encrypt(String filepath) throws IOException {
		File input = new File(filepath);
		Scanner sc = new Scanner(input);
		String msg = "";
		long N = 0;
		long e = 0;
		boolean first_line_flag = true;
		while (sc.hasNextLine()) {
			if (first_line_flag) {
				String temp = sc.nextLine();
				String temp1 = temp.substring(0, temp.indexOf(" "));
				String temp2 = temp.substring(temp.indexOf(" ") + 1, temp.length());
				e = Long.parseLong(temp1);
				N = Long.parseLong(temp2);
				first_line_flag = false;
			}
			msg += sc.nextLine() + "\n";
		}
		sc.close();
		String x = N + "";
		int nlen = x.length();
		Dictionary dict_enc = dict_enc();
		int numofletters_in_block = 0;
		String st = 78 + "";
		while (Long.parseLong(st) < N) {
			st += "78";
			try {
				Long.parseLong(st);
				}catch(Exception e1) {
				break;
				}
			numofletters_in_block++;
		}
		String ecnrypted_msg = "";
		for (int i = 0; i < msg.length(); i += numofletters_in_block) {
			String encrypted_block = "";
			String toEncrypt = "";
			for (int j = i; j < i+numofletters_in_block; j++) {
				if(msg.length()<(j)+1)
					break;
				char chr = msg.charAt(j);
				int char_key = (int) dict_enc.get(chr);
				String mid = char_key+"";

				if(mid.length() == 1)
					mid = "0"+mid;
				toEncrypt += mid;
			}
			BigInteger key_value = new BigInteger(toEncrypt + "");
			long encrypted_value = key_value.modPow(BigInteger.valueOf(e), BigInteger.valueOf(N)).longValue();
			encrypted_block = encrypted_value+"";
			if (encrypted_block.length()< nlen) {
				for (int j = encrypted_block.length(); j < nlen; j++) {
					encrypted_block = "0" + encrypted_block;
				}
			}
			ecnrypted_msg += encrypted_block;

		}
		String rsapath = filepath.substring(0, filepath.indexOf(".") + 1) + "rsa";
		File output = new File(rsapath);
		FileWriter writer = new FileWriter(rsapath);
		writer.write(ecnrypted_msg);
		writer.close();
		System.out.println("Message encrypted successfully and saved in \"" + rsapath + "\"");

	}

	  public static void decrypt(String filepath, long N, long D) throws IOException {
		 long d = D;
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
		while (Long.parseLong(s) < N) {
			s += "78";
			 try {
				Long.parseLong(s);
				}catch(Exception e) {
				break;
				}
			numofletters_in_block++;
		}
			while(nlen <= encrypted_message.length()){
				String cut = encrypted_message.substring(0,nlen);
				encrypted_message = encrypted_message.substring(nlen);
				BigInteger encrypted = new BigInteger(cut);
				long decrypted_value = encrypted.modPow(BigInteger.valueOf(d), BigInteger.valueOf(N)).longValue();
				String dv = decrypted_value+"";
				if (dv.length()%2 != 0) {
					dv = "0" + dv;
				}
				if (dv.length()< numofletters_in_block*2) {
					for (int j = dv.length(); j < numofletters_in_block*2; j++) {
						dv = "0" + dv;
					}}
				for (int i = 0; i < numofletters_in_block*2; i+=2) {
					

					char res = (char) dict_dec.get(Integer.parseInt(dv.substring(i,i+2)));
					decrypted_message += res;
				}
				
			}
		String decpath = filepath.substring(0, filepath.indexOf(".") + 1) + "dec";
		File output = new File(decpath);
		FileWriter writer = new FileWriter(decpath);
		writer.write(decrypted_message);
		writer.close();
		System.out.println("Message decrypted successfully and saved in \"" + decpath + "\"");
	}
	public static Dictionary dict_enc() {
		Dictionary<Character, Integer> dict  = new Hashtable<Character, Integer>();
		int x = 0;
		for (int i = 65; i < 91; i++) {
			char ch = (char) i;
			dict.put(ch,x);
			
			x++;
		}
		int y = 26;
		for (int i = 97; i < 123; i++) {
			char ch = (char) i;
			dict.put(ch,y);
			
			y++;
		}
		int z = 52;
		for (int i = 48; i < 58; i++) {
			char ch = (char) i;
			dict.put(ch,z);
	
			z++;
		}
		
		char[] rest = {'.',	 '?',	'!',	',',	';',	':',	'-',	'(',	')',	'[',	']',
				'{',	'}',	'\'',	'"',	' ',	'\n'};
		
		int f = 62;
		for (int i = 0; i < rest.length; i++) {
			char ch = rest[i];
			dict.put(ch,f);
			f++;
		}
		return dict;
	}
	public static Dictionary dict_dec() {
		Dictionary<Integer, Character> dict  = new Hashtable<Integer , Character>();
		int x = 0;
		for (int i = 65; i < 91; i++) {
			char ch = (char) i;
			dict.put(x,ch);
			
			x++;
		}
		
		int y = 26;
		for (int i = 97; i < 123; i++) {
			char ch = (char) i;
			dict.put(y,ch);
			
			y++;
		}
		int z = 52;
		for (int i = 48; i < 58; i++) {
			char ch = (char) i;
			dict.put(z,ch);
	
			z++;
		}
		
		char[] rest = {'.',	 '?',	'!',	',',	';',	':',	'-',	'(',	')',	'[',	']',
				'{',	'}',	'\'',	'"',	' ',	'\n'};
		
		int f = 62;
		for (int i = 0; i < rest.length; i++) {
			char ch = rest[i];
			dict.put(f,ch);
			f++;
		}
		return dict;
	}
}