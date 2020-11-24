import java.io.*;
import java.util.Scanner;

public class Profile implements Serializable{ //object 저장을 위해 직렬화 한 프로필 클래스
	//사용자 정보
	private String ID;
	private String PW;	
	//옵션값들
	int op_Quiz = 10;
	int op_AutoDel = 20;
	boolean op_Score = true;//true : 백분율, false : 분수식
	
	public boolean pwComp(String inputpw){ //비밀번호 비교
		if(inputpw.compareTo(PW) == 0) return true;
		return false;
	}
	public String getID(){//이름 불러오기
		return ID;
	}
	public void pw(String newPW){//비밀번호 지정
		PW = newPW;
	}
	public void id(String newID){//아이디 지정
		ID = newID;
	}
}