package com.gzeport.app.casserver.pojo;


import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.dom4j.Element;


@Entity
@Table(name = "T_BASE_COMPANY", schema = "PORTAL")
public class BaseCompany implements  java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String companyId;
	private String companyName;
	private Date originateDate;
	private String postCode;
	private String telephone;
	private String fax;
	private String homePage;
	private String engName;
	private Long typeId;
	private String englist;
	private String eportCard;
	private String address;
	private String depositBank;
	private String bankCount;
	private String orgCode;
	private String bussinessCode;
	private String approvalCode;
	private String customsCode;
	private String countryExaminesCode;
	private String taxRegistrationCode;
	private String maritimeAffairCode;
	private String frontierInspectionCode;
	private String foreignTradeCode;
	private String portOfficeEntryCode;
	private String outsideEnterCode;
	private String bussinessScope;
	private String trafficCode;
	private String companyState;
	private String payType;
	private String contactUser;
	private String createCompany;
	private String createUser;
	private Date createDate;
	private String areaCode;
	private String declareRegNo;
	private String companyCategory;
	private String companyNature;
//	private String chinaEportEnterpriseCode;

	/** default constructor */
	public BaseCompany() {
	}

	/** minimal constructor */
	public BaseCompany(String companyId, String companyState) {
		this.companyId = companyId;
		this.companyState = companyState;
	}

	/** full constructor */
	public BaseCompany(String companyId, String companyName, Date originateDate, String postCode, String telephone, String fax, String homePage, String engName, Long typeId, String englist,
			String eportCard, String address, String depositBank, String bankCount, String orgCode, String bussinessCode, String approvalCode, String customsCode, String countryExaminesCode,
			String taxRegistrationCode, String maritimeAffairCode, String frontierInspectionCode, String foreignTradeCode, String portOfficeEntryCode,String outsideEnterCode,
			String bussinessScope, String trafficCode, String companyState, String payType, String contactUser, String createCompany, String createUser, Date createDate,
			String areaCode) {
		this.companyId = companyId;
		this.companyName = companyName;
		this.originateDate = originateDate;
		this.postCode = postCode;
		this.telephone = telephone;
		this.fax = fax;
		this.homePage = homePage;
		this.engName = engName;
		this.typeId = typeId;
		this.englist = englist;
		this.eportCard = eportCard;
		this.address = address;
		this.depositBank = depositBank;
		this.bankCount = bankCount;
		this.orgCode = orgCode;
		this.bussinessCode = bussinessCode;
		this.approvalCode = approvalCode;
		this.customsCode = customsCode;
		this.countryExaminesCode = countryExaminesCode;
		this.taxRegistrationCode = taxRegistrationCode;
		this.maritimeAffairCode = maritimeAffairCode;
		this.frontierInspectionCode = frontierInspectionCode;
		this.foreignTradeCode = foreignTradeCode;
		this.portOfficeEntryCode = portOfficeEntryCode;
	//	this.chinaEportEnterpriseCode = chinaEportEnterpriseCode;
		this.outsideEnterCode = outsideEnterCode;
		this.bussinessScope = bussinessScope;
		this.trafficCode = trafficCode;
		this.companyState = companyState;
		this.payType = payType;
		this.contactUser = contactUser;
		this.createCompany = createCompany;
		this.createUser = createUser;
		this.createDate = createDate;
		this.areaCode = areaCode;
	}

	@Id
	@Column(name = "COMPANY_ID", unique = true, nullable = false, length = 20)
	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Column(name = "COMPANY_NAME", length = 200)
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ORIGINATE_DATE", length = 7)
	public Date getOriginateDate() {
		return this.originateDate;
	}

	public void setOriginateDate(Date originateDate) {
		this.originateDate = originateDate;
	}

	@Column(name = "POST_CODE", length = 10)
	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Column(name = "TELEPHONE", length = 18)
	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Column(name = "FAX", length = 15)
	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "HOME_PAGE", length = 500)
	public String getHomePage() {
		return this.homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	@Column(name = "ENG_NAME", length = 15)
	public String getEngName() {
		return this.engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
	}

	@Column(name = "TYPE_ID", precision = 11, scale = 0)
	public Long getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	@Column(name = "ENGLIST", length = 80)
	public String getEnglist() {
		return this.englist;
	}

	public void setEnglist(String englist) {
		this.englist = englist;
	}

	@Column(name = "EPORT_CARD", length = 50)
	public String getEportCard() {
		return this.eportCard;
	}

	public void setEportCard(String eportCard) {
		this.eportCard = eportCard;
	}

	@Column(name = "ADDRESS", length = 50)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "DEPOSIT_BANK", length = 80)
	public String getDepositBank() {
		return this.depositBank;
	}

	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}

	@Column(name = "BANK_COUNT", length = 50)
	public String getBankCount() {
		return this.bankCount;
	}

	public void setBankCount(String bankCount) {
		this.bankCount = bankCount;
	}

	@Column(name = "ORG_CODE", length = 15)
	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	@Column(name = "BUSSINESS_CODE", length = 15)
	public String getBussinessCode() {
		return this.bussinessCode;
	}

	public void setBussinessCode(String bussinessCode) {
		this.bussinessCode = bussinessCode;
	}

	@Column(name = "APPROVAL_CODE", length = 15)
	public String getApprovalCode() {
		return this.approvalCode;
	}

	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}

	@Column(name = "CUSTOMS_CODE", length = 15)
	public String getCustomsCode() {
		return this.customsCode;
	}

	public void setCustomsCode(String customsCode) {
		this.customsCode = customsCode;
	}

	@Column(name = "COUNTRY_EXAMINES_CODE", length = 15)
	public String getCountryExaminesCode() {
		return this.countryExaminesCode;
	}

	public void setCountryExaminesCode(String countryExaminesCode) {
		this.countryExaminesCode = countryExaminesCode;
	}

	@Column(name = "TAX_REGISTRATION_CODE", length = 15)
	public String getTaxRegistrationCode() {
		return this.taxRegistrationCode;
	}

	public void setTaxRegistrationCode(String taxRegistrationCode) {
		this.taxRegistrationCode = taxRegistrationCode;
	}

	@Column(name = "MARITIME_AFFAIR_CODE", length = 15)
	public String getMaritimeAffairCode() {
		return this.maritimeAffairCode;
	}

	public void setMaritimeAffairCode(String maritimeAffairCode) {
		this.maritimeAffairCode = maritimeAffairCode;
	}

	@Column(name = "FRONTIER_INSPECTION_CODE", length = 15)
	public String getFrontierInspectionCode() {
		return this.frontierInspectionCode;
	}

	public void setFrontierInspectionCode(String frontierInspectionCode) {
		this.frontierInspectionCode = frontierInspectionCode;
	}

	@Column(name = "FOREIGN_TRADE_CODE", length = 15)
	public String getForeignTradeCode() {
		return this.foreignTradeCode;
	}

	public void setForeignTradeCode(String foreignTradeCode) {
		this.foreignTradeCode = foreignTradeCode;
	}

	@Column(name = "PORT_OFFICE_ENTRY_CODE", length = 15)
	public String getPortOfficeEntryCode() {
		return this.portOfficeEntryCode;
	}

	public void setPortOfficeEntryCode(String portOfficeEntryCode) {
		this.portOfficeEntryCode = portOfficeEntryCode;
	}

