import java.io.*;
import java.util.Scanner;

public class Diction implements Serializable{
	int num = 0;//�� �ܾ� ����
	String[] lan1 = new String[0];//����
	String[] lan2 = new String[0];//�ѱ�
	int[] cor = new int[0];
	int[] incor = new int[0];
	
	public void addWord(){
		Scanner scan = new Scanner(System.in);
		System.out.print(Message.line);
		System.out.println("�� �ܾ �Է��մϴ�. (-1�� �Է��ϸ� �Է� ����)");
		while(true){
			System.out.print("���� �ܾ�: ");
			String newlan1 = scan.next();
			if (newlan1.compareTo("-1")==0){
				System.out.println("�ܾ� �Է��� �����մϴ�.");
				return ;
			}
			System.out.print("�ѱ� ��: ");
			String newlan2 = scan.next();
			if (newlan2.compareTo("-1")== 0){
				System.out.println("�ܾ� �Է��� �����մϴ�.");
				return ;
			}
			
			num++;//��ü �ܾ� �� ����
			
			String[] temp1 = lan1.clone(); String[] temp2 = lan2.clone();
			int[] temp3 = cor.clone(); int[] temp4 = incor.clone();
			lan1 = new String[num]; lan2 = new String[num];
			cor = new int[num]; incor = new int[num];
			
			for(int i = 0; i < num-1; i++){
			    lan1[i] = temp1[i]; lan2[i] = temp2[i];
			    cor[i] = temp3[i]; incor[i] = temp4[i];
			}//��ĭ �ø� �迭�� ������ �迭 ����
			lan1[num-1] = newlan1; lan2[num-1] = newlan2; //���� ���� �ܾ� ����
		}
	}
	public void autoDel(){
		for(int i=0; i<num; i++){
			if (cor[i]>=WordTeacher.prof.op_AutoDel){//�ڵ� ������ ���� Ƚ���� ���� ���
				num--;//��ü �ܾ� �� ����
				
				String[] temp1 = lan1.clone(); String[] temp2 = lan2.clone();
				int[] temp3 = cor.clone(); int[] temp4 = incor.clone();
				lan1 = new String[num]; lan2 = new String[num];
				cor = new int[num]; incor = new int[num];
				for(int j=0; j<i; j++){
					lan1[j] = temp1[j]; lan2[j] = temp2[j];
					cor[j] = temp3[j]; incor[j] = temp4[j];
				}//i�� ���������� �״�� ����
				for(int j=i; j<num; j++){
					lan1[j] = temp1[j+1]; lan2[j] = temp2[j+1];
					cor[j] = temp3[j+1]; incor[j] = temp4[j+1];
				}//i�����ʹ� ��ĭ�� ���		
			}
		}
	}
}