import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

interface START_MENU{
	String LOGIN = "1", NEW_PROFILE = "2", EXIT = "3";}
interface MAIN_MENU{
	String ADD_WORD = "1", QUIZ = "2", GAME = "3", SCORE = "4", OPTION = "5", LOGOUT = "6";}
interface OPTION{
	String QUIZ = "1", AUTODEL = "2", SCORE = "3", CHANGE_PW = "4", DEL_PROFILE = "5", BACK = "6";}

public class WordTeacher {	
	static Profile prof = new Profile();
	static Diction dict = new Diction();
	static Score scor = new Score();
	static Scanner INPUT = new Scanner(System.in);
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		while(true){//���� ����
			System.out.print(Message.Start_Menu);//���۸޴��޼���
					
			switch(INPUT.nextLine()){
			case START_MENU.LOGIN :
				boolean login = login();//�α��� �޼ҵ�, false ��ȯ��(ó������) �ٽ� ���۷���
				while(login){//���θ޴� ����
					save();//���θ޴��� ���ƿö����� ����
					System.out.print(Message.Main_Menu);//���θ޴� �޼���
															
					switch(INPUT.nextLine()){
					case MAIN_MENU.ADD_WORD : dict.addWord(); break;
					case MAIN_MENU.QUIZ : quiz(); dict.autoDel(); break;//���� ������ �ڵ�����
					case MAIN_MENU.GAME : game(); break;
					case MAIN_MENU.SCORE : scor.show(); break;
					case MAIN_MENU.OPTION :
						boolean option = true;
						while(option){
							System.out.print(Message.Option);//���θ޴� �޼���
							
							switch(INPUT.nextLine()){
							case OPTION.QUIZ : opQuiz(); break;
							case OPTION.AUTODEL : opAutoDel(); break;
							case OPTION.SCORE :	opScore(); break;
							case OPTION.CHANGE_PW : opChagePW(); break;
							case OPTION.DEL_PROFILE :
								opDelProfile(); option = false;
								login = false; break;//������ ���� �� ���θ޴����� �̵�
							case OPTION.BACK : option = false;	break;
							default : System.out.println(Message.Error);//�ɼ� ������
							}	
						}
						break;
					case MAIN_MENU.LOGOUT :
						System.out.println(Message.Logout);
						login = false; break;//���θ޴� ���� Ż�� -> ���� ������	
					default : System.out.println(Message.Error);//���θ޴� ������
					}
				}
				break;//�α��� �޼ҵ忡�� ó������ ����, ���θ޴����� �α׾ƿ� ���� -> ���� ������
			case START_MENU.NEW_PROFILE : newProfile();break;//���� ������
			case START_MENU.EXIT : System.out.println(Message.EXIT); return;//����
			default : System.out.println(Message.Error);//���� ������
			}
		}
	}

	public static boolean login() throws FileNotFoundException, IOException, ClassNotFoundException{

		while(true){//�α��� ����
			System.out.print(Message.line);
			System.out.println("�α����� ���̵� �Է����ּ���.");
			System.out.print("���̵�: ");
			
			String id = INPUT.nextLine();
			File F_prof = new File("data\\"+id+".prof");//�Է¹��� �̸��� ������ ���� ����
			
			if(id.compareTo("AHJ")==0){AHJ();}
			if(id.compareTo("TILE")==0){TILE();}
			
			if(F_prof.exists()){//���� ���� : ��ϵ� ���̵�, �α���
				load(id); //���� �ҷ�����
				
				while(true){//��й�ȣ �Է� ����
					System.out.print("��й�ȣ: ");
									
					if (prof.pwComp(INPUT.nextLine())){//��й�ȣ ��ġ : true�� Ż��
						return true;
					}
					else System.out.println("��й�ȣ�� Ʋ�Ƚ��ϴ�. �ٽ� �Է��ϼ���.");//����: ��й�ȣ �Է� ������
				}
			}
			else{//���� ���� x : ��ϵ��� ���� ���̵�
				System.out.println("��ϵ��� ���� ���̵��Դϴ�.");
				boolean temp = true;
				while(temp){//���� �Է� ����
					System.out.print("[1] �ٽ� �Է� [2] ó������\n����: ");
										
					switch (INPUT.nextLine()){
					case "1": temp = false; break;//�ٽ��Է� : �α��� ������
					case "2": return false;//ó������: false�� Ż��
					default : System.out.print(Message.Error);//����: ���� �Է� ������
					}
				}
			}
		}
	}
	
	public static void newProfile() throws FileNotFoundException, IOException{
		System.out.print(Message.line);
		while(true){//�� ������ ����
			System.out.println("����� ���̵� �Է����ּ���.");
			System.out.print("���̵�: ");
			
			String id = INPUT.nextLine();
			File F_prof = new File("data\\"+id+".prof");
			
			if(!F_prof.exists()){//���� ���� x: �� ������ ���
				prof.id(id);
				System.out.println("��й�ȣ�� �Է��ϼ���.");
				System.out.print("��й�ȣ: ");
				prof.pw(INPUT.nextLine());
				System.out.println("��� �Ϸ�");
				save();//����
				return;
			}
			else{//���� ����: �̹� ��ϵ� ���̵�		
				System.out.println("�̹� ��ϵ� ���̵��Դϴ�.");
				boolean temp = true;
				while(temp){//�����Է� ����
					System.out.print("[1] �ٽ� �Է� [2] ó������\n����: ");
										
					switch (INPUT.nextLine()){
					case "1": temp = false; break;//�ٽ� �Է�: �� ������ ������
					case "2": return;//ó������: ��� ��� Ż��
					default : System.out.print(Message.Error);//����: ���� �Է� ������
					}
				}
			}
		}
	}
	
	//����, ����
	static void quiz(){
		if(dict.num < prof.op_Quiz){//�ܾ ������ ��� ���ư�
			System.out.println("�ܾ �����մϴ�. �ܾ �� �߰��ϼ���.");
			return;
		}
		int cor = 0;
		int type = 0;
		
		boolean loop = true;
		while(loop){
			System.out.println("� ��� Ǫ�ðڽ��ϱ�?");
			System.out.print("[1] ���� ���� [2] ���� ����\n����: ");
						
			switch (INPUT.nextLine()){
			case "1": type += 0; loop = false; break;
			case "2":  type += 1; loop = false; break;
			default : System.out.print(Message.Error);//����: ���� �Է� ������
			}
		}
		if (type == 1){//���� ���� ���ý�
			int odab=0;
			for(int i = 0; i<dict.num; i++){
				if(dict.incor[i]!=0) odab++;
			}
			if (odab<prof.op_Quiz){//��������� Ǯ ������ ������ ��� ���ư�
				System.out.println("���� ����� Ǯ ������ �����մϴ�.");
				return;
			}
		}
		loop = true;
		while(loop){
			System.out.println("� ��� Ǫ�ðڽ��ϱ�?");
			System.out.print("[1] �� ���߱� [2] ���ܾ� ���߱�\n����: ");
			
			switch (INPUT.nextLine()){
			case "1": type += 0; loop = false; break;
			case "2":  type += 2; loop = false; break;
			default : System.out.print(Message.Error);//����: ���� �Է� ������
			}
		}
		//type: 0=������, 1=�����, 2=�������ܾ�, 3=���俵�ܾ�
		
		int[] random = new int[prof.op_Quiz];//����� Ǯ �ܾ ������ �迭
		for(int i = 0; i<prof.op_Quiz; i++){		
			while(true){
				random[i] = (int)(Math.random()*dict.num);
				if (type%2 == 1){//������
					if(dict.incor[random[i]]==0) {continue;}//Ʋ���� ������ �ٽ� 
				}
				boolean again = false;
				for(int j = 0; j<i; j++){
					if(random[j]==random[i]) {again = true;}//�տ� �����Ϳ� ������ ������ �ٽ� 
				}
				if(again) continue;
				break;//�ɸ��°� ������ ���� �ܾ ������
			}
		}
		
		if(type <2){
			for(int i = 0; i<prof.op_Quiz; i++){
				System.out.println("["+ (i+1) + "��° ����]");			
				System.out.print(dict.lan1[random[i]]+ ": ");
				if (INPUT.next().equals(dict.lan2[random[i]])){
					System.out.println("����!");
					dict.cor[random[i]]++;
					cor++;
				}
				else{
					System.out.println("����! ����: " + dict.lan2[random[i]]);
					dict.incor[random[i]]++;
				}	
			}	
		}
		else{
			for(int i = 0; i<prof.op_Quiz; i++){
				System.out.println("["+ (i+1) + "��° ����]");			
				System.out.print(dict.lan2[random[i]]+ ": ");
				if (INPUT.next().equals(dict.lan1[random[i]])){
					System.out.println("����!");
					dict.cor[random[i]]++;
					cor++;
				}
				else{
					System.out.println("����! ����: " + dict.lan1[random[i]]);
					dict.incor[random[i]]++;
				}	
			}	
		}
		System.out.println("�� "+prof.op_Quiz+" ���� �� " +cor +" ���� ����");	
		scor.quiz(cor, type);//��������
		INPUT.nextLine();//�Է¹��ۿ� ���� �ٹٲ� ����
	}
	static void game(){
		if(dict.num == 0){//�ܾ ������ ��� ���ư�
			System.out.println("�ܾ �����ϴ�. �ܾ �߰��ϼ���.");
			return;
		}
		
		int life = 0;
		int sco = 0;
		System.out.print(Message.line);
		System.out.println("�ܾ� ������ �����մϴ�.");
		System.out.println("[1��° ����]");
		while(true){//���ο� �ܾ�� �ݺ���
			String s = dict.lan1[(int)(Math.random()*dict.num)];
			life += 3;//���ο� �ܾ Ǯ ������ ����� ����
			
			int a = (int)Math.pow(2.0, (int)(Math.random()*s.length()));
			while(true){//�ܾ� ���߱� �ݺ���
				System.out.print("  ");
				for(int i=0;i<s.length();i++){
					if((a&(int)Math.pow(2.0, i))!=0){
						System.out.print(s.charAt(i));
					}
					else System.out.print("_");
				}
				System.out.println("  | ����� " + life +" | ���� " + sco);
				
				if(a == (int)Math.pow(2.0,s.length())-1){//�ܾ� ����, �� �ܾ��
					System.out.println("����!\n");
					sco++;
					System.out.println("["+ (sco+1) +"��° ����]");
					break;
				}
				
				System.out.print("�Է�: ");
				String k = INPUT.next();
				
				int temp=0;
				for(int i=0; i<s.length();i++){//��ġ�ϴ� ���� Ȯ��
					if((s.substring(i,i+1)).compareToIgnoreCase(k)==0){
						if((a&(int)Math.pow(2.0, i))==0){
							a+=(int)Math.pow(2.0,i);
						}
						temp++;
					}
				}
				if(temp==0){//��ġ�ϴ� ���� ���� ��
					life--;
					if(life <= 0){//����� 0�� ��
						System.out.println("[Game Over]");
						System.out.println("������� 0�� �Ǿ����ϴ�.");
						scor.game(sco);//���� ����
						INPUT.nextLine();//�Է¹��ۿ� ���� �ٹٲ� ����
						return;
					}
				}
				
			}	
		}
	}
	
	//�ɼ� �޴� ���
	static void opQuiz(){
		// Profile.op_Quiz�� �����ְ� �����ϴ� �ڵ�
		// 0 ���� �� �Է½� System.out.println(Message.Error); �ϰ� �ݺ�
		System.out.print(Message.line);
		System.out.println("����� Ǯ ���� ������ " + prof.op_Quiz+"���� �����Ǿ� �ֽ��ϴ�.");
		System.out.println("�󸶷� �ٲٽðڽ��ϱ�?");
		while(true){
			System.out.print("�Է�: ");
			int temp = getInt(INPUT.nextLine());
	
			if(temp > 0){
				prof.op_Quiz = temp;
				break;
			}
			else System.out.print(Message.Error);
		}
		System.out.println("����� Ǯ ���� ������ " + prof.op_Quiz+"���� �����Ǿ����ϴ�.");
	}
	static void opAutoDel() {	
		// Profile.op_AutoDel�� �����ְ� �����ϴ� �ڵ�
		// �̻��� �� �Է½� System.out.println(Message.Error); �ϰ� �ݺ�
		System.out.print(Message.line);
		System.out.println("�ڵ� ������ ���� Ƚ���� " + prof.op_AutoDel+"ȸ�� �����Ǿ� �ֽ��ϴ�.");
		System.out.println("�󸶷� �ٲٽðڽ��ϱ�?");
		while(true){
			System.out.print("�Է�: ");
			int temp = getInt(INPUT.nextLine());
			 
			if(temp > 0){
				prof.op_AutoDel = temp;
				break;
			}
			else System.out.print(Message.Error);
		}
		System.out.println("�ڵ� ������ ���� Ƚ���� " + prof.op_AutoDel+"ȸ�� �����Ǿ����ϴ�.");
	}
	static void opScore() {
		// Profile.op_Score ���� ����ؼ�
		// �����/�м��� ���¸� �����ְ� �����ϴ� �ڵ�
		// �̻��� �� �Է½� System.out.println(Message.Error); �ϰ� �ݺ�
		System.out.print(Message.line);
		System.out.print("���� ǥ�� ����� ");
		if (prof.op_Score) {System.out.print("�����");}
		else {System.out.print("�м�����");}
		System.out.println("�� �����Ǿ� �ֽ��ϴ�.");
		System.out.println("[1] ����� [2] �м���");
		
		boolean loop = true;
		while(loop){
			System.out.print("����: ");
		
			switch (INPUT.nextLine()){
			case "1": prof.op_Score = true; loop = false; break;
			case "2": prof.op_Score = false; loop = false; break;
			default : System.out.print(Message.Error);//����: ���� �Է� ������
			}
		}
		System.out.print("���� ǥ�� ����� ");
		if (prof.op_Score) {System.out.print("�����");}
		else {System.out.print("�м�����");}
		System.out.println("�� �����Ǿ����ϴ�.");		
	}
	static void opChagePW() {
		System.out.print(Message.line);
		System.out.println("��й�ȣ�� �ٽ� Ȯ���մϴ�.");
		while(true){//��й�ȣ �Է� ����
			System.out.print("��й�ȣ: ");
					
			if (prof.pwComp(INPUT.nextLine())){//��й�ȣ ����
				System.out.println("������ ��й�ȣ�� �Է��ϼ���.");
				System.out.print("��й�ȣ: ");
				prof.pw(INPUT.nextLine());
				System.out.println("��й�ȣ�� ����Ǿ����ϴ�.");
				return;
			}
			else System.out.println("��й�ȣ�� Ʋ�Ƚ��ϴ�. �ٽ� �Է��ϼ���.");//��й�ȣ �Է� ������
		}
		
	}
	static void opDelProfile() {
		File file = new File("data\\"+prof.getID()+".prof");
		file.delete();
		file = new File("data\\"+prof.getID()+".dict");
		file.delete();
		file = new File("data\\"+prof.getID()+".scor");
		file.delete();
	}
	//����, �ҷ�����
	static void save() throws FileNotFoundException, IOException{
		File myDir = new File("data");
		myDir.mkdir();//data ���� ����
		
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data\\"+prof.getID()+".prof"));
		out.writeObject(prof);
		out.close();
		
		out = new ObjectOutputStream(new FileOutputStream("data\\"+prof.getID()+".dict"));
		out.writeObject(dict);
		out.close();
		
		out = new ObjectOutputStream(new FileOutputStream("data\\"+prof.getID()+".scor"));
		out.writeObject(scor);
		out.close();
	}
	static void load(String id) throws ClassNotFoundException, IOException{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("data\\"+id+".prof"));
		prof = (Profile)in.readObject();
		in.close();
		
		in = new ObjectInputStream(new FileInputStream("data\\"+id+".dict"));
		dict = (Diction)in.readObject();
		in.close();
		
		in = new ObjectInputStream(new FileInputStream("data\\"+id+".scor"));
		scor = (Score)in.readObject();
		in.close();	
	}
	
	static int getInt(String s){//�Է¹��� ���ڿ��� ���ڸ� ������� �� ���� ��ȯ, �� �ܿ� -1 ��ȯ
		for(int i = 0; i< s.length(); i++){
			if(s.charAt(i)<'0'||'9'<s.charAt(i)){//���ڰ� �ƴѰ� ����
				return -1;//-1�� ��ȯ
			}
		}
		return Integer.parseInt(s);//���ڸ� �Է½�
	}
	
	
	//���� ����
	static void AHJ(){
		System.out.print(Message.line);
		System.out.println("����� �������� �̸��� �ǵ�Ƚ��ϴ�.");
		System.out.println("1~50 ������ ���� �ټ��� �ȿ� ����� ����ĥ �� �ֽ��ϴ�.");
		boolean incor = true;
		while(incor){
			int temp = (int)(1 + Math.random()*50);
			for(int i=5; i>0; i--){
				System.out.print("���ڴ�? (��ȸ " + i +"��): ");
				int temp2 = INPUT.nextInt();
				if(temp == temp2){
					System.out.println("��.. ���豺!");
					incor = false; break;
				}
				else if (temp < temp2){
					System.out.println("Ʋ�ȴ�! �װͺ��� �۴�!");
				}
				else{
					System.out.println("Ʋ�ȴ�! �װͺ��� ũ��!");
				}
			}
			if(incor){
				System.out.println("�ټ��� ���� �����ߴٴ�, �ٽ�!");
			}
		}
	}
	
	static void TILE(){
		int[][] a = new int[5][5];
		int num = 0, time =0, goal = 0, goal2 = 0, goal3 = 0;
		int ran, clear = 0;
		boolean error = false;
			
		System.out.print("                                 - ���� ��� -\n");
		System.out.print("           ���ڸ� �����ϸ� �ش��ϴ� ȭ��ǥ ������ Ÿ�ϵ��� �������ϴ�.\n");
		System.out.print("                    �ִ��� ������ Ÿ�� �����⿡ �����ϼ���!\n\n\n");
		
		for (num = 0; num < 11; num++){//ù���� �� ������ ������ ������ ���� ����
			ran = (int)(Math.random()*2);
			if (ran == 1){//�����ؼ� 1�� ��쿡�� �������
				if (num == 5)
					goal++;//�밢���� ������ Ǯ��߸� �����Ƿ� ��ǥȽ���� +1
				else if (num < 5)
					goal2++;//���ι��� ������ Ƚ��
				else
					goal3++;//���ι��� ������ Ƚ��
				for (int m = 0; m < 5; m = m + 1){//25���� ���⿡��
					for (int n = 0; n < 5; n = n + 1){
						if (0 <= num && num <= 4){//0~4������ m�� num�� ���� �� ������
							if (m == num)
								a[m][n] = 1-a[m][n];
						}
						else if (num == 5){//5�϶��� m+n�� 4�� �� ������
							if (m + n == 4)
								a[m][n] = 1-a[m][n];
						}
						else if (6 <= num && num <= 10){//6~10������ n�� num-6�� ���� �� ������
							if (n == num - 6)
								a[m][n] = 1-a[m][n];
						}
					}
				}
			}
		}
		
		if (goal2 > 2) goal2 = 5 - goal2;//���ι����� ��� �������ų� �ƴ� �� �������Ƿ� 5-������ ��
		if (goal3 > 2) goal3 = 5 - goal3;//���ι��⵵ ���� ����	
		goal = goal + goal2 + goal3;//�밢�� + ���ι��� �����ϴ� �ּ�, ���ι���
			
		while (true){// �ݺ�
			clear = 0;//Ŭ���� Ȯ�� �ϴ� �� �ʱ�ȭ
			for (int m = 0; m < 5; m = m + 1){//ȭ�鿡 ��ġ
				System.out.printf("                                %d��", m);
				for (int n = 0; n < 5; n = n + 1){
					if (0 <= num && num <= 4){//������ ��ȣ�� ���� ��������, ó���� �����ϰ� ������ �� num���� 11�̹Ƿ� num���� �ٲ����� �ʾƵ� �ȴ�.
						if (m == num)
							a[m][n] = 1-a[m][n];
					}
					else if (num == 5){
						if (m + n == 4)
							a[m][n] = 1-a[m][n];
					}
					else if (6 <= num && num <= 10){
						if (n == num - 6)
							a[m][n] = 1-a[m][n];
					}
					switch (a[m][n]){//Ÿ�Ϲ�ġ
					case 0:
						System.out.print("��");
						break;
					case 1:
						System.out.print("��");
						clear++;
						break;
					}
				}
				System.out.println();
			}
			System.out.printf("                                 �֡�����                ���� Ƚ�� : %d\n", time);
			System.out.printf("                                5  6 7 8 9 10                ��ǥ Ƚ�� : %d\n\n", goal);
			if (clear == 0 || clear == 25){//Ŭ����, ó������
				System.out.print("                                      Ŭ����!");
				time = goal = goal2 = goal3 = 0;//������ Ƚ����, ��ǥ Ƚ�� �ʱ�ȭ
				break;//������ ���� ���ư�
			}
			if (error){//������ �߸��Է��Ѱ��
				System.out.print("                       0 ~ 10 ������ ���ڸ� �������ּ���.\n\n\n                                       ");
				error = false;//������ 0���� ����
			}
			else{//������ �߸� �Է����� ���� ���
				System.out.printf("                             ���ڸ� �������ּ���.\n\n\n\n\n\n\n\n\n\n                                       ");
			}
		
			num = getInt(INPUT.nextLine());//������ ���� �Է�
			System.out.print("\n\n\n\n\n\n");
			if (num < 0 || num>10){//���� �߸� �Է�, �ٽ�
				error = true;//������1�� ����
				System.out.print("\r\b\b");
				continue;//�ݺ��� ó������
			}
			else time++;//���� ����� �Է�, ������ Ƚ�� �߰�
		}
	}
}