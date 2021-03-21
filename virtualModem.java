package userApplication;
import java.util.Scanner;
import java.io.File;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import ithakimodem.Modem;

/*
*
* Δίκτυα Υπολογιστών I
*
* Experimental Virtual Lab
*
* Java virtual modem communications seed code
*
*/

public class virtualModem {
	public static void main(String[] param) {
		
		System.out.println("Press one for demo \nPress 2 for packets \nPress 3 for image\nPress 4 for error image\nPress 5 for gps traces\nPress 6  for gps image\nPress 7 for ARQ\n");
		Scanner in = new Scanner ( System.in );
		if (in.nextInt()==1) {	
			(new virtualModem()).demo();
		}
		else if (in.nextInt()==2) {
			(new virtualModem()).packet();
		}
		else if (in.nextInt()==3) {
			(new virtualModem()).getImage();
		}
		else if (in.nextInt()==4) {
			(new virtualModem()).getErrorImage();
		}
		else if (in.nextInt()==4) {
			(new virtualModem()).getGps();
		}
		else if (in.nextInt()==5) {
			(new virtualModem()).getGps();
		}
		else if (in.nextInt()==6) {
			(new virtualModem()).getGpsImage();
		}
		else if (in.nextInt()==6) {
			(new virtualModem()).arq();
		}
		else {
			System.out.println("ooops");
		}
		
	
	}
	public void demo() {
		int k;
		Modem modem;
		modem = new Modem();
		modem.setSpeed(9000);
		modem.setTimeout(2000);
		modem.open("ithaki");
		for (;;) {
			try {
				k = modem.read();
				if (k == -1)
					break;
				System.out.print((char) k);
			} catch (Exception x) {
				break;
			}
		}
		modem.close();
	}

	public void packet() {
		int a, paketa = 0, b= 0, elegxos = 0;
		long[] time = new long[4];
		File packet = new File("/Users/Τάσος/Desktop/ergasia/packets.xls");
		String s = new String();
		Modem modem = new Modem();

		s = "E0020\r";

		modem.setSpeed(10000);
		modem.setTimeout(2000);
		modem.open("ithaki");

		for (;;) { // Gia to hello stranger
			a = modem.read();
			if (a == -1)
				break;
			System.out.print((char) a);
		}

		try {
			elegxos = 0;
			FileOutputStream out = new FileOutputStream(packet);

			while (paketa< 5000) { // aritmos paketwn

				modem.write(s.getBytes());

				for (;;) {
					try {
						a = modem.read();
						System.out.print((char) a);
						if (a == -1)
							break;
						if (a == 80 && elegxos == 0) { // otan diavazw P krataw xrono anaxwrisis
							time[0] = System.currentTimeMillis();

						}

					} catch (Exception x) {
						break;
					}

					if (b == 79 && a == 80) { // otan diavazw OP krataw xrono afixis
						time[1] = time[0];
						time[0] = System.currentTimeMillis(); 
					}

					b = a;

				}

				time[2] = time[0] - time[1];
				
				paketa++;

				try {

					out.write(String.valueOf(time[2]).getBytes());
					out.write(10);
				} catch (IOException e) {
					System.out.println("Den graftike");
				}

				b = a;

			}

		} catch (FileNotFoundException e) {
			System.out.println("exairesi");
		}

	}

	public void getImage() {
		int a,b;
		Modem modem;
		File image;
		
		modem = new Modem();
		image = new File("/Users/Τάσος/Desktop/ergasia/image.jpg");
		String s = new String();
		modem.setSpeed(20000);
		modem.setTimeout(6000);
		modem.open("ithaki");
		s = "M4416CAM=PTZ\r";
		modem.write(s.getBytes());
		
		try {
			int flag = 0;
			FileOutputStream out = new FileOutputStream(image);
			
			if (modem.write(s.getBytes()) == false) {
				System.out.println("ooops");
			}
			a = modem.read();// ksekinaw lamvanw apo modem
			
			for (;;) {
				b = a; //krataw to a-1
				a = modem.read();
				if (b == -1) {
					break;
				}
				//arxi tis eikonas
				if (b == 255 && a == 216) {
					flag = 1;
				}
				if (flag == 1) {
					try {
						out.write(b);// eggrafi
					} catch (IOException e) {
						System.out.println("den graftike");
					}
				}
				//telos tis eikonas
				if (b == 255 && a == 217) {
					break;
				}
			}
			try {
				out.close();// kleinw to  arxeio
			} catch (IOException e) {
				System.out.println("sfalma sto kleisimo");
			}
			modem.close();// kleisimo modem
		} catch (FileNotFoundException e) {
			System.out.println("exairesi");
		}

	}
	
	
	
