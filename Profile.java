import java.io.*;
import java.util.Scanner;

public class Profile implements Serializable{ //object ������ ���� ����ȭ �� ������ Ŭ����
	//����� ����
	private String ID;
	private String PW;	
	//�ɼǰ���
	int op_Quiz = 10;
	int op_AutoDel = 20;
	boolean op_Score = true;//true : �����, false : �м���
	
	public boolean pwComp(String inputpw){ //��й�ȣ ��
		if(inputpw.compareTo(PW) == 0) return true;
		return false;
	}
	public String getID(){//�̸� �ҷ�����
		return ID;
	}
	public void pw(String newPW){//��й�ȣ ����
		PW = newPW;
	}
	public void id(String newID){//���̵� ����
		ID = newID;
	}
}