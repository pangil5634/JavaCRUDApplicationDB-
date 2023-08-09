package com.mycom.word;

//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection; // 추가됌.
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class WordCRUD implements ICRUD{
	
	// 전역 변수 선언 
	// SQL 구문을 담는 문자열 변수
	final String WORD_SELECTAll = "select * from Dictonary"; 
	final String WORD_SELECT = "select * from Dictonary where word like ? ";
	final String WORD_INSERT = "insert into Dictonary (level, word, meaning, regdate) "
			+ "values (?, ?, ?, ?) ";
	final String WORD_UPDATE = "update dictonary set meaning=? where id=? ";
	final String WORD_DELETE = "delete from dictonary where id = ?";
	
	ArrayList<Word> list; // 데이터를 저장하고 있는 ArrayLis
	Scanner sc;
	final String fname = "Dictonary.txt"; // 파일 이름을 저장하고 있는 문자열 변수
	Connection conn; // SQL구문을 위한 Connection
	
	
	
	// 생성자
	WordCRUD(Scanner sc) {
		list = new ArrayList<>(); //Word Type의 ArrayList 생성
		this.sc = sc;
		conn = DBConnection.getConnection(); 
	}
	
	
	
	// 날짜를 Return 해주는 메소드
	public String getCurrentDate() {
		LocalDate now = LocalDate.now();
		
		DateTimeFormatter f =  DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return f.format(now); // 현재 시간이 포맷된다.
	}
	
	
	
	@Override
	// Query문을 통해 데이터를 추가하는 메소드
	public int add(Word one) {
		// TODO Auto-generated method stub
		
		PreparedStatement pstmt;
		
		int retval = 0;
		try {
			pstmt = conn.prepareStatement(WORD_INSERT);
			pstmt.setInt(1, one.getLevel());
			pstmt.setString(2, one.getWord());
			pstmt.setString(3, one.getMeaning());
			pstmt.setString(4, getCurrentDate());
			retval = pstmt.executeUpdate();
			pstmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retval;
	}
	
	
	// 입력 받은 데이터를 리스트에 추가하는 메소드
	public void addItem() {
		
		// 난이도 입력 받기
		System.out.print("=> 난이도 (1, 2, 3) & 새 단어 입력 : ");
		int level = sc.nextInt();
		String word = sc.nextLine();
		
		
		// 뜻 입력 받기
		System.out.print("뜻 입력 : ");
		String meaning = sc.nextLine();
		
		// 새로운 단어 생성하고, 단어 등록하기
		Word one = new Word(0, level, word, meaning);
		int retval = add(one);
		
		if(retval > 0)
			System.out.println("새 단어가 단어장에 추가되었습니다.\n");
		else 
			System.out.println("새 단어 추가중 에러가 발생되었습니다.\n");
	}

	
	
	@Override
	// Query문을 통해 데이터를 수정하는 메소드
	public int update(Word one) {
		// TODO Auto-generated method stub
		
		PreparedStatement pstmt;
		
		int retval = 0;
		try {
			pstmt = conn.prepareStatement(WORD_UPDATE);
			pstmt.setString(1, one.getMeaning());
			pstmt.setInt(2, one.getId());
			retval = pstmt.executeUpdate();
			pstmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retval;
	}

	


	// 단어를 수정하는 메소드
	public void updateItem() {
		// TODO Auto-generated method stub
		
		// 수정할 단어를 입력 받는다.
		System.out.print("=> 수정할 단어 검색 : ");
		String keyword = sc.next();
		
		// 해당 단어가 있는 케이스를 출력해준다.
		listAll(keyword);
		
		// 수정할 번호를 입력받는다.
		System.out.print("=> 수정할 번호 선택 : ");
		int id = sc.nextInt();
		sc.nextLine();
		
		// 수정하고자 하는 단어의 뜻을 입력 받는다.
		System.out.print("=> 뜻 입력 : ");
		String meaning = sc.nextLine();
		
		int val = update(new Word(list.get(id-1).getId(),0, "", meaning  ));
		
		if(val > 0)
			System.out.println("단어가 수정되었습니다.");
		else 
			System.out.println("[수정] 에러 발생!!");
		
	}

		
		
	@Override
	// Query문을 통해 데이터를 삭제하는 메소드
	public int delete(Word one) {
		// TODO Auto-generated method stub
		
		PreparedStatement pstmt;
		
		int retval = 0;
		try {
			pstmt = conn.prepareStatement(WORD_DELETE);
			pstmt.setInt(1, one.getId());
			retval = pstmt.executeUpdate();
			pstmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retval;
	}
		
		
		
	// 단어를 삭제하는 메소드
	public void deleteItem() {
		// TODO Auto-generated method stub	
			
		// 삭제할 단어 입력 받기
		System.out.print("=> 삭제할 단어 검색 : ");
		String keyword = sc.next();
			
		// 해당 단어가 있는 케이스를 출력해준다.
		listAll(keyword);
				
		// 삭제할 단어의 번호를 입력 받는다.
		System.out.print("=> 삭제할 번호 선택 : ");
		int id = sc.nextInt();
		sc.nextLine(); // 남아 있는 공백을 없애기 위한 구문
			
			
		// 삭제 전 재확인을 묻는다.
		System.out.print("=> 정말로 삭제하실래요? (Y/N) : ");
		String answer = sc.next();
			
		// Y가 입력되었을 경우 단어를 삭제한다. 
		if(answer.equalsIgnoreCase("Y")) { //equalsIgnoreCase의 경우 대소문자를 무시하고 비교한다.		
			
			int val = delete(new Word(list.get(id-1).getId(),0, "", ""  ));
			
			if(val > 0)
				System.out.println("단어가 삭제되었습니다.");
			else 
				System.out.println("[삭] 에러 발생!!");
		} else {
			System.out.println("취소되었습니다.");
		}			
	}
		

	
	// keyword를 갖고 있는 데이터만 출력하는 메소드
	public void listAll(String keyword) {
		LoadData(keyword);
		System.out.println("------------------------------");
		for(int i = 0; i < list.size(); i++) {
			System.out.printf("%-2d ", i+1);
			System.out.println(list.get(i).toString());
		}
		System.out.println("------------------------------");
	}
		
	
	
	// 레벨에 맞는 단어를 출력하는 메소드
	public void listAll(int level) {
		int count = 0;
		
		System.out.println("------------------------------");
		for(int i = 0; i < list.size(); i++) {
			int ilevel = list.get(i).getLevel(); // 영어 단어 가져오기
			
			if(level == ilevel) {
				System.out.print(count+1 + " ");
				System.out.println(list.get(i).toString());
				count++;
			} else {
				continue;
			}
		}
		System.out.println("------------------------------");
	}
	
	
	
	// 레벨을 입력 받아 해당 레벨의 정보를 출력하는 메소드
	public void searchLevel() {
		// TODO Auto-generated method stub
		
		System.out.print("=> 원하는 레벨은? (1~3) ");
		int level = sc.nextInt();
		listAll(level); // int형 listAll 메소드 호출 
	}
	
	

	//단어를 입력 받아 해당 단어를 포함하는 정보를 출력하는 메소드
	public void searchWord() {
		// TODO Auto-generated method stub
		System.out.print("=> 원하는 단어는? ");
		String keyword = sc.next();
		listAll(keyword); // String형 listAll 메소드 호출 
	}
	
	
	
	// DB 파일을 Query문으로 접근하여 Load하는 메소드
	public void LoadData(String keyword) {
		
		list.clear(); // 리스트의 데이터를 초기화해준다.
		
		try {
			// 변수 선언
			PreparedStatement stmt ;
			ResultSet rs;
			
			if(keyword.equals("")) {
				stmt = conn.prepareStatement(WORD_SELECTAll);
				rs =  stmt.executeQuery();
			} else {
				stmt = conn.prepareStatement(WORD_SELECT);
				stmt.setString(1,  "%" + keyword + "%");
				rs = stmt.executeQuery();
			}
			
			
			while(true) {
				if(rs.next()) {// 현재 데이터가 있는지 알 수 있다.
					
					// 임시 변수에 데이터를 저장한다.
					int id = rs.getInt("id");
					int level = rs.getInt("level");
					String word = rs.getString("word");
					String meaning = rs.getString("meaning");
					
					// 저장한 데이터를 가지고 Word라는 객체를 만들고, 객체를 list에 추가한다.
					list.add(new Word(id, level, word, meaning));
				} else {
					break;
				}
			}
			
			// 사용한 statment, resultSet를 닫아준다.
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	// 데이터를 파일로 저장하는 메소드
	public void saveFile() throws IOException {
		// TODO Auto-generated method stub
		
		// 변수 선언
		PrintWriter pr = new PrintWriter(new FileWriter(fname));
		
		// 파일에 저장하는 반복문
		for(Word one : list) {
			pr.write(one.toFileString() + "\n"); // write는 마지막에 개행문자가 없으므로 넣어주어야 한다.
		}
		
		// 파일을 오픈한 변수를 닫아준다. (사용 종료)
		pr.close();
		System.out.println("===> 데이터 저장 완료!");
		
	}

}