	public void getErrorImage() {
		int d,j;
		Modem modem;
		File image;
		
		modem = new Modem();
		image = new File("/Users/Τάσος/Desktop/ergasia/errorimage.jpg");
		String s = new String();
		modem.setSpeed(20000);
		modem.setTimeout(6000);
		modem.open("ithaki");
		s = " G4055\r";
		modem.write(s.getBytes());
		
		try {
			int elegxos = 0;
			FileOutputStream out = new FileOutputStream(image);
			
			if (modem.write(s.getBytes()) == false) {
				System.out.println("oops");
			}
			d = modem.read();// ksekinaw na lamvanw apo to modem
			for (;;) {
				j = d; //krataw to d-1
				d = modem.read();
				if (d == -1) {
					break;
				}
				// entopismos arxis eikonas
				if (j == 255 && d == 216) {
					elegxos = 1;
				}
				if (elegxos == 1) {
					try {
						out.write(j);// eggrafi sto arxeio
					} catch (IOException e) {
						System.out.println("den graftike");
					}
				}
				// entopismos telous eikonas
				if (j == 255 && d == 217) {
					break;
				}
			}
			try {
				out.close();// kleisimo arxeiou
			} catch (IOException e) {
				System.out.println("sfalma sto kleisimo tou arxeiou");
			}
			modem.close();// kleisimo modem
		} catch (FileNotFoundException e) {
			System.out.println("exairesi");
		}

	}
	
	
	
	public void getGps(){
		int gps;
		Modem modem;
		modem = new Modem();

		String s = new String();
		modem.setSpeed(8000);
		modem.setTimeout(5000);
		modem.open("ithaki");
		s="P4263R=1008050\r";
		modem.write(s.getBytes());
		for (;;) {
			try {
				gps = modem.read();
				if (gps == -1)
					break;
				System.out.print((char) gps);
			} catch (Exception x) {
				System.out.print("exairesi");
				break;
			}
		}
		modem.close();
		
	}
	
	public void getGpsImage(){
		int a,b;
		Modem modem;
		modem = new Modem();
		File image;
		image = new File("/Users/Τάσος/Desktop/ergasia/gps.jpg");
		String s = new String();
		modem.setSpeed(10000);
		modem.setTimeout(6000);
		modem.open("ithaki");
		s="P4263T=225753403739T=225752403738T=225747403741T=225747403744\r";
		modem.write(s.getBytes());
		
		try {
			FileOutputStream out = new FileOutputStream(image);
			int elegxos = 0;
			if (modem.write(s.getBytes()) == false) {
				System.out.println("Den egrapse");
			}
			a = modem.read();// ksekinaw na lamvanw apo modem
			for (;;) {
				b = a;
				a = modem.read();
				if (a == -1) {
					break;
				}
				//arxi tis eikonas
				if (b == 255 && a == 216) {
					elegxos = 1;
				}
				if (elegxos == 1) {
					try {
						out.write(b); // eggrafi sto arxeio
					} catch (IOException e) {
						System.out.println("den ksekinise");
					}
				}
				//telos tis eikonas
				if (b == 255 && a == 217) {
					break;
				}
			}
			try {
				out.close();// kleinw to arxeio
			} catch (IOException e) {
				System.out.println("sfalma sto kleisimo");
			}
			modem.close();// kleisimo modem
		} catch (FileNotFoundException e) {
			System.out.println("exairesi");
		}

		modem.close();
		
	}
	

