package appbookmaster.bean;

public class Content {
	private Integer id;
	private String content;
	private Integer chapterid;
	private Integer scrollto;
	private String status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getChapterid() {
		return chapterid;
	}
	public void setChapterid(Integer chapterid) {
		this.chapterid = chapterid;
	}
	public Integer getScrollto() {
		return scrollto;
	}
	public void setScrollto(Integer scrollto) {
		this.scrollto = scrollto;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Content(Integer id, String content, Integer chapterid,
			Integer scrollto, String status) {
		super();
		this.id = id;
		this.content = content;
		this.chapterid = chapterid;
		this.scrollto = scrollto;
		this.status = status;
	}
	
}
