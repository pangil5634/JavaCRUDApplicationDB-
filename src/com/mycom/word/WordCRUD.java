package com.mycom.word;

//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection; // 추가됌.
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class WordCRUD implements ICRUD{
	
	// 전역 변수 선언 
	final String selectall = "select * from Dictonary"; // SQL 구문을 담는 문자열 변수
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
	//
	
	
	
	@Override
	// 사용자에게 입력 받는 메소드 
	public Object add() {
		// TODO Auto-generated method stub
		
		
		// 난이도 입력 받기
		System.out.print("=> 난이도 (1, 2, 3) & 새 단어 입력 : ");
		int level = sc.nextInt();
		String word = sc.nextLine();
		
		
		// 뜻 입력 받기
		System.out.print("뜻 입력 : ");
		String meaning = sc.nextLine();
		
		
		// 단어를 생성해서 return한다.
		return new Word(0, level, word, meaning);
	}
	
	// 입력 받은 데이터를 리스트에 추가하는 메소드
	public void addItem() {
		Word one = (Word) add(); // casting
		list.add(one);
		System.out.println("새 단어가 단어장에 추가되었습니다.\n");
	}

	@Override
	public int update(Object obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Object obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void selectOne(int id) {
		// TODO Auto-generated method stub
		
	}
	
	public void listAll() {
		System.out.println("------------------------------");
		for(int i = 0; i < list.size(); i++) {
			System.out.printf("%-2d ", i+1);
			System.out.println(list.get(i).toString());
		}
		System.out.println("------------------------------");
	}
	
	
	// 해당 키워드를 갖고 있는 리스트를 출력하는 메소드
	public ArrayList<Integer> listAll(String keyword) {
		
		ArrayList<Integer> idlist = new ArrayList<>();
		int count = 0;
		
		System.out.println("------------------------------");
		for(int i = 0; i < list.size(); i++) {
			String word = list.get(i).getWord(); // 영어 단어 가져오기
			
			if(word.contains(keyword)) {
				System.out.print(count+1 + " ");
				System.out.println(list.get(i).toString());
				idlist.add(i);
				count++;
			} else {
				continue;
			}
		}
		System.out.println("------------------------------");
		
		return idlist;
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
	
	
	
	
	// 단어를 수정하는 메소드
	public void updateItem() {
		// TODO Auto-generated method stub
		
		// 수정할 단어를 입력 받는다.
		System.out.print("=> 수정할 단어 검색 : ");
		String keyword = sc.next();
		
		// 해당 단어가 있는 케이스를 출력하고, 그 단어들의 index 번호를 저장하는 ArrayList를 생성한다.
		ArrayList<Integer> idlist = this.listAll(keyword);
		
		
		// 수정할 번호를 입력받는다.
		System.out.print("=> 수정할 번호 선택 : ");
		int id = sc.nextInt();
		sc.nextLine();
		
		// 수정하고자 하는 단어의 뜻을 입력 받는다.
		System.out.print("=> 뜻 입력 : ");
		String meaning = sc.nextLine();
		
		// 
		Word word = list.get(idlist.get(id-1));
		
		word.setMeaning(meaning); // 동일한 주소값을 갖고 있기 때문에 자동적으로 변하는 것 같다. 
		System.out.println("단어가 수정되었습니다.");
		
	}

	
	// 단어를 삭제하는 메소드
	public void deleteItem() {
		// TODO Auto-generated method stub
		
		
		// 삭제할 단어 입력 받기
		System.out.print("=> 삭제할 단어 검색 : ");
		String keyword = sc.next();
		
		// 해당 단어가 있는 케이스를 출력하고, 그 단어들의 index 번호를 저장하는 ArrayList를 생성한다.
		ArrayList<Integer> idlist = this.listAll(keyword);
		
		// 삭제할 단어의 번호를 입력 받는다.
		System.out.print("=> 삭제할 번호 선택 : ");
		int id = sc.nextInt();
		sc.nextLine(); // 남아 있는 공백을 없애기 위한 구문
		
		
		// 삭제 전 재확인을 묻는다.
		System.out.print("=> 정말로 삭제하실래요? (Y/N) : ");
		String answer = sc.next();
		
		// Y가 입력되었을 경우 단어를 삭제한다. 
		if(answer.equalsIgnoreCase("Y")) { //equalsIgnoreCase의 경우 대소문자를 무시하고 비교한다.
			list.remove((int)idlist.get(id-1));
			System.out.println("단어가 삭제되었습니다.");
		} else {
			System.out.println("취소되었습니다.");
		}	
		
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
	
	
	
	
	/*
	// 입력된 파일이름으로부터 데이터를 load하는 메소드
	public void loadFile() throws IOException, FileNotFoundException {
		
		BufferedReader br = new BufferedReader(new FileReader(fname));
			
		String line = null;
			
		int count = 0;
			
		while(true) {
			line = br.readLine();
				
			if(line == null)
				break;
				
			String data[] = line.split("\\|");
				
			int level = Integer.parseInt(data[0]);
			String word = data[1];
			String meaning = data[2];
			list.add(new Word(0, level, word, meaning));
			count++;
				
		}
			
		br.close();
		System.out.println("===> "  + count + "개 로딩완료!");	

	}
	*/

	
	
	// 데이터를 파일로 저장하는 메소드
	public void saveFile() throws IOException {
		// TODO Auto-generated method stub
		
		// 변수 선언
		// PrintWriter pr = new PrintWriter(new FileWriter("test.txt"));
		PrintWriter pr = new PrintWriter(new FileWriter(fname));
		
		// 파일에 저장하는 반복문
		for(Word one : list) {
			pr.write(one.toFileString() + "\n");
			// write는 마지막에 개행문자가 없으므로 넣어주어야 한다.
		}
		
		// 파일을 오픈한 변수를 닫아준다. (사용 종료)
		pr.close();
		System.out.println("===> 데이터 저장 완료!");
		
	}
	
	
	
	// DB 파일을 Query문으로 접근하여 Load하는 메소드
	public void LoadData() {
		
		list.clear(); // 리스트의 데이터를 초기화해준다.
		
		try {
			
			// 변수 선언
			Statement stmt = conn.createStatement(); // Connection으로 연결한 객체에게, Query 작업을 실행하기 위한 객체이다. 
			ResultSet rs = stmt.executeQuery(selectall); // Query문을 통해서 얻은 SELECT문의 결과를 저장하는 객체.
			
			
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
	


}