	public void arq() {
		int k, j = 0, repeat = 0, l, i, xor = 0, elegxos = 0;
		long[] time = new long[4];
		int[] repeats = new int[20];
		boolean f = true;
		File packet = new File("/Users/Τάσος/Desktop/ergasia/packetarq.xls");
		String p = new String();
		String ack = new String();
		String nack = new String();
		String fcs = new String();

		Modem modem;
		modem = new Modem();

		ack = "Q5797\r";
		nack = "R4921\r";
		modem.setSpeed(10000);
		modem.setTimeout(2000);
		modem.open("ithaki");

		for (;;) { // Gia to hello stranger
			k = modem.read();
			if (k == -1)
				break;
			System.out.print((char) k);
		}

		try {
			FileOutputStream out = new FileOutputStream(packet);
			int m = 0;
			while (m < 1500) { // arithmos paketwn
				if (f == true) { // f->flag gia tin egkyrothta tou paketou AN TRUE SWSTO,STEILE ACK
					modem.write(ack.getBytes());
					elegxos = 0;
					repeat = 0; // counter epanekpompwn tou paketou,0 otan exw swsto paketo

				} else if (f == false) { // An false stelnw nack na ksanaparw to idio
					modem.write(nack.getBytes());
					elegxos = 1;
					repeat++; // afksanw kata 1 kathe fora pou pairnv nack kai ginetai epanekpompi tou paketou

				}

				for (;;) {
					try {
						k = modem.read();
						System.out.print((char) k);
						if (k == -1)
							break;
						if (k == 80 && elegxos == 0) { // otan diavaseis P (apo to Pstart) krata ton xrono anaxwrishs
													// paketou
							elegxos = 1;
							time[0] = System.currentTimeMillis();

						}
						if (k == 60) {
							System.out.println();
							p = "";
							fcs = "";
							k = modem.read();
							while (k != 62) { // oso diaforetiko tou >
								p = p + (char) k; // krata to psifio apo ton 16psifio
								k = modem.read(); // ksanadiavase
							}
							System.out.println("Packet=" + p);
							xor = 0;
							xor = p.charAt(0) ^ p.charAt(1);
							i = 2;
							// xor tou paketou gia elegxo
							while (i < 16) {
								xor = xor ^ p.charAt(i);
								i++;
							}
							System.out.println("xor=" + xor);
							k = modem.read(); // kai sunexise (diavazei to keno)

							l = 0;
							// apothikeusi tou fcs pou einai ta epomena 3
							while (l < 3) {
								k = modem.read();
								fcs = fcs + (char) k;
								l++;
							}
							System.out.println("FCS=" + fcs); // ektupwse ton
							System.out.println();

						}

					} catch (Exception x) {
						break;
					}

					
					if (j == 79 && k == 80) { // otan stalthoun oi xaraktires OP apo to PSTOP break

						break;
					}

					j = k;

				}

				if (xor == Integer.parseInt(fcs)) {

					System.out.print("Everything's good");
					time[1] = time[0];
					time[0] = System.currentTimeMillis(); // krataw xrono afikis
					time[2] = time[0] - time[1];
					System.out.printf("\nTime 2: " + time[2] + " = " + time[1] + " - " + time[0] + "\n"); // ypologismos
																											// xronou
																											// apokrisis

					f = true;
					repeats[repeat]++;
					m++;

					try {

						out.write(String.valueOf(time[2]).getBytes());// eggrafi sto arxeio
						out.write(10);
					} catch (IOException e) {
						System.out.println("Den egrapse");
					}

				} else {

					f = false;
				}

				j = k;

			}

			try {
				out.close();// kleisimo tou arxeiou
			} catch (IOException e) {
				System.out.println("Den ekleise");
			}
			for (i = 0; i < 20; i++) {
				System.out.print("\n");
				System.out.printf(i + " epanalipseis: " + repeats[i] + " paketa\n");
			}

		} catch (FileNotFoundException e) {
			System.out.println("oxi");

		}
		modem.close();

	}
}

