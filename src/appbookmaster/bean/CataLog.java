package appbookmaster.bean;

public class CataLog {
	private Integer id;
	private String catalog;
	private String title;
	private String description;
	private Integer pid;
	
	private String status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public CataLog(Integer id, String catalog, String title, String description, Integer pid) {
		super();
		this.id = id;
		this.catalog = catalog;
		this.title = title;
		this.description = description;
		this.pid = pid;
	}
	public CataLog(Integer id, String catalog, String title, String status) {
		super();
		this.id = id;
		this.catalog = catalog;
		this.title = title;
		this.status = status;
	}
	public CataLog(Integer id, String catalog, String title) {
		super();
		this.id = id;
		this.catalog = catalog;
		this.title = title;
	}
}