/*	@Column(name = "CHINA_EPORT_ENTERPRISE_CODE", length = 15)
	public String getChinaEportEnterpriseCode() {
		return this.chinaEportEnterpriseCode;
	}

	public void setChinaEportEnterpriseCode(String chinaEportEnterpriseCode) {
		this.chinaEportEnterpriseCode = chinaEportEnterpriseCode;
	}*/

	@Column(name = "OUTSIDE_ENTER_CODE", length = 15)
	public String getOutsideEnterCode() {
		return this.outsideEnterCode;
	}

	public void setOutsideEnterCode(String outsideEnterCode) {
		this.outsideEnterCode = outsideEnterCode;
	}

	@Column(name = "BUSSINESS_SCOPE", length = 100)
	public String getBussinessScope() {
		return this.bussinessScope;
	}

	public void setBussinessScope(String bussinessScope) {
		this.bussinessScope = bussinessScope;
	}

	@Column(name = "TRAFFIC_CODE", length = 15)
	public String getTrafficCode() {
		return this.trafficCode;
	}

	public void setTrafficCode(String trafficCode) {
		this.trafficCode = trafficCode;
	}

	@Column(name = "COMPANY_STATE", nullable = false, length = 5)
	public String getCompanyState() {
		return this.companyState;
	}

	public void setCompanyState(String companyState) {
		this.companyState = companyState;
	}

	@Column(name = "PAY_TYPE", length = 10)
	public String getPayType() {
		return this.payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@Column(name = "CONTACT_USER", length = 30)
	public String getContactUser() {
		return this.contactUser;
	}

	public void setContactUser(String contactUser) {
		this.contactUser = contactUser;
	}

	@Column(name = "CREATE_COMPANY", length = 30)
	public String getCreateCompany() {
		return this.createCompany;
	}

	public void setCreateCompany(String createCompany) {
		this.createCompany = createCompany;
	}

	@Column(name = "CREATE_USER", length = 30)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", length = 7)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "AREA_CODE", length = 5)
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Column(name = "DECLARE_REG_NO", length = 20)
	public String getDeclareRegNo() {
		return declareRegNo;
	}

	public void setDeclareRegNo(String declareRegNo) {
		this.declareRegNo = declareRegNo;
	}

	@Column(name = "COMPANY_CATEGORY", length = 10)
	public String getCompanyCategory() {
		return companyCategory;
	}

	public void setCompanyCategory(String companyCategory) {
		this.companyCategory = companyCategory;
	}

	@Column(name = "COMPANY_TYPE", length = 10)
	public String getCompanyNature() {
		return companyNature;
	}

	public void setCompanyNature(String companyNature) {
		this.companyNature = companyNature;
	}

}