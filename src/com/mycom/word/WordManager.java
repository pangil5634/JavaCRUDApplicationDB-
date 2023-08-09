package com.mycom.word;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class WordManager {
	
	// 전역 변수 선언 
	Scanner sc = new Scanner(System.in);
	WordCRUD wordCRUD; // wordCRUD에 대한 메소드를 담고 있는 클래스 선
	
	
	
	// 생성자 
	WordManager(){
		wordCRUD = new WordCRUD(sc); // wordCRUD class 인스턴스화
	}
	
	
	
	// 전체적인 프로그램을 실행하는 main 구문
	public void start() throws FileNotFoundException, IOException {

		wordCRUD.LoadData(""); // DB 파일에 있는 데이터 로드하는 메소드 
		
		while(true) {
			int menu = selectMenu(); // 사용자로부터 메뉴를 입력 받아 저장하는 변수
			
			if(menu == 0) {
				break;
			} else if (menu == 1) {
				wordCRUD.listAll("");
			} else if (menu == 2) {
				wordCRUD.searchLevel();
			} else if (menu == 3) {
				wordCRUD.searchWord();
			} else if (menu == 4) {
				// create
				wordCRUD.addItem();
			} else if (menu == 5) {
				// update
				wordCRUD.updateItem();
			} else if (menu == 6) {
				// delete
				wordCRUD.deleteItem();
			} else if (menu == 7) {
				// file save
				wordCRUD.saveFile();		
			}
		}
	}
	
	
	
	// 사용자로부터 메뉴를 선택받아 리턴하는 메소드
	public int selectMenu() {
		System.out.print("*** 영단어 마스터 ***\n"
				+ "**********************\n"
				+ "1. 모든 단어 보기\n"
				+ "2. 수준별 단어 보기\n"
				+ "3. 단어 검색\n"
				+ "4. 단어 추가\n"
				+ "5. 단어 수정\n"
				+ "6. 단어 삭제\n"
				+ "7. 파일 저장\n"
				+ "0. 나가기\n"
				+ "**********************\n"
				+ "=> 원하는 메뉴는? : ");
			
		return sc.nextInt();
		
	}
}
