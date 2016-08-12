package appbookmaster.bean;

public class BookMark {
	private Integer id;
	private Integer chapterid;
	private String catalog;
	private String title;
	private String bookmark;
	private String datetime;
	private String status;
	private Integer scrollto;
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
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBookmark() {
		return bookmark;
	}
	public void setBookmark(String bookmark) {
		this.bookmark = bookmark;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getScrollto() {
		return scrollto;
	}
	public void setScrollto(Integer scrollto) {
		this.scrollto = scrollto;
	}
	public BookMark(Integer id, Integer chapterid, String catalog, String title, String bookmark, String datetime, String status, Integer scrollto) {
		super();
		this.id = id;
		this.chapterid = chapterid;
		this.catalog = catalog;
		this.title = title;
		this.bookmark = bookmark;
		this.datetime = datetime;
		this.status = status;
		this.scrollto = scrollto;
	}
	
}
