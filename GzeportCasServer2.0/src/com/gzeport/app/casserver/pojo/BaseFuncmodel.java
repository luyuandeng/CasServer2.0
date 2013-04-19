package com.gzeport.app.casserver.pojo;


import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * BaseFuncmodel entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_BASE_FUNCMODEL", schema = "PORTAL")
public class BaseFuncmodel implements java.io.Serializable {

	// Fields

	private String funcId;
	private BaseFuncmodel baseFuncmodel;
	private String funcCode;
	private String funcName;
	private String funcSysType;
	private String funcIsmenu;
	private String funcIslast;
	private String funcUrl;
	private String funcResume;
	private String funcUseType;
	private Set<BaseFuncmodel> baseFuncmodels = new HashSet<BaseFuncmodel>(0);

	// Constructors

	/** default constructor */
	public BaseFuncmodel() {
	}

	/** minimal constructor */
	public BaseFuncmodel(String funcId) {
		this.funcId = funcId;
	}

	/** full constructor */
	public BaseFuncmodel(String funcId, BaseFuncmodel baseFuncmodel,
			String funcCode, String funcName, String funcSysType,
			String funcIsmenu, String funcIslast, String funcUrl,
			String funcResume, String funcUseType,
			Set<BaseFuncmodel> baseFuncmodels) {
		this.funcId = funcId;
		this.baseFuncmodel = baseFuncmodel;
		this.funcCode = funcCode;
		this.funcName = funcName;
		this.funcSysType = funcSysType;
		this.funcIsmenu = funcIsmenu;
		this.funcIslast = funcIslast;
		this.funcUrl = funcUrl;
		this.funcResume = funcResume;
		this.funcUseType = funcUseType;
		this.baseFuncmodels = baseFuncmodels;
	}

	// Property accessors
	@Id
	@Column(name = "FUNC_ID", unique = true, nullable = false, length = 12)
	public String getFuncId() {
		return this.funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UP_FUNC_ID")
	public BaseFuncmodel getBaseFuncmodel() {
		return this.baseFuncmodel;
	}

	public void setBaseFuncmodel(BaseFuncmodel baseFuncmodel) {
		this.baseFuncmodel = baseFuncmodel;
	}

	@Column(name = "FUNC_CODE", length = 2000)
	public String getFuncCode() {
		return this.funcCode;
	}

	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}

	@Column(name = "FUNC_NAME", length = 100)
	public String getFuncName() {
		return this.funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	@Column(name = "FUNC_SYS_TYPE", length = 3)
	public String getFuncSysType() {
		return this.funcSysType;
	}

	public void setFuncSysType(String funcSysType) {
		this.funcSysType = funcSysType;
	}

	@Column(name = "FUNC_ISMENU", length = 1)
	public String getFuncIsmenu() {
		return this.funcIsmenu;
	}

	public void setFuncIsmenu(String funcIsmenu) {
		this.funcIsmenu = funcIsmenu;
	}

	@Column(name = "FUNC_ISLAST", length = 1)
	public String getFuncIslast() {
		return this.funcIslast;
	}

	public void setFuncIslast(String funcIslast) {
		this.funcIslast = funcIslast;
	}

	@Column(name = "FUNC_URL", length = 512)
	public String getFuncUrl() {
		return this.funcUrl;
	}

	public void setFuncUrl(String funcUrl) {
		this.funcUrl = funcUrl;
	}

	@Column(name = "FUNC_RESUME", length = 1500)
	public String getFuncResume() {
		return this.funcResume;
	}

	public void setFuncResume(String funcResume) {
		this.funcResume = funcResume;
	}

	@Column(name = "FUNC_USE_TYPE", length = 10)
	public String getFuncUseType() {
		return this.funcUseType;
	}

	public void setFuncUseType(String funcUseType) {
		this.funcUseType = funcUseType;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "baseFuncmodel")
	public Set<BaseFuncmodel> getBaseFuncmodels() {
		return this.baseFuncmodels;
	}

	public void setBaseFuncmodels(Set<BaseFuncmodel> baseFuncmodels) {
		this.baseFuncmodels = baseFuncmodels;
	}

}