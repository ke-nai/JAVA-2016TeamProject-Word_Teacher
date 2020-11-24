public interface Message{

	public static String line//절취선
	="-------------------------------------------------------------------------------\n";
	public static String Start_Menu
	=line
	+"                           ●    Word Teacher    ●\n"
	+line
	+"[1] 로그인                 [2] 새 프로필                [3] 종료\n"
	+"선택: ";
	public static String Main_Menu
	=line
	+"                                  - Menu -\n"
	+line
	+"[1] 새 단어                [2] 단어 퀴즈                [3] 단어 게임\n"
	+"[4] 점수 보기              [5] 옵션                     [6] 로그아웃\n"
	+"선택: ";
	public static String Option
	=line
	+"                                 - Option -\n"
	+line
	+"[1] 퀴즈 갯수 변경         [2] 단어 자동 삭제           [3] 점수 표시 방식\n"
	+"[4] 비밀번호 변경          [5] 프로필 삭제              [6] 뒤로 가기\n"
	+"선택: ";
	public static String Error
	=line
	+"\n                      [ Error: 유효하지 않은 선택입니다! ]\n\n";
	public static String EXIT
	=line
	+"                          [ 프로그램을 종료합니다. ]";
	public static String Logout
	=line
	+"                           [ 로그아웃 되었습니다. ]";
}