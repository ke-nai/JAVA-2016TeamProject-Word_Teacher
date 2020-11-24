import java.io.*;
import java.util.Date;


public class Score implements Serializable{
	//���� ���ھ� ����
	int[] quiz_cor = new int[5];
	int[] quiz_all = new int[5];
	Date[] quiz_date = new Date[5];
	int[] quiz_type = new int[5];//type: 0=������, 1=�����, 2=�������ܾ�, 3=���俵�ܾ�
	//���� ���ھ� ����
	int[] game_sco = {-1,-1,-1,-1,-1};
	Date[] game_date = new Date[5];
	
	//���ο� ���ھ� ���
	public void quiz(int cor, int type){
		//[0~3]�� [1~4]�� �ű�� [0]�� ���ο� �� ���� 
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
	
	//���ھ� �����ֱ�
	public void show(){
		System.out.print(Message.line);
		System.out.println("[���� ����]");
		for(int i = 0; i<5; i++){
			if (quiz_all[i] ==0) break;//������ ������ �Ѿ��
			
			if(quiz_type[i] == 0){
				System.out.print("����, �� ���� | ");}
			else if(quiz_type[i] == 1){
				System.out.print("����, �� ���� | ");}
			else if(quiz_type[i] == 2){
				System.out.print("����, ���ܾ� ���� | ");}
			else{
				System.out.print("����, ���ܾ� ���� | ");}
	
			if (WordTeacher.prof.op_Score){//�����
				System.out.print(100*quiz_cor[i]/quiz_all[i] + "��");
			}
			else{
				System.out.print(quiz_cor[i]+"/"+quiz_all[i]);
			}
			System.out.println(" | " + quiz_date[i].toString());
		}
		
		System.out.println("[���� ����]");
		for(int i = 0; i<5; i++){
			if(game_sco[i] == -1) break;//������ ������ �Ѿ��
			System.out.println(game_sco[i] +"�� | "+ game_date[i].toString());
		}
	}
}