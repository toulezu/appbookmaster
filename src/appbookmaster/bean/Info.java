package appbookmaster.bean;

public class Info {
	private Integer id;
	private String name;
	private String author;
	private String description;
	private String about;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public Info(String name, String author, String description, String about) {
		super();
		this.name = name;
		this.author = author;
		this.description = description;
		this.about = about;
	}
	
}
