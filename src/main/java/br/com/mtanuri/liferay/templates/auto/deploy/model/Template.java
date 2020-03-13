package br.com.mtanuri.liferay.templates.auto.deploy.model;

public class Template {

	private String getFullName;
	private String script;
	private long classNameId;
	private long resourceClassNameId;
	private long groupId;

	public Template(String getFullName, String script, long classNameId, long resourceClassNameId, long groupId) {
		super();
		this.getFullName = getFullName;
		this.script = script;
		this.classNameId = classNameId;
		this.resourceClassNameId = resourceClassNameId;
		this.groupId = groupId;
	}

	public String getName() {
		return this.getFullName.split("\\.")[0];
	}

	public String getType() {
		return this.getFullName.split("\\.")[1];
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getGetFullName() {
		return getFullName;
	}

	public long getClassNameId() {
		return classNameId;
	}

	public long getResourceClassNameId() {
		return resourceClassNameId;
	}

	public long getGroupId() {
		return groupId;
	}

	@Override
	public String toString() {
		return "Template [getFullName=" + getFullName + ", script=" + script + "]";
	}

}
