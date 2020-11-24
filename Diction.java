import java.io.*;
import java.util.Scanner;

public class Diction implements Serializable{
	int num = 0;//총 단어 갯수
	String[] lan1 = new String[0];//영어
	String[] lan2 = new String[0];//한글
	int[] cor = new int[0];
	int[] incor = new int[0];
	
	public void addWord(){
		Scanner scan = new Scanner(System.in);
		System.out.print(Message.line);
		System.out.println("새 단어를 입력합니다. (-1을 입력하면 입력 종료)");
		while(true){
			System.out.print("영어 단어: ");
			String newlan1 = scan.next();
			if (newlan1.compareTo("-1")==0){
				System.out.println("단어 입력을 종료합니다.");
				return ;
			}
			System.out.print("한글 뜻: ");
			String newlan2 = scan.next();
			if (newlan2.compareTo("-1")== 0){
				System.out.println("단어 입력을 종료합니다.");
				return ;
			}
			
			num++;//전체 단어 수 증가
			
			String[] temp1 = lan1.clone(); String[] temp2 = lan2.clone();
			int[] temp3 = cor.clone(); int[] temp4 = incor.clone();
			lan1 = new String[num]; lan2 = new String[num];
			cor = new int[num]; incor = new int[num];
			
			for(int i = 0; i < num-1; i++){
			    lan1[i] = temp1[i]; lan2[i] = temp2[i];
			    cor[i] = temp3[i]; incor[i] = temp4[i];
			}//한칸 늘린 배열에 이전의 배열 저장
			lan1[num-1] = newlan1; lan2[num-1] = newlan2; //새로 받은 단어 저장
		}
	}
	public void autoDel(){
		for(int i=0; i<num; i++){
			if (cor[i]>=WordTeacher.prof.op_AutoDel){//자동 삭제할 맞춘 횟수를 넘은 경우
				num--;//전체 단어 수 감소
				
				String[] temp1 = lan1.clone(); String[] temp2 = lan2.clone();
				int[] temp3 = cor.clone(); int[] temp4 = incor.clone();
				lan1 = new String[num]; lan2 = new String[num];
				cor = new int[num]; incor = new int[num];
				for(int j=0; j<i; j++){
					lan1[j] = temp1[j]; lan2[j] = temp2[j];
					cor[j] = temp3[j]; incor[j] = temp4[j];
				}//i번 이전까지는 그대로 대입
				for(int j=i; j<num; j++){
					lan1[j] = temp1[j+1]; lan2[j] = temp2[j+1];
					cor[j] = temp3[j+1]; incor[j] = temp4[j+1];
				}//i번부터는 한칸씩 당김		
			}
		}
	}
}