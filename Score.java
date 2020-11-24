import java.io.*;
import java.util.Date;


public class Score implements Serializable{
	//퀴즈 스코어 정보
	int[] quiz_cor = new int[5];
	int[] quiz_all = new int[5];
	Date[] quiz_date = new Date[5];
	int[] quiz_type = new int[5];//type: 0=랜덤뜻, 1=오답뜻, 2=랜덤영단어, 3=오답영단어
	//게임 스코어 정보
	int[] game_sco = {-1,-1,-1,-1,-1};
	Date[] game_date = new Date[5];
	
	//새로운 스코어 기록
	public void quiz(int cor, int type){
		//[0~3]을 [1~4]로 옮기고 [0]에 새로운 값 대입 
		for(int i = 4; i>0; i--){
			quiz_cor[i] = quiz_cor[i-1];
			quiz_all[i] = quiz_all[i-1];
			quiz_date[i] = quiz_date[i-1];
			quiz_type[i] = quiz_type[i-1];
		}
		quiz_cor[0] = cor;
		quiz_all[0] = WordTeacher.prof.op_Quiz;
		quiz_date[0] = new Date();
		quiz_type[0] = type;
	}
	public void game(int sco){
		for(int i = 4; i>0; i--){
			game_sco[i] = game_sco[i-1];
			game_date[i] = game_date[i-1];
		}
		game_sco[0] = sco;
		game_date[0] = new Date();
	}
	
	//스코어 보여주기
	public void show(){
		System.out.print(Message.line);
		System.out.println("[퀴즈 점수]");
		for(int i = 0; i<5; i++){
			if (quiz_all[i] ==0) break;//점수가 없으면 넘어가기
			
			if(quiz_type[i] == 0){
				System.out.print("랜덤, 뜻 퀴즈 | ");}
			else if(quiz_type[i] == 1){
				System.out.print("오답, 뜻 퀴즈 | ");}
			else if(quiz_type[i] == 2){
				System.out.print("랜덤, 영단어 퀴즈 | ");}
			else{
				System.out.print("오답, 영단어 퀴즈 | ");}
	
			if (WordTeacher.prof.op_Score){//백분율
				System.out.print(100*quiz_cor[i]/quiz_all[i] + "점");
			}
			else{
				System.out.print(quiz_cor[i]+"/"+quiz_all[i]);
			}
			System.out.println(" | " + quiz_date[i].toString());
		}
		
		System.out.println("[게임 점수]");
		for(int i = 0; i<5; i++){
			if(game_sco[i] == -1) break;//점수가 없으면 넘어가기
			System.out.println(game_sco[i] +"점 | "+ game_date[i].toString());
		}
	}
}