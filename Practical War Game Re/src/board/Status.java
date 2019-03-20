package board;

public enum Status {
	NORMAL("default"), CHECK("check"), CHECKMATE("checkmate");
		
	private String name;
	private Status(String name){
		this.name = name;
	}
	
	public String getName(){return name;};
	@Override
	public String toString() {
		return getName();
	}
}
