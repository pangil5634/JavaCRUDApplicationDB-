package com.mycom.word;

public interface ICRUD {

	public int add(Word one); // 데이터를 추가하는 메소드
	public int update(Word one); // 데이터를 수정하는 메소드
	public int delete(Word one); // 데이터를 삭제하는 메소드
}
