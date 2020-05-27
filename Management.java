import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("unchecked")
public class Management<R> {

	RedBlackTree rbt = new RedBlackTree();
	Augmented agt = new Augmented<>();
	String[] parse = null;
	ArrayList<String> listDate = new ArrayList<>();
	ArrayList<String> listID = new ArrayList<>();
	ArrayList<Double> listDay = new ArrayList<>();
	ArrayList<Double> listMonth = new ArrayList<>();
	ArrayList<Double> listYear = new ArrayList<>();
	ArrayList<Double> listAge = new ArrayList<>();
	String[] parse2 = null;
	BufferedReader br = null;
	{
		try {
			br = new BufferedReader(new FileReader("C:\\Users\\mustafa\\Desktop\\tree\\people.txt"));
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				parse = line.split(",");
				listID.add(parse[0]); // idler
				listDate.add(parse[1]);// tarihler
				parse2 = listDate.get(i).split("/");
				listDay.add(Double.parseDouble(parse2[0]));
				listMonth.add(Double.parseDouble(parse2[1]));
				listYear.add(Double.parseDouble(parse2[2]));
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("ERROR!");
		}

		finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		calAge(listDay, listMonth, listYear);
		for (int i = 0; i < listID.size(); i++) {
			rbt.insert(rbt.tempNode((R) listID.get(i), listDate.get(i), listAge.get(i)));
			agt.insert(agt.tempNode((R) listID.get(i), listDate.get(i), listAge.get(i)));
		}
		consoleUI();
	}

	// yas hesabi
	public void calAge(ArrayList<Double> day, ArrayList<Double> month, ArrayList<Double> year) {
		double age = 0;
		for (int i = 0; i < day.size(); i++) {
			age = (31 - day.get(i)) + (30 * (12 - month.get(i)) + (365 * (2018 - year.get(i))));
			listAge.add(age / 365);
		}
	}

	// menu
	public void consoleUI() {
		try {
			Scanner scan = new Scanner(System.in);
			while (true) {
				System.out.println("\n1.- Add items\n" + "2.- Delete items\n" + "3.- Check items\n" + "4.- Print tree\n"
						+ "5.- Delete tree\n" + "6.- GetNumSmallerDate\n" + "7.- GetNumSmallerID\n"
						+ "8.- Minimum Age\n" + "9.- Maximum Age\n" + "10.- Number People\n");
				int choice = scan.nextInt();
				int item = 0;
				String[] parse = null;
				double age = 0;
				switch (choice) {

				case 1:// ekleme

					while (item != -999) {
						age = 0;
						scan.nextLine();
						System.out.println("Enter ID= ");
						R id = (R) scan.nextLine();
						System.out.println("Enter Birthday(Use /)= ");
						String date = scan.next();
						parse = date.split("/");
						age = (31 - (Double.parseDouble(parse[0]))) + (30 * (12 - Double.parseDouble(parse[1])))
								+ (365 * (2018 - Double.parseDouble(parse[2])));

						rbt.case1(id, date, age);
						agt.case1(id, date, age);
						System.out.println("Enter E to finish");
						String e = scan.next().toLowerCase();
						if (e.equals("e"))
							item = -999;
						else
							item = 0;
					}
					break;

				case 2:// silme eleman
					while (item != -999) {
						age = 0;
						scan.nextLine();
						System.out.println("Enter ID= ");
						R id = (R) scan.nextLine();
						System.out.println("Enter Birthday(Use /)= ");
						String date = scan.next();
						parse = date.split("/");
						age = (31 - (Double.parseDouble(parse[0]))) + (30 * (12 - Double.parseDouble(parse[1])))
								+ (365 * (2018 - Double.parseDouble(parse[2])));

						rbt.case2(id, date, age);
						agt.case2(id, date, age);
						System.out.println("Enter E to finish");
						String e = scan.next().toLowerCase();
						if (e.equals("e"))
							item = -999;
						else
							item = 0;
					}
					break;
				case 3:// kontrol
					while (item != -999) {
						age = 0;
						scan.nextLine();
						System.out.println("Enter ID= ");
						R id = (R) scan.nextLine();
						System.out.println("Enter Birthday(Use /)= ");
						String date = scan.next();
						parse = date.split("/");
						age = (31 - (Double.parseDouble(parse[0]))) + (30 * (12 - Double.parseDouble(parse[1])))
								+ (365 * (2018 - Double.parseDouble(parse[2])));
						rbt.case3(id, date, age);
						agt.case3(id, date, age);
						System.out.println("Enter E to finish");
						String e = scan.next().toLowerCase();
						if (e.equals("e"))
							item = -999;
						else
							item = 0;
					}
					break;
				case 4:// yazdirma
					System.out.println("***********Red Black Tree***********\n");
					rbt.case4();
					System.out.println();
					System.out.println("***********Augmented Reb Black Tree***********\n");
					agt.case4();
					break;
				case 5:// silme aðaci
					rbt.case5();
					System.out.println();
					agt.case5();
					break;
				case 6:// girilen tarihten öncekiler
					System.out.println("Enter Birthday(Use /)= ");
					scan.nextLine();
					String date = scan.nextLine();
					rbt.case6(date);
					System.out.println();
					agt.case6(date);
					break;
				case 7:// girilen id önceki
					System.out.println("Enter ID= ");
					scan.nextLine();
					R id = (R) scan.nextLine();
					rbt.case7(id);
					System.out.println();
					agt.case7(id);
					break;
				case 8:// minimum yaþ
					rbt.case8();
					System.out.println();
					agt.case8();
					break;
				case 9:// maximum yaþ
					rbt.case9();
					System.out.println();
					agt.case9();
					break;
				case 10:// eleman sayisi
					rbt.case10();
					System.out.println();
					agt.case10();
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR!");
		}

	}

}
