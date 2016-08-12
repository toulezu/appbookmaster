package appbookmaster.bean;

public class LastRead {
	private Integer id;
	private Integer chapterid;
	private Integer scrollto;
	private String status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public LastRead(Integer id, Integer chapterid, Integer scrollto,
			String status) {
		super();
		this.id = id;
		this.chapterid = chapterid;
		this.scrollto = scrollto;
		this.status = status;
	}
	
}
