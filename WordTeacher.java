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
		while(true){//시작 루프
			System.out.print(Message.Start_Menu);//시작메뉴메세지
					
			switch(INPUT.nextLine()){
			case START_MENU.LOGIN :
				boolean login = login();//로그인 메소드, false 반환시(처음으로) 다시 시작루프
				while(login){//메인메뉴 루프
					save();//메인메뉴로 돌아올때마다 저장
					System.out.print(Message.Main_Menu);//메인메뉴 메세지
															
					switch(INPUT.nextLine()){
					case MAIN_MENU.ADD_WORD : dict.addWord(); break;
					case MAIN_MENU.QUIZ : quiz(); dict.autoDel(); break;//퀴즈 수행후 자동삭제
					case MAIN_MENU.GAME : game(); break;
					case MAIN_MENU.SCORE : scor.show(); break;
					case MAIN_MENU.OPTION :
						boolean option = true;
						while(option){
							System.out.print(Message.Option);//메인메뉴 메세지
							
							switch(INPUT.nextLine()){
							case OPTION.QUIZ : opQuiz(); break;
							case OPTION.AUTODEL : opAutoDel(); break;
							case OPTION.SCORE :	opScore(); break;
							case OPTION.CHANGE_PW : opChagePW(); break;
							case OPTION.DEL_PROFILE :
								opDelProfile(); option = false;
								login = false; break;//프로필 삭제 후 메인메뉴까지 이동
							case OPTION.BACK : option = false;	break;
							default : System.out.println(Message.Error);//옵션 루프로
							}	
						}
						break;
					case MAIN_MENU.LOGOUT :
						System.out.println(Message.Logout);
						login = false; break;//메인메뉴 루프 탈출 -> 시작 루프로	
					default : System.out.println(Message.Error);//메인메뉴 루프로
					}
				}
				break;//로그인 메소드에서 처음으로 선택, 메인메뉴에서 로그아웃 선택 -> 시작 루프로
			case START_MENU.NEW_PROFILE : newProfile();break;//시작 루프로
			case START_MENU.EXIT : System.out.println(Message.EXIT); return;//종료
			default : System.out.println(Message.Error);//시작 루프로
			}
		}
	}

	public static boolean login() throws FileNotFoundException, IOException, ClassNotFoundException{

		while(true){//로그인 루프
			System.out.print(Message.line);
			System.out.println("로그인할 아이디를 입력해주세요.");
			System.out.print("아이디: ");
			
			String id = INPUT.nextLine();
			File F_prof = new File("data\\"+id+".prof");//입력받은 이름의 프로필 파일 지정
			
			if(id.compareTo("AHJ")==0){AHJ();}
			if(id.compareTo("TILE")==0){TILE();}
			
			if(F_prof.exists()){//파일 존재 : 등록된 아이디, 로그인
				load(id); //파일 불러오기
				
				while(true){//비밀번호 입력 루프
					System.out.print("비밀번호: ");
									
					if (prof.pwComp(INPUT.nextLine())){//비밀번호 일치 : true로 탈출
						return true;
					}
					else System.out.println("비밀번호가 틀렸습니다. 다시 입력하세요.");//예외: 비밀번호 입력 루프로
				}
			}
			else{//파일 존재 x : 등록되지 않은 아이디
				System.out.println("등록되지 않은 아이디입니다.");
				boolean temp = true;
				while(temp){//숫자 입력 루프
					System.out.print("[1] 다시 입력 [2] 처음으로\n선택: ");
										
					switch (INPUT.nextLine()){
					case "1": temp = false; break;//다시입력 : 로그인 루프로
					case "2": return false;//처음으로: false로 탈출
					default : System.out.print(Message.Error);//예외: 숫자 입력 루프로
					}
				}
			}
		}
	}
	
	public static void newProfile() throws FileNotFoundException, IOException{
		System.out.print(Message.line);
		while(true){//새 프로필 루프
			System.out.println("등록할 아이디를 입력해주세요.");
			System.out.print("아이디: ");
			
			String id = INPUT.nextLine();
			File F_prof = new File("data\\"+id+".prof");
			
			if(!F_prof.exists()){//파일 존재 x: 새 프로필 등록
				prof.id(id);
				System.out.println("비밀번호를 입력하세요.");
				System.out.print("비밀번호: ");
				prof.pw(INPUT.nextLine());
				System.out.println("등록 완료");
				save();//저장
				return;
			}
			else{//파일 존재: 이미 등록된 아이디		
				System.out.println("이미 등록된 아이디입니다.");
				boolean temp = true;
				while(temp){//숫자입력 루프
					System.out.print("[1] 다시 입력 [2] 처음으로\n선택: ");
										
					switch (INPUT.nextLine()){
					case "1": temp = false; break;//다시 입력: 새 프로필 루프로
					case "2": return;//처음으로: 등록 취소 탈출
					default : System.out.print(Message.Error);//예외: 숫자 입력 루프로
					}
				}
			}
		}
	}
	
	//퀴즈, 게임
	static void quiz(){
		if(dict.num < prof.op_Quiz){//단어가 부족할 경우 돌아감
			System.out.println("단어가 부족합니다. 단어를 더 추가하세요.");
			return;
		}
		int cor = 0;
		int type = 0;
		
		boolean loop = true;
		while(loop){
			System.out.println("어떤 퀴즈를 푸시겠습니까?");
			System.out.print("[1] 랜덤 퀴즈 [2] 오답 퀴즈\n선택: ");
						
			switch (INPUT.nextLine()){
			case "1": type += 0; loop = false; break;
			case "2":  type += 1; loop = false; break;
			default : System.out.print(Message.Error);//예외: 숫자 입력 루프로
			}
		}
		if (type == 1){//오답 퀴즈 선택시
			int odab=0;
			for(int i = 0; i<dict.num; i++){
				if(dict.incor[i]!=0) odab++;
			}
			if (odab<prof.op_Quiz){//오답퀴즈로 풀 문제가 부족할 경우 돌아감
				System.out.println("오답 퀴즈로 풀 문제가 부족합니다.");
				return;
			}
		}
		loop = true;
		while(loop){
			System.out.println("어떤 퀴즈를 푸시겠습니까?");
			System.out.print("[1] 뜻 맞추기 [2] 영단어 맞추기\n선택: ");
			
			switch (INPUT.nextLine()){
			case "1": type += 0; loop = false; break;
			case "2":  type += 2; loop = false; break;
			default : System.out.print(Message.Error);//예외: 숫자 입력 루프로
			}
		}
		//type: 0=랜덤뜻, 1=오답뜻, 2=랜덤영단어, 3=오답영단어
		
		int[] random = new int[prof.op_Quiz];//퀴즈로 풀 단어를 가져올 배열
		for(int i = 0; i<prof.op_Quiz; i++){		
			while(true){
				random[i] = (int)(Math.random()*dict.num);
				if (type%2 == 1){//오답모드
					if(dict.incor[random[i]]==0) {continue;}//틀린적 없으면 다시 
				}
				boolean again = false;
				for(int j = 0; j<i; j++){
					if(random[j]==random[i]) {again = true;}//앞에 뽑은것에 같은게 있으면 다시 
				}
				if(again) continue;
				break;//걸리는게 없으면 다음 단어를 뽑으러
			}
		}
		
		if(type <2){
			for(int i = 0; i<prof.op_Quiz; i++){
				System.out.println("["+ (i+1) + "번째 문제]");			
				System.out.print(dict.lan1[random[i]]+ ": ");
				if (INPUT.next().equals(dict.lan2[random[i]])){
					System.out.println("정답!");
					dict.cor[random[i]]++;
					cor++;
				}
				else{
					System.out.println("오답! 정답: " + dict.lan2[random[i]]);
					dict.incor[random[i]]++;
				}	
			}	
		}
		else{
			for(int i = 0; i<prof.op_Quiz; i++){
				System.out.println("["+ (i+1) + "번째 문제]");			
				System.out.print(dict.lan2[random[i]]+ ": ");
				if (INPUT.next().equals(dict.lan1[random[i]])){
					System.out.println("정답!");
					dict.cor[random[i]]++;
					cor++;
				}
				else{
					System.out.println("오답! 정답: " + dict.lan1[random[i]]);
					dict.incor[random[i]]++;
				}	
			}	
		}
		System.out.println("총 "+prof.op_Quiz+" 문제 중 " +cor +" 문제 정답");	
		scor.quiz(cor, type);//점수저장
		INPUT.nextLine();//입력버퍼에 남은 줄바꿈 제거
	}
	static void game(){
		if(dict.num == 0){//단어가 부족할 경우 돌아감
			System.out.println("단어가 없습니다. 단어를 추가하세요.");
			return;
		}
		
		int life = 0;
		int sco = 0;
		System.out.print(Message.line);
		System.out.println("단어 게임을 시작합니다.");
		System.out.println("[1번째 문제]");
		while(true){//새로운 단어로 반복문
			String s = dict.lan1[(int)(Math.random()*dict.num)];
			life += 3;//새로운 단어를 풀 때마다 생명력 공급
			
			int a = (int)Math.pow(2.0, (int)(Math.random()*s.length()));
			while(true){//단어 맞추기 반복문
				System.out.print("  ");
				for(int i=0;i<s.length();i++){
					if((a&(int)Math.pow(2.0, i))!=0){
						System.out.print(s.charAt(i));
					}
					else System.out.print("_");
				}
				System.out.println("  | 생명력 " + life +" | 점수 " + sco);
				
				if(a == (int)Math.pow(2.0,s.length())-1){//단어 맞춤, 새 단어로
					System.out.println("정답!\n");
					sco++;
					System.out.println("["+ (sco+1) +"번째 문제]");
					break;
				}
				
				System.out.print("입력: ");
				String k = INPUT.next();
				
				int temp=0;
				for(int i=0; i<s.length();i++){//일치하는 문자 확인
					if((s.substring(i,i+1)).compareToIgnoreCase(k)==0){
						if((a&(int)Math.pow(2.0, i))==0){
							a+=(int)Math.pow(2.0,i);
						}
						temp++;
					}
				}
				if(temp==0){//일치하는 문자 없을 때
					life--;
					if(life <= 0){//생명력 0일 때
						System.out.println("[Game Over]");
						System.out.println("생명력이 0이 되었습니다.");
						scor.game(sco);//점수 저장
						INPUT.nextLine();//입력버퍼에 남은 줄바꿈 제거
						return;
					}
				}
				
			}	
		}
	}
	
	//옵션 메뉴 기능
	static void opQuiz(){
		// Profile.op_Quiz을 보여주고 변경하는 코드
		// 0 이하 값 입력시 System.out.println(Message.Error); 하고 반복
		System.out.print(Message.line);
		System.out.println("퀴즈로 풀 문제 개수가 " + prof.op_Quiz+"개로 설정되어 있습니다.");
		System.out.println("얼마로 바꾸시겠습니까?");
		while(true){
			System.out.print("입력: ");
			int temp = getInt(INPUT.nextLine());
	
			if(temp > 0){
				prof.op_Quiz = temp;
				break;
			}
			else System.out.print(Message.Error);
		}
		System.out.println("퀴즈로 풀 문제 개수가 " + prof.op_Quiz+"개로 설정되었습니다.");
	}
	static void opAutoDel() {	
		// Profile.op_AutoDel을 보여주고 변경하는 코드
		// 이상한 값 입력시 System.out.println(Message.Error); 하고 반복
		System.out.print(Message.line);
		System.out.println("자동 삭제할 맞춘 횟수가 " + prof.op_AutoDel+"회로 설정되어 있습니다.");
		System.out.println("얼마로 바꾸시겠습니까?");
		while(true){
			System.out.print("입력: ");
			int temp = getInt(INPUT.nextLine());
			 
			if(temp > 0){
				prof.op_AutoDel = temp;
				break;
			}
			else System.out.print(Message.Error);
		}
		System.out.println("자동 삭제할 맞춘 횟수가 " + prof.op_AutoDel+"회로 설정되었습니다.");
	}
	static void opScore() {
		// Profile.op_Score 값을 고려해서
		// 백분율/분수식 상태를 보여주고 변경하는 코드
		// 이상한 값 입력시 System.out.println(Message.Error); 하고 반복
		System.out.print(Message.line);
		System.out.print("점수 표시 방식이 ");
		if (prof.op_Score) {System.out.print("백분율");}
		else {System.out.print("분수식으");}
		System.out.println("로 설정되어 있습니다.");
		System.out.println("[1] 백분율 [2] 분수식");
		
		boolean loop = true;
		while(loop){
			System.out.print("변경: ");
		
			switch (INPUT.nextLine()){
			case "1": prof.op_Score = true; loop = false; break;
			case "2": prof.op_Score = false; loop = false; break;
			default : System.out.print(Message.Error);//예외: 숫자 입력 루프로
			}
		}
		System.out.print("점수 표시 방식이 ");
		if (prof.op_Score) {System.out.print("백분율");}
		else {System.out.print("분수식으");}
		System.out.println("로 설정되었습니다.");		
	}
	static void opChagePW() {
		System.out.print(Message.line);
		System.out.println("비밀번호를 다시 확인합니다.");
		while(true){//비밀번호 입력 루프
			System.out.print("비밀번호: ");
					
			if (prof.pwComp(INPUT.nextLine())){//비밀번호 맞음
				System.out.println("변경할 비밀번호를 입력하세요.");
				System.out.print("비밀번호: ");
				prof.pw(INPUT.nextLine());
				System.out.println("비밀번호가 변경되었습니다.");
				return;
			}
			else System.out.println("비밀번호가 틀렸습니다. 다시 입력하세요.");//비밀번호 입력 루프로
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
	//저장, 불러오기
	static void save() throws FileNotFoundException, IOException{
		File myDir = new File("data");
		myDir.mkdir();//data 폴더 생성
		
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
	
	static int getInt(String s){//입력받은 문자열이 숫자만 있을경우 그 숫자 반환, 그 외에 -1 반환
		for(int i = 0; i< s.length(); i++){
			if(s.charAt(i)<'0'||'9'<s.charAt(i)){//숫자가 아닌것 포함
				return -1;//-1을 반환
			}
		}
		return Integer.parseInt(s);//숫자만 입력시
	}
	
	
	//숨은 게임
	static void AHJ(){
		System.out.print(Message.line);
		System.out.println("당신은 개발자의 이름을 건드렸습니다.");
		System.out.println("1~50 사이의 수를 다섯번 안에 맞춰야 도망칠 수 있습니다.");
		boolean incor = true;
		while(incor){
			int temp = (int)(1 + Math.random()*50);
			for(int i=5; i>0; i--){
				System.out.print("숫자는? (기회 " + i +"번): ");
				int temp2 = INPUT.nextInt();
				if(temp == temp2){
					System.out.println("흠.. 맞췄군!");
					incor = false; break;
				}
				else if (temp < temp2){
					System.out.println("틀렸다! 그것보다 작다!");
				}
				else{
					System.out.println("틀렸다! 그것보다 크다!");
				}
			}
			if(incor){
				System.out.println("다섯번 만에 못맞추다니, 다시!");
			}
		}
	}
	
	static void TILE(){
		int[][] a = new int[5][5];
		int num = 0, time =0, goal = 0, goal2 = 0, goal3 = 0;
		int ran, clear = 0;
		boolean error = false;
			
		System.out.print("                                 - 게임 방법 -\n");
		System.out.print("           숫자를 선택하면 해당하는 화살표 방향의 타일들이 뒤집힙니다.\n");
		System.out.print("                    최대한 빠르게 타일 뒤집기에 도전하세요!\n\n\n");
		
		for (num = 0; num < 11; num++){//첫판일 때 열가지 방향의 뒤집기 여부 조절
			ran = (int)(Math.random()*2);
			if (ran == 1){//랜덤해서 1일 경우에만 뒤집어둠
				if (num == 5)
					goal++;//대각선은 무조건 풀어야만 깨지므로 목표횟수에 +1
				else if (num < 5)
					goal2++;//가로방향 뒤집힌 횟수
				else
					goal3++;//세로방향 뒤집힌 횟수
				for (int m = 0; m < 5; m = m + 1){//25가지 방향에서
					for (int n = 0; n < 5; n = n + 1){
						if (0 <= num && num <= 4){//0~4까지는 m과 num이 같을 때 뒤집힘
							if (m == num)
								a[m][n] = 1-a[m][n];
						}
						else if (num == 5){//5일때는 m+n이 4일 때 뒤집힘
							if (m + n == 4)
								a[m][n] = 1-a[m][n];
						}
						else if (6 <= num && num <= 10){//6~10까지는 n이 num-6과 같을 때 뒤집힘
							if (n == num - 6)
								a[m][n] = 1-a[m][n];
						}
					}
				}
			}
		}
		
		if (goal2 > 2) goal2 = 5 - goal2;//세로방향이 모두 뒤집혔거나 아닐 때 같아지므로 5-뒤집힌 수
		if (goal3 > 2) goal3 = 5 - goal3;//가로방향도 위와 같음	
		goal = goal + goal2 + goal3;//대각선 + 가로방향 같게하는 최소, 세로방향
			
		while (true){// 반복
			clear = 0;//클리어 확인 하는 값 초기화
			for (int m = 0; m < 5; m = m + 1){//화면에 배치
				System.out.printf("                                %d→", m);
				for (int n = 0; n < 5; n = n + 1){
					if (0 <= num && num <= 4){//선택한 번호를 따라 뒤집어줌, 처음에 랜덤하게 뒤집은 뒤 num값이 11이므로 num값을 바꿔주지 않아도 된다.
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
					switch (a[m][n]){//타일배치
					case 0:
						System.out.print("□");
						break;
					case 1:
						System.out.print("■");
						clear++;
						break;
					}
				}
				System.out.println();
			}
			System.out.printf("                                 ↗↑↑↑↑↑                현재 횟수 : %d\n", time);
			System.out.printf("                                5  6 7 8 9 10                목표 횟수 : %d\n\n", goal);
			if (clear == 0 || clear == 25){//클리어, 처음으로
				System.out.print("                                      클리어!");
				time = goal = goal2 = goal3 = 0;//뒤집은 횟수와, 목표 횟수 초기화
				break;//루프를 깨고 돌아감
			}
			if (error){//이전에 잘못입력한경우
				System.out.print("                       0 ~ 10 사이의 숫자를 선택해주세요.\n\n\n                                       ");
				error = false;//에러값 0으로 돌림
			}
			else{//이전에 잘못 입력하지 않은 경우
				System.out.printf("                             숫자를 선택해주세요.\n\n\n\n\n\n\n\n\n\n                                       ");
			}
		
			num = getInt(INPUT.nextLine());//뒤집을 방향 입력
			System.out.print("\n\n\n\n\n\n");
			if (num < 0 || num>10){//숫자 잘못 입력, 다시
				error = true;//에러값1로 변경
				System.out.print("\r\b\b");
				continue;//반복문 처음으로
			}
			else time++;//숫자 제대로 입력, 뒤집기 횟수 추가
		}
	}
}