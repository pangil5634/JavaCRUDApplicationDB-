package com.mycom.word;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	
	final String selectall = "select * from Dictonary";
	
	ArrayList<Word> list;
	Scanner sc;
	final String fname = "Dictonary.txt";
	Connection conn;
	
	// 생성자 : Word Type의 ArrayList 생성되게 끔 한다.
	WordCRUD(Scanner sc) {
		list = new ArrayList<>();
		this.sc = sc;
		conn = DBConnection.getConnection(); // 따로 인스턴스화 하지 않고 사용 가능 
	}
	
	public void LoadData() {
		
		list.clear(); // 리스트의 데이터를 초기화해준다.
		

		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(selectall);
			while(true) {
				if(rs.next()) {// 현재 데이터가 있는지 알 수 있다.
					int id = rs.getInt("id");
					int level = rs.getInt("level");
					String word = rs.getString("word");
					String meaning = rs.getString("meaning");
					list.add(new Word(id, level, word, meaning));
				} else {
					break;
				}
			}
			
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	// 사용자에게 입력 받는 메소드 
	public Object add() {
		// TODO Auto-generated method stub
		
		System.out.print("=> 난이도 (1, 2, 3) & 새 단어 입력 : ");
		int level = sc.nextInt();
		String word = sc.nextLine();
		
		System.out.print("뜻 입력 : ");
		String meaning = sc.nextLine();
		
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
	
	public void updateItem() {
		// TODO Auto-generated method stub
		System.out.print("=> 수정할 단어 검색 : ");
		String keyword = sc.next();
		
		ArrayList<Integer> idlist = this.listAll(keyword);
		
		System.out.print("=> 수정할 번호 선택 : ");
		int id = sc.nextInt();
		sc.nextLine();
		
		System.out.print("=> 뜻 입력 : ");
		String meaning = sc.nextLine();
		
		Word word = list.get(idlist.get(id-1));
		
		word.setMeaning(meaning);
		System.out.println("단어가 수정되었습니다.");
		
	}

	public void deleteItem() {
		// TODO Auto-generated method stub
		
		System.out.print("=> 삭제할 단어 검색 : ");
		String keyword = sc.next();
		
		ArrayList<Integer> idlist = this.listAll(keyword);
		
		System.out.print("=> 삭제할 번호 선택 : ");
		int id = sc.nextInt();
		sc.nextLine();
		
		System.out.print("=> 정말로 삭제하실래요? (Y/N) : ");
		String answer = sc.next();
		
		if(answer.equalsIgnoreCase("Y")) {
			list.remove((int)idlist.get(id-1));
			System.out.println("단어가 삭제되었습니다.");
		} else {
			System.out.println("취소되었습니다.");
		}	
		
	}
	
	/*
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

	public void saveFile() throws IOException {
		// TODO Auto-generated method stub
		
//		PrintWriter pr = new PrintWriter(new FileWriter("test.txt"));
		PrintWriter pr = new PrintWriter(new FileWriter(fname));
		
		for(Word one : list) {
			pr.write(one.toFileString() + "\n");
			// write는 마지막에 개행문자가 없으므로 넣어주어야 한다.
		}
		
		pr.close();
		System.out.println("===> 데이터 저장 완료!");
		
	}

	public void searchLevel() {
		// TODO Auto-generated method stub
		
		System.out.print("=> 원하는 레벨은? (1~3) ");
		int level = sc.nextInt();
		listAll(level);
		
	}

	public void searchWord() {
		// TODO Auto-generated method stub
		System.out.print("=> 원하는 단어는? ");
		String keyword = sc.next();
		listAll(keyword);
	}
	
}
